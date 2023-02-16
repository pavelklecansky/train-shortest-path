package cz.klecansky.nndsa;

import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.io.ExporterCsv;
import cz.klecansky.nndsa.io.ImporterCsv;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class MainController {

    @FXML
    public HBox hbox;
    @FXML
    public Button addVertexButton;
    @FXML
    public Button addEdgeButton;
    @FXML
    public Button deleteEdgeButton;

    @FXML
    public ListView<String> verticesListView;
    @FXML
    public ListView<String> edgeListView;
    public Button exportGraphButton;
    @FXML
    private Button importGraphButton;

    private final ImporterCsv importerCsv;
    private final ExporterCsv exporterCsv;
    private Graph<String, Integer> graph;

    public MainController() {
        importerCsv = new ImporterCsv();
        exporterCsv = new ExporterCsv();
    }

    @FXML
    void importGraph(ActionEvent event) throws IOException {
        File fileFromFileChooser = getFileFromFileChooser();
        graph = importerCsv.importGraph(fileFromFileChooser);
        verticesListView.getItems().addAll(graph.getVerticesKey());
        edgeListView.getItems().addAll(graph.getEdges());
        enableButtons();
    }

    @FXML
    public void exportGraph(ActionEvent actionEvent) throws IOException {
        File fileForSaveFromFileChooser = getFileForSaveFromFileChooser();
        exporterCsv.exportGraph(fileForSaveFromFileChooser, graph);
    }

    @FXML
    public void addVertex(ActionEvent actionEvent) {
        System.out.println("addVertex");
    }

    @FXML
    public void addEdge(ActionEvent actionEvent) {
        System.out.println("addEdge");
    }

    @FXML
    public void deleteEdge(ActionEvent actionEvent) {
        System.out.println("deleteEdge");
    }


    private void enableButtons() {
        addEdgeButton.setDisable(false);
        addVertexButton.setDisable(false);
        deleteEdgeButton.setDisable(false);
        exportGraphButton.setDisable(false);
    }

    private File getFileFromFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showOpenDialog(hbox.getScene().getWindow());
    }

    private File getFileForSaveFromFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save CSV file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV file", "*.csv"));
        fileChooser.setInitialDirectory(new File("."));
        return fileChooser.showSaveDialog(hbox.getScene().getWindow());
    }


}