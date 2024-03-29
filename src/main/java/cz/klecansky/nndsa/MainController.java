package cz.klecansky.nndsa;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import cz.klecansky.nndsa.algorithms.ShortestPathDisplay;
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
import cz.klecansky.nndsa.utils.Triplet;
import cz.klecansky.nndsa.utils.Utils;
import javafx.application.Platform;
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
    public Button editRailSwitchButton;
    @FXML
    public Button deleteRailSwitchButton;
    @FXML
    public Button editRailButton;
    @FXML
    public Button addTrainButton;
    @FXML
    public Button editTrainButton;
    @FXML
    public Button deleteTrainButton;
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
    void importRailwayInfrastructure() throws IOException {
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
    public void exportRailwayInfrastructure() throws IOException {
        File fileForSaveFromFileChooser = getFileForSaveFromFileChooser();
        exporterCsv.exportGraph(fileForSaveFromFileChooser, railwayInfrastructure);
    }

    @FXML
    public void addRailSwitch() {
        Dialog<RailSwitch> dialog = Utils.railSwitchDialog();
        Optional<RailSwitch> result = dialog.showAndWait();
        result.ifPresent(railSwitch -> {
            railwayInfrastructure.addSwitch(railSwitch);
            reloadUi();
        });
    }

    @FXML
    public void addRail() {
        Dialog<RailDialogReturn> dialog = Utils.railDialog(railwayInfrastructure.getSwitches().stream().map(RailSwitch::getName).toList());
        Optional<RailDialogReturn> result = dialog.showAndWait();
        result.ifPresent(rail -> {
            railwayInfrastructure.addRail(rail.railName(), rail.startRailSwitchKey(), rail.endRailSwitchKey(), new Rail(rail.railName(), rail.railLength()));
            reloadUi();
        });
    }

    @FXML
    public void deleteRail() {
        Dialog<String> dialog = Utils.deleteRailDialog(railwayInfrastructure.getRails().stream().map(Rail::getName).sorted().distinct().toList());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(rail -> {
            railwayInfrastructure.deleteRail(rail);
            reloadUi();
        });
    }

    @FXML
    public void editRailSwitch() {
        Dialog<Pair<String, RailSwitch>> dialog = Utils.editRailSwitch(railwayInfrastructure);
        Optional<Pair<String, RailSwitch>> result = dialog.showAndWait();
        result.ifPresent(railSwitchPair -> {
            try {
                railwayInfrastructure.editRailSwitch(railSwitchPair.getKey(), railSwitchPair.getValue());
                reloadUi();
            } catch (Exception exception) {
                Utils.alert(exception.getMessage());
            }
        });
    }


    @FXML
    public void deleteRailSwitch() {
        Dialog<String> dialog = Utils.deleteRailSwitch(railwayInfrastructure.getSwitchKeys());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(railSwitch -> {
            railwayInfrastructure.deleteRailSwitch(railSwitch);
            reloadUi();
        });
    }

    @FXML
    public void editRail() {
        Dialog<Pair<String, Rail>> dialog = Utils.editRail(railwayInfrastructure);
        Optional<Pair<String, Rail>> result = dialog.showAndWait();
        result.ifPresent(railPair -> {
            try {
                railwayInfrastructure.editRail(railPair.getKey(), railPair.getValue());
                reloadUi();
            } catch (Exception exception) {
                Utils.alert(exception.getMessage());
            }
        });
    }

    @FXML
    public void addTrain() {
        Dialog<Train> dialog = Utils.addTrainDialog(railwayInfrastructure);
        Optional<Train> result = dialog.showAndWait();
        result.ifPresent(rail -> {
            try {
                railwayInfrastructure.addTrain(rail);
                reloadUi();
            } catch (Exception exception) {
                Utils.alert(exception.getMessage());
            }
        });
    }

    @FXML
    public void editTrain() {
        Dialog<Pair<String, Train>> dialog = Utils.editTrainDialog(railwayInfrastructure);
        Optional<Pair<String, Train>> result = dialog.showAndWait();
        result.ifPresent(trainPair -> {
            try {
                railwayInfrastructure.editTrain(trainPair.getKey(), trainPair.getValue());
                reloadUi();
            } catch (Exception exception) {
                Utils.alert(exception.getMessage());
            }
        });
    }

    @FXML
    public void deleteTrain() {
        Dialog<String> dialog = Utils.deleteTrain(railwayInfrastructure.getTrains().stream().map(Train::getName).toList());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(rail -> {
            railwayInfrastructure.deleteTrain(rail);
            reloadUi();
        });
    }

    @FXML
    public void shortestPath() {
        Dialog<ShortestPathDialogReturn> dialog = Utils.shortestPathDialog(railwayInfrastructure);
        Optional<ShortestPathDialogReturn> result = dialog.showAndWait();
        result.ifPresent(shortestPathReturn -> {
            System.out.println(shortestPathReturn);
            try {
                List<ShortestPathDisplay> shortestPath = railwayInfrastructure.shortestPath(shortestPathReturn.firstViaRailSwitch(), shortestPathReturn.startRail(), shortestPathReturn.secondViaRailSwitch(), shortestPathReturn.endRail(), shortestPathReturn.trainLength());
                double shortestPathDistance = shortestPath.get(shortestPath.size() - 1).getMinDistance() + shortestPathReturn.trainLength();
                shortestPathLabel.setText(Utils.shortestPathFormat(shortestPathDistance));
                shortestPathListView.getItems().clear();
                shortestPathListView.getItems().addAll(shortestPath.stream().map(ShortestPathDisplay::toString).toList());
            } catch (Exception exception) {
                Utils.alert(exception.getMessage());
                shortestPathListView.getItems().clear();
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
        List<String> rails = new ArrayList<>(railwayInfrastructure.getRailsDetailInfo().stream().map(this::railsDetailInfoToFormatedString).toList());
        Utils.SortForRailsAndRailsSwitches(rails);
        railListView.getItems().addAll(rails);
        List<String> trains = new ArrayList<>(railwayInfrastructure.getTrains().stream().map(Train::toString).toList());
        trainListView.getItems().addAll(trains);
        Platform.runLater(() -> graphUi.init());
    }

    private String railsDetailInfoToFormatedString(Triplet<String, String, Rail> detailedInfo) {
        return String.format("%s: %s-%s %s", detailedInfo.third().getName(), detailedInfo.first(), detailedInfo.second(), detailedInfo.third().getLength());
    }

    private void enableButtons() {
        addRailButton.setDisable(false);
        addRailSwitchButton.setDisable(false);
        deleteRailButton.setDisable(false);
        exportRailwayInfrastructureButton.setDisable(false);
        shortestPathButton.setDisable(false);
        editRailSwitchButton.setDisable(false);
        deleteTrainButton.setDisable(false);
        deleteRailSwitchButton.setDisable(false);
        addTrainButton.setDisable(false);
        editTrainButton.setDisable(false);
        editRailButton.setDisable(false);
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