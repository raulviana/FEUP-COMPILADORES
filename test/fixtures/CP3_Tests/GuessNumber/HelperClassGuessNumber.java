
import java.util.Random;
import java.util.Scanner;

public class HelperClassGuessNumber {

    int numberToGuess;
    Scanner input = new Scanner(System.in);

    public HelperClassGuessNumber(boolean random, int number){

        if(random) {
            Random rand = new Random();
            numberToGuess = rand.nextInt(number);
        } else
            numberToGuess = number;
    }

    public void printLowGuess(){

        System.out.println("Your Guess is too low");
    }

    public void highLowGuess() {

        System.out.println("Your Guess is too high");
    }

    public int getNumberToGuess() {
        return numberToGuess;
    }

    public int getGuessedNumber() {
        System.out.println("Guess a number between 1 and 1000: ");
        return input.nextInt();
    }

    public void printWon(int numberOfTries) {
        System.out.println("You win!");
        System.out.println("The number was " + numberToGuess);
        System.out.println("It took you " + numberOfTries + " tries");
    }

    public void printLost(int numberOfTries) {
        System.out.println("You lost!");
        System.out.println("The number was " + numberToGuess);
        System.out.println("You didn't guess in " + numberOfTries + " tries");
    }
}