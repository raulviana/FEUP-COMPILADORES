import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import static org.junit.Assert.*;


public class CompUtils {

    public static File getTempFolder(String folderName) {
        // This is the property name for accessing OS temporary directory or
        // folder.
        String property = "java.io.tmpdir";

        // Get the temporary directory and print it.
        String tempDir = System.getProperty(property);

        // If we are on Linux, usually the temporary folder is shared by all users.
        // This can be problematic in regard to read/write permissions
        // Suffix the name of the user to make the temporary folder unique to the user
        String userName = System.getProperty("user.name");
        folderName = folderName == null ? "tmp_" + userName : folderName + "_" + userName;

        File systemTemp = existingFolder(null, tempDir);

        return mkdir(systemTemp, folderName);
    }

    public static File existingFolder(File parentFolder, String foldername) {
        File folder = new File(parentFolder, foldername);

        if (!folder.isDirectory()) {
            throw new RuntimeException("Could not open folder '" + folder.getPath() + "'");
        }

        return folder;
    }

    /**
     * Given a string representing a filepath to a folder, returns a File object representing the folder.
     *
     * <p>
     * If the folder doesn't exist, the method will try to create the folder and necessary sub-folders. If an error
     * occurs (ex.: the folder could not be created, the given path does not represent a folder), throws an exception.
     *
     * *
     * <p>
     * If the given folderpath is an empty string, returns the current working folder.
     *
     * <p>
     * If the method returns it is guaranteed that the folder exists.
     *
     * @param folderpath
     *                       String representing a folder.
     * @return a File object representing a folder, or null if unsuccessful.
     */
    public static File mkdir(String folderpath) {

        // Check null argument. If null, it would raise and exception and stop
        // the program when used to create the File object.
        if (folderpath == null) {
            throw new RuntimeException("Input 'folderpath' is null");
        }

        // Check if folderpath is empty
        if (folderpath.isEmpty()) {
            // Return working directory
            return new File(".");
        }

        // Create File object
        File folder = new File(folderpath);

        // The following checks where done in that sequence to avoid having
        // more than one level of if-nesting.

        // Check if File is a folder
        final boolean isFolder = folder.isDirectory();
        if (isFolder) {
            return folder;
        }

        // Check if is a file. If true, stop
        final boolean folderExists = folder.isFile();
        if (folderExists) {
            throw new RuntimeException("Path '" + folderpath + "' exists, but " + "doesn't represent a folder");
        }

        // Try to create folder.
        final boolean folderCreated = folder.mkdirs();
        if (folderCreated) {
            try {
                System.out.println("Folder created (" + folder.getCanonicalPath() + ").");
            } catch (IOException ex) {
                System.out.println("Folder created (" + folder.getAbsolutePath() + ").");
            }
            return folder;

        }

        // Check if folder exists
        if (folder.exists()) {
            System.out.println("Folder created (" + folder.getAbsolutePath() + ") but 'mkdirs' returned false.");
            return folder;
        }

        // Couldn't create folder
        throw new RuntimeException("Path '" + folderpath + "' does not exist and " + "could not be created");

    }

    /**
     * Helper method which accepts a File as input.
     *
     * @param folder
     * @return
     */
    public static File mkdir(File folder) {
        return mkdir(folder.getPath());
    }

    /**
     * Helper method which accepts a parent File and a child String as input.
     *
     * @param parentFolder
     * @param child
     * @return
     */
    public static File mkdir(File parentFolder, String child) {
        return mkdir(new File(parentFolder, child));
    }

    public static File resourceCopy(String resource, File destinationFolder) {

        boolean useResourcePath = true;
        boolean overwrite = true;

        // Check if destination file already exists
        String resourceOutput = resource;
        if (!useResourcePath) {
            resourceOutput = getResourceName(resourceOutput);
        }

        File destination = new File(destinationFolder, resourceOutput);

        if (destination.isFile() && !overwrite) {
            return destination;
        }

        try (InputStream stream = resourceToStream(resource);) {

            if (stream == null) {
                throw new RuntimeException("Resource '" + resource + "' does not exist");
            }

            copy(stream, destination);
        } catch (IOException e) {
            System.out.println("Skipping resource '" + resource + "'.");
            return null;
        }

        return destination;
    }

    /**
     * Returns the last name of the resource.
     *
     * <p>
     * Example, if input is 'package/resource.ext', returns 'resource.ext'.
     *
     * @param resource
     * @return
     */
    public static String getResourceName(String resource) {
        // Try backslash
        int indexOfLastSlash = resource.lastIndexOf('/');

        // Try slash
        if (indexOfLastSlash == -1) {
            indexOfLastSlash = resource.lastIndexOf('\\');
        }

        return resource.substring(indexOfLastSlash + 1);
    }

    public static InputStream resourceToStream(String resourceName) {
        // Obtain the current classloader
        ClassLoader classLoader = CompUtils.class.getClassLoader();

        // Load the file as a resource
        InputStream stream = classLoader.getResourceAsStream(resourceName);
        if (stream == null) {
            System.out.println("Could not load resource '" + resourceName + "'.");
        }

        return stream;
    }

    /**
     * Copies the contents of the source stream to the destination file.
     *
     * <p>
     * After copy, the source stream is closed.
     *
     * @param source
     * @param destination
     * @return
     */
    public static boolean copy(InputStream source, File destination) {
        boolean success = true;

        File f2 = destination;

        // Create folders for f2
        File parentFile = f2.getParentFile();
        if (parentFile != null) {
            parentFile.mkdirs();
        }

        // Using try-with-resources
        try (OutputStream out = new FileOutputStream(f2); InputStream in = source) {

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            System.out.println("Copied stream to file '" + destination.getPath() + "'.");

        } catch (IOException e) {
            System.out.println("IoException while copying stream to file '" + destination + "'");
            success = false;
        }

        return success;
    }





    public static void testParser(String jmmResource, boolean mustFail, String classWithMain) {
        // Copy contents of resource to a temporary file
        File tempFolder = CompUtils.getTempFolder("comp_jmm_test");
        File testFile = CompUtils.resourceCopy(jmmResource, tempFolder);

        boolean success = true;

        try {

            // Get class with main
            Class<?> mainClass = Class.forName(classWithMain);

            // It is expected that class has a main function
            Method mainMethod = mainClass.getMethod("main", String[].class);

            // Invoke main method with file as argument
            String[] mainArgs = { testFile.getAbsolutePath() };
            Object[] invokeArgs = { mainArgs };
            mainMethod.invoke(null, invokeArgs);

        } catch (Exception e) {
            System.out.println("Test failed: " + e);
            e.printStackTrace();
            success = false;
        } finally {
            // Clean-up
            testFile.delete();
        }

        // Flip result, in case failure is needed
        if (mustFail) {
            success = !success;
        }

        if (!success) {
            if (mustFail) {
                System.out.println("Expected parser to throw exception");
            } else {
                System.out.println("Expected parser to complete successfully");
            }
            fail();
        }
    }


}