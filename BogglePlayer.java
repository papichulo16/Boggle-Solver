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
import java.util.*;

public class BogglePlayer
{
    //private Trie gameTree;
    private AVL gameAVL;
    private boolean[][] used;

    // initialize BogglePlayer with a file of English words
    public BogglePlayer(String wordFile) throws IOException
    {
        // initialize tree
        gameAVL = new AVL();
        used = new boolean[4][4];

        // Read the dictionary
        BufferedReader file = new BufferedReader(new FileReader(wordFile));
        String word;
        while ((word = file.readLine()) != null) {
            word = word.trim().toUpperCase();
            gameAVL.insert(word);    // ← only once per word
        }

        //System.out.println("Num nodes: " + gameTree.size);
        System.out.println("Num AVL nodes: " + gameAVL.nodeCount);

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

    //dfs for avl
    private void dfs(int row, int col, String curWord, ArrayList<Location> path, PriorityQueue<Word> bestWords, char[][] board) {
        char ch = board[row][col];
        String newWord;
        if (ch == 'Q') {
            newWord = curWord + "QU";
        } else {
            newWord = curWord + ch;
        }



        //debug statements
        //System.out.println("DFS at "+row+","+col+" → \""+newWord+"\"");
        if (!gameAVL.isPrefix(newWord)) {
            //debug statements
            //System.out.println("  prune \""+newWord+"\"");
            return;
        }

        // extend path
        ArrayList<Location> newPath = new ArrayList<>(path);
        newPath.add(new Location(row, col));

        // if it’s a valid word, add it
        if (newWord.length() >= 3 && gameAVL.hasWord(newWord)) {
            Word w = new Word(newWord);
            w.setPath(newPath);
            bestWords.offer(w);
        }

        // recurse on all unused neighbors
        for (Location nb : getUsableLocations(new Location(row, col))) {
            if (nb != null && !used[nb.row][nb.col]) {
                used[nb.row][nb.col] = true;
                dfs(nb.row, nb.col, newWord, newPath, bestWords, board);
                used[nb.row][nb.col] = false;
            }
        }
    }

    public Word[] getWords(char[][] board) {
        //priorityqueue, once again not sure if this can be done faster but it seemed easy
        PriorityQueue<Word> toBeDumped = new PriorityQueue<>((a, b) -> { //need a comparator
            int wordlen = Integer.compare(b.getWord().length(), a.getWord().length());
            if (wordlen != 0) return wordlen;
            return a.getWord().compareTo(b.getWord());
        });

        //dfs each location and refresh the used array for each time
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    Arrays.fill(used[i], false);
                }
                used[row][col] = true;
                dfs(row, col, "", new ArrayList<>(), toBeDumped, board);
            }
        }

        //return the 20 best words and avoid duplicates
        //at least thats what I think the hashmap is doing otherwise ive failed
        HashMap<String,Boolean> seen = new HashMap<>();
        ArrayList<Word> resultWords = new ArrayList<>();

        while (!toBeDumped.isEmpty() && resultWords.size() < 20) {
            Word word = toBeDumped.poll();
            if (seen.put(word.getWord(), Boolean.TRUE) == null) {
                resultWords.add(word);
            }
        }

        return resultWords.toArray(new Word[0]);
    }


}
