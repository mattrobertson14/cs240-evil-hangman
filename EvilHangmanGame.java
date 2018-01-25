package hangman;

import java.io.*;
import java.util.*;
import java.util.regex.Pattern;

public class EvilHangmanGame implements IEvilHangmanGame {

  // Variables that need to keep there state between function calls
  private String wordSoFar;
  private TreeSet<String> words;
  private TreeSet<Character> guessedChars;

	public void startGame(File dictionary, int wordLength){

    // Make sure that the dictinary file isn't empty
    if (dictionary.length() == 0){
      System.out.println(String.format("Error: Dictionary file, %s, is empty. Please choose another file.", dictionary.getName()));
      System.exit(0);
    }

     // Instantiate words, guessedChars, wordSoFar,
    //   and scanner at the start of each game
    words = new TreeSet<String>();
    guessedChars = new TreeSet<Character>();
    wordSoFar = "";
    Scanner scanner = null;

    // Read in dictionary file
    try{
      scanner = new Scanner(dictionary);
      String next;

      // Grab every word inside of the file
      while (scanner.hasNext()){
        next = scanner.next();

        // Only add the words with the correct length to the set
        if (next.length() == wordLength){
          next.toLowerCase();
          words.add(next);
        }
      }

      // Make sure at least one word of the correct length exists
      if (words.size() == 0){
        System.out.println(String.format("Error: There are no words of length %d in your dictionary file: %s. Please choose another word length.", wordLength, dictionary.getName()));
        System.exit(0);
      }

      // Make word blank (e.x. 4 letter word = "----")
      for (int i = 0; i < wordLength; i++){
        wordSoFar += "-";
      }

    } catch (IOException ex){ // catch any IOExceptions that happen
      System.out.println("Error: File not found. Please choose another file.");
      System.exit(0);

    } finally { // make sure to close the scanner no matter what
      if (scanner != null){
        scanner.close();
      }
    }
  }

	public Set<String> makeGuess(char guess) throws IEvilHangmanGame.GuessAlreadyMadeException{

    // Create new exception to throw if guess has already been made
    IEvilHangmanGame.GuessAlreadyMadeException ex = new IEvilHangmanGame.GuessAlreadyMadeException();

    // Check if the character has been guessed already
    if (guessedChars.contains(guess)){
      throw ex; // throw exception to caller
    }

    // Create new map every guess to run evil hangman algorithm
    Map<String, TreeSet<String>> map = new HashMap<String, TreeSet<String>>();
    TreeSet<String> set; // set variable to be used inside the for loop

    // Evil Hangman Algorithm. This is where the map is created
    for (String s : words){
      String key = getPattern(s, guess);
      if (map.containsKey(key)){
        set = map.get(key); // have variable point to existing set.
      } else {
        set = new TreeSet<String>(); // have variable point to new set
      }
      set.add(s);           // add string to set
      map.put(key, set);    // add key(String pattern) and value (set)
    }

    boolean firstPass = true;

    // Find out which set to return (which one is the largest->contains no )
    for(Map.Entry<String, TreeSet<String>> entry : map.entrySet()){

      // We start by setting the first set we come to to the dictionary and
      //    then start comparing the rest of them.
      if (firstPass){
        words = entry.getValue();
        firstPass = false;
        wordSoFar = entry.getKey();
        continue;
      }

      // Check which set should be returned: largest -> contains no instances of the char ->
      //    right most instance of the char
      if (entry.getValue().size() > words.size()){
        words = entry.getValue();
        wordSoFar = entry.getKey();
      } else if (entry.getValue().size() == words.size()){
        int x = getCharCount(entry.getKey(), guess);
        int y = getCharCount(words.first(), guess);

        if (x == 0 && y != 0){
          words = entry.getValue();
          wordSoFar = entry.getKey();
        } else if (y == 0 && x != 0){
          continue;
        } else if (x < y) {
          words = entry.getValue();
          wordSoFar = entry.getKey();
        } else if (y < x) {
          continue;
        } else if (x == y){
          int m = 0;
          int n = 0;
          while(m == n){
            m = getRightMostPosition(entry.getKey(), guess);
            n = getRightMostPosition(words.first(), guess);

            if (m < n){
              words = entry.getValue();
              wordSoFar = entry.getKey();
            }
          }
        }
      }
    }

    guessedChars.add(guess); //

    return words; // Add correct set of words a.k.a. the new dictionary
  }


  // HELPER FUNCTION to get key value for map depending on
  //    how many instances of a certain character there are
  public String getPattern(String str, char character){
    StringBuilder sb = new StringBuilder();
    char[] arr = str.toCharArray();
    for (int i = 0; i < arr.length; i++){
      if (arr[i] == character){
        sb.append(character);
      } else {
        sb.append(wordSoFar.charAt(i));
      }
    }

    return sb.toString();
  }

  public String getGuessedChars(){
    StringBuilder sb = new StringBuilder();
    for (char c : guessedChars){
      sb.append(c + " ");
    }
    return sb.toString();
  }

  public String getWord(){
    return wordSoFar;
  }

  public int getCharCount(String word, char character){
    int total = 0;
    char[] arr = word.toCharArray();
    for (char c : arr){
      if (c == character){
        total++;
      }
    }
    return total;
  }

  public int getRightMostPosition(String word, char character){
    int index = 0;
    char[] arr = word.toCharArray();
    for (int i = arr.length-1; i >= 0; i--){
      index++;
      if (arr[i] == character){
        return index;
      }
    }
    return index;
  }


}
