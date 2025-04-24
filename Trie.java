/*
  Authors (group members): Luis Abraham, Dominick Morales, Justin Bower, and Jacob Woods
  Email addresses of group members: labrahamesco2024@my.fit.edu, <dominick's address>, <justin's address>, <jacob's address>
  Group name: The Chantastic Four

  Course: CSE 2010
  Section: 2pm Lab (Jacob is in 3:30pm lab)

  Description of the overall algorithm and key data structures:
  This is the Data Structure that we will use
*/

public class Trie {
    public Node root;
    public int size;

    public Trie() {
        this.root = null;
        this.size = 0;
    }

    // this function returns the child node with data equivalent to target from a given parent
    // returns null if no child is found
    // if parentNode is null, then we are looking at the first character of a string
    public Node findChild(char target, Node parentNode) {
        Node current;

        if (parentNode != null)
            current = parentNode.child;
        else {
            current = this.root.child;
        }

        while (current != null) {
            if (current.data.charAt(0) == target)
                break;

            if (current.data.charAt(0) > target)
                return null;

            current = current.sibling;
        }

        return current;
    }

    // given a parent node (aka the previous letter), it will either create a new Node as a child or find an equivalent child
    // if parentNode field is null, it will assume that youre trying to add the first letter
    public Node add(String newChar, Node parentNode) {
        // in case there is no root
        if (this.root == null) {
            this.root = new Node(null);
            this.size++;
        }

        // in case we are on letter one
        if (parentNode == null)
            parentNode = this.root;

        // search to see if the node we are creating already exists
        Node current = parentNode.child;

        // in case there are no children
        if (current == null) {
            Node ret = new Node(newChar);
            this.size++;
            parentNode.child = ret;
            ret.parent = parentNode;

            return ret;
        }

        // in case the current node is what we are looking for
        if (current.data.equals(newChar))
            return current;

        // go through the siblings
        while (current.sibling != null) {
            // if the current node is what we are looking for
            if (current.sibling.data.equals(newChar))
                return current.sibling;

            current = current.sibling;
        }

        // the node we are going to add does not exist so lets add it to the list
        Node ret = new Node(newChar);
        this.size++;
        current.sibling = ret;

        return ret;
    }

    public void setsuffixes() {
        Node current = this.root;

        while (current.child != null) {
            current = current.child;
        }

        while (current != this.root) {
            if (current.sibling == null && current.child == null && current.child.isWord) {
                current.parent.data += current.data;
                current.parent.child = null;
                current = current.parent;
                current.isWord == true;
            }
            else if (current.sibling != null) {

            }
        }
    }

}
