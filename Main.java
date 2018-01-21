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
    int maxGuesses = Integer.parseInt(args[2]);
    File file = new File(dictionaryFile);

    EvilHangmanGame game = new EvilHangmanGame();

    int guesses = 0;
    game.startGame(file, wordLength);

    while (guesses < maxGuesses){
      System.out.println("guess made");
      guesses++;
    }

  }

}
