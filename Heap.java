public class Heap {
    public HeapNode[] tree;
    public int size;

    public Heap() {
        this.tree = new HeapNode[20];
        this.size = 0;
    }

    // function to upheap so that the heap remains a heap
    private void upheap(int idx) {
        // in case we have reached the root node
        if (idx < 1) 
            return;

        // get the parent to compare
        int parent = (int) ((idx - 1) / 2);
        
        // compare to see if the current index is of more priority than the parent
        // in this case, the higher the bid the higher the priority
        // also this is implied that the new item is newer compared to the old one so this also takes time into account
        if (tree[parent].data.getWord().length() > tree[idx].data.getWord().length()) {
            // swap items
            HeapNode temp = tree[parent];
            
            tree[parent] = tree[idx];
            tree[idx] = temp;
            
            // recursively call 
            upheap(parent);
        }
    }

    // keep the heap a heap but the other direction   
    private void downheap(int idx) {
        // get the two children
        int left = idx * 2 + 1;
        int right = idx * 2 + 2;
        HeapNode temp;
        
        int bestChild = left;
        
        // if left node does not exist, end
        if (left >= size) 
            return;
        
        // right node exists
        if (right < size) {
            // right node has priority
            if (tree[right].data.getWord().length() < tree[left].data.getWord().length()) 
                bestChild = right;
        }
        
        // check to see if the higher priority child has higher priority than the parent
        if (tree[bestChild].data.getWord().length() < tree[idx].data.getWord().length()) {
            // swap then recursive call
            temp = tree[idx];
            tree[idx] = tree[bestChild];
            tree[bestChild] = temp;
            
            downheap(bestChild);
        }
    }
    
    public Word remove() {
        HeapNode ret = tree[0];

        // move the last leaf node to the root
        tree[0] = tree[size - 1];
        tree[size - 1] = null;
        size--;
        
        // downheap the root to keep it sorted
        downheap(0);

        return ret.data;
    }
    
    public void add (Word newWord) {
        // check if the heap is full
        if (size == 20) {
            // if the new word is longer than the shortest item, remove shortest item
            if (tree[0].data.getWord().length() >= newWord.getWord().length())
                return;
            
            remove();
        }
        
        tree[size] = new HeapNode(newWord);
        upheap(size);
        
        size++;
    }
}
