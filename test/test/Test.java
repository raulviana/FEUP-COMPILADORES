class Test {

    boolean booleanVar;

    public boolean setBooleanVar(boolean booleanVar1) {

        booleanVar = booleanVar1;

        return true;

    }

    public boolean getBooleanVar() {

        return booleanVar;
    }

    public static void main(String[] args) {

        Test conditionalTest;

        conditionalTest = new Test();
        conditionalTest.setBooleanVar(true);

        while (conditionalTest.getBooleanVar()) {

            conditionalTest.setBooleanVar(false);
        }

    }


}