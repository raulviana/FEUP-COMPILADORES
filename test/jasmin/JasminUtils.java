package jasmin;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.function.Consumer;
import java.util.UUID;
import java.util.stream.Collectors;

import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.utilities.StringLines;
import pt.up.fe.specs.util.system.ProcessOutputAsString;
import pt.up.fe.specs.util.system.StreamToString;
import pt.up.fe.specs.util.system.OutputType;
import pt.up.fe.specs.util.system.ProcessOutput;
import pt.up.fe.specs.util.providers.WebResourceProvider;

import java.lang.reflect.Method;

public class JasminUtils {
	
	private static final Set<String> TEST_CLASSPATH = new HashSet<>(Arrays.asList("./test/fixtures/libs/compiled"));

	public static Long HUMAN_DELAY_MS = 250l;
	public static Long TIMEOUT_NS = 5_000_000_000l;

	public static Set<String> getTestClasspath() {
		return TEST_CLASSPATH;
	}

    public static String testJmm(String jmmResource, String expectedOutput) {	
		 return testJmm(jmmResource,expectedOutput, null, true);
	}

	
    public static String testJmm(String jmmResource, String expectedOutput, String stdin) {	
		return testJmm(jmmResource,expectedOutput, stdin, true);
	}

	public static String testJmm(String jmmResource, String expectedOutput, Boolean test) {	
		return testJmm(jmmResource,expectedOutput, null, test);
	}


    public static String testJmm(String jmmResource, String expectedOutput, String stdin, Boolean test) {	
		// Create jmm file
        File tempFolder = SpecsIo.getTempFolder(jmmResource);
        File testFile = SpecsIo.resourceCopy(jmmResource, tempFolder);
		
		String jCode = JmmCompiler.compile(testFile);
		String jFilename = SpecsIo.removeExtension(testFile.getName()) + ".j";
		SpecsIo.write(new File(tempFolder, jFilename), jCode);
	
		File resultsFolder = SpecsIo.mkdir("resultsProject"); 


		String jBaseFilename = SpecsIo.removeExtension(jmmResource) + ".j";		
		File jBaseFile = new File(jBaseFilename);

		File testFolder = SpecsIo.mkdir(resultsFolder, jBaseFile.getParent());

		File jmmFile = new File(testFolder, testFile.getName());
		SpecsIo.write(jmmFile, SpecsIo.read(testFile));
		
		File jFile = new File(testFolder, jBaseFile.getName());
		SpecsIo.write(jFile, jCode);

		//System.out.println("Writing generated code to folder: " + testFolder.getAbsolutePath());	
	

//		SpecsIo.mkdir("j_files");
//		File jFile = new File(
		
		if(test)
			testJasminBase(jCode, expectedOutput, stdin);
		return jCode;
	}

	


	public static File getJasminJar(){
		var tempFolder = SpecsIo.getTempFolder("jasmin-comp");
		//System.out.println("Jasmin temp folder: " + tempFolder.getAbsolutePath());

		if(!new File(tempFolder, "jasmin.zip").isFile()) {
			var jasminUrl = WebResourceProvider.newInstance("http://specs.fe.up.pt/classes/", "jasmin.zip");
			System.out.println("Downloading Jasmin to folder " + tempFolder.getAbsolutePath());
			var jasminZip = jasminUrl.write(tempFolder);
			SpecsIo.extractZip(jasminZip, tempFolder);
		}
		
		File jasminJar = new File(tempFolder, "jasmin.jar");
		if(!jasminJar.isFile()) {
			throw new RuntimeException("Could not locate jasmin.jar inside " + tempFolder.getAbsolutePath());
		}
		
		return jasminJar;
	}
	
	public static String normalize(String string){
		return SpecsStrings.normalizeFileContents(string, true);
	}
	 
