package cz.klecansky.nndsa.utils;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import javafx.util.converter.NumberStringConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class Utils {

    public static Dialog<Triplet<String, String, Double>> edgeDialog(List<String> vertices) {
        Dialog<Triplet<String, String, Double>> dialog = new Dialog<>();
        dialog.setTitle("Add edge");
        dialog.setHeaderText("Add new edge to graph");

        ButtonType addButtonType = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> verticesKey = new ArrayList<>(vertices);
        Utils.SortForVerticesAndEdges(verticesKey);

        ChoiceBox<String> firstVertex = new ChoiceBox<>();
        firstVertex.getItems().addAll(verticesKey);

        ChoiceBox<String> secondVertex = new ChoiceBox<>();
        secondVertex.getItems().addAll(verticesKey);

        TextField numberField = new TextField();
        numberField.setTextFormatter(new TextFormatter<>(new NumberStringConverter()));

        grid.add(new Label("First Vertex:"), 0, 0);
        grid.add(firstVertex, 1, 0);
        grid.add(new Label("Second Vertex:"), 0, 1);
        grid.add(secondVertex, 1, 1);
        grid.add(new Label("Weight: "), 0, 2);
        grid.add(numberField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                return new Triplet<>(firstVertex.getValue(), secondVertex.getValue(), Double.parseDouble(numberField.getText()));
            }
            return null;
        });

        return dialog;
    }

    public static Dialog<Pair<String, String>> removeEdgeDialog(List<String> vertices) {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Remove edge");
        dialog.setHeaderText("Remove edge from graph");

        ButtonType addButtonType = new ButtonType("Remove", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        List<String> verticesKey = new ArrayList<>(vertices);
        Utils.SortForVerticesAndEdges(verticesKey);

        ChoiceBox<String> firstVertex = new ChoiceBox<>();
        firstVertex.getItems().addAll(verticesKey);

        ChoiceBox<String> secondVertex = new ChoiceBox<>();
        secondVertex.getItems().addAll(verticesKey);

        grid.add(new Label("First Vertex:"), 0, 0);
        grid.add(firstVertex, 1, 0);
        grid.add(new Label("Second Vertex:"), 0, 1);
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

    public static void SortForVerticesAndEdges(List<String> strings) {
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
