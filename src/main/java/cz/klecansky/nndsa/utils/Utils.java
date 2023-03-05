package cz.klecansky.nndsa.utils;

import cz.klecansky.nndsa.rail.*;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.*;

public final class Utils {


    public static final String VERTICES_EDGES_DIVIDER = "#rails#";
    public static final String EDGES_CROSSING_DIVIDER = "#crossing#";


    public static String shortestPathFormat(double length) {
        return String.format("Shortest path: %1.1f", length);
    }

    public static Dialog<RailSwitch> railSwitchDialog() {
        Dialog<RailSwitch> dialog = new Dialog<>();
        dialog.setTitle("Add rail switch");
        dialog.setHeaderText("Add new rail switch to infrastructure");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField nameField = new TextField();

        ChoiceBox<String> railSwitchType = new ChoiceBox<>();
        railSwitchType.getItems().addAll(Arrays.stream(RailSwitchType.values()).map(RailSwitchType::name).toList());


        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Rail switch type:"), 0, 1);
        grid.add(railSwitchType, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new RailSwitch(nameField.getText());
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Pair<String, RailSwitch>> editRailSwitch(RailwayInfrastructure infrastructure) {
        Dialog<Pair<String, RailSwitch>> dialog = new Dialog<>();
        dialog.setTitle("Edit rail switch");
        dialog.setHeaderText("Edit rail switch in infrastructure");

        ButtonType addButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> railSwitchKey = new ArrayList<>(infrastructure.getSwitchKeys());
        Utils.SortForRailsAndRailsSwitches(railSwitchKey);

        ChoiceBox<String> railSwitchSelect = new ChoiceBox<>();
        railSwitchSelect.getItems().addAll(railSwitchKey);

        TextField railSwitchName = new TextField();
        railSwitchName.setDisable(true);

        railSwitchSelect.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            RailSwitch railSwitch = infrastructure.getRailSwitch(newValue);
            railSwitchName.setText(railSwitch.getName());
            railSwitchName.setDisable(false);
        });

        grid.add(new Label("Select rail switch: "), 0, 0);
        grid.add(railSwitchSelect, 1, 0);
        grid.add(new Label("Rail switch name: "), 0, 1);
        grid.add(railSwitchName, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(railSwitchSelect.getValue(), new RailSwitch(railSwitchName.getText()));
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Pair<String, Rail>> editRail(RailwayInfrastructure infrastructure) {
        Dialog<Pair<String, Rail>> dialog = new Dialog<>();
        dialog.setTitle("Edit rail");
        dialog.setHeaderText("Edit rail in infrastructure");

        ButtonType addButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> railKey = new ArrayList<>(infrastructure.getRails().stream().map(Rail::getName).sorted().distinct().toList());
        Utils.SortForRailsAndRailsSwitches(railKey);

        ChoiceBox<String> railSelection = new ChoiceBox<>();
        railSelection.getItems().addAll(railKey);

        TextField railName = new TextField();
        railName.setDisable(true);
        TextField railLength = new TextField();
        railLength.setDisable(true);

        ChoiceBox<Train> train = new ChoiceBox<>();

        railSelection.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            Rail rail = infrastructure.getRail(newValue);
            railName.setText(rail.getName());
            railName.setDisable(false);
            railLength.setText(String.valueOf(rail.getLength()));
            railLength.setDisable(false);
            train.setValue(rail.getTrain());
        });

        grid.add(new Label("Select rail: "), 0, 0);
        grid.add(railSelection, 1, 0);
        grid.add(new Label("Rail name: "), 0, 1);
        grid.add(railName, 1, 1);
        grid.add(new Label("Rail length: "), 0, 2);
        grid.add(railLength, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(railSelection.getValue(), new Rail(railName.getText(), Double.parseDouble(railLength.getText()), train.getValue()));
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Pair<String, Train>> editTrainDialog(RailwayInfrastructure infrastructure) {
        Dialog<Pair<String, Train>> dialog = new Dialog<>();
        dialog.setTitle("Edit train");
        dialog.setHeaderText("Edit train in infrastructure");

        ButtonType addButtonType = new ButtonType("Edit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> railSwitchKey = new ArrayList<>(infrastructure.getSwitchKeys());
        Utils.SortForRailsAndRailsSwitches(railSwitchKey);

        ChoiceBox<String> trainSelection = new ChoiceBox<>();
        trainSelection.getItems().addAll(infrastructure.getTrains().stream().map(Train::getName).toList());


        TextField trainName = new TextField();
        trainName.setDisable(true);
        TextField trainLength = new TextField();
        trainLength.setDisable(true);

        ChoiceBox<String> nearRailSwitch = new ChoiceBox<>();
        nearRailSwitch.getItems().addAll(railSwitchKey);
        nearRailSwitch.setDisable(true);

        ChoiceBox<String> rail = new ChoiceBox<>();
        rail.setDisable(true);


        trainSelection.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            Train train = infrastructure.getTrain(newValue);
            trainName.setText(train.getName());
            trainName.setDisable(false);
            trainLength.setText(String.valueOf(train.getLength()));
            trainLength.setDisable(false);
            nearRailSwitch.setValue(train.getNearRailSwitch());
            nearRailSwitch.setDisable(false);
            rail.setValue(train.getRail());
            rail.setDisable(false);

        });

        nearRailSwitch.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            rail.getItems().clear();

            List<String> neighbours = infrastructure.getRailNeighbourKeys(newValue);
            rail.getItems().addAll(neighbours);
            rail.setDisable(false);
        });

