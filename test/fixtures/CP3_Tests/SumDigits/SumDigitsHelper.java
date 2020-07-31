
import java.util.Scanner;

public class SumDigitsHelper {
    Scanner input = new Scanner(System.in);

    public SumDigitsHelper(){}

    public int readNumber() {
        System.out.println("Number to sum digits: ");
        return input.nextInt();
    }

    public int countDigits(int number){
        int counter = 0;
        while(number!=0){
            number /= 10;
            counter++;
        }
        return counter;
    }

    public void printResult(int result, int number) {
        System.out.println("Number: " + number);
        System.out.println("Sum of digits: " + result);
    }

    public int getLastNumber(int number){
        return number%10;
    }

}