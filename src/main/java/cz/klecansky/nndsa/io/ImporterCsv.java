package cz.klecansky.nndsa.io;

import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailSwitchType;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import cz.klecansky.nndsa.utils.Utils;

import java.io.*;

public class ImporterCsv {

    public RailwayInfrastructure importRailwayInfrastructure(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File is null or not existing.");
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        RailwayInfrastructure railwayInfrastructure = new RailwayInfrastructure();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals(Utils.VERTICES_EDGES_DIVIDER)) {
                break;
            }
            railwayInfrastructure.addSwitch(new RailSwitch(line.trim(), RailSwitchType.NONE));
        }
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(",");
            railwayInfrastructure.addRail(split[0], split[1], new Rail(Double.parseDouble(split[2])));
        }

        return railwayInfrastructure;
    }
}
