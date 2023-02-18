package cz.klecansky.nndsa;

import cz.klecansky.nndsa.algorithms.DijkstraAlgorithm;
import cz.klecansky.nndsa.algorithms.Dijsktra;
import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.io.ExporterCsv;
import cz.klecansky.nndsa.io.ImporterCsv;
import cz.klecansky.nndsa.utils.Triplet;
import cz.klecansky.nndsa.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
    public Button shortestPathButton;
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
        reloadLists();
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
        Dialog<Triplet<String, String, Double>> dialog = Utils.edgeDialog(graph.getVerticesKey().stream().toList());
        Optional<Triplet<String, String, Double>> result = dialog.showAndWait();
        result.ifPresent(vertex -> {
            graph.addEdge(vertex.getFirst(), vertex.getSecond(), vertex.getThird());
            reloadLists();
        });
    }

    @FXML
    public void deleteEdge(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = Utils.removeEdgeDialog(graph.getVerticesKey().stream().toList());
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(vertex -> {
            graph.deleteEdge(vertex.getKey(), vertex.getValue());
            reloadLists();
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
            Vertex<String, Integer> sourceVertex = graph.vertexByKey(vertex.getKey());
            dijkstraAlgorithm.execute(sourceVertex);
            Vertex<String, Integer> targetVertex = graph.vertexByKey(vertex.getValue());
            LinkedList<Vertex<String, Integer>> path = dijkstraAlgorithm.getPath(targetVertex);
            System.out.println(path);
        });

    }


    private void reloadLists() {
        verticesListView.getItems().clear();
        edgeListView.getItems().clear();
        List<String> verticesKey = new ArrayList<>(graph.getVerticesKey().stream().toList());
        Utils.SortForVerticesAndEdges(verticesKey);
        verticesListView.getItems().addAll(verticesKey);
        List<String> edges = new ArrayList<>(graph.getEdges().stream().map(Edge::toString).toList());
        Utils.SortForVerticesAndEdges(edges);
        edgeListView.getItems().addAll(edges);
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