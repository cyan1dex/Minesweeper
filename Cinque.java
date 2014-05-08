public class Cinque
{
   public static void main(String[] args)
   {
      String guess = null;
      String answer = null;
      String cheatCode = null;
      // randomly select a word from the dictionary array

      /*
       * If that word has duplicate letters select a new word
       * while(hasDupes(answer)
       * answer = new random word
       */
      while (!guess.equals(answer)) // while the guess does not equal the answer
      // loop
      {
         // Using some logic you need to check for a cheat code and/or the guess
         // is a valid word from the dictionary (check validity with a method).
         // If neither is true proceed with
         // checking the correctInPlace and totalCorrect

         // Check for the correct in place
         int correctInPlace = correctInPlace(guess, answer);
         /*
          * check for the total correct letters Notice that you use the
          * removeDupes method on the guess this will prevent double letters in
          * the guess from return the wrong amounts
          */
         int totalCorrect = totalCorrect(removeDupes(guess), answer);
      }

   }

   public static boolean hasDupes(String word)
   {
      /*
       * You need some logic here to test if the word had duplicates, if it ever
       * does return true, else return false
       */
      return false;
   }

   public static int correctInPlace(String guess, String answer)
   {
      int totalInPlace = 0;
      /*
       * Put both words into an array and test each letter one at a time in a
       * loop. If each letter at the same position has the same letter increment
       * totalInPlace
       */
      return totalInPlace;
   }

   public static String removeDupes(String guess)
   {
      String noDupes = "";
      if (hasDupes(guess)) // if there is duplicates, remove them
      {
         // logic here to create the string noDupes from guess that has no
         // duplicate letters
         return noDupes;
      }
      else
         // there is no dupes, just return the word
         return guess;
   }

   public static int totalCorrect(String guess, String answer)
   {
      int totalCorrect = 0;
      return totalCorrect;
   }
}
