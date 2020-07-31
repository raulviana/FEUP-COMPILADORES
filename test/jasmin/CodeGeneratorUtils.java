package jasmin;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Pattern;

import pt.up.fe.specs.util.utilities.StringLines;
import pt.up.fe.specs.util.SpecsStrings;


public class CodeGeneratorUtils {
/*
  public static String getBetweenStrings(String text,String textFrom,String textTo) {
    String result = "";
    result = text.substring(text.indexOf(textFrom) + textFrom.length(),text.length());
    result =result.substring(0,result.indexOf(textTo));
    return result;
  }
 */ 
  
  public static String getMethod(String code, String methodName) {	
	var regex = "\\.method\\s+public\\s+"+methodName+"((.|\\s)+?)\\.end\\s+method";
	var results = SpecsStrings.getRegex(code, regex);
	
	if(results.isEmpty()) {
		throw new RuntimeException("Could not find method '"+methodName+"' in the following code:\n" + code);
	}	
	
	return results.get(0);
  }

  public static String getLimit(String method, String limitType) {	
	var regex = "\\.limit\\s+"+limitType+"\\s+(.+?)(\\n|\\r)";
	var results = SpecsStrings.getRegex(method, regex);
	
	if(results.isEmpty()) {
		throw new RuntimeException("Could not find limit '"+limitType+"' in the following code:\n" + method);
	}	

	return results.get(0);	
	
  }

  public static String getExprIndex(String method, String expression){
	  
	var regex = expression + "((.|\\s)+?)(\\n|\\r)";
	var results = SpecsStrings.getRegex(method, regex);
	
	if(results.isEmpty()) {
		throw new RuntimeException("Could not find expression '"+expression+"' in the following code:\n" + method);
	}		  
		  
    String index = results.get(0);

    String[] tokens = index.split("_|\\s");
    if (tokens.length < 2)
      return "";
    return tokens[1];
  }

  public static int getMethodLimit(String code, String methodName, String limitType) {
    String method = "";
    String limit = "";
    method = CodeGeneratorUtils.getMethod(code, methodName);
    limit = CodeGeneratorUtils.getLimit(method, limitType);
    int limitValue;
    try {
        limitValue = Integer.parseInt(limit);
    }
    catch (NumberFormatException e){
        limitValue = -1;
    }
    return limitValue;
  }

  public static boolean codeContainsExpressionRegex(String code, String regex) {
	  Pattern pattern = Pattern.compile(regex, Pattern.DOTALL | Pattern.MULTILINE);

	  return SpecsStrings.matches(code,pattern);
  }

  public static boolean codeContainsExpression(String code, String[] expressions) {
    boolean ret = false;
    for(int i = 0; i < expressions.length; i++){
        ret |= code.contains(expressions[i]);
    }
    return ret;
  }

  public static boolean methodContainsExpression(String code, String methodName, String[] expressions) {
    String method = "";
    method = CodeGeneratorUtils.getMethod(code, methodName);
    method = method.replaceAll("\\s","");
    boolean ret = false;
    for(int i = 0; i < expressions.length; i++){
        String str = expressions[i].replaceAll("\\s","");
        ret |= method.contains(str);
    }
    return ret;
  }

  public static int getMehtodExprIndex(String code, String methodName, String expression) {
    String method = "";
    String index = "";
    method = CodeGeneratorUtils.getMethod(code, methodName);
    index = CodeGeneratorUtils.getExprIndex(method, expression);
    int indexValue;
    try {
        indexValue = Integer.parseInt(index);
    }
    catch (NumberFormatException e){
        indexValue = -1;
    }
    return indexValue;
  }


}