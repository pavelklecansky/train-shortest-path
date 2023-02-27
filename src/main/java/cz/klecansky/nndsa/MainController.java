package cz.klecansky.nndsa;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.io.ExporterCsv;
import cz.klecansky.nndsa.io.ImporterCsv;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import cz.klecansky.nndsa.rail.Train;
import cz.klecansky.nndsa.ui.GraphUi;
import cz.klecansky.nndsa.ui.Weight;
import cz.klecansky.nndsa.utils.RailDialogReturn;
import cz.klecansky.nndsa.utils.ShortestPathDialogReturn;
import cz.klecansky.nndsa.utils.Utils;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
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
    public Button addRailSwitchButton;
    @FXML
    public Button addRailButton;
    @FXML
    public Button deleteRailButton;

    @FXML
    public ListView<String> railSwitchListView;
    @FXML
    public ListView<String> railListView;
    @FXML
    public Button exportRailwayInfrastructureButton;
    @FXML
    public Button shortestPathButton;
    @FXML
    public ListView<String> shortestPathListView;
    @FXML
    public ListView<String> trainListView;
    @FXML
    public Label shortestPathLabel;
    @FXML
    private Button importRailwayInfrastructureButton;

    private final ImporterCsv importerCsv;
    private final ExporterCsv exporterCsv;
    private RailwayInfrastructure railwayInfrastructure;
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
    void importRailwayInfrastructure(ActionEvent event) throws IOException {
        File fileFromFileChooser = getFileFromFileChooser();
        railwayInfrastructure = importerCsv.importRailwayInfrastructure(fileFromFileChooser);
        reloadUi();
        enableButtons();
    }

    private void ChangeEmptyGraphUi(RailwayInfrastructure graph) {
        SmartGraphPanel<String, Weight> newGraphUi = GraphUi.getGraphUi(graph);
        hbox.getChildren().set(hbox.getChildren().indexOf(graphUi), newGraphUi);
        graphUi = newGraphUi;
        HBox.setHgrow(graphUi, Priority.ALWAYS);
    }

    @FXML
    public void exportRailwayInfrastructure(ActionEvent actionEvent) throws IOException {
        File fileForSaveFromFileChooser = getFileForSaveFromFileChooser();
        exporterCsv.exportGraph(fileForSaveFromFileChooser, railwayInfrastructure);
    }

    @FXML
    public void addRailSwitch(ActionEvent actionEvent) {
        Dialog<RailSwitch> dialog = Utils.railSwitchDialog();
        Optional<RailSwitch> result = dialog.showAndWait();
        result.ifPresent(railSwitch -> {
            railwayInfrastructure.addSwitch(railSwitch);
            reloadUi();
        });
    }

    @FXML
    public void addRail(ActionEvent actionEvent) {
        Dialog<RailDialogReturn> dialog = Utils.railDialog(railwayInfrastructure.getSwitches().stream().map(RailSwitch::getName).toList());
        Optional<RailDialogReturn> result = dialog.showAndWait();
        result.ifPresent(rail -> {
            railwayInfrastructure.addRail(rail.railName(), rail.startRailSwitchKey(), rail.endRailSwitchKey(), new Rail(rail.railName(), rail.railLength(), rail.train()));
            reloadUi();
        });
    }

    @FXML
    public void deleteRail(ActionEvent actionEvent) {
        Dialog<Pair<String, String>> dialog = Utils.removeEdgeDialog(railwayInfrastructure.getSwitches().stream().map(RailSwitch::getName).toList());
        Optional<Pair<String, String>> result = dialog.showAndWait();
        result.ifPresent(rail -> {
            railwayInfrastructure.deleteRail(rail.getKey(), rail.getValue());
            reloadUi();
        });
    }

    @FXML
    public void shortestPath(ActionEvent actionEvent) {
        Dialog<ShortestPathDialogReturn> dialog = Utils.shortestPathDialog(railwayInfrastructure);
        Optional<ShortestPathDialogReturn> result = dialog.showAndWait();
        result.ifPresent(shortestPathReturn -> {
            System.out.println(shortestPathReturn);
            try {
                List<Vertex<String, RailSwitch, Rail>> shortestPath = railwayInfrastructure.shortestPath(shortestPathReturn.firstViaRailSwitch(), shortestPathReturn.startRail(), shortestPathReturn.secondViaRailSwitch(), shortestPathReturn.endRail(), shortestPathReturn.trainLength());
                double shortestPathDistance = shortestPath.get(shortestPath.size() - 1).getMinDistance();
                shortestPathLabel.setText(Utils.shortestPathFormat(shortestPathDistance));
                shortestPathListView.getItems().clear();
                shortestPathListView.getItems().addAll(shortestPath.stream().map(Vertex::getKey).toList());
            } catch (Exception exception) {
                Utils.alert(exception.getMessage());
            }

        });
    }

    private void reloadUi() {
        ChangeEmptyGraphUi(railwayInfrastructure);
        railSwitchListView.getItems().clear();
        railListView.getItems().clear();
        trainListView.getItems().clear();
        List<String> verticesKey = new ArrayList<>(railwayInfrastructure.getSwitches().stream().map(RailSwitch::toString).toList());
        Utils.SortForRailsAndRailsSwitches(verticesKey);
        railSwitchListView.getItems().addAll(verticesKey);
        List<String> edges = new ArrayList<>(railwayInfrastructure.getRailsInfo().stream().toList());
        Utils.SortForRailsAndRailsSwitches(edges);
        railListView.getItems().addAll(edges);
        List<String> trains = new ArrayList<>(railwayInfrastructure.getTrains().stream().map(Train::toString).toList());
        trainListView.getItems().addAll(trains);
        Platform.runLater(() -> {
            graphUi.init();
        });
    }

    private void enableButtons() {
        addRailButton.setDisable(false);
        addRailSwitchButton.setDisable(false);
        deleteRailButton.setDisable(false);
        exportRailwayInfrastructureButton.setDisable(false);
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