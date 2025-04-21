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
    private Trie gameTree;
    private boolean[][] used;
    public int count = 0;
   

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

    
    public ArrayList getUsableLocations(Location cur) {
      int idx = 0;
      ArrayList<Location> ret = new ArrayList<>();

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
          

          ret.add(new Location(cur.row + i, cur.col + j));
          idx++;
        }
      }
      
      return ret;
    }
    
    
    
       public Word[] getWords(char[][] board)
    {
      StringBuilder path = new StringBuilder();
      ArrayList<Location> pathL = new ArrayList<>();
      Node tempnode;
      int y = 0;
      Word[] myWords = new Word[20];// assuming 20 words are found
        
      PriorityQueue<Word> results = new PriorityQueue<>(20,Comparator.comparingInt(w -> w.getWord().length()));
      
      for (int i=0;i<4;i++) {
          for (int j=0;j<4;j++) {
                  tempnode = gameTree.findChild((byte)board[i][j],null);
                  used[i][j] = true;
                  path.append(board[i][j]);
                  Location loc = new Location(i,j);
                  pathL.add(loc);
                  findWord(tempnode,getUsableLocations(loc),results,board,path,pathL);
                  path.setLength(path.length()-1);
                  pathL.clear();
                  used[i][j] = false;
          }
      }
      count = 0;
       return results.toArray(new Word[0]);
    }
    
    
    public void findWord(Node currentNode, ArrayList<Location> possibleLocations, PriorityQueue<Word> topWords,
                     char[][] board, StringBuilder path, ArrayList<Location> pathLocations) {
    for (Location loc : possibleLocations) {
        if (loc == null || used[loc.row][loc.col]) continue;

        char c = board[loc.row][loc.col];
        Node nextNode = gameTree.findChild((byte) c, currentNode);

        if (nextNode != null) {
            // Choose
            used[loc.row][loc.col] = true;
            path.append(c);
            pathLocations.add(loc);

            if (nextNode.isWord && path.length() >= 3) {
                nextNode.isWord = false;

                Word newWord = new Word(path.toString());
                newWord.setPath(new ArrayList<>(pathLocations));

                // Add to topWords only if it's longer than the shortest
                if (topWords.size() < 20) {
                    topWords.offer(newWord);
                } else if (newWord.word.length() > topWords.peek().word.length()) {
                    topWords.poll(); // remove shortest
                    topWords.offer(newWord);
                }
            }

            // Explore further
            findWord(nextNode,getUsableLocations(loc), topWords, board, path, pathLocations);

            // Un-choose (backtrack)
            used[loc.row][loc.col] = false;
            pathLocations.remove(pathLocations.size() - 1);
            path.setLength(path.length() - 1);
        }
    }
    }
}