	public static ProcessOutputAsString runProcess(List<String> command, boolean storeOutput, boolean printOutput) {
		return runProcess(command, null, storeOutput, printOutput);
	}
 
	 
	public static ProcessOutputAsString runProcess(List<String> command, String input, boolean storeOutput, boolean printOutput) {

		var workingDir = SpecsIo.getWorkingDir();

		// Build process
		ProcessBuilder builder = new ProcessBuilder(command);
        builder.directory(workingDir);		
		Consumer<OutputStream> stdin = null;
		if(input != null && !input.isEmpty()) {
			stdin = outputStream -> {
				try(PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(outputStream)))) {
					for(var line : StringLines.getLines(input)) {
						// Simulate person typing (1s between each iteration)
						SpecsSystem.sleep(HUMAN_DELAY_MS);
						pw.println(line);		
						pw.flush();
					}
				}

			};
		}


        Function<InputStream, String> stdout = new StreamToString(printOutput, storeOutput, OutputType.StdOut);
        Function<InputStream, String> stderr = new StreamToString(printOutput, storeOutput, OutputType.StdErr);

        ProcessOutput<String, String> output = SpecsSystem.runProcess(builder, stdout, stderr, stdin, TIMEOUT_NS);
		
		if(output.isError()) {
			throw new RuntimeException("There was a problem when executing the command " + command + ", please check the output");
		}
		
        return new ProcessOutputAsString(output.getReturnValue(), output.getStdOut(), output.getStdErr());
    }


    public static String getResource(String resource) {	
		return SpecsIo.getResource(() -> resource);
	}

    public static void testJasmin(String jResource, String expectedOutput) {	
		testJasmin(jResource, expectedOutput, null);
	}

    public static void testJasmin(String jResource, String expectedOutput, String stdin) {	
		testJasminBase(SpecsIo.getResource(() -> jResource), expectedOutput, stdin);
	}
	
    public static void testJasminBase(String code, String expectedOutput, String stdin) {

		var jasminFile = SpecsIo.getTempFile(null, "j");
		SpecsIo.write(jasminFile, code);
		
		if(!jasminFile.isFile()) {
			throw new RuntimeException("Could not find input file " + jasminFile.getAbsolutePath());
		}
		
		File jasminJar = JasminUtils.getJasminJar();
		File jasminFoder = jasminJar.getParentFile();
		
		File tempOutputFolder = SpecsIo.mkdir(jasminFoder, UUID.randomUUID().toString());
		
		var jasminArgs = Arrays.asList("java", "-jar", jasminJar.getAbsolutePath(), "-d", tempOutputFolder.getAbsolutePath(), jasminFile.getAbsolutePath());

		var output = SpecsSystem.runProcess(jasminArgs, true, false);

		System.out.println("Jasmin:\n" + output.getOutput());


		// Look for .class inside temporary output folder
		var classFiles = SpecsIo.getFiles(tempOutputFolder);
		if(classFiles.size() == 0) {
			throw new RuntimeException("No class files found after running Jasmin ("+tempOutputFolder.getAbsolutePath()+")");
		}
		
		if(classFiles.size() != 1) {
			System.out.println("More than one class file generated! Using file " + classFiles.get(0));
		}


		//File classFile = new File("jvm/HelloWorld.class");
		File classFile = classFiles.get(0);
		
		// Add folder of the class file
		String classpath = classFile.getParentFile().getAbsolutePath();
		
		// Buid test classpath
		String testClasspath = JasminUtils.getTestClasspath().stream().collect(Collectors.joining(File.pathSeparator));
		if(!testClasspath.isEmpty()) {
			classpath += File.pathSeparator + testClasspath;
		}
		
		var command = Arrays.asList("java", "-cp", classpath, SpecsIo.removeExtension(classFile.getName()));
		System.out.println("Executing " + command.stream().collect(Collectors.joining(" ")));
		
		
		var classOutput = JasminUtils.runProcess(command, stdin, true, true);
		
		System.out.println("Program output:\n" + classOutput.getOutput());
		
		assertEquals(JasminUtils.normalize(expectedOutput), JasminUtils.normalize(classOutput.getOutput()));
    }

}