package cz.klecansky.nndsa.io;

import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.*;
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
            String[] split = line.split("\\|");
            String[] rail = split[0].split(",");
            String railName = rail[0];
            String startRailSwitchKey = rail[1];
            String endRailSwitchKey = rail[2];
            double railLength = Double.parseDouble(rail[3]);
            Train newTrain = null;
            if (split.length > 1) {
                String[] trainSplit = split[1].split(",");
                String trainName = trainSplit[0];
                double trainLength = Double.parseDouble(trainSplit[1]);
                String railNear = trainSplit[2];
                newTrain = new Train(trainName, trainLength, railNear);
                railwayInfrastructure.setTrainNearFor(railNear);
            }
            railwayInfrastructure.addRail(railName, startRailSwitchKey, endRailSwitchKey, new Rail(railName, railLength, newTrain));
        }

        return railwayInfrastructure;
    }
}
