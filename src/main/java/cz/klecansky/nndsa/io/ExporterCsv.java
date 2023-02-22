package cz.klecansky.nndsa.io;

import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import cz.klecansky.nndsa.utils.Utils;

import java.io.*;

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
        for (RailSwitch railSwitch : railwayInfrastructure.getSwitches()) {
            stringBuilder.append(railSwitch.getName()).append("\n");
        }
        stringBuilder.append(Utils.VERTICES_EDGES_DIVIDER).append("\n");
        for (String railInfo : railwayInfrastructure.getRailsInfo()) {
            stringBuilder.append(railInfo).append("\n");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(stringBuilder.toString());
        }
    }
}