        grid.add(new Label("Select train: "), 0, 0);
        grid.add(trainSelection, 1, 0);
        grid.add(new Label("Train name: "), 0, 1);
        grid.add(trainName, 1, 1);
        grid.add(new Label("Train length: "), 0, 2);
        grid.add(trainLength, 1, 2);
        grid.add(new Label("Near rail switch:"), 0, 3);
        grid.add(nearRailSwitch, 1, 3);
        grid.add(new Label("Rail:"), 0, 4);
        grid.add(rail, 1, 4);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(trainSelection.getValue(), new Train(trainName.getText(), Double.parseDouble(trainLength.getText()), nearRailSwitch.getValue(), rail.getValue()));
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Train> addTrainDialog(RailwayInfrastructure infrastructure) {
        Dialog<Train> dialog = new Dialog<>();
        dialog.setTitle("Add train");
        dialog.setHeaderText("Add new train to infrastructure");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> railSwitchKey = new ArrayList<>(infrastructure.getSwitchKeys());
        Utils.SortForRailsAndRailsSwitches(railSwitchKey);

        ChoiceBox<String> nearRailSwitch = new ChoiceBox<>();
        nearRailSwitch.getItems().addAll(railSwitchKey);

        ChoiceBox<String> rail = new ChoiceBox<>();
        rail.setDisable(true);

        nearRailSwitch.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            rail.getItems().clear();

            List<String> neighbours = infrastructure.getRailNeighbourKeys(newValue);
            rail.getItems().addAll(neighbours);
            rail.setDisable(false);
        });

        TextField trainName = new TextField();
        TextField trainLength = new TextField();

        grid.add(new Label("Train name: "), 0, 0);
        grid.add(trainName, 1, 0);
        grid.add(new Label("Train length: "), 0, 1);
        grid.add(trainLength, 1, 1);
        grid.add(new Label("Near rail switch:"), 0, 2);
        grid.add(nearRailSwitch, 1, 2);
        grid.add(new Label("Rail:"), 0, 3);
        grid.add(rail, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Train(trainName.getText(), Double.parseDouble(trainLength.getText()), nearRailSwitch.getValue(), rail.getValue());
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<RailDialogReturn> railDialog(List<String> vertices) {
        Dialog<RailDialogReturn> dialog = new Dialog<>();
        dialog.setTitle("Add rail");
        dialog.setHeaderText("Add new rail to infrastructure");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> verticesKey = new ArrayList<>(vertices);
        Utils.SortForRailsAndRailsSwitches(verticesKey);

        TextField railName = new TextField();

        ChoiceBox<String> firstVertex = new ChoiceBox<>();
        firstVertex.getItems().addAll(verticesKey);

        ChoiceBox<String> secondVertex = new ChoiceBox<>();
        secondVertex.getItems().addAll(verticesKey);

        TextField numberField = new TextField();

        grid.add(new Label("Rail name:"), 0, 0);
        grid.add(railName, 1, 0);
        grid.add(new Label("First rail switch:"), 0, 1);
        grid.add(firstVertex, 1, 1);
        grid.add(new Label("Second rail switch:"), 0, 2);
        grid.add(secondVertex, 1, 2);
        grid.add(new Label("Weight: "), 0, 3);
        grid.add(numberField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new RailDialogReturn(railName.getText(), firstVertex.getValue(), secondVertex.getValue(), Double.parseDouble(numberField.getText()));
            }
            return null;
        });

        return dialog;
    }


    public static Dialog<String> deleteRailSwitch(List<String> list) {
        return deleteDialog(list, "Remove rail switch", "Remove rail switch from infrastructure");
    }

    public static Dialog<String> deleteTrain(List<String> list) {
        return deleteDialog(list, "Remove train", "Remove train from infrastructure");
    }

    public static Dialog<String> deleteRailDialog(List<String> list) {
        return deleteDialog(list, "Remove rail", "Remove rail from infrastructure");
    }

    public static Dialog<String> deleteDialog(List<String> vertices, String title, String headerText) {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(headerText);

        ButtonType addButtonType = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> verticesKey = new ArrayList<>(vertices);
        Utils.SortForRailsAndRailsSwitches(verticesKey);

        ChoiceBox<String> rails = new ChoiceBox<>();
        rails.getItems().addAll(verticesKey);


        grid.add(new Label("Rail:"), 0, 0);
        grid.add(rails, 1, 0);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return rails.getValue();
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<ShortestPathDialogReturn> shortestPathDialog(RailwayInfrastructure infrastructure) {
        Dialog<ShortestPathDialogReturn> dialog = new Dialog<>();
        dialog.setTitle("Shortest Path");
        dialog.setHeaderText("Shortest Path");

        ButtonType addButtonType = new ButtonType("Calculate", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> railSwitchKey = new ArrayList<>(infrastructure.getSwitchKeys());
        Utils.SortForRailsAndRailsSwitches(railSwitchKey);

        ChoiceBox<String> firstViaRailSwitch = new ChoiceBox<>();
        firstViaRailSwitch.getItems().addAll(railSwitchKey);

        ChoiceBox<String> startRail = new ChoiceBox<>();
        startRail.setDisable(true);

        firstViaRailSwitch.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            startRail.getItems().clear();

            List<String> neighbours = infrastructure.getRailNeighbourKeys(newValue);
            startRail.getItems().addAll(neighbours);
            startRail.setDisable(false);
        });

        ChoiceBox<String> secondViaRaiSwitch = new ChoiceBox<>();
        secondViaRaiSwitch.getItems().addAll(railSwitchKey);

        ChoiceBox<String> endRail = new ChoiceBox<>();
        endRail.setDisable(true);

        secondViaRaiSwitch.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            endRail.getItems().clear();

            List<String> neighbours = infrastructure.getRailNeighbourKeys(newValue);
            endRail.getItems().addAll(neighbours);
            endRail.setDisable(false);
        });

        TextField trainLength = new TextField();

        grid.add(new Label("Via rail switch:"), 0, 0);
        grid.add(firstViaRailSwitch, 1, 0);
        grid.add(new Label("Start rail:"), 0, 1);
        grid.add(startRail, 1, 1);
        grid.add(new Separator(), 0, 2, 2, 1);
        grid.add(new Label("Via rail switch:"), 0, 3);
        grid.add(secondViaRaiSwitch, 1, 3);
        grid.add(new Label("End rail:"), 0, 4);
        grid.add(endRail, 1, 4);
        grid.add(new Separator(), 0, 5, 2, 1);
        grid.add(new Label("Train length:"), 0, 6);
        grid.add(trainLength, 1, 6);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new ShortestPathDialogReturn(firstViaRailSwitch.getValue(), startRail.getValue(), secondViaRaiSwitch.getValue(), endRail.getValue(), Double.parseDouble(trainLength.getText()));
            }
            return null;
        });

        return dialog;
    }

    public static void alert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setHeaderText(message);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void SortForRailsAndRailsSwitches(List<String> strings) {
        strings.sort(new Comparator<String>() {
            public int compare(String o1, String o2) {
                return extractInt(o1) - extractInt(o2);
            }

            int extractInt(String s) {
                String substring = s.substring(0, Math.min(s.length(), 3));
                String num = substring.replaceAll("\\D", "");
                // return 0 if no digits found
                return num.isEmpty() ? 0 : Integer.parseInt(num);
            }
        });
    }
}
