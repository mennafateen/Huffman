/**
 * Created by menna on 11/2/2016.
 */
import java.io.File;
import java.util.*;
public class Huffman {

    public static void main(String args[]) {
        Scanner cin = new Scanner(System.in);
        String original = cin.nextLine();
        Compress(original);


    }

    public static String Compress(String original) {
        String result = "";
        HashMap<Character, Integer> freq = calculateFrequency(original);
       // PriorityQueue<Node> nodes = calculateProbability(freq, original.length());
        Tree tree = new Tree();
        HashMap<Character, String> table = tree.constructTree(freq, original.length());
//        tree.printTree();
//        System.out.println("-----------------");
//
//        for (Map.Entry<Character, String> entry : table.entrySet()) {
//            Character key = entry.getKey();
//            String value = entry.getValue();
//            System.out.println("key, " + key + " value " + value);
//        }
//
        for (int i = 0; i < original.length(); i++) {
            result += table.get(original.charAt(i));
        }
        System.out.println(result);
        return result;
    }

    public static File makeTableFile(HashMap<Character, String> table) {
        //File file = new File();

    }

    public static HashMap<Character, Integer> calculateFrequency(String text) {
        HashMap<Character, Integer> frequency = new HashMap<>();
        for (int i = 0; i < text.length(); i++) {
            char symbol = text.charAt(i);
            //int count =
                   if(frequency.get(symbol) != null) {
           // if (count != 0) {
                frequency.put(symbol, frequency.get(symbol) + 1);
            }
            else {
                    frequency.put(symbol, 1);
            }
        }
        return frequency;
    }
    public static PriorityQueue<Node> calculateProbability(HashMap<Character, Integer> frequency, int total) {
        //PriorityQueue<Pair> Nodes = new PriorityQueue<>(frequency.size(), new Pair.compare());
        PriorityQueue<Node> Nodes = new PriorityQueue<>(frequency.size(), new Node.compare());
        for (Map.Entry<Character, Integer> entry : frequency.entrySet()) {
            Character c = entry.getKey(); Integer i = entry.getValue();
            //Pair node = new Pair(new Node(c), i/total);
            Node node = new Node(c, (double)i/total);
            Nodes.add(node);
        }
        return Nodes;

    }



}
