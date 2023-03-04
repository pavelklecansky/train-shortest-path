package cz.klecansky.nndsa.io;

import cz.klecansky.nndsa.rail.*;
import cz.klecansky.nndsa.utils.Triplet;
import cz.klecansky.nndsa.utils.Utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ExporterCsv {

    // TODO ještě se rozhodnout jak to bude s tím exportem hran(edges)
    public void exportGraph(File file, RailwayInfrastructure railwayInfrastructure) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (railwayInfrastructure == null) {
            throw new IllegalArgumentException("Graph is null.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        List<String> switches = new ArrayList<>(railwayInfrastructure.getSwitches().stream().map(RailSwitch::getName).toList());
        Utils.SortForRailsAndRailsSwitches(switches);
        for (String railSwitch : switches) {
            stringBuilder.append(railSwitch).append("\n");
        }
        stringBuilder.append(Utils.VERTICES_EDGES_DIVIDER).append("\n");
        for (Triplet<String, String, Rail> railInfo : railwayInfrastructure.getDistinctRailsDetailInfo()) {
            Rail rail = railInfo.getThird();
            String formatedTrain = "";
            if (rail.getTrain() != null) {
                Train train = rail.getTrain();
                formatedTrain = String.format("%s,%1.0f,%s", train.getName(), train.getLength(), train.getNearRailSwitch());
            }
            stringBuilder.append(String.format("%s,%s,%s,%1.0f|%s", rail.getName(), railInfo.getFirst(), railInfo.getSecond(), rail.getLength(), formatedTrain)).append("\n");
        }
        stringBuilder.append(Utils.EDGES_CROSSING_DIVIDER).append("\n");
        for (Crossing crossing : railwayInfrastructure.getCrossings()) {
            stringBuilder.append(String.format("%s,%s,%s", crossing.getFirstOuter().getName(), crossing.getMiddle().getName(), crossing.getSecondOuter().getName())).append("\n");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(stringBuilder.toString());
        }
    }
}
