package hangman;

import java.io.*;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
  //@SuppressWarnings("serial")
	//public static class GuessAlreadyMadeException extends Exception { }

  private TreeSet<String> set;

	public void startGame(File dictionary, int wordLength){
    try{
      Scanner scanner = new Scanner(dictionary);
      set = new TreeSet<String>();
      while (scanner.hasNext()){
        set.add(scanner.next());
      }

      scanner.close();
    } catch (IOException ex){
      System.out.println("Error: File not found");
    }
  }

	public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException{



    return set;
  }
}
