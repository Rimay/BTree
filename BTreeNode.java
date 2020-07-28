class BTreeNode {

    int n;  //number of key-value pairs in the B-tree
    int min_degree;  //Minimum degree (defines the range for number of keys)
    boolean leaf;   //true when node is leaf. Otherwise false
    long[] keys;    //Array of the keys stored in the node.
    long[] values;  //Array of the values[recordID] stored in the node. This will only be filled when the node is a leaf node.
    BTreeNode[] children;   //Pointers to the children, if this node is not a leaf.  If his node is a leaf, then null.
    BTreeNode next; //point to other next node when it is a leaf node. Otherwise null


    // find the value of the key k, if not exist, return -1
    // only for leaf node
    long get_v(long k) {
        for (int i=0; i<n; i++) {
            if (keys[i]==k) return values[i];
        }
        return -1;
    }

    // remove the key and value for the given k,
    //      if delete successfully return true
    //      else return false
    boolean remove(long k) {
        boolean f=false;
        for (int i=0; i<n; i++) {
            if (keys[i]==k) {
                for (int j =i+1; j<n; j++) {
                    keys[j-1] = keys[j];
                    values[j-1] = values[j];
                }
                this.n--;
                f = true;
                break;
            }
        }
        return f;
    }

    // add a pair  <studentid, recordid> to a node
    // only for leaf node
    void add_kv(long studentid, long recordid) {
        int pos=0, i=this.n;
        for (pos=0; pos < this.n; pos++) {
            if (studentid < this.keys[pos]) { break; }
        }
        while (pos < i) {
            this.keys[i] = this.keys[i-1];
            this.values[i] = this.values[i-1];
            i--;
        }
        this.keys[pos] = studentid;
        this.values[pos] = recordid;
        n++;
    }

    // Constructor
    BTreeNode(int min_degree, boolean leaf) {
        // max_degree = 2*min_degree-1
        this.n = 0;
        this.min_degree = min_degree;
        this.leaf = leaf;
        this.keys = new long[2*min_degree-1];
        this.values = new long[2*min_degree-1];
        this.children = new BTreeNode[2 * min_degree];
        this.next = null;
    }
}
