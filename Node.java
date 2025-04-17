public class Node {
    public byte data;
    
    public Node parent;
    public Node sibling;
    public Node child;

    public boolean isWord;
    public String a;

    public Node(byte data) {
        this.data = data;
        this.parent = null;
        this.sibling = null;
        this.child = null;
        this.isWord = false;
        a = null;
    }
    
    public Node(byte data, Node parent) {
        this.data = data;
        this.parent = parent;
        this.sibling = null;
        this.child = null;
    }
    
}
