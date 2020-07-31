package jasmin;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import java.io.File;
import java.lang.reflect.Method;
import pt.up.fe.specs.util.SpecsIo;

public class JmmCompiler {
	
    private static String CLASS_WITH_MAIN = "jmm";


    /**
     * Compiles a .jmm file to .j code with no additional arguments.
     */
    public static String compile(File jmm){
        return compile(jmm, new String[0]);
    }

	
	/**
	 * Compiles a .jmm file to .j code.
     *
     * @param jmm the .jmm file
     * @return a string with the .j code
	 */
    public static String compile(File jmm, String... args){

		// This method needs to be implemented before the tests can run
		// Below there is an example implementation, that assumes that the .j file is generated in the repository root
		// Adapt the code according to your implementation.
		//throw new RuntimeException("Implement JmmCompiler.compile() in order to test the execution of .jmm files");



		// Executes J-- compiler		
		try {
            // Get class with main
            Class<?> mainClass = Class.forName(CLASS_WITH_MAIN);

            // It is expected that class has a main function
            Method mainMethod = mainClass.getMethod("main", String[].class);

            // Invoke main method with file as argument
            List<String> allArgs = new ArrayList<>();
            allArgs.add(jmm.getAbsolutePath());
            allArgs.addAll(Arrays.asList(args));
            String[] mainArgs = allArgs.toArray(size -> new String[size]);
            Object[] invokeArgs = { mainArgs };
            mainMethod.invoke(null, invokeArgs);

        } catch (Exception e) {
			throw new RuntimeException("Error with compiling jmm", e);
		}

		var filename = SpecsIo.removeExtension(jmm.getName()) + ".j";
		
		// TODO: where is the .j fible? This code works when the .j file has the same name as the .jmm file, and is generated in the repository root. Adapt code according to how your J-- compiler works
		var jFile = new File("src/jasmin/" + filename);
				
		if(!jFile.isFile()) {
			throw new RuntimeException("Could not find file " + jFile.getAbsolutePath());
		}

		return SpecsIo.read(jFile);
				
	}
	
}
