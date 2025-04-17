/*

  Authors (group members): Luis Abraham, Dominick Morales, Justin Bower, and Jacob Woods
  Email addresses of group members: labrahamesco2024@my.fit.edu, <dominick's address>, <justin's address>, <jacob's address>
  Group name: The Chantastic Four

  Course: CSE 2010
  Section: 2pm Lab (Jacob is in 3:30pm lab)

  Description of the overall algorithm and key data structures:
  For the data structure to store the wordlist we are using a Trie with 3 pointers: a child, sibling, and parent. Siblings are used to cycle through letters in current position while going to a parent you are moving back a position and a child up a position. 
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BogglePlayer
{
    private Trie gameTree;

    // initialize BogglePlayer with a file of English words
    public BogglePlayer(String wordFile) throws IOException
    {
        // initialize tree
        gameTree = new Trie();

        // Read the dictionary
        BufferedReader file = new BufferedReader(new FileReader(wordFile));
        String line;
        
        // for each line
        while ( (line = file.readLine()) != null) {
          line = line.trim().toLowerCase();
          Node curNode = null;

          for (int i = 0; i < line.length(); i++) {
            char cur = line.charAt(i);
            
            curNode = gameTree.add((byte) cur, curNode);

            if (i == line.length() - 1)
              curNode.isWord = true;
          }
        }

        System.out.println("Num nodes: " + gameTree.size);
    }

    // based on the board, find valid words
    //
    // board: 4x4 board, each element is a letter, 'Q' represents "QU", 
    //    first dimension is row, second dimension is column
    //    ie, board[row][col]     
    //
    // Return at most 20 valid words in UPPERCASE and 
    //    their paths of locations on the board in myWords;
    //
    // See Word.java for details of the Word class and
    //     Location.java for details of the Location class

    public Word[] getWords(char[][] board)
    {
      /*
       * HINT FOR YOU GUYS: you should make a separate recursive function/utilize while loops
       * Also create a 2D array of booleans where a True will symbolize that a cell is in use
       * and a False says that a cell is not in use.
       * 
       * I think you will really only need the findChild() function in the Trie class, if you need anything else
       * just DM me and I will make it happen. It should not be that crazy bad to solve (hopefully) and if it is
       * then also you can DM me and I will help out.
       *
       * Check that we have found 20 words, and be chillin and grillin. Good luck guys.
       */
      Word[] myWords = new Word[20];  // assuming 20 words are found

        return myWords;
    }

}
