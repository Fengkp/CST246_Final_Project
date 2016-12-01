import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Scanner;

public class Main extends Application {

    private File selectedFile;
    private long wordCount;
    private StringBuilder fileText;
    Label syllableCountLbl = new Label("Syllable Count: ");
    Label wordCountLbl = new Label("Word Count: ");
    Label sentenceCountLbl = new Label("Sentence Count: ");
    Label fleschScoreLbl = new Label("Flesch Score: ");

    public void truncateFile(double percentIncrement) throws IOException {      // receives a decimal in place of percent to increment every time the method is called

        double temp = wordCount * (percentIncrement / 100);
        long wordsPerFile = (long)temp;
        StringBuilder currentLine = new StringBuilder();
        Scanner file = new Scanner(selectedFile);
        String fileName = "inputData/output" + (int)percentIncrement + "Percent.txt";
        File outputFile = new File((fileName));
        Writer writer = new PrintWriter(outputFile);

        for (int counter = 0; counter < wordsPerFile; counter++) {
            String currentWord = file.next();
            currentLine.append(currentWord + " ");
        }
        writer.write(currentLine.toString());
        writer.close();
        file.close();
    }

    public long wordCount() throws IOException {
        wordCount = 0;
        Scanner file = new Scanner(selectedFile);

        while (file.hasNext()) {
            file.next();
            wordCount++;
        }
        file.close();
        return wordCount;
    }

