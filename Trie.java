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
    // Initialize nodes to not create a new one every add or search
    public Node root,current;
    public int size;

    public Trie() {
        this.root = null;
        this.size = 0;
    }

    // this function returns the child node with data equivalent to target from a given parent
    // returns null if no child is found
    // if parentNode is null, then we are looking at the first character of a string
    public Node findChild(String target, Node parentNode) {
        //Node current;

        if (parentNode != null)
            current = parentNode.child;
        else {
            current = this.root.child;
        }
        int compare = target.compareTo(current.data);

        while (current != null) {
            if (compare == 0)
                break;

            if (compare < 0)
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
            this.root = new Node((String) "");
            this.size++;
        }

        // in case we are on letter one
        if (parentNode == null)
            parentNode = this.root;

        // search to see if the node we are creating already exists
        current = parentNode.child;

        // in case there are no children
        if (current == null) {
            Node ret = new Node(newChar);
            this.size++;
            parentNode.child = ret;

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

}
