public class Trie {
    public Node root;
    public int size;
    
    public Trie() {
        this.root = null;
        this.size = 0;
    }
    
    // this function returns the child node with data equivalent to target from a given parent
    // returns null if no child is found
    public Node findChild(byte target, Node parentNode) {
        Node current = parentNode.child;
        
        while (current != null) {
            if (current.data == target)
                break;
            
            if (current.data > target)
                return null;
            
            current = current.sibling;
        }
        
        return current;
    }
    
    // given a parent node (aka the previous letter), it will either create a new Node as a child or find an equivalent child
    // if parentNode field is null, it will assume that youre trying to add the first letter
    public Node add(byte newChar, Node parentNode) {
        // in case there is no root
        if (this.root == null) {
            this.root = new Node(newChar);
            this.size++;
            
            return this.root;
        }

        // in case we are on letter one
        if (parentNode == null)
            parentNode = this.root;
        
        // search to see if the node we are creating already exists
        Node current = parentNode.child;
        Node ret;
        
        // in case there are no children
        if (current == null) {
            ret = new Node(newChar, parentNode);
            this.size++;
            
            parentNode.child = ret;
            return ret;
        }
        
        // in case the current node is what we are looking for
        if (current.data == newChar)
            return current;
        
        // go through the siblings
        while (current.sibling != null) {
            // if the current node is what we are looking for
            if (current.sibling.data == newChar)
                return current.sibling;

            current = current.sibling;
        }
        
        // the node we are going to add does not exist so lets add it to the list
        ret = new Node(newChar, parentNode);
        this.size++;
        current.sibling = ret;

        return ret; 
    }
    
}
