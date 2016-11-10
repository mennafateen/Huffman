/**
 * Created by menna on 11/2/2016.
 */
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.Scanner;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

public class Huffman extends Application {

    public static void main(String args[]) {
        launch(args);
    }

    public static File Compress(String path) {
        String original = "";
        try {
            original = new Scanner(new File(path)).useDelimiter("\\Z").next(); // convert everything in file into one string -> content
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

        File originFile = new File(path);
        String dir = originFile.getParent();

        File file = new File(dir + "\\table.txt");

        String write = "";
        for (Map.Entry<Character, String> entry : table.entrySet()) {
            Character key = entry.getKey();
            String value = entry.getValue();
            write += key;
            write += "\n";
            write += value;
            write += "\n";
            //System.out.println("key, " + key + " value " + value);
        }
        try (PrintWriter out = new PrintWriter(dir + "\\table.txt")) { // make a new compressed file in same working dir
            out.println(write); // write tags
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        File compressed = new File(dir + "\\compressed.txt"); // change to / on linux

        try(PrintWriter out = new PrintWriter(dir + "\\compressed.txt")) { // make a new compressed file in same working dir
            out.println(result); // write tags
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return compressed;
      //  return result;
    }


    public static File deCompress(String path) {
        String code = "";
        try {
            code = new Scanner(new File(path)).useDelimiter("\\Z").next(); // convert everything in file into one string -> content
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        File originFile = new File(path);
        String dir = originFile.getParent();

        File table = new File(dir + "\\table.txt");

        String result = "";
        HashMap<String, Character> reversedTable = new HashMap<>();
        try(Scanner cin = new Scanner(table)) {
            while (cin.hasNext()) {
                Character ch = cin.nextLine().charAt(0);
                System.out.println(ch);
                String bcode = cin.nextLine();
                System.out.println(bcode);
                reversedTable.put(bcode, ch);
            }
        }
        catch (FileNotFoundException f) {
            f.printStackTrace();
        }

        String temp = "";
        for (int i = 0; i < code.length(); i++) {
            temp += code.charAt(i);
            if (reversedTable.get(temp) != null) {
                result += reversedTable.get(temp);
                temp = "";
            }
        }
        System.out.println(result);

        File decompressed = new File(dir + "\\decompressed.txt");

        try(PrintWriter out = new PrintWriter(dir + "\\decompressed.txt")) { // make a decompressed file in same working dir
            out.println(result);
            out.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return decompressed;
      //  return result;
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


    // GUI

    private Desktop desktop = Desktop.getDesktop(); // opening files like this doesnt work on linux
    private void openFile(File file) {
        try {
            desktop.open(file); // use processbuilder for linux
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    Button browse, compress, decompress;
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("LZ77 Compressor/Decompressor Tool");
        Label home = new Label("Choose file to compress/decompress:");
        TextField fillWithPath = new TextField();
        fillWithPath.setMaxWidth(200);
        browse = new Button("Browse");
        FileChooser choose = new FileChooser();
        browse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser.ExtensionFilter extFilter =
                        new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt"); // only choose text files
                choose.getExtensionFilters().add(extFilter);

                // show open file dialog
                File file = choose.showOpenDialog(primaryStage);
                fillWithPath.setText(file.getAbsolutePath()); // extension filter needed here

            }
        });
        VBox layout1  = new VBox(20);
        layout1.setAlignment(Pos.CENTER);
        // StackPane layout1 = new StackPane();
        compress = new Button("Compress");
        decompress = new Button("Decompress");

        compress.setOnAction(e -> {
            if (fillWithPath.getCharacters().toString().isEmpty()) { // FILE NOT CHOSEN
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a file.");
                alert.show();
            }
            else if (!new File(fillWithPath.getCharacters().toString()).isFile()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a valid path");
                alert.show();
            }
            else {
                File o = Compress(fillWithPath.getText());
                openFile(o);
            }


        });
        decompress.setOnAction(e -> {
            if(fillWithPath.getCharacters().toString().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a file.");
                alert.show();
            }
            else if (!new File(fillWithPath.getCharacters().toString()).isFile()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please choose a valid path");
                alert.show();
            }
            else {
                File d = deCompress(fillWithPath.getText());
                openFile(d);
            }
        });


        layout1.getChildren().addAll(home, fillWithPath, browse, compress, decompress);
        Scene scene = new Scene(layout1, 300, 250);
        layout1.setBackground(new Background(new BackgroundFill(Color.LAVENDERBLUSH, CornerRadii.EMPTY, Insets.EMPTY))); //THIS IS SO FUN OMG - LOVE THE COLOR NAMES
        compress.setFont(Font.font("Lucida Grande")); // just for fun :) عشان انا فاضية
        decompress.setFont(Font.font("Lucida Grande"));
        browse.setFont(Font.font("Lucida Grande"));
        fillWithPath.setFont(Font.font("Lucida Grande"));
        home.setFont(Font.font("Lucida Grande"));
        primaryStage.setScene(scene);
        primaryStage.show();


    }



}
