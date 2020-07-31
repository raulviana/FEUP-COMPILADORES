package symbolTable;

public class SymbolVar extends Symbol {

    Initialized initialized = Initialized.NOT_INITIALIZED;

    public String constant = null;
    public boolean inited = false;
    public boolean changedInIfOrWile = false;

    public SymbolVar(String name) {
        super(name);
    }

    public Initialized getInitialized() {
        return initialized;
    }

    public void updateInitialized(boolean partially) {
        if (partially && initialized != Initialized.INITIALIZED)
            initialized = Initialized.PARTIALLY_INITIALIZED;
        else
            initialized = Initialized.INITIALIZED;
    }

    public void dump(String prefix) {

        System.out.println(prefix + "Variable: " + this.name);

        String ifIsObject = "";
        if(this.type == Type.OBJECT)
            ifIsObject = ": " + this.object_name;

        System.out.println(prefix + "  Type: " + this.type + ifIsObject);

    }

    public void setInitialized(Initialized initialized) {
        this.initialized = initialized;
    }
}
