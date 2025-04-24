/*
  Authors (group members): Luis Abraham, Dominick Morales, Justin Bower, and Jacob Woods
  Email addresses of group members: labrahamesco2024@my.fit.edu, <dominick's address>, <justin's address>, <jacob's address>
  Group name: The Chantastic Four

  Course: CSE 2010
  Section: 2pm Lab (Jacob is in 3:30pm lab)

  Description of the overall algorithm and key data structures:
  This is a node that we will use
*/

public class Node {
    public String data;

    public Node sibling;
    public Node child;

    public boolean isWord;

    public Node(String data) {
        this.data = data;
        this.sibling = null;
        this.child = null;
        this.isWord = false;
    }
}
