package hangman;

import java.io.*;
import java.util.*;

public class Main {

  public static void main(String[] args){

    if (args.length < 1){
      System.out.println("\n\tERROR: Please enter a File, word length, and number of guesses\n");
      System.exit(0);
    }
    String dictionaryFile = args[0];

    if (args.length < 2){
      System.out.println("\n\tERROR: Please enter a word length and a number of guesses\n");
      System.exit(0);
    }
    int wordLength = Integer.parseInt(args[1]);

    if (args.length < 3){
      System.out.println("\n\tERROR: Please enter a number of guesses\n");
      System.exit(0);
    }
    int guesses = Integer.parseInt(args[2]);
    File file = new File(dictionaryFile);

    EvilHangmanGame game = new EvilHangmanGame();

    game.startGame(file, wordLength);

    Scanner scanner = new Scanner(System.in);
    String guess = "";
    boolean correctFormat;
    Set<String> words = new TreeSet<String>();

    while (guesses != 0){
      System.out.println(String.format("\nYou Have %d Guesses Left\nUsed Letters: %s\nWord: %s", guesses, game.getGuessedChars(), game.getWord()));

      correctFormat = false;
      while (!correctFormat){
        System.out.print("Enter Guess: ");
        if (scanner.hasNext("[a-zA-Z]")){
          guess = scanner.next();
          guess.toLowerCase();
          try{
            words = game.makeGuess(guess.charAt(0));
          } catch(IEvilHangmanGame.GuessAlreadyMadeException ex) {
            System.out.println("Guess Already Made, Guess Again");
            continue;
          }
          correctFormat = true;
        } else {
          System.out.println("Invalid Input");
          scanner.next();
        }
      }

      int count = game.getCharCount(game.getWord(), guess.charAt(0));

      if (!game.getWord().contains("-")) {
        System.out.println("You Win!");
        System.out.println("The Word: " + game.getWord());
        System.exit(0);
      }

      if (count == 0){
        if (guesses == 1){break;}
        System.out.println(String.format("Sorry, there are no %s's", guess));
        guesses--;
      } else if (count == 1){
        System.out.println(String.format("Yes, there is 1 %s", guess));
      } else {
        System.out.println(String.format("Yes, there are %d %s's", count, guess));
      }
    }

    String correctWord = null;
    for (String s : words){
      correctWord = s;
      break;
    }

    System.out.println(String.format("You Lose!\nThe word was: %s", correctWord));

  }

}
