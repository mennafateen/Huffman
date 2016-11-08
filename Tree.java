import java.util.*;

/**
 * Created by menna on 11/2/2016.
 */
public class Tree {
    public Node root;

    public Tree () {
        root = new Node();
    }

    public HashMap<Character, String> constructTree(HashMap<Character, Integer> frequency, int total) { //(PriorityQueue<Node> nodes) {
        PriorityQueue<Node> nodes = new PriorityQueue<>(frequency.size(), new Node.compare()); // calculate probability
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            Character c = entry.getKey(); Integer i = entry.getValue();
            //Pair node = new Pair(new Node(c), i/total);
            Node node = new Node(c, (double)i/total);
            nodes.add(node);
        }
      //  nodes = new PriorityQueue<>(new Node.compare());
        HashMap<Character, String> codeTable = new HashMap<>();
        while (nodes.size() > 1) {
            Node leaf1 = nodes.poll(); Node leaf2 = nodes.poll();
            //System.out.println(leaf1.symbol + "um? " + leaf1.probability);
            Node parent = new Node(' ', leaf1.probability + leaf2.probability);
            parent.right = leaf1; parent.left = leaf2;
            leaf1.parent = parent; leaf2.parent = parent;
            nodes.add(parent);
        }
        root = nodes.peek();


        // BFS on tree to return code table
        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t;
        while (!q.isEmpty()) {
            t = q.remove();
            if (t.right != null) {
                t.right.binarycode = t.binarycode + "1";
                q.add(t.right);
            }
            if (t.left != null) {
                t.left.binarycode = t.binarycode + "0";
                q.add(t.left);
            }
            if (t.symbol != ' ') {
                codeTable.put(t.symbol, t.binarycode);
            }

        }


        return codeTable;
    }

    public void printTree() {

        Queue<Node> q = new LinkedList<>();
        q.add(this.root);
        Node t;
        while (!q.isEmpty()) {
            t = q.remove();
            System.out.println(t.symbol + " " + t.binarycode + " " + t.probability + " " + t.parent);
            if (t.right != null) {
               // System.out.println(t.symbol + " " + t.binarycode + " " + t.probability + " " + t.parent);
                q.add(t.right);
            }
            if (t.left != null) {
                //System.out.println(t.symbol + " " + t.binarycode + " " + t.probability + " " + t.parent);
                q.add(t.left);
            }
           // if (t.symbol != ' ') {
             //   codeTable.put(t.symbol, t.binarycode);
            //}

        }


    }
}