    public long getTime() {
        Helper method1 = new TextAnalyzer1(fileText.toString());
        wordCountLbl.setText("Word Count: " + Long.toString(method1.getNumberOfWords()));
        sentenceCountLbl.setText("Sentence Count: " + Long.toString(method1.getNumberOfSentences()));
        syllableCountLbl.setText("Syllable Count: " + Long.toString(method1.getNumberOfSyllables()));
        fleschScoreLbl.setText("Flesch Score: " + Double.toString(method1.getFleschScore()));

        return 0;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        // Graph
        Stage graphStage = new Stage();
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart <Number, Number> lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Line Chart");

        XYChart.Series series = new XYChart.Series();
        series.setName("Data");

        Scene graphScene = new Scene (lineChart, 500, 500);
        graphStage.setScene(graphScene);
        graphStage.setTitle("Graph");
        graphStage.show();

        // Main Window
        BorderPane layout = new BorderPane();
        Scene scene = new Scene(layout,1280, 720);
        stage.setScene(scene);
        stage.setTitle("CST246 Text Editor");

        // Text Area
        TextArea textA = new TextArea();

        layout.setCenter(textA);

        // Status Bar
        HBox statusHBox = new HBox();
        statusHBox.getChildren().addAll(syllableCountLbl, wordCountLbl, sentenceCountLbl, fleschScoreLbl);
        statusHBox.setSpacing(60);
        statusHBox.setPadding(new Insets(5));
        layout.setBottom(statusHBox);

        // Menu Bar
        MenuBar menuB = new MenuBar();
            // File Menu
            Menu fileMenu = new Menu("File");
            MenuItem newFileMI = new MenuItem("New");   // Clear text area
                newFileMI.setOnAction(e -> textA.clear());
            MenuItem openFileMI = new MenuItem("Open...");      // Open text file
                FileChooser fileBrowser = new FileChooser();
                fileBrowser.setInitialDirectory(new File("inputData"));
                fileBrowser.getExtensionFilters().addAll((new FileChooser.ExtensionFilter("Text Files", "*.txt")));
                openFileMI.setOnAction(e -> {
                    selectedFile = fileBrowser.showOpenDialog(null);
                    stage.setTitle("CST246 Text Editor - " + selectedFile.getName());
                    try {
                        Scanner file = new Scanner(selectedFile);
                        fileText = new StringBuilder();
                        while (file.hasNext())
                            fileText.append(file.next() + " ");
                        textA.setText(fileText.toString());
                        textA.wrapTextProperty().set(true);

                        getTime();

                        series.getData().add(new XYChart.Data(1, 2));
                        lineChart.getData().add(series);
//
//                        Helper method2 = new TextAnalyzer2(fileText.toString());
//                        long[] countList = method2.getCounts();
//                        System.out.println(countList[1]);
//                        wordCountLbl.setText("Word Count: " + Long.toString(countList[0]));
//                        sentenceCountLbl.setText("Sentence Count: " + Long.toString(countList[1]));
//                        syllableCountLbl.setText("Syllable Count: " + Long.toString(countList[2]));
//                        //fleschScoreLbl.setText("Flesch Score: " + Double.toString(method2.getFleschScore()));
                    } catch (IOException IOEx) {
                        IOEx.getMessage();
                    }
                });
            MenuItem truncateFileMI = new MenuItem("Truncate");
                truncateFileMI.setOnAction(e -> {
                            try {
                                wordCount();
                                for (int i = 10; i < 101; i += 10)
                                    truncateFile(i);
                            } catch (IOException IOEx) {
                                IOEx.getMessage();
                            }
                });
            MenuItem saveCountsMI = new MenuItem("Save Counts");        // Save word, syllable, sentence count, flesch score
                saveCountsMI.setOnAction(e -> {
                    try {
                        StringBuilder counts = new StringBuilder();
//                        counts.append(wordCountLbl)
                        Writer writer = new PrintWriter(new File("outputData/results.txt"));
                        writer.write(wordCountLbl.getText() + "\n"
                            + syllableCountLbl.getText() + "\n"
                            + sentenceCountLbl.getText() + "\n"
                            + fleschScoreLbl.getText());
                    } catch (IOException IOEx) {
                        IOEx.getMessage();
                    }
                });
            MenuItem saveTextMI = new MenuItem("Save Text...");    // Save text in text area
            MenuItem exitMI = new MenuItem("Exit");
                exitMI.setOnAction(e -> stage.close());
            fileMenu.getItems().addAll(newFileMI, openFileMI, truncateFileMI, saveCountsMI,
                    saveTextMI, exitMI);
            // Edit Menu
            Menu editMenu = new Menu("Edit");   // Display values of each menu item onto status bar
            MenuItem wordCountMI = new MenuItem("Word Count");
                wordCountMI.setOnAction(e -> {
                    fileText = new StringBuilder();
                    fileText.append(textA.getText());
                    Helper method1 = new TextAnalyzer1(fileText.toString());
                    wordCountLbl.setText("Word Count: " + Long.toString(method1.getNumberOfWords()));
                    //Helper method2 = new TextAnalyzer2(fileText.toString());
                });
            MenuItem sentenceCountMI = new MenuItem("Sentence Count");
                sentenceCountMI.setOnAction(e -> {
                    fileText = new StringBuilder();
                    fileText.append(textA.getText());
                    Helper method1 = new TextAnalyzer1(fileText.toString());
                    sentenceCountLbl.setText("Sentence Count: " + Long.toString(method1.getNumberOfSentences()));
                });
            MenuItem syllableCountMI = new MenuItem("Syllable Count");
                syllableCountMI.setOnAction(e -> {
                    fileText = new StringBuilder();
                    fileText.append(textA.getText());
                    Helper method1 = new TextAnalyzer1(fileText.toString());
                    syllableCountLbl.setText("Syllable Count: " + Long.toString(method1.getNumberOfSyllables()));
                });
            MenuItem fleschMI = new MenuItem("Flesch Score");
                fleschMI.setOnAction(e -> {
                    fileText = new StringBuilder();
                    fileText.append(textA.getText());
                    Helper method1 = new TextAnalyzer1(fileText.toString());
                    fleschScoreLbl.setText("Flesch Score: " + Double.toString(method1.getFleschScore()));
                });
            editMenu.getItems().addAll(wordCountMI, sentenceCountMI,
                    syllableCountMI, fleschMI);
            menuB.getMenus().addAll(fileMenu, editMenu);
        layout.setTop(menuB);

        stage.show();




    }
}
