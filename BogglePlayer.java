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
import java.util.ArrayList;

public class BogglePlayer
{
    private Trie gameTree;
    private boolean[][] used;

    // initialize BogglePlayer with a file of English words
    public BogglePlayer(String wordFile) throws IOException
    {
        // initialize tree
        gameTree = new Trie();
        used = new boolean[4][4];

        // Read the dictionary
        BufferedReader file = new BufferedReader(new FileReader(wordFile));
        String line;
        
        // for each line
        while ( (line = file.readLine()) != null) {
          line = line.trim().toUpperCase();
          Node curNode = null;

          for (int i = 0; i < line.length(); i++) {
            char cur = line.charAt(i);
            
            curNode = gameTree.add((byte) cur, curNode);

            if (i == line.length() - 1 && i >= 2)
              curNode.isWord = true;
          }
        }

        System.out.println("Num nodes: " + gameTree.size);

        file.close();
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
    
    // gets the current usable locations while using the used[][] as context 
    public Location[] getUsableLocations(Location cur) {
      int idx = 0;
      Location[] ret = new Location[8];

      // iterate through each possible offset
      for (int i = 1; i >= -1; i--) {
        for (int j = 1; j >= -1; j--) {
          // in case we are looking at the current location
          if (i == 0 && j == 0)
            continue;
          
          // in case we are out of bounds
          if (cur.row + i > 3 || cur.col + j > 3 || cur.row + i < 0 || cur.col + j < 0)
            continue;
          
          // in case the spot is already in use
          if (used[cur.row + i][cur.col + j])
            continue;
          

          ret[idx] = new Location(cur.row + i, cur.col + j);
          idx++;
        }
      }
      
      return ret;
    }
    
    // this will get all words from position starting with curPos
    public Word[] getConnectingWords(char[][] board, Word[] words, Location curPos, ArrayList<Location> path, Node curNode) {
      // add what we added to the current word
      used[curPos.row][curPos.col] = true;
      path.add(curPos);
      int addIdx = -1;
      
      // check to see if current node makes a full word
      if (curNode.isWord) {
        // find an index to add to in the array
        for (int i = 0; i < 20; i++) {
          // if empty spot
          if (words[i] == null) {
            addIdx = i;

            break;
          }
        }
        
        // in case array is full, we must see if this new string is better than at least one item in the array
        if (addIdx == -1) {
          int worstIdx = 0;

          for (int i = 1; i < 20; i++) {
            if (words[i].getWord().length() < words[worstIdx].getWord().length())
              worstIdx = i;
          }
          
          if (words[worstIdx].getWord().length() < path.size()) 
            addIdx = worstIdx;
        }
      }
        
      // if we found an index to add to
      if (addIdx > -1) {
        // add to array and then create a new word
        curNode.isWord = false;
        String wordFound = "";
        Node current = curNode;

        while (current.data != 0) {
          wordFound = (char) current.data + wordFound;
          current = current.parent;
        }

        //System.out.println("[*] Found word: " + wordFound);
        
        // create a copy word of current path and add that to array
        Word temp = new Word();
        temp.setWord(wordFound);
        
        for (Location cur: path) {
          temp.addLetterRowAndCol(cur.row, cur.col);
        }
        
        words[addIdx] = temp;
      }

      // this is the part where we look through child nodes to recursively call
      Location[] allPossibleLocations = getUsableLocations(curPos);
      Node childNode;

      // iterate through each location
      for (Location connection: allPossibleLocations) {
        // in case we are done
        if (connection == null)
          break;
        
        //System.out.println("Trying coordinates: (" + connection.row + ", " + connection.col + ") - " + board[connection.row][connection.col] + " FROM (" + curPos.row + ", " + curPos.col + ") - " + board[curPos.row][curPos.col]);

        // check to see if current word path exists
        childNode = gameTree.findChild((byte) board[connection.row][connection.col], curNode);
        
        // if it does exist, recursive call
        if (childNode != null) {
          //System.out.println("Child node: " + (char) childNode.data);
          words = getConnectingWords(board, words, connection, path, childNode);
        }
      }
      
      // reverse what we added
      used[curPos.row][curPos.col] = false;
      path.remove(path.size() - 1);

      return words;
    }

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
    
      
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          System.out.print(board[i][j] + " ");
        }
        System.out.println();
      }
      
      
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          //System.out.println("New set:");
          myWords = getConnectingWords(board, myWords, new Location(i,j), new ArrayList<Location>(), gameTree.findChild((byte) board[i][j], null));
        }
      }
      
      /* 
      for (int i = 0; i < 20; i++) {
        System.out.print(myWords[i].getWord() + "(" + myWords[i].getPathLength() + "): ");
        
        for (int j = 0; j < myWords[i].getPathLength(); j++) {
          System.out.print("(" + myWords[i].getLetterRow(j) + ", " + myWords[i].getLetterCol(j) + "), ");
        }

        System.out.println();
        
      }
      */
      
      return myWords;
    }

}
