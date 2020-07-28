/**
 * Do NOT modify.
 * This is the class with the main function
 */

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.*;

/**
 * B+Tree Structure
 * Key - StudentId
 * Leaf Node should contain [ key,recordId ]
 */
class BTree {

    private BTreeNode root;     //Pointer to the root node.
    private int t;  //Number of key-value pairs allowed in the tree/the minimum degree of B+Tree

    BTree(int t) {
        this.root = null;
        this.t = t;
    }


    long search(long studentId) {
        /*
         * Implement this function to search in the B+Tree.
         * Return recordID for the given StudentID.
         * Otherwise, print out a message that the given studentId has not been found in the table and return -1.
         */
        BTreeNode cur = root;
        if (cur==null)  return -1;
        while (!cur.leaf) {
            int pos=-1;
            for (pos=0; pos<cur.n; pos++) {
                if (studentId < cur.keys[pos]) {break;}
            }
            cur = cur.children[pos];
        }
        return cur.get_v(studentId);
    }

    // insert the student object into the csv file
    // append a new line and not to write to it
    void insert_to_csv(Student student) {
        long studentId=student.studentId;
        String studentName=student.studentName;
        String major=student.major;
        String level=student.level;
        int age=student.age;
        long recordId=student.recordId;
        // try-catch is a must
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("src/Student.csv",true)); // append=true
            bw.write(studentId + "," + studentName + "," + major + "," + level + "," + age + "," + recordId+"\n");
            bw.close();
        }  catch (Exception e) {
            e.printStackTrace();
        }
    }

    void split(long k, long v, List<BTreeNode> path) {
        long key=-1;
        BTreeNode cur=null, l=null,r=null;
        // loop with all the node in `path` until we find a node that can not be split
        for (int i=path.size()-1; i>=0; i--) {
            cur = path.get(i);
            int pos;
            long[] keys=new long[2*t], values=new long[2*t];    // keys,values will store the sorted <k,v> of length 2*t
            System.arraycopy(cur.keys, 0,keys,0,cur.n);
            System.arraycopy(cur.values, 0,values,0,cur.n);
            // the leaf node
            if (i==path.size()-1) {
                // insert k,v to keys, values
                pos = Arrays.binarySearch(keys, 0, 2*t-1, k);
                pos = pos<0 ? -pos-1 : pos;
                for (int j=cur.n; pos<j; j--) {
                    keys[j]=keys[j-1];
                    values[j]=values[j-1];
                }
                keys[pos]=k;
                values[pos]=v;
                key=cur.keys[t];

                // left node is cur node, right node will be created (both leaf node)
                l=cur;
                r=new BTreeNode(t,true);
                System.arraycopy(keys, t, r.keys, 0, t);    // copy the last t keys from keys to right node
                System.arraycopy(values, t, r.values, 0, t);// copy the last t values from values to right node
                Arrays.fill(l.keys, t, 2*t-1, 0);       // remove the keys[t:] in the left node
                Arrays.fill(l.values, t, 2*t-1, 0);     // remove the values[t:] in the left node
                /*
                           4
                   1,2,3       4,5,6
                 */
                // modify the l,r's n to t
                l.n = t;
                r.n = t;
                // modify the next pointer
                r.next = l.next;
                l.next = r;
            }
            // the node in the middle or the root node
            else {
                BTreeNode[] children= new BTreeNode[2*t+1];
                System.arraycopy(cur.children, 0, children, 0, cur.n + 1);
                pos = Arrays.binarySearch(keys, 0, cur.n, key);
                pos = pos<0 ? -pos-1 : pos;
                for (int j=cur.n; pos<j; j--) {
                    children[j+1]=children[j];
                    keys[j]=keys[j-1];
                }
                keys[pos]=key;
                children[pos]=l;
                children[pos+1]=r;
                // if one of the node in the middle is not full, add the key and split children to it and insert done
                if (cur.n<2*t-1) {
                    cur.keys = keys.clone();
                    cur.children = children;
                    cur.n++;
                    return;
                }
                // otherwise we need to split this middle node to two nodes,
                // the left node is cur node, the right node is newly created
                /*
                           4
                    1,2,3      4,5,6
                */
                l=cur;
                r=new BTreeNode(t, false);
                key=l.keys[t];
                System.arraycopy(keys, t, r.keys, 0, t);    // copy the last t values from values to right node
                Arrays.fill(l.keys, t, 2*t-1, 0);           // remove the keys[t:] in the left node
                // copy the last t-1 children from `children` to right node's children
                // remove the l's last t-1 children
                for (int j=t+1; j<=2*t; j++) {
                    r.children[j-t]=children[j];
                    if (j<2*t)  l.children[j]=null;
                }
                // modify the l,r's n to t
                l.n = t;
                r.n = t;
                // split the root node
                if (i==0) {
                    BTreeNode new_root=new BTreeNode(t, false);
                    new_root.keys[0]=key;
                    new_root.n=1;
                    new_root.children[0]=l;
                    new_root.children[1]=r;
                    this.root=new_root;
                }
            }
        }
    }

    BTree insert(Student student) {
        /*
         * Implement this function to insert in the B+Tree.
         * Also, insert in student.csv after inserting in B+Tree.
         */
        BTreeNode cur=root;
        long studentid = student.studentId, recordid = student.recordId;

        // if the btree has only one root node and it's empty
        if (cur==null) {
            cur = new BTreeNode(t, true);
            cur.add_kv(studentid, recordid);
            root = cur;
            return this;
        }
        // if the btree has only one root node and it's full or not full
        if (cur.leaf) {
            if (cur.n<cur.keys.length) {
                cur.add_kv(studentid, recordid);
            }
            else {
                BTreeNode new_root=new BTreeNode(t, false), l=root, r=new BTreeNode(t,true); l.leaf=true;
                int pos,i;
                long[] keys=new long[2*t], values=new long[2*t];
                System.arraycopy(root.keys, 0,keys,0,root.n);
                System.arraycopy(root.values, 0,values,0,root.n);

                pos = Arrays.binarySearch(keys, 0, 2*t-1, studentid);
                pos = pos<0 ? -pos-1 : pos;
                for (i=root.n; pos<i; i--){
                    keys[i]=keys[i-1];
                    values[i]=values[i-1];
                }
                keys[pos]=studentid;
                values[pos]=recordid;
                /*
                          4
                  1,2,3       4,5,6
                 */
                System.arraycopy(keys, t, r.keys, 0, t);
                System.arraycopy(values, t, r.values, 0, t);
                new_root.keys[0]=l.keys[t];
                new_root.n=1;
                l.n = t;
                r.n = t;
                l.next=r;
                Arrays.fill(l.keys, t, 2*t-1, 0);
                Arrays.fill(l.values, t, 2*t-1, 0);
                new_root.children[0]=l;
                new_root.children[1]=r;
                this.root=new_root;
            }
            return this;
        }
        // the root node is not the leaf node, then search down from the root node to the leaf node. path: contain all the node from root to leaf
        List<BTreeNode> path = new ArrayList<>();
        path.add(cur);
        while (!cur.leaf) {
            int i;
            for (i=0; i < cur.n; i++) {
                if (studentid < cur.keys[i])    break;
            }
            cur = cur.children[i];
            path.add(cur);
        }

        // if the leaf node is not empty, add it to the leaf node directly
        if (cur.n<2*t-1) {
            cur.add_kv(studentid, recordid);
        }
        // otherwise we need to split the node recursively until a node in `path` is not full
        else {
            split(studentid, recordid, path);
        }
        return this;
    }

    boolean delete(long studentid) {
        /*
         * Implement this function to delete in the B+Tree.
         * Also, delete in student.csv after deleting in B+Tree, if it exists.
         * Return true if the student is deleted successfully otherwise, return false.
         */
        BTreeNode cur=root;
        // search down from the root node to the leaf node. path: contain all the node from root to leaf
        List<BTreeNode> path = new ArrayList<>();
        path.add(cur);
        while (!cur.leaf) {
            int i;
            for (i=0; i < cur.n; i++) {
                if (studentid < cur.keys[i])    break;
            }
            cur = cur.children[i];
            path.add(cur);
        }
        BTreeNode leafnode = path.get(-1);
        // directly delete the k-v in leaf node
        if (leafnode.n>t) {
            return leafnode.remove(studentid);
        }

        return true;
    }

    List<Long> print() {

        List<Long> listOfRecordID = new ArrayList<>();

        /*
         * Return a list of recordIDs from left to right of leaf nodes.
         */
        BTreeNode cur=this.root;
        // find the leftmost leaf node
        while (cur.children[0] != null) cur = cur.children[0];
        // loop through all the leaf node
        // from the left to the right
        while (cur !=null) {
            for (int i=0; i<cur.n; i++) {
//                System.out.println(cur.keys[i] + " " + cur.values[i]);
                listOfRecordID.add(cur.values[i]);
            }
            cur = cur.next;
        }
        return listOfRecordID;
    }

}
