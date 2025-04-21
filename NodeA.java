//AVL node class

public class NodeA {
    //i figured id try full words instead of single letters
    //lookups might take a little longer but the memory usage might be better
    String word;

    NodeA left, right;
    int height;

    NodeA(String word) {
        this.word = word;
        this.height = 1;
    }
}
