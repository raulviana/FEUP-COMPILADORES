class PrintArray {

    public static void main(String[] args) {
    }

    PrintArray() {}

    public void printElement(int pos, int value) {
        System.out.println("Element " + pos + ": " + value);
    }

    public void printLength(int length){
        System.out.println("Array length: " + length);
    }

    public void printTitle(){
        System.out.println("---Array---");
    }

    public void printArray(int[] array){
        printTitle();
        for(int i=0;i<array.length;i++){
            printElement(i+1, array[i]);
        }
    }
}