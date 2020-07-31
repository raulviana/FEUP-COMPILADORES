import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

//import com.sun.prism.shader.Solid_TextureYV12_AlphaTest_Loader;
import symbolTable.*;

public class CodeGenerator {

    private PrintWriter printWriterFile;
    private final HashMap<String, Symbol> symbolTable;
    private int loopCounter;
    private int localVars;
    private int nParams;
    private int maxStack;
    private int totalStack;
    private StringBuilder methodCode;
    private StringBuilder bodyCode;
    private boolean optimize;

    public CodeGenerator(SemanticAnalysis semanticAnalysis, boolean optimize) {
        symbolTable = semanticAnalysis.getSymbolTable();
        this.loopCounter = 0;
        this.localVars = 0;
        this.nParams = 0;
        this.maxStack = 0;
        this.totalStack = 0;
        this.optimize = optimize;
        this.methodCode = new StringBuilder();
        this.bodyCode = new StringBuilder();
    }

    public void generate(SimpleNode node) {

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            if (node.jjtGetChild(i) instanceof ASTClassDeclaration) {
                ASTClassDeclaration classNode = (ASTClassDeclaration) node.jjtGetChild(i);

                this.printWriterFile = createOutputFile(classNode.name);
                this.generateClass(classNode);
                this.printWriterFile.close();

            }
        }
    }

    private void generateClass(ASTClassDeclaration classNode) {
        this.localVars = 0;
        this.printWriterFile.println(".class public " + classNode.name);

        if (classNode.ext != null)
            this.printWriterFile.println(".super " + classNode.ext);
        else
            this.printWriterFile.println(".super java/lang/Object");

        SymbolClass symbolClass = (SymbolClass) this.symbolTable.get(classNode.name);

        generateClassVariables(classNode);
        generateExtend(classNode);
        generateMethods(classNode, symbolClass);
    }

    private void generateClassVariables(SimpleNode node) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);
            if (child instanceof ASTVarDeclaration) {
                generateGlobalVar((ASTVarDeclaration) child);
            }
        }
    }

    private void generateGlobalVar(ASTVarDeclaration var) {

        if (var.jjtGetChild(0) instanceof ASTType) {
            ASTType nodeType = (ASTType) var.jjtGetChild(0);
            printWriterFile.println(".field private " + var.name + " " + getType(nodeType));
        }
    }

    private String getType(ASTType nodeType) {

        if (nodeType.isArray)
            return "[I";

        switch (nodeType.type) {
            case "int":
                return "I";
            case "String":
                return "Ljava/lang/String";
            case "boolean":
                return "Z";
            case "void":
                return "V";
        }

        return "L" + nodeType.type + ";";
    }

    private String getSymbolType(Type symbolType) {

        if (symbolType == Type.INT_ARRAY)
            return "[I";

        switch (symbolType) {
            case INT:
                return "I";
            case STRING:
                return "Ljava/lang/String";
            case BOOLEAN:
                return "Z";
            case VOID:
                return "V";
        }
        return "";
    }

    private void generateExtend(ASTClassDeclaration node) {
        if (node.ext != null)
            this.printWriterFile.println("\n.method public <init>()V\n\taload_0\n\tinvokenonvirtual "
                    + node.ext + "/<init>()V\n\treturn\n.end method\n");
        else
            generateConstructor();
    }

    private void generateConstructor() {
        printWriterFile.println("\n.method public <init>()V");
        printWriterFile.println("\taload_0");
        printWriterFile.println("\tinvokenonvirtual java/lang/Object/<init>()V");
        printWriterFile.println("\treturn");
        printWriterFile.println(".end method\n");
    }

    private void generateMethods(SimpleNode node, SymbolClass symbolClass) {
        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) node.jjtGetChild(i);

            if (child instanceof ASTMainDeclaration) {
                SymbolMethod symbolMethod = getSymbolMethod(symbolClass.symbolTableMethods.get("main"), i);
                generateMainMethod(child, symbolClass, symbolMethod);
            }
            if (child instanceof ASTMethodDeclaration) {
                ASTMethodDeclaration methodDeclaration = (ASTMethodDeclaration) child;
                SymbolMethod symbolMethod = getSymbolMethod(symbolClass.symbolTableMethods.get(methodDeclaration.name), i);

                if (symbolMethod == null) {
                    throw new RuntimeException("ERROR generating code for method " + methodDeclaration.name);
                }
                generateMethod(methodDeclaration, symbolClass, symbolMethod);
            }
        }

    }

    private void generateMainMethod(SimpleNode mainNode, SymbolClass symbolClass, SymbolMethod symbolMethod) {
        StringBuilder limits = new StringBuilder();
        this.bodyCode = new StringBuilder();
        this.methodCode = new StringBuilder();

        this.methodCode.append(".method public static main([Ljava/lang/String;)V\n");

        generateIndexes(mainNode, symbolMethod);
        generateMethodBody(mainNode, symbolClass, symbolMethod);

        int localLimits = this.localVars + 1; //nParams = 1

        limits.append("\t.limit stack " + this.maxStack + "\n");
        limits.append("\t.limit locals " + localLimits + "\n\n");

        this.bodyCode.append("\treturn\n");
        this.bodyCode.append(".end method\n\n");

        this.maxStack = 0;
        this.totalStack = 0;
        this.localVars = 0;

        this.methodCode.append(limits);
        this.methodCode.append(this.bodyCode);

        this.printWriterFile.println(this.methodCode);
    }

    private void generateMethod(ASTMethodDeclaration methodNode, SymbolClass symbolClass, SymbolMethod symbolMethod) {
        StringBuilder limits = new StringBuilder();
        this.bodyCode = new StringBuilder();
        this.methodCode = new StringBuilder();

        generateMethodHeader(methodNode);
        generateIndexes(methodNode, symbolMethod);

        int localLimits = this.localVars + this.nParams;
        if (!symbolMethod.isStatic())
            localLimits += 1;

        generateMethodBody(methodNode, symbolClass, symbolMethod);

        limits.append("\t.limit stack " + this.maxStack + "\n");
        limits.append("\t.limit locals " + localLimits + "\n\n");
        this.bodyCode.append(".end method\n\n");

        this.localVars = 0;
        this.nParams = 0;
        this.maxStack = 0;
        this.totalStack = 0;

        this.methodCode.append(limits);
        this.methodCode.append(this.bodyCode);

        this.printWriterFile.println(this.methodCode);
    }

    private SymbolMethod getSymbolMethod(ArrayList<SymbolMethod> listSymbolMethod, int num) {
        if (listSymbolMethod.size() == 1)
            return listSymbolMethod.get(0);

        for (SymbolMethod symbolMethod : listSymbolMethod) {
            if (symbolMethod.num == num) {
                return symbolMethod;
            }
        }
        return null;
    }

    private void generateMethodHeader(ASTMethodDeclaration methodNode) {

        StringBuilder methodArgs = new StringBuilder();
        StringBuilder methodType = new StringBuilder();

        for (int i = 0; i < methodNode.jjtGetNumChildren(); i++) {
            SimpleNode child = (SimpleNode) methodNode.jjtGetChild(i);
            if (child instanceof ASTArg) {
                if (child.jjtGetChild(0) instanceof ASTType) {
                    methodArgs.append(generateMethodArgument((ASTArg) child));
                    this.nParams++;
                }
            }
            if (child instanceof ASTType) {
                methodType.append(getType((ASTType) child));
            }
        }

        this.printWriterFile.println(".method public " + methodNode.name + "(" + methodArgs + ")" + methodType);
    }

    private String generateMethodArgument(ASTArg argNode) {
        if (argNode.jjtGetChild(0) instanceof ASTType)
            return getType((ASTType) argNode.jjtGetChild(0));

        return "";
    }

    private void generateIndexes(SimpleNode methodNode, SymbolMethod symbolMethod) {

        int indexCounter = 1;

        for (int i = 0; i < methodNode.jjtGetNumChildren(); i++) {

            if (methodNode.jjtGetChild(i) instanceof ASTArg) {
                symbolMethod.symbolTable.get(((ASTArg) methodNode.jjtGetChild(i)).val).setIndex(indexCounter);
                indexCounter++;
                continue;
            }

            if (methodNode.jjtGetChild(i) instanceof ASTVarDeclaration) {
                ASTVarDeclaration varDeclaration = (ASTVarDeclaration) methodNode.jjtGetChild(i);
                symbolMethod.symbolTable.get(varDeclaration.name).setIndex(indexCounter);
                this.localVars++;
                indexCounter++;
                continue;
            }
        }
    }

    private void generateMethodBody(SimpleNode method, SymbolClass symbolClass, SymbolMethod symbolMethod) {
        for (int i = 0; i < method.jjtGetNumChildren(); i++) {

            SimpleNode node = (SimpleNode) method.jjtGetChild(i);

            //Already processed
            if (node instanceof ASTArg || node instanceof ASTVarDeclaration) {
                continue;
            }

            if (node instanceof ASTReturn) {

                Type type = generateExpression((SimpleNode) node.jjtGetChild(0), symbolClass, symbolMethod); //expression
                generateMethodReturn(type);
                return;
            }

            //If is not any of the others, it is a statement
            generateStatement(node, symbolClass, symbolMethod, false);

        }
    }

    private void generateStatement(SimpleNode node, SymbolClass symbolClass, SymbolMethod symbolMethod, boolean insideIfOrWhile) {
        if (node instanceof ASTEquality) {
            generateEquality((ASTEquality) node, symbolClass, symbolMethod, insideIfOrWhile);

        } else if (node instanceof ASTIf) {
            generateIfExpression(node, symbolClass, symbolMethod);

        } else if (node instanceof ASTWhile) {
            if(this.optimize)
                generateOptimizedWhileExpression(node, symbolClass, symbolMethod);
            else
                generateWhileExpression(node, symbolClass, symbolMethod);

        } else if (node instanceof ASTStatementBlock) {
            for (int i = 0; i < node.jjtGetNumChildren(); i++)
                generateStatement((SimpleNode) node.jjtGetChild(i), symbolClass, symbolMethod, insideIfOrWhile);
        }

        //If it is not any of the others it is an expression
        generateExpression(node, symbolClass, symbolMethod);

        while (totalStack > 0) {
            this.bodyCode.append("\tpop\n");
            totalStack--;
        }
    }


    private void generateWhileExpression(SimpleNode node, SymbolClass symbolClass, SymbolMethod symbolMethod) {
        this.loopCounter++;
        int thisCounter = this.loopCounter;
        SimpleNode testExpression = (SimpleNode) node.jjtGetChild(0);
        SimpleNode statement = (SimpleNode) node.jjtGetChild(1);

        this.bodyCode.append("while_" + thisCounter + "_begin:\n");

        //evaluate expression
        if (!generateConditional(testExpression, symbolClass, symbolMethod, thisCounter, "while_", "_end", true))
            return;

        generateStatement(statement, symbolClass, symbolMethod, true);
        this.bodyCode.append("\tgoto while_" + thisCounter + "_begin\n");
        this.bodyCode.append("while_" + thisCounter + "_end:\n");
    }


    private void generateOptimizedWhileExpression(SimpleNode node, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        this.loopCounter++;
        int thisCounter = this.loopCounter;
        SimpleNode testExpression = (SimpleNode) node.jjtGetChild(0);
        SimpleNode statement = (SimpleNode) node.jjtGetChild(1);

        if (canWhileBeOptimizedTemplate1(testExpression)) {

            //infinite while -> if false while is never performed, if true while is infinite
            if(checkWhileOptimized(testExpression)) {

                this.bodyCode.append("while_" + thisCounter + "_begin:\n");
                generateStatement(statement, symbolClass, symbolMethod, true);
                this.bodyCode.append("\tgoto while_" + thisCounter + "_begin\n");
            }

            return;

        }

       if(canWhileBeOptimizedTemplate2(testExpression, symbolClass, symbolMethod)) {

           //if all the values are available and it doesn't execute once, then while is never performed
            if (checkWhileOptimizedTemplate2(testExpression, symbolClass, symbolMethod)) {

                //TRANSFORM INTO A DO-WHILE
                this.bodyCode.append("while_" + thisCounter + "_begin:\n");
                generateStatement(statement, symbolClass, symbolMethod, true);

                //evaluate expression -> if true, skips to the begin
                if (!generateConditional(testExpression, symbolClass, symbolMethod, thisCounter, "while_", "_begin", false))
                    return;

                return;
            }

            return;
        }

        this.generateWhileExpression(node, symbolClass, symbolMethod);

    }

    private boolean canWhileBeOptimizedTemplate2(SimpleNode expression, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (expression instanceof ASTBoolean)
            return true;

        if (expression instanceof ASTAND) {

            if(expression.jjtGetNumChildren() == 2) {

                Node firstChild = expression.jjtGetChild(0);
                Node secondChild = expression.jjtGetChild(1);

                if (firstChild instanceof ASTIdentifier){

                    if(!identifierIsConstant3(((ASTIdentifier) firstChild).val, symbolMethod))
                        return false;


                } else {
                    if(!canWhileBeOptimizedTemplate2((SimpleNode) firstChild, symbolClass, symbolMethod))
                        return false;
                }

                if (secondChild instanceof ASTIdentifier){

                    if(!identifierIsConstant3(((ASTIdentifier) secondChild).val, symbolMethod))
                        return false;

                } else {

                    if(!canWhileBeOptimizedTemplate2((SimpleNode) firstChild, symbolClass, symbolMethod))
                        return false;
                }

                return true;
            }
        }

        if (expression instanceof ASTLESSTHAN) {

            if(expression.jjtGetNumChildren() == 2) {
                if (expression.jjtGetChild(0) instanceof ASTLiteral && expression.jjtGetChild(1) instanceof ASTLiteral) {
                    return true;
                }

                Node firstChild = expression.jjtGetChild(0);
                Node secondChild = expression.jjtGetChild(1);

                if (firstChild instanceof ASTIdentifier){

                    if(!identifierIsConstant3(((ASTIdentifier) firstChild).val, symbolMethod))
                        return false;

                } else if (!(firstChild instanceof ASTLiteral))
                    return false;

                if (secondChild instanceof ASTIdentifier){

                    if(!identifierIsConstant3(((ASTIdentifier) secondChild).val, symbolMethod))
                        return false;

                } else if (!(secondChild instanceof ASTLiteral))
                    return false;

                return true;
            }

        }

        if(expression instanceof ASTNegation){
            if(expression.jjtGetNumChildren() == 1) {
                return canWhileBeOptimizedTemplate2((SimpleNode) expression.jjtGetChild(0), symbolClass, symbolMethod);
            }
        }

        return false;

    }

    private boolean checkWhileOptimizedTemplate2(SimpleNode expression, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (expression instanceof ASTBoolean)
            return ((ASTBoolean) expression).val;

        if (expression instanceof ASTAND) {

            if(expression.jjtGetNumChildren() == 2) {

                Node firstChild = expression.jjtGetChild(0);
                Node secondChild = expression.jjtGetChild(1);
                boolean firstChildValue;
                boolean secondChildValue;

                if (firstChild instanceof ASTIdentifier){

                    String value;
                    if((value = identifierIsConstant(((ASTIdentifier) firstChild).val, symbolMethod)) == null)
                        return false;

                    firstChildValue = value.equals("true");


                } else {
                   if(!(firstChildValue = checkWhileOptimizedTemplate2((SimpleNode) firstChild, symbolClass, symbolMethod)))
                        return false;
                }

                if (secondChild instanceof ASTIdentifier){

                    String value;
                    if((value = identifierIsConstant(((ASTIdentifier) secondChild).val, symbolMethod)) == null)
                        return false;

                    secondChildValue = value.equals("true");

                } else {

                    if(!(secondChildValue = checkWhileOptimizedTemplate2((SimpleNode) firstChild, symbolClass, symbolMethod)))
                        return false;
                }

                return firstChildValue && secondChildValue;
            }
        }

        if (expression instanceof ASTLESSTHAN) {

            if(expression.jjtGetNumChildren() == 2) {
                if (expression.jjtGetChild(0) instanceof ASTLiteral && expression.jjtGetChild(1) instanceof ASTLiteral) {
                    return Integer.parseInt(((ASTLiteral) expression.jjtGetChild(0)).val) < Integer.parseInt(((ASTLiteral) expression.jjtGetChild(1)).val);
                }

                Node firstChild = expression.jjtGetChild(0);
                Node secondChild = expression.jjtGetChild(1);
                int firstChildValue;
                int secondChildValue;

                if (firstChild instanceof ASTIdentifier){
                    String value;
                    if((value = identifierIsConstant(((ASTIdentifier) firstChild).val, symbolMethod)) == null)
                        return false;

                    firstChildValue = Integer.parseInt(value);

                } else if (firstChild instanceof ASTLiteral)
                    firstChildValue = Integer.parseInt(((ASTLiteral) firstChild).val);
                else
                    return false;

                if (secondChild instanceof ASTIdentifier){

                    String value;
                    if((value = identifierIsConstant(((ASTIdentifier) secondChild).val, symbolMethod)) == null)
                        return false;

                    secondChildValue = Integer.parseInt(value);

                } else if (secondChild instanceof ASTLiteral)
                    secondChildValue = Integer.parseInt(((ASTLiteral) secondChild).val);
                else
                    return false;

                return firstChildValue < secondChildValue;
            }

        }

        if(expression instanceof ASTNegation){
            if(expression.jjtGetNumChildren() == 1) {
                return !checkWhileOptimizedTemplate2((SimpleNode) expression.jjtGetChild(0), symbolClass, symbolMethod);
            }
        }

        return false;
    }

    private String identifierIsConstant(String val, SymbolMethod symbolMethod) {

        if ((symbolMethod.symbolTable.get(val) != null) && (symbolMethod.symbolTable.get(val).constant != null)) {
            return symbolMethod.symbolTable.get(val).constant;
        }

        return null;
    }

    private boolean identifierIsConstant3(String val, SymbolMethod symbolMethod) {

        return (symbolMethod.symbolTable.get(val) != null) && (symbolMethod.symbolTable.get(val).constant != null);
    }

    private boolean canWhileBeOptimizedTemplate1(SimpleNode expression) {

        if (expression instanceof ASTBoolean)
            return true;

        if (expression instanceof ASTAND) {

            if(expression.jjtGetNumChildren() == 2) {

                return canWhileBeOptimizedTemplate1((SimpleNode) expression.jjtGetChild(0)) && canWhileBeOptimizedTemplate1((SimpleNode) expression.jjtGetChild(1));
            }
        }

        if (expression instanceof ASTLESSTHAN) {

            if(expression.jjtGetNumChildren() == 2) {
                if (expression.jjtGetChild(0) instanceof ASTLiteral && expression.jjtGetChild(1) instanceof ASTLiteral) {
                    return true;
                }
            }

        }

        if(expression instanceof ASTNegation){

            if(expression.jjtGetNumChildren() == 1) {
                return canWhileBeOptimizedTemplate1((SimpleNode) expression.jjtGetChild(0));
            }
        }

        return false;

    }

    private boolean checkWhileOptimized(SimpleNode expression) {

        if (expression instanceof ASTBoolean)
            return ((ASTBoolean) expression).val;

        if (expression instanceof ASTAND) {

            if(expression.jjtGetNumChildren() == 2) {

                return checkWhileOptimized((SimpleNode) expression.jjtGetChild(0)) && checkWhileOptimized((SimpleNode) expression.jjtGetChild(1));
            }
        }

        if (expression instanceof ASTLESSTHAN) {

            if(expression.jjtGetNumChildren() == 2) {
                if (expression.jjtGetChild(0) instanceof ASTLiteral && expression.jjtGetChild(1) instanceof ASTLiteral) {
                    return Integer.parseInt(((ASTLiteral) expression.jjtGetChild(0)).val) < Integer.parseInt(((ASTLiteral) expression.jjtGetChild(1)).val);
                }
            }

        }

        if(expression instanceof ASTNegation){
            if(expression.jjtGetNumChildren() == 1) {
                return !checkWhileOptimized((SimpleNode) expression.jjtGetChild(0));
            }
        }

        return false;
    }


    private void generateIfExpression(SimpleNode node, SymbolClass symbolClass, SymbolMethod symbolMethod) {
        this.loopCounter++;
        int thisCounter = this.loopCounter;

        SimpleNode expression = (SimpleNode) node.jjtGetChild(0);
        SimpleNode ifBlock = (SimpleNode) node.jjtGetChild(1);
        SimpleNode elseBlock = (SimpleNode) node.jjtGetChild(2);

        //**********Expression Test************
        if (!generateConditional(expression, symbolClass, symbolMethod, thisCounter, "if_", "_else", true))
            return;

        //**************************************

        // *********IF BLOCK*********************
        generateStatement(ifBlock, symbolClass, symbolMethod, true);
        this.bodyCode.append("\tgoto if_" + thisCounter + "_end\n");
        //************************** */

        //*******ELSE BLOCK ***********/
        this.bodyCode.append("if_" + thisCounter + "_else:\n");
        generateStatement(elseBlock, symbolClass, symbolMethod, true);
        this.bodyCode.append("if_" + thisCounter + "_end:\n");
        //******************************** */
    }

    private boolean generateConditional(SimpleNode expression, SymbolClass symbolClass, SymbolMethod symbolMethod, int thisCounter, String firstPartTag, String secondPartTag, boolean normal) {

        if (expression instanceof ASTBoolean) {
            generateBoolean((ASTBoolean) expression);
            reduceStack(1);

            this.bodyCode.append("\t" + ((normal) ? "ifeq" : "ifne") + " " + firstPartTag + thisCounter + secondPartTag + "\n");
            this.totalStack = 0;
            return true;

        }

        if (expression instanceof ASTLESSTHAN) {

            if (expression.jjtGetNumChildren() != 2)
                return false;


            // identifier < 0
            if (expression.jjtGetChild(1) instanceof ASTLiteral && ((ASTLiteral) expression.jjtGetChild(1)).val.equals("0")) {
                generateExpression((SimpleNode) expression.jjtGetChild(0), symbolClass, symbolMethod);

                this.bodyCode.append("\t" + (normal ? "ifge" : "iflt") + " " + firstPartTag + thisCounter + secondPartTag + "\n");
                reduceStack(1);
            } else {
                generateExpression((SimpleNode) expression.jjtGetChild(0), symbolClass, symbolMethod);
                generateExpression((SimpleNode) expression.jjtGetChild(1), symbolClass, symbolMethod);

                this.bodyCode.append("\t" + (normal ? "if_icmpge" : "if_icmplt") + " " + firstPartTag + thisCounter + secondPartTag + "\n");
                reduceStack(2);
            }

            this.totalStack = 0;
            return true;

        }

        if (expression instanceof ASTAND) {

            if (expression.jjtGetNumChildren() != 2)
                return false;

            // Code for first child
            generateExpression((SimpleNode) expression.jjtGetChild(0), symbolClass, symbolMethod);
            reduceStack(1);
            this.bodyCode.append("\tifeq " + firstPartTag + thisCounter + secondPartTag + "\n");

            //Code for second child
            generateExpression((SimpleNode) expression.jjtGetChild(1), symbolClass, symbolMethod);
            reduceStack(1);

            this.bodyCode.append("\t" + (normal ? "ifeq" : "ifne") + " " + firstPartTag + thisCounter + secondPartTag + "\n");
            this.totalStack = 0;

            return true;

        }

        if (expression instanceof ASTNegation) {

            if (expression.jjtGetNumChildren() != 1)
                return false;

            generateExpression((SimpleNode) expression.jjtGetChild(0), symbolClass, symbolMethod);
            reduceStack(1);
            this.bodyCode.append("\t" + (normal ? "ifne" : "ifeq") + " " + firstPartTag + thisCounter + secondPartTag + "\n");
            this.totalStack = 0;

            return true;

        }

        this.generateExpression(expression, symbolClass, symbolMethod);
        reduceStack(1);
        this.bodyCode.append("\t" + (normal ? "ifeq" : "ifne") + " " + firstPartTag + thisCounter + secondPartTag + "\n");
        this.totalStack = 0;

        return true;
    }

    private void generateMethodReturn(Type type) {

        if (type != null) {
            switch (type) {
                case BOOLEAN:
                case INT:
                    this.bodyCode.append("\tireturn\n");
                    incrementStack();
                    this.totalStack = 0;
                    break;
                case VOID:
                    this.bodyCode.append("\treturn\n");
                    this.totalStack = 0;
                    break;
                default:
                    this.bodyCode.append("\tareturn\n");
                    incrementStack();
                    this.totalStack = 0;
                    break;
            }
        }
    }

    private void generateOptimizedEquality(ASTEquality node, SymbolClass symbolClass, SymbolMethod symbolMethod, boolean insideIfOrWhile) {
        ASTIdentifier lhs = (ASTIdentifier) node.jjtGetChild(0);  //left identifier
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);  //right side

        if (symbolMethod.symbolTable.get(lhs.val) != null && !insideIfOrWhile) {
            // Se ainda não foi iniciada, vamos usar o valor
            if (!symbolMethod.symbolTable.get(lhs.val).inited) {
                symbolMethod.symbolTable.get(lhs.val).inited = true;

                if (rhs instanceof ASTLiteral) {
                    symbolMethod.symbolTable.get(lhs.val).constant = ((ASTLiteral) rhs).val;
                } else if (rhs instanceof ASTBoolean) {
                    symbolMethod.symbolTable.get(lhs.val).constant = String.valueOf(((ASTBoolean) rhs).val);
                }
            }
            // Se já foi iniciada e voltar a ser já não se pode usar o valor
            else
                symbolMethod.symbolTable.get(lhs.val).constant = null;

        } else if (symbolClass.symbolTableFields.get(lhs.val) != null) {

        }
    }

    private void generateEquality(ASTEquality node, SymbolClass symbolClass, SymbolMethod symbolMethod, boolean insideIfOrWhile) {
        ASTIdentifier lhs = (ASTIdentifier) node.jjtGetChild(0);  //left identifier
        SimpleNode rhs = (SimpleNode) node.jjtGetChild(1);  //right side

        //Special Case for element of array
        if (lhs.jjtGetNumChildren() != 0 && lhs.jjtGetChild(0) instanceof ASTaccessToArray) {

            storeElementOfArray(lhs, rhs, symbolClass, symbolMethod);

        } else if (symbolMethod.symbolTable.get(lhs.val) != null) {

            storeLocalVariable(lhs, rhs, symbolClass, symbolMethod);

        } else if (symbolClass.symbolTableFields.get(lhs.val) != null) {

            storeField(lhs, rhs, symbolClass, symbolMethod);
        }

        if (this.optimize)
            generateOptimizedEquality(node, symbolClass, symbolMethod, insideIfOrWhile);

        this.bodyCode.append("\n");
    }

    private void storeElementOfArray(ASTIdentifier lhs, SimpleNode rhs, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        ASTaccessToArray arrayAccess = (ASTaccessToArray) lhs.jjtGetChild(0);
        generateAccessToArray(lhs, arrayAccess, symbolClass, symbolMethod);
        generateExpression(rhs, symbolClass, symbolMethod);
        this.bodyCode.append("\tiastore\n");
        reduceStack(3);     //pop from the stack the arrayref, index and value
    }

    private void storeField(ASTIdentifier lhs, SimpleNode rhs, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        Type varType = symbolClass.symbolTableFields.get(lhs.val).getType();

        this.bodyCode.append("\taload_0\n");
        incrementStack();

        this.generateExpression(rhs, symbolClass, symbolMethod);
        this.bodyCode.append("\tputfield " + symbolClass.name + "/" + lhs.val + " " + getSymbolType(varType) + "\n");
        reduceStack(2); //pop from the stack objectref and value
    }

    private void storeLocalVariable(ASTIdentifier lhs, SimpleNode rhs, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        int index = symbolMethod.symbolTable.get(lhs.val).getIndex();

        //For iinc instruction
        if (generateIinc(lhs, rhs, index))
            return;

        this.generateExpression(rhs, symbolClass, symbolMethod);

        Type varType = symbolMethod.symbolTable.get(lhs.val).getType();

        String type = (varType == Type.INT || varType == Type.BOOLEAN) ? "i" : "a";

        String store = (index <= 3) ? "store_" : "store ";

        reduceStack(1);
        this.bodyCode.append("\t" + type + store + index + "\n");

    }

    private boolean generateIinc(ASTIdentifier lhs, SimpleNode rhs, int index) {

        if (rhs instanceof ASTSUM) {
            if (rhs.jjtGetNumChildren() == 2) {
                if (rhs.jjtGetChild(0) instanceof ASTIdentifier && rhs.jjtGetChild(1) instanceof ASTLiteral) {

                    ASTIdentifier identifier = (ASTIdentifier) rhs.jjtGetChild(0);

                    return generateIinc(lhs, identifier, (ASTLiteral) rhs.jjtGetChild(1), index);

                } else if (rhs.jjtGetChild(1) instanceof ASTIdentifier && rhs.jjtGetChild(0) instanceof ASTLiteral) {

                    ASTIdentifier identifier = (ASTIdentifier) rhs.jjtGetChild(1);

                    return generateIinc(lhs, identifier, (ASTLiteral) rhs.jjtGetChild(0), index);
                }
            }
        }

        return false;
    }

    private boolean generateIinc(ASTIdentifier lhs, ASTIdentifier identifier, ASTLiteral literal, int index) {

        if (identifier.val.equals(lhs.val)) {
            this.bodyCode.append("\tiinc " + index + " " + Integer.parseInt(literal.val) + "\n");

            return true;
        }

        return false;
    }

    private Type generateExpression(SimpleNode node, SymbolClass symbolClass, SymbolMethod symbolMethod) {
        if (node != null) {
            if (node instanceof ASTAND) {
                generateAnd((ASTAND) node, symbolClass, symbolMethod);
                return Type.BOOLEAN;

            } else if (node instanceof ASTLESSTHAN) {

                generateLessThan((ASTLESSTHAN) node, symbolClass, symbolMethod);
                return Type.BOOLEAN;

            } else if (node instanceof ASTSUM) {
                //constant folding
                if(this.optimize){
                    if(node.jjtGetChild(0) instanceof ASTLiteral && node.jjtGetChild(1) instanceof ASTLiteral){
                        int lhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(0)).val);
                        int rhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(1)).val);
                        loadIntLiteral(Integer.toString(lhs+rhs));
                    }
                }
                else{
                    generateOperation(node, symbolClass, symbolMethod);
                    this.bodyCode.append("\tiadd\n");
                    reduceStack(1);
                }
                return Type.INT;

            } else if (node instanceof ASTSUB) {
                //constant folding
                if(this.optimize){
                    if(node.jjtGetChild(0) instanceof ASTLiteral && node.jjtGetChild(1) instanceof ASTLiteral){
                        int lhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(0)).val);
                        int rhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(1)).val);
                        loadIntLiteral(Integer.toString(lhs-rhs));
                    }
                }
                else{
                    generateOperation(node, symbolClass, symbolMethod);
                    this.bodyCode.append("\tisub\n");
                    reduceStack(1);
                }
                return Type.INT;

            } else if (node instanceof ASTMUL) {
                //constant folding
                if(this.optimize){
                    if(node.jjtGetChild(0) instanceof ASTLiteral && node.jjtGetChild(1) instanceof ASTLiteral){
                        int lhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(0)).val);
                        int rhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(1)).val);
                        loadIntLiteral(Integer.toString(lhs*rhs));
                    }
                }
                else{                
                    generateOperation(node, symbolClass, symbolMethod);
                    this.bodyCode.append("\timul\n");
                    reduceStack(1);
                }
                return Type.INT;

            } else if (node instanceof ASTDIV) {
                //constant folding                
                if(this.optimize){
                    if(node.jjtGetChild(0) instanceof ASTLiteral && node.jjtGetChild(1) instanceof ASTLiteral){
                        int lhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(0)).val);
                        int rhs = Integer.parseInt(((ASTLiteral)node.jjtGetChild(1)).val);
                        loadIntLiteral(Integer.toString(lhs/rhs));
                    }
                }
                else{                
                    generateOperation(node, symbolClass, symbolMethod);
                    this.bodyCode.append("\tidiv\n");
                    reduceStack(1);
                }
                return Type.INT;

            } else if (node instanceof ASTIdentifier) {

                return generateIdentifier((ASTIdentifier) node, symbolClass, symbolMethod);

            } else if (node instanceof ASTLiteral) {
                loadIntLiteral(((ASTLiteral) node).val);
                return Type.INT;

            } else if (node instanceof ASTNewObject) {
                ASTNewObject object = (ASTNewObject) node;
                generateNewObject(object, symbolClass, symbolMethod);
                return Type.OBJECT;

            } else if (node instanceof ASTInitializeArray) {
                generateArrayInitialization((ASTInitializeArray) node, symbolClass, symbolMethod);
                return Type.INT_ARRAY;

            } else if (node instanceof ASTNegation) {
                generateNegation((ASTNegation) node, symbolClass, symbolMethod);
                return Type.BOOLEAN;

            } else if (node instanceof ASTBoolean) {
                this.generateBoolean((ASTBoolean) node);
                return Type.BOOLEAN;

            } else if (node instanceof ASTDotExpression) {
                return this.generateDotExpression(node, symbolClass, symbolMethod);
            }
        }

        return null;
    }

    private Type generateIdentifier(ASTIdentifier node, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (node.jjtGetNumChildren() == 1) {

            Type returnType = null;
            if (node.jjtGetChild(0) instanceof ASTaccessToArray) {

                returnType = generateAccessToArray(node, (ASTaccessToArray) node.jjtGetChild(0), symbolClass, symbolMethod);

                this.bodyCode.append("\tiaload\n");
                reduceStack(1);     //pop from the stack the arrayref and the index and push the value

                if (returnType == Type.INT_ARRAY)
                    return Type.INT;
                else if (returnType == Type.STRING_ARRAY)
                    return Type.STRING;
            }

            return returnType;
        }

        return loadVariable(node.val, symbolClass, symbolMethod);

    }

    private void generateAnd(ASTAND node, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (node.jjtGetNumChildren() != 2)
            return;

        this.loopCounter++;
        int thisCounter = this.loopCounter;

        // Code for first child
        generateExpression((SimpleNode) node.jjtGetChild(0), symbolClass, symbolMethod);
        reduceStack(1);
        this.bodyCode.append("\tifeq AND_" + thisCounter + "\n");

        //Code for second child
        generateExpression((SimpleNode) node.jjtGetChild(1), symbolClass, symbolMethod);
        reduceStack(1);
        this.bodyCode.append("\tifeq AND_" + thisCounter + "\n");

        //If both are true
        // *********IN CASE EXPRESSION IS TRUE *********************
        this.bodyCode.append("\ticonst_1\n");
        this.bodyCode.append("\tgoto AND_" + thisCounter + "_end\n");
        //************************** */

        //******* IN CASE EXPRESSION IS FALSE ***********/
        this.bodyCode.append("AND_" + thisCounter + ":\n");
        this.bodyCode.append("\ticonst_0\n");
        this.bodyCode.append("AND_" + thisCounter + "_end:\n");
        //******************************** */

        incrementStack();
    }

    private void generateLessThan(ASTLESSTHAN node, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (node.jjtGetNumChildren() != 2)
            return;

        this.loopCounter++;
        int thisCounter = this.loopCounter;

        // identifier < 0
        if (node.jjtGetChild(1) instanceof ASTLiteral && ((ASTLiteral) node.jjtGetChild(1)).val.equals("0")) {
            generateExpression((SimpleNode) node.jjtGetChild(0), symbolClass, symbolMethod);
            this.bodyCode.append("\tifge lessThan_" + thisCounter + "\n");
            reduceStack(1);
        } else {
            generateExpression((SimpleNode) node.jjtGetChild(0), symbolClass, symbolMethod);
            generateExpression((SimpleNode) node.jjtGetChild(1), symbolClass, symbolMethod);
            reduceStack(2);
            this.bodyCode.append("\tif_icmpge lessThan_" + thisCounter + "\n");
        }

        // *********IN CASE EXPRESSION IS TRUE *********************
        this.bodyCode.append("\ticonst_1\n");
        this.bodyCode.append("\tgoto lessThan_" + thisCounter + "_end\n");
        // **************************

        // ******* IN CASE EXPRESSION IS FALSE
        this.bodyCode.append("lessThan_" + thisCounter + ":\n");
        this.bodyCode.append("\ticonst_0\n");
        this.bodyCode.append("lessThan_" + thisCounter + "_end:\n");
        // ********************************

        incrementStack();

    }


    private void generateNegation(ASTNegation node, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (node.jjtGetNumChildren() != 1)
            return;

        this.loopCounter++;
        int thisCounter = this.loopCounter;

        generateExpression((SimpleNode) node.jjtGetChild(0), symbolClass, symbolMethod);

        reduceStack(1);
        this.bodyCode.append("\tifne negation_" + thisCounter + "\n");

        // *********IN CASE EXPRESSION IS TRUE *********************
        this.bodyCode.append("\ticonst_1\n");
        this.bodyCode.append("\tgoto negation_" + thisCounter + "_end\n");
        //************************** */

        //******* IN CASE EXPRESSION IS FALSE ***********/
        this.bodyCode.append("negation_" + thisCounter + ":\n");
        this.bodyCode.append("\ticonst_0\n");
        this.bodyCode.append("negation_" + thisCounter + "_end:\n");
        //******************************** */

        incrementStack();

    }

    private void generateBoolean(ASTBoolean node) {
        this.bodyCode.append("\ticonst_" + ((node.val) ? "1\n" : "0\n"));
        incrementStack();
    }

    private void generateOperation(SimpleNode operation, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (operation.jjtGetNumChildren() != 2)
            return;

        generateExpression((SimpleNode) operation.jjtGetChild(0), symbolClass, symbolMethod);
        generateExpression((SimpleNode) operation.jjtGetChild(1), symbolClass, symbolMethod);
    }

    private Type loadVariable(String val, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        if (this.optimize) {
            if ((symbolMethod.symbolTable.get(val) != null) && (symbolMethod.symbolTable.get(val).constant != null)) {
                if (!symbolMethod.symbolTable.get(val).changedInIfOrWile) {
                    if (symbolMethod.symbolTable.get(val).getType() == Type.INT) {
                        loadIntLiteral(symbolMethod.symbolTable.get(val).constant);
                        return Type.INT;
                    }
                }
            }
        }

        if (val.equals("this")) {
            this.bodyCode.append("\taload_0\n");
            incrementStack();
            return Type.OBJECT;
        }

        if (symbolMethod.symbolTable.get(val) != null) {
            Type varType = symbolMethod.symbolTable.get(val).getType();
            int index = symbolMethod.symbolTable.get(val).getIndex();

            String type = (varType == Type.INT || varType == Type.BOOLEAN) ? "i" : "a";
            String store = (index <= 3) ? "load_" : "load ";

            this.bodyCode.append("\t" + type + store + index + "\n");
            incrementStack();

            return varType;

        }

        if (symbolClass.symbolTableFields.get(val) != null) {

            Type varType = symbolClass.symbolTableFields.get(val).getType();

            this.bodyCode.append("\taload_0\n");
            incrementStack();

            this.bodyCode.append("\tgetfield " + symbolClass.name + "/" + val + " " + getSymbolType(varType) + "\n");
            incrementStack();
            reduceStack(1);

            return varType;

        }

        return null;
    }

    private void loadIntLiteral(String val) {
        String output = "";
        int value = Integer.parseInt(val);

        if ((value >= 0) && (value <= 5)) {
            output += "\ticonst_" + value;

        } else if (value == -1) {
            output += "\ticonst_m1";

        } else if (value > -129 && value < 128) {
            output += "\tbipush " + value;

        } else if (value > -32769 && value < 32768) {
            output += "\tsipush " + value;

        } else {
            output += "\tldc " + value;

        }
        this.bodyCode.append(output + "\n");
        incrementStack();
    }

    private void generateNewObject(ASTNewObject object, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        this.bodyCode.append("\tnew " + object.val + "\n\tdup\n");
        incrementStack();   //increment new object
        incrementStack();   //increment dup

        ArrayList<Type> methodCallTypes = processArgs(object, symbolClass, symbolMethod);

        StringBuilder callArgs = new StringBuilder();
        //Get list of arguments type
        if (methodCallTypes.size() > 0) {
            for (Type t : methodCallTypes) {
                if (t != null) {
                    callArgs.append(getSymbolType(t));
                }
            }
        }

        int decrement = methodCallTypes.size() + 1;

        this.bodyCode.append("\tinvokespecial " + object.val + "/<init>(" + callArgs + ")V\n");
        reduceStack(decrement);

    }

    private void generateArrayInitialization(ASTInitializeArray arrayInit, SymbolClass symbolClass, SymbolMethod symbolMethod) {
        this.generateExpression((SimpleNode) arrayInit.jjtGetChild(0), symbolClass, symbolMethod);

        this.bodyCode.append("\tnewarray int\n");
        incrementStack();
        reduceStack(1);
    }

    private Type generateAccessToArray(ASTIdentifier node, ASTaccessToArray arrayAccess, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        Type returnType = loadVariable(node.val, symbolClass, symbolMethod);

        this.generateExpression((SimpleNode) arrayAccess.jjtGetChild(0), symbolClass, symbolMethod);

        return returnType;
    }

    private Type generateDotExpression(SimpleNode node, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        SimpleNode leftPart = (SimpleNode) node.jjtGetChild(0);
        SimpleNode rightPart = (SimpleNode) node.jjtGetChild(1);

        if (rightPart instanceof ASTIdentifier) {

            ASTIdentifier rightIdentifier = (ASTIdentifier) node.jjtGetChild(1);

            if (leftPart instanceof ASTIdentifier) {
                ASTIdentifier leftIdentifier = (ASTIdentifier) node.jjtGetChild(0);

                if (leftIdentifier.val.equals("this")) {
                    return generateThisStatement(leftIdentifier, rightIdentifier, symbolClass, symbolMethod);
                }

                if (rightIdentifier.val.equals("length")) {

                    this.generateExpression(leftIdentifier, symbolClass, symbolMethod);
                    incrementStack();
                    reduceStack(1);
                    this.bodyCode.append("\tarraylength\n");
                    return Type.INT;

                }

                return generateCall(leftIdentifier, rightIdentifier, symbolClass, symbolMethod);

            }

            if (leftPart instanceof ASTNewObject) {

                ASTNewObject leftIdentifier = (ASTNewObject) node.jjtGetChild(0);

                return generateCallObject(leftIdentifier, rightIdentifier, symbolClass, symbolMethod);

            }
        }

        return null;
    }


    private Type generateCallObject(ASTNewObject node1, ASTIdentifier node2, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        this.generateExpression(node1, symbolClass, symbolMethod);

        if (symbolTable.containsKey(node1.val)) {

            if (symbolTable.get(node1.val) instanceof SymbolClass) {

                SymbolClass sc = (SymbolClass) symbolTable.get(node1.val);
                return generateCallForMethod(node2, sc, symbolClass, symbolMethod, true);
            }
        }

        return null;

    }

    private Type generateThisStatement(ASTIdentifier identifier1, ASTIdentifier identifier2, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        this.generateExpression(identifier1, symbolClass, symbolMethod);

        //if it is a call for variable/field
        if (!identifier2.method) {
            if (symbolClass.symbolTableFields.containsKey(identifier2.val)) {
                return symbolClass.symbolTableFields.get(identifier2.val).getType();
            }

            return null;

        }

        //Check if current class has any method with the same signature
        if (symbolClass.symbolTableMethods.containsKey(identifier2.val)) {

            return generateCallForMethod(identifier2, symbolClass, symbolClass, symbolMethod, true);
        }

        //check if method is defined in super class, if it is not defined in the current class
        if (symbolTable.containsKey(symbolClass.superClass)) {

            SymbolClass sc = (SymbolClass) symbolTable.get(symbolClass.superClass);
            return generateCallForMethod(identifier2, sc, symbolClass, symbolMethod, true);
        }

        return null;
    }

    private Type generateCall(ASTIdentifier identifier1, ASTIdentifier identifier2, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        //Import
        if (symbolTable.containsKey(identifier1.val)) {
            if (symbolTable.get(identifier1.val) instanceof SymbolClass) {
                SymbolClass sc = (SymbolClass) symbolTable.get(identifier1.val);

                return generateCallForMethod(identifier2, sc, symbolClass, symbolMethod, false);
            }

            return null;
        }

        //Verify if first part of dot expression was declared inside the class or method
        if (symbolMethod.symbolTable.containsKey(identifier1.val) || symbolClass.symbolTableFields.containsKey(identifier1.val)) {
            if (symbolMethod.symbolTable.containsKey(identifier1.val)) {

                this.generateExpression(identifier1, symbolClass, symbolMethod);

                if (symbolMethod.symbolTable.get(identifier1.val).getType().equals(Type.OBJECT)) {

                    SymbolClass sc = (SymbolClass) symbolTable.get(symbolMethod.symbolTable.get(identifier1.val).getObject_name());
                    return generateCallForMethod(identifier2, sc, symbolClass, symbolMethod, true);
                }
            }

            return null;
        }

        return null;
    }

    private Type generateCallForMethod(ASTIdentifier identifier2, SymbolClass classOfMethod, SymbolClass symbolClass, SymbolMethod symbolMethod, boolean virtual) {

        //Check for methods
        if (identifier2.method) {

            ArrayList<Type> methodCallTypes = processArgs(identifier2, symbolClass, symbolMethod);

            //Get return type of method
            Type returnType = getReturnTypeMethod(classOfMethod, methodCallTypes, identifier2);
            if (returnType == null) returnType = Type.VOID;

            StringBuilder callArgs = new StringBuilder();
            //Get list of arguments type
            if (methodCallTypes.size() > 0) {
                for (Type t : methodCallTypes) {
                    if (t != null) {
                        callArgs.append(getSymbolType(t));
                    }
                }
            }


            String methodName = identifier2.val;
            String methodType = getSymbolType(returnType);
            String objectName = classOfMethod.name;

            int decrement = methodCallTypes.size();
            if (virtual) decrement++;
            if (returnType != Type.VOID) decrement--;

            this.bodyCode.append("\t" + ((virtual) ? "invokevirtual " : "invokestatic ") + objectName + "/" + methodName + "(" + callArgs + ")" + methodType + "\n\n");
            reduceStack(decrement);

            return returnType;
        }

        return null;
    }

    private Type getReturnTypeMethod(SymbolClass symbolClass, ArrayList<Type> methodCallTypes, ASTIdentifier identifier) {

        if (symbolClass.symbolTableMethods.containsKey(identifier.val)) {
            return checkIfMethodExists(symbolClass.symbolTableMethods.get(identifier.val), methodCallTypes);
        }

        return null;
    }

    private ArrayList<Type> processArgs(SimpleNode node, SymbolClass symbolClass, SymbolMethod symbolMethod) {

        ArrayList<Type> methodSignature = new ArrayList<>();

        for (int i = 0; i < node.jjtGetNumChildren(); i++) {
            methodSignature.add(this.generateExpression((SimpleNode) node.jjtGetChild(i), symbolClass, symbolMethod));
        }

        return methodSignature;
    }

    private Type checkIfMethodExists(ArrayList<SymbolMethod> methodArrayList, ArrayList<Type> methodSignature) {
        for (SymbolMethod sm : methodArrayList) {
            //If it has the same signature
            if (methodSignature.size() == sm.types.size()) {
                if (sm.types.equals(methodSignature))
                    return sm.getType();
            }
        }
        return null;
    }

    private void incrementStack() {
        this.totalStack++;
        if (this.totalStack > this.maxStack) {
            this.maxStack = this.totalStack;
        }
    }

    private void reduceStack(int decrement) {
        this.totalStack -= decrement;
    }


    private PrintWriter createOutputFile(String className) {
        try {
            File dir = new File("src/jasmin/");
            if (!dir.exists())
                dir.mkdirs();

            File file = new File(dir + "/" + className + ".j");
            if (!file.exists())
                file.createNewFile();

            return new PrintWriter(file);

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
