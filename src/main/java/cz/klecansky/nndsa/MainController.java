package cz.klecansky.nndsa;

import com.brunomnsilva.smartgraph.example.City;
import com.brunomnsilva.smartgraph.example.Distance;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import cz.klecansky.nndsa.algorithms.DijkstraAlgorithm;
import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.io.ExporterCsv;
import cz.klecansky.nndsa.io.ImporterCsv;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.ui.GraphUi;
import cz.klecansky.nndsa.ui.Weight;
import cz.klecansky.nndsa.utils.Triplet;
import cz.klecansky.nndsa.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

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
    @FXML
    public Button exportGraphButton;
    @FXML
    public Button shortestPathButton;
    @FXML
    public ListView<String> shortestPathListView;
    @FXML
    private Button importGraphButton;

    private final ImporterCsv importerCsv;
    private final ExporterCsv exporterCsv;
    private Graph<String, Rail> graph;
    private SmartGraphPanel<String, Weight> graphUi;

    public MainController() {
        importerCsv = new ImporterCsv();
        exporterCsv = new ExporterCsv();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        graphUi = GraphUi.getEmptyGraphUi();
        hbox.getChildren().add(graphUi);
        HBox.setHgrow(graphUi, Priority.ALWAYS);
    }

    @FXML
    void importGraph(ActionEvent event) throws IOException {
        File fileFromFileChooser = getFileFromFileChooser();
        graph = importerCsv.importGraph(fileFromFileChooser);
        ChangeEmptyGraphUi(graph);
        reloadUi();
        enableButtons();
    }

    private void ChangeEmptyGraphUi(Graph<String, Rail> graph) {
        SmartGraphPanel<String, Weight> newGraphUi = GraphUi.getGraphUi(graph);
        hbox.getChildren().set(hbox.getChildren().indexOf(graphUi), newGraphUi);
        graphUi = newGraphUi;
        HBox.setHgrow(graphUi, Priority.ALWAYS);
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
        Dialog<Triplet<String, String, Double>> dialog = Utils.edgeDialog(graph.getVerticesKey().stream().toList());
        Optional<Triplet<String, String, Double>> result = dialog.showAndWait();
        result.ifPresent(vertex -> {
            graph.addEdge(vertex.getFirst(), vertex.getSecond(), new Rail(vertex.getThird()));
            reloadUi();
        });
    }

    @FXML
    public void deleteEdge(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = Utils.removeEdgeDialog(graph.getVerticesKey().stream().toList());
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(vertex -> {
            graph.deleteEdge(vertex.getKey(), vertex.getValue());
            reloadUi();
        });
    }

    @FXML
    public void shortestPath(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = Utils.shortestPathDialog(graph.getVerticesKey().stream().toList());
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(vertex -> {
//            Dijsktra dijsktra = new Dijsktra();
//            Vertex<String, Integer> sourceVertex = graph.vertexByKey(vertex.getKey());
//            dijsktra.computePath(sourceVertex);
//            Vertex<String, Integer> targetVertex = graph.vertexByKey(vertex.getValue());
//            List<Vertex<String, Integer>> shortestPathTo = dijsktra.getShortestPathTo(targetVertex);
//            System.out.println(shortestPathTo);
            DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(graph);
            Vertex<String, Rail> sourceVertex = graph.vertexByKey(vertex.getKey());
            dijkstraAlgorithm.execute(sourceVertex);
            Vertex<String, Rail> targetVertex = graph.vertexByKey(vertex.getValue());
            LinkedList<Vertex<String, Rail>> path = dijkstraAlgorithm.getPath(targetVertex);
            shortestPathListView.getItems().clear();
            shortestPathListView.getItems().addAll(path.stream().map(Vertex::getKey).toList());
        });
    }

    private void reloadUi() {
        verticesListView.getItems().clear();
        edgeListView.getItems().clear();
        List<String> verticesKey = new ArrayList<>(graph.getVerticesKey().stream().toList());
        Utils.SortForVerticesAndEdges(verticesKey);
        verticesListView.getItems().addAll(verticesKey);
        List<String> edges = new ArrayList<>(graph.getEdges().stream().map(Edge::toString).toList());
        Utils.SortForVerticesAndEdges(edges);
        edgeListView.getItems().addAll(edges);
        Platform.runLater(() -> {
            graphUi.init();
        });
    }

    private void enableButtons() {
        addEdgeButton.setDisable(false);
        addVertexButton.setDisable(false);
        deleteEdgeButton.setDisable(false);
        exportGraphButton.setDisable(false);
        shortestPathButton.setDisable(false);
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