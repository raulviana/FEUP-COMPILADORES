package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolMethod extends Symbol {

    public int num;

    public HashMap<String, SymbolVar> symbolTable = new HashMap<>();

    public ArrayList<Type> types = new ArrayList<>();

    public SymbolMethod(String name) {
        super(name);
    }

    public void addSymbol(String name, SymbolVar symbol) {
        this.symbolTable.put(name, symbol);
    }

    public SymbolVar getSymbol(String name) {return this.symbolTable.get(name); }

    public void addType(Type type) { this.types.add(type); }

    public void dump(String prefix) {

        System.out.println(prefix + "Method: " + this.name);

        String ifIsObject = "";
        if(this.type == Type.OBJECT)
            ifIsObject = ": " + this.object_name + " ";

        String ifIsStatic = "";
        if(this.isStatic)
            ifIsObject = " is static";

        System.out.println(prefix + "  Return Type: " + this.type + ifIsObject + ifIsStatic);

        String methodSignature = "";

        for(int i = 0; i < this.types.size(); i++){
            methodSignature += this.types.get(i) + " ";
        }

        System.out.println(prefix + "  Method Signature: " + methodSignature);

        if(this.symbolTable.size() > 0)
            System.out.println(prefix + "  Local Variables: ");

        for(Map.Entry<String, SymbolVar> entry : this.symbolTable.entrySet()) {

            entry.getValue().dump(prefix + "   ");
        }

    }
}
