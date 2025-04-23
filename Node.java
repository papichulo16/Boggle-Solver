public class Node {
    public byte data;
    public Node sibling;
    public Node child;
    public boolean isWord;

    public Node(byte data) {
        this.data = data;
        this.sibling = null;
        this.child = null;
        this.isWord = false;
    }
}
