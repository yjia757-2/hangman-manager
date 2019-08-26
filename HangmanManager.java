// Yiran Jia
// 4/26/18
// CSE143
// TA: JASON WAATAJA 
// Assignment 4: Evil Hangman 
//
// This programs keeps track of the state of a game of hangman. Unlike the usual 
// hangman game, this program keeps updating a wide selection of words with the same 
// pattern based on what the user has guessed. It delays picking a word until it 
// is forced to.  

import java.util.*;
public class HangmanManager {
   private Set<String> readyWords;
   private SortedSet<Character> guessedLetters;
   private String nowPattern; 
   private int leftGuess; 

   // pre: word length is at least 1 and maximum number of wrong guesses is at least 0 
   // (throw an IllegalArgumentException if not)
   // post: construct a set of words with the given word length. Words are selected 
   // from the dictionary. There is no duplicates of words in the set. Construct 
   // a set of characters that stores the letters the user guessed. Initialize the 
   // pattern that will be displayed for the game. Take the information of the 
   /// maximal number of guesses allowed in. 
   public HangmanManager (Collection<String> dictionary, int length, int max) {
      if(length < 1 || max < 0) {
         throw new IllegalArgumentException();
      }
      readyWords =  new TreeSet<String>(); 
      for (String word : dictionary) {
         if (word.length() == length) {
            readyWords.add(word);
         }
      }
      guessedLetters = new TreeSet<Character>(); 
      nowPattern = "-";
      for (int i = 1; i < length; i++) {
         nowPattern += " -";
      }  
      leftGuess = max;      
   } 
    
   // post: return the current set of words being considered by the hangman manager
   public Set<String> words() {
      return readyWords;
   } 
      
   // post: return how many guesses the player has left
   public int guessesLeft() {
      return leftGuess;
   }
    
   // post: return a set of all letters that have been guessesd by the user. 
   // the letters are displayed alphabetically
   public Set<Character> guesses() { 
      return guessedLetters;
   }

   // pre: the set of words is not empty (throw an IllegalStateException if not)
   // post: return the current pattern to be displayed for the hangman game taking
   // into account guesses that have been made. Letters that have not yet been guessed
   // are displayed as a dash and there are spaces separating the letters
   public String pattern() {
      if (readyWords.isEmpty()) {
         throw new IllegalStateException();
      }
      return nowPattern;
   }
   
   // pre: the number of guesses left is at least 1 and the set of words is not empty
   // (throw an IllegalStateException if not)
   // when the set of words is nonempty, the character being guessed was not
   // guessed previously (throw an IllegalArgumentException if not)
   // post: record the next guess made by the user. Decide what set of words to 
   // use going forward. Return the number of occurrences of the guessed letter 
   // in the new pattern and update the number of guesses left.
   public int record(char guess) {
      if (leftGuess < 1 || readyWords.isEmpty()) {
         throw new IllegalStateException();
      } 
      if (!readyWords.isEmpty() && guessedLetters.contains(guess)) {
          throw new IllegalArgumentException();
      }
      guessedLetters.add(guess); 
      Map<String, Set<String>> choices = organize(guess);
      readyWords = longestSet(choices);
      String check = "" + guess;
      if (!nowPattern.contains(check)) {
               leftGuess -= 1;
      } 
      int occurance = howOften(nowPattern, guess);
      return occurance;
   }
   
   // post: return the set that contains the most words after comparing all sets 
   // of strings in the given map. Update its pattern to the current pattern to be 
   // displayed for the hangman game
   private Set<String> longestSet(Map<String, Set<String>> choices) {
      int count = 0;
      for (String patternWord: choices.keySet()) {
         int big = choices.get(patternWord).size();
         if (big > count) {
            nowPattern = patternWord; 
            count = big;
         }
      }
      return choices.get(nowPattern);
   }
 
   // post: based on the given letter, return a map which inclues keys 
   // as all different patterns of words in the current set. Group words that share 
   // the same pattern in that set as value. 
   private Map<String, Set<String>> organize(char guess) {
      Map<String, Set<String>> choices = new TreeMap<String, Set<String>>();
      for (String each : readyWords) {
         String patternWord = dashes(each, guess);
         addTo(choices, patternWord, each);
      } 
      return choices;
   }
   
   // post: return how many times the given character appears in this string
   private int howOften (String nowPattern, char guess) {
      int times = 0;
      for (int i = 0; i < nowPattern.length(); i++) {
         if (nowPattern.charAt(i) == guess) {
            times++;
         }
      }
      return times;
   }
   
   // post: add the third string parameter into the set of that key, which is the 
   // second string parameter. Create a key with empty set of string first if 
   // there's no key of the second string parameter in that map
   private void addTo(Map<String, Set<String>> choices, String patternWord, 
      String each) {
      if (!choices.containsKey(patternWord)) {
         choices.put(patternWord, new TreeSet<String>());
      }
      choices.get(patternWord).add(each);
   }
    
   // post: return string pattern based on the given word and letter. The pattern will 
   // display this letter with the letters that have already been in. 
   private String dashes(String each, char guess) {
      String result = nowPattern;
      for (int i = 0; i < each.length(); i++) {
         if (each.charAt(i) != result.charAt(i*2)) {
            if (each.charAt(i) == guess) {
               result = result.substring(0,i*2) + guess + 
               result.substring(i*2+1,result.length());
            }
         }
      }
      return result;
   }
}