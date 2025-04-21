//AVL tree implementation
// very helpful btw
// https://www.w3schools.com/dsa/dsa_data_avltrees.php

public class AVL {
    //self explanatory
    public NodeA root;

    //i dont even know if I track these correctly
    public int nodeCount;


    //insert for root
    //also if you just insert a word it will be added from the top and move down
    public void insert(String word) {
        root = insert(root, word);
    }



    //insert for the tree using recursion
    //we also deal with our balancing here
    private NodeA insert(NodeA node, String word) {
        if (node == null) {
            return new NodeA(word);
        }

        //Compare the word to the current node's word
        int cmp = word.compareTo(node.word);
        if (cmp < 0) {
            node.left = insert(node.left, word);
            nodeCount++;
        }
        else if (cmp > 0) {
            node.right = insert(node.right, word);
            nodeCount++;
        }
        else{
            //we found a duplicate word
            //we dont nodecount it
            return node; // wow duplicate word
        }

        //balance checks, for the four avl cases
        node.height = 1 + Math.max(height(node.left), height(node.right));
        int balance = getBalance(node);
        if (balance >  1 && getBalance(node.left)  >= 0) return rotateRight(node);
        if (balance >  1 && getBalance(node.left)  <  0) {
            node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.right) <= 0) return rotateLeft(node);
        if (balance < -1 && getBalance(node.right) >  0) {
            node.right = rotateRight(node.right);
            return rotateLeft(node);
        }

        return node;

    }


    //rotations for balancing
    //came from the w3schools link
    private NodeA rotateLeft(NodeA x) {
        NodeA y  = x.right;
        NodeA T2 = y.left;

        // 1) y becomes new root of this subtree
        y.left  = x;
        // 2) x.right is re‑attached to T2
        x.right = T2;

        // 3) update heights bottom‑up
        x.height = 1 + Math.max(height(x.left),  height(x.right));
        y.height = 1 + Math.max(height(y.left),  height(y.right));

        return y;
    }

    private NodeA rotateRight(NodeA y) {
        NodeA x  = y.left;
        NodeA T2 = x.right;

        // 1) x becomes new root of this subtree
        x.right = y;
        // 2) y.left is re‑attached to T2
        y.left  = T2;

        // 3) update heights
        y.height = 1 + Math.max(height(y.left),  height(y.right));
        x.height = 1 + Math.max(height(x.left),  height(x.right));

        return x;
    }

    //height of the node
    private int height(NodeA n) {
        if (n == null) {
            return 0;
        } else {
            return n.height;
        }
    }

    //balance of the node (important, we need to know if we need to rotate)
    private int getBalance(NodeA n) {
        if (n == null) {
            return 0;
        }
        return height(n.left) - height(n.right);
    }

    //is the word in the tree?
    public boolean hasWord(String word) {
        //we look for the word in the tree
        NodeA cur = root;
        while (cur != null) {
            //since this is a binary search tree at its heart, we can just do this
            int cmp = word.compareTo(cur.word);
            if (cmp < 0) {
                cur = cur.left;
            } else if (cmp > 0) {
                cur = cur.right;
            } else {
                return true; //found the word
            }
        }
        return false; //word not found
    }

    //is the string a prefix of any word in the tree?
    public boolean isPrefix(String prefix){
        //we look for something that might be at the start of a word
        //very important for pathfinding in the boggle game
        if(prefix == null){
            return false;
        }

        //idk man
        if(prefix.length() == 0){
            return false;
        }

        NodeA cur = root;


        while(cur != null){
            //i wonder if this can be improved since this uses a java built in
            if(cur.word.startsWith(prefix)){
                return true; //found the prefix
            }
            if(prefix.compareTo(cur.word) < 0){
                cur = cur.left;
            } else{
                cur = cur.right;
            }
        }

        return false; //is not a prefix

    }


}
