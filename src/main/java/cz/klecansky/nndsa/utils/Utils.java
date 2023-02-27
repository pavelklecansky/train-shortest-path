package cz.klecansky.nndsa.utils;

import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailSwitchType;
import cz.klecansky.nndsa.rail.Train;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;

import java.util.*;

public final class Utils {


    public static final String VERTICES_EDGES_DIVIDER = "#rails#";


    public static String shortestPathFormat(double length) {
        return String.format("Shortest path: %1.2f", length);
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
                return new RailSwitch(nameField.getText(), RailSwitchType.valueOf(railSwitchType.getValue()));
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

        Separator separator = new Separator();

        Text trainText = new Text("Train");
        trainText.setStyle("-fx-font-weight: bold");
        trainText.setStyle("-fx-font-size: 16px");

        TextField trainName = new TextField();
        TextField trainLength = new TextField();

        grid.add(new Label("Rail name:"), 0, 0);
        grid.add(railName, 1, 0);
        grid.add(new Label("First rail switch:"), 0, 1);
        grid.add(firstVertex, 1, 1);
        grid.add(new Label("Second rail switch:"), 0, 2);
        grid.add(secondVertex, 1, 2);
        grid.add(new Label("Weight: "), 0, 3);
        grid.add(numberField, 1, 3);
        grid.add(separator, 0, 4, 2, 1);
        grid.add(trainText, 0, 5, 1, 1);
        grid.add(new Label("Train name: "), 0, 6);
        grid.add(trainName, 1, 6);
        grid.add(new Label("Train length: "), 0, 7);
        grid.add(trainLength, 1, 7);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                Train train = null;
                if (!trainText.getText().trim().isEmpty() && !trainLength.getText().trim().isEmpty()) {
                    train = new Train(trainName.getText(), Double.parseDouble(trainLength.getText()));
                }
                return new RailDialogReturn(railName.getText(), firstVertex.getValue(), secondVertex.getValue(), Double.parseDouble(numberField.getText()), train);
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Pair<String, String>> removeEdgeDialog(List<String> vertices) {
        return getTwoRailDialog(vertices, "Remove Rail", "Remove");
    }

    public static Dialog<Pair<String, String>> shortestPathDialog(List<String> vertices) {
        return getTwoRailDialog(vertices, "Shortest Path", "Calculate");
    }


    private static Dialog<Pair<String, String>> getTwoRailDialog(List<String> vertices, String title, String buttonText) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(title);

        ButtonType addButtonType = new ButtonType(buttonText, ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> verticesKey = new ArrayList<>(vertices);
        Utils.SortForRailsAndRailsSwitches(verticesKey);

        ChoiceBox<String> firstVertex = new ChoiceBox<>();
        firstVertex.getItems().addAll(verticesKey);

        ChoiceBox<String> secondVertex = new ChoiceBox<>();
        secondVertex.getItems().addAll(verticesKey);

        grid.add(new Label("First rail switch:"), 0, 0);
        grid.add(firstVertex, 1, 0);
        grid.add(new Label("Second rail switch:"), 0, 1);
        grid.add(secondVertex, 1, 1);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Pair<>(firstVertex.getValue(), secondVertex.getValue());
            }
            return null;
        });

        return dialog;
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
