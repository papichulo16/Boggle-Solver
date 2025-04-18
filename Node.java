public class Node {
    public String data;
    
    public Node parent;
    public Node sibling;
    public Node child;

    public boolean isWord;

    public Node(String data) {
        this.data = data;
        this.parent = null;
        this.sibling = null;
        this.child = null;
        this.isWord = false;
    }
    
    public Node(String data, Node parent) {
        this.data = data;
        this.parent = parent;
        this.sibling = null;
        this.child = null;
        this.isWord = false;
    }
}
