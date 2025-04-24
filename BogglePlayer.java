/*
  Authors (group members): Luis Abraham, Dominick Morales, Justin Bower, and Jacob Woods
  Email addresses of group members: labrahamesco2024@my.fit.edu, <dominick's address>, <justin's address>, <jacob's address>
  Group name: The Chantastic Four

  Course: CSE 2010
  Section: 2pm Lab (Jacob is in 3:30pm lab)

  Description of the overall algorithm and key data structures:
  For the data structure to store the wordlist we are using a Trie with 3 pointers: a child, sibling, and parent.
  Siblings are used to cycle through letters in current position while going to a parent you are moving back a position and a child up a position.

  Our findWords function is recursive and looks through each cell and tries to find as many words as possible, then uses the best words.
*/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class BogglePlayer
{
  // variables we will need for later on
  private Trie gameTree;
  private boolean[][] used;

  // make it so we dont end up making so many location objects
  // this appears to actually have improved score so thats awesome
  private static final Location[][] pool = new Location[4][4];
  static {
    for (int r = 0; r < 4; r++) {
      for (int c = 0; c < 4; c++) {
        pool[r][c] = new Location(r, c);
      }
    }
  }

  // initialize BogglePlayer with a file of English words
  public BogglePlayer(String wordFile) throws IOException
  {
    // initialize tree
    gameTree = new Trie();
    used = new boolean[4][4];

    // Read the dictionary
    BufferedReader file = new BufferedReader(new FileReader(wordFile));
    String line;

    // for each line, make sure we got a trie going
    while ( (line = file.readLine()) != null) {
      line = line.trim().toUpperCase();
      Node curNode = null;

      for (int i = 0; i < line.length(); i++) {

        curNode = gameTree.add(line.substring(i,i+1), curNode);

        if (i == line.length() - 1 && i >= 2)
          curNode.isWord = true;
      }
    }
    gameTree.setsuffixes(gameTree.root,gameTree.root);

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

        //location pool indexes
        int xx = cur.row + i;
        int yy = cur.col + j;

        // in case we are out of bounds
        if (xx > 3 || yy > 3 || xx < 0 || yy < 0)
          continue;

        // in case the spot is already in use
        if (used[xx][yy])
          continue;


        ret[idx] = pool[xx][yy];
        idx++;
      }
    }

    return ret;
  }

  // this will get all words from position starting with curPos
  // this is also recursive
  public Word[] getConnectingWords(char[][] board, Word[] words, Location curPos, ArrayList<Location> path, Node curNode, StringBuilder pref) {
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

      // mfw we have to make a string object
      // its better though since we use a string builder
      String wordFound = pref.toString();
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

    // iterate through each location
    for (Location connection: allPossibleLocations) {
      // in case we are done
      if (connection == null)
        break;

      //System.out.println("Trying coordinates: (" + connection.row + ", " + connection.col + ") - " + board[connection.row][connection.col] + " FROM (" + curPos.row + ", " + curPos.col + ") - " + board[curPos.row][curPos.col]);

      // check to see if current word path exists
      char letter = board[connection.row][connection.col];
      Node childNode = gameTree.findChild(letter, curNode);

      // if it does exist, recursive call
      if (childNode != null) {
        //System.out.println("Child node: " + (char) childNode.data);
        // add the letter we are using
        pref.append(letter);

        // recursive call
        words = getConnectingWords(board, words, connection, path, childNode, pref);

        // make sure we are overwriting the letter we just added since we are done
        pref.setLength(pref.length()-1);
      }
    }

    // reverse what we added
    used[curPos.row][curPos.col] = false;
    path.remove(path.size() - 1);

    return words;
  }

  public Word[] getWords(char[][] board)
  {
    Word[] myWords = new Word[20];  // assuming 20 words are found

      /*
       * this prints the grid, debugging purposes
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          System.out.print(board[i][j] + " ");
        }
        System.out.println();
      }
      */

    // get the starting letter for the word search at each and every cell
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        // get the letter
        Node letterOne = gameTree.findChild(board[i][j], null);

        if(letterOne != null){
          //stringbuilder is very good to avoid making too many strings since strings are immutable
          StringBuilder pref = new StringBuilder().append(board[i][j]);

          // this is the starting location pointer
          Location toPass = pool[i][j];

          // call recursive function
          myWords = getConnectingWords(board, myWords, toPass, new ArrayList<Location>(), letterOne, pref);
        }
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