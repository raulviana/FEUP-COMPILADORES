import symbolTable.Symbol;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class jmm {

    private static boolean DEBUG = false;
    private static boolean SEMANTIC = false;
    private static boolean AST = false;
    private static boolean ACTIVATE_ERROR = false;
    private static boolean OPTIMIZE = false;


    // When in root folder (comp2020-3a)
    // gradle build
    // or
    // gradle test
    // to run:
    // java -jar comp2020-3a.jar test/fixtures/public/Simple.jmm
    // java -jar comp2020-3a.jar test/fixtures/public/fail/syntactical/MultipleSequential.jmm
    //java -jar comp2020-3a.jar test/fixtures/public/fail/semantic/binop_incomp.jmm

    public static void main(String[] args) throws ParseException {
        if (args.length == 0 || args.length > 4) {
            System.err.println("Usage: java Jmm <filename> -debug(-ast/-semantic) and/or -error and/or -o");
            return;
        }


        for (int i = 1; i < args.length; i++) {
            switch (args[i]) {
                case "-debug":
                    DEBUG = true;
                    break;
                case "-error":
                    ACTIVATE_ERROR = true;
                    break;
                case "-ast":
                    AST = true;
                    break;
                case "-semantic":
                    SEMANTIC = true;
                    break;
                case "-o":
                    OPTIMIZE = true;
                    break;

                default:
                    System.out.println("Usage: java Jmm <filename> -debug(-ast/-semantic) and/or -error and/or -o");
					return;
            }
        }


        ParserAST myParser;
        try {
            myParser = new ParserAST(new FileInputStream(args[0]));
        } catch (FileNotFoundException e) {
            System.out.println("file " + args[0] + " not found.");
            return;
        }

        if (DEBUG || AST)
            System.out.println("Starting Parsing\n");

        SimpleNode root = myParser.ParseExpression(); // returns reference to root node
        if (myParser.getNerros() > 0) {
            throw new RuntimeException("Has syntactic errors");
        }

        if (DEBUG || AST) {
            root.dump(""); // prints the tree on the screen
            System.out.println("Finished Parsing");
        }

        SemanticAnalysis semanticAnalysis = new SemanticAnalysis(ACTIVATE_ERROR);
        semanticAnalysis.startAnalysing(root);


        if (DEBUG || SEMANTIC)
            System.out.println("Starting Semantic Analysis\n");

        if (semanticAnalysis.getNerros() > 0) {
            throw new RuntimeException("Has " + semanticAnalysis.getNerros() + " semantic errors");
        }

        if (semanticAnalysis.getNwarnings() > 0) {
            System.err.println("Has " + semanticAnalysis.getNwarnings() + " semantic warnings");
        }

        if (DEBUG || SEMANTIC) {
            semanticAnalysis.dump();
            System.out.println("Finished Semantic Analysis\n");
        }

        CodeGenerator generator = new CodeGenerator(semanticAnalysis, OPTIMIZE);
        generator.generate(root);

        System.out.println("Jasmin code generated");
    }
}
