import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BogglePlayer {
    private Trie gameTree;
    private boolean[][] used;

    // initialize BogglePlayer with a file of English words
    public BogglePlayer(String wordFile) throws IOException {

        // initialize tree
        gameTree = new Trie();
        used = new boolean[4][4];

        // Read the dictionary
        BufferedReader file = new BufferedReader(new FileReader(wordFile));
        String line;


        // for each line we add to the trie
        while ((line = file.readLine()) != null) {
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
// first dimension is row, second dimension is column
// ie, board[row][col]
//
// Return at most 20 valid words in UPPERCASE and
// their paths of locations on the board in myWords;
//
// See Word.java for details of the Word class and
// Location.java for details of the Location class
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


    //depth first search
    // abysmal?
    public void dfs(int row, int col, Node par, String curWord, ArrayList<Location> curPath, PriorityQueue<Word> wordQueue, char[][] myboard) {
        char cur = myboard[row][col];
        Node curNode;
        String newWord;
        ArrayList<Location> newPath = new ArrayList<>(curPath);
        newPath.add(new Location(row, col));

        if (cur == 'Q') {
            // 1) find the Q‑node
            Node qNode = gameTree.findChild((byte) 'Q', par);
            if (qNode == null) return; // stop if there is no Q‑child
            // 2) under that Q‑node, find the U‑child
            Node uNode = gameTree.findChild((byte) 'U', qNode);
            if (uNode == null) return; // stop if no U follows Q
            // 3) advance into the U‑node
            curNode = uNode;
            newWord = curWord + "QU";
        } else {
            curNode = gameTree.findChild((byte) cur, par); //find what we are looking for
            if (curNode == null) return;
            newWord = curWord + cur;
        }

        if (curNode.isWord && newWord.length() >= 3) { //we found a word that is >= 3 size
            Word newFound = new Word(newWord);
            newFound.setPath(newPath);
            wordQueue.offer(newFound);
        }


        Location[] usableLocations = getUsableLocations(new Location(row, col));
        for (Location loc : usableLocations) {
            if (loc != null && !used[loc.row][loc.col]) {
                used[loc.row][loc.col] = true; //this is important for pathfinding
                dfs(loc.row, loc.col, curNode, newWord, newPath, wordQueue, myboard); //recursively call dfs to find more words
                used[loc.row][loc.col] = false; //unmark once we are done

            }

        }

    }


    public Word[] getWords(char[][] board) {
        //i mutilated this function
        PriorityQueue<Word> toBeDumped = new PriorityQueue<>((a, b) -> { //honestly not sure if this is the fastest way to do this
            int wordlen = Integer.compare(b.getWord().length(), a.getWord().length()); //need a comparator to sort the words
            if (wordlen != 0) return wordlen;
            return a.getWord().compareTo(b.getWord());
        });


        // for each letter on the board, we will start a dfs
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                for (int i = 0; i < 4; i++) {
                    Arrays.fill(used[i], false); //reset the used array so we can take a look for each letter
                }
                used[row][col] = true;// mark the starting letter as used
                dfs(row, col, gameTree.root, "", new ArrayList<>(), toBeDumped, board); //dfs time

            }

        }

        //dump the words into an array, avoid already seen words
        //I also dont know whether duplicates actually occur but this is one way to prevent it
        HashMap<String, Boolean> seen = new HashMap<>();
        ArrayList<Word> resultWords = new ArrayList<>();

        // this gets us the best words since its a priority queue
        while (!toBeDumped.isEmpty() && resultWords.size() < 20) {
            Word word = toBeDumped.poll();
            if (seen.put(word.getWord(), Boolean.TRUE) == null) {
                resultWords.add(word);
            }
        }


        //are you supposed to return it like this?
        return resultWords.toArray(new Word[0]);

    }


}
