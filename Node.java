/**
 * Created by menna on 11/2/2016.
 */
import java.util.Comparator;
public class Node {
    char symbol;
    String binarycode;
    double probability;
    Node left, right, parent;

    public Node() {
        this.symbol = ' ';
        this.binarycode = " ";
        this.probability = 0.0;
        this.left = this.right = this.parent = null;

    }

    public Node(char symbol) {
        this.symbol = symbol;
        this.binarycode = "";
        this.probability = 0.0;
        this.right = null; this.left = null; this.parent = null;
    }

    public Node(char symbol, double probability) {
        this.binarycode = "";
        this.symbol = symbol;
        this.probability = probability;
        this.right = null; this.left = null; this.parent = null;

    }

    public static class compare implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            return  (int) ((n1.probability - n2.probability) * 10);
        }
    }

}
