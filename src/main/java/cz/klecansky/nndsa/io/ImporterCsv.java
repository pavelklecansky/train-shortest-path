package cz.klecansky.nndsa.io;

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
            if (line.equals(Utils.RAIL_SWITCHES_RAILS_DIVIDER)) {
                break;
            }
            railwayInfrastructure.addSwitch(new RailSwitch(line.trim()));
        }
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals(Utils.RAILS_CROSSING_DIVIDER)) {
                break;
            }
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
                newTrain = new Train(trainName, trainLength, railNear, railName);
                railwayInfrastructure.setTrainNearFor(railNear);
            }
            railwayInfrastructure.addRail(railName, startRailSwitchKey, endRailSwitchKey, new Rail(railName, railLength, newTrain));
        }

        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals(Utils.RAIL_SWITCHES_RAILS_DIVIDER)) {
                break;
            }
            String[] crossingSplit = line.split(",");
            RailSwitch firstOuter = railwayInfrastructure.getRailSwitch(crossingSplit[0]);
            RailSwitch middle = railwayInfrastructure.getRailSwitch(crossingSplit[1]);
            RailSwitch secondOuter = railwayInfrastructure.getRailSwitch(crossingSplit[2]);
            railwayInfrastructure.addCrossing(new Crossing(firstOuter, middle, secondOuter));
        }

        return railwayInfrastructure;
    }
}
