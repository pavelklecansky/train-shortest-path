package cz.klecansky.nndsa.rail;

import cz.klecansky.nndsa.algorithms.Dijkstra;
import cz.klecansky.nndsa.algorithms.ShortestPathDisplay;
import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.utils.Triplet;

import java.util.*;

public class RailwayInfrastructure {
    private final Graph<String, RailSwitch, Rail> graph;
    private final List<Crossing> crossings;

    public RailwayInfrastructure() {
        this.graph = new EdgeWeightedGraph<>();
        this.crossings = new ArrayList<>();
    }

    public void addSwitch(RailSwitch railSwitch) {
        graph.addVertex(railSwitch.getName(), railSwitch);
    }

    public void addRail(String railKey, String switchKey, String secondSwitchKey, Rail rail) {
        graph.addEdge(railKey, switchKey, secondSwitchKey, rail);
    }

    public List<RailSwitch> getSwitches() {
        return graph.getVerticesValue();
    }

    public List<Rail> getRails() {
        return graph.getEdgeValue();
    }

    public List<Train> getTrains() {
        return getRails().stream().filter(Rail::hasTrain).map(Rail::getTrain).sorted().distinct().toList();
    }

    public List<Crossing> getCrossings() {
        return crossings;
    }

    public void addCrossing(Crossing crossing) {
        crossings.add(crossing);
    }

    public boolean isPartOfIllegalPath(String key, String key1, String key2) {
        RailSwitch firstOuter = getRailSwitch(key);
        RailSwitch middle = getRailSwitch(key1);
        RailSwitch secondOuter = getRailSwitch(key2);
        Crossing crossing = new Crossing(firstOuter, middle, secondOuter);

        return crossings.contains(crossing);
    }

    public List<Triplet<String, String, Rail>> getDistinctRailsDetailInfo() {
        return graph.getDistinctDetailEdgeValues();
    }

    public List<Triplet<String, String, Rail>> getRailsDetailInfo() {
        return graph.getDetailEdgeValues();
    }

    public List<ShortestPathDisplay> shortestPath(String fromVia, String railSwitchStart, String toVia, String railSwitchEnd, double trainLength) {
        graph.clearDijkstra();
        Rail startRail = graph.getEdgeValue(railSwitchStart);
        if (startRail.getVacancy() < trainLength) {
            throw new IllegalArgumentException("Train cannot have bigger length than starting rail.");
        }
        Rail endRail = graph.getEdgeValue(railSwitchEnd);
        if (endRail.getVacancy() < trainLength) {
            throw new IllegalArgumentException("Train has not enough space to fit in ending rail.");
        }
        Dijkstra dijkstra = new Dijkstra();
        Vertex<String, RailSwitch, Rail> sourceVertex = graph.vertexByKey(fromVia);
        Vertex<String, RailSwitch, Rail> targetVertex = graph.vertexByKey(toVia);
        List<ShortestPathDisplay> shortestPathDisplays = dijkstra.computePath(sourceVertex, targetVertex, this, trainLength);

        ShortestPathDisplay shortestPathDisplay = shortestPathDisplays.get(shortestPathDisplays.size() - 1);
        if (shortestPathDisplay.getMinDistance() == Double.MAX_VALUE) {
            throw new RuntimeException("Shortest path was not found.");
        }

        return shortestPathDisplays;
    }

    public void setTrainNearFor(String railNear) {
        RailSwitch railSwitch = getRailSwitch(railNear);
        railSwitch.setTrainNear(true);
    }


    public List<String> getRailNeighbours(String newValue) {
        return graph.getVertexEdgeKeys(newValue);
    }

    public List<String> getSwitchKeys() {
        return getSwitches().stream().map(RailSwitch::getName).toList();
    }

    public void deleteRail(String rail) {
        graph.deleteEdge(rail);
    }

    public Rail getRail(String rail) {
        return graph.getEdgeValue(rail);
    }

    public void addTrain(Train train) {
        Rail rail = getRail(train.getRail());
        if (rail.getLength() < train.getLength()) {
            throw new IllegalArgumentException("Train is longer then rail");
        }
        getRail(train.getRail()).setTrain(train);
        setTrainNearFor(train.getNearRailSwitch());
    }

    public void deleteTrain(String trainName) {
        Optional<Train> first = getTrains().stream().filter(train -> train.getName().equals(trainName)).findFirst();
        if (first.isPresent()) {
            Train train = first.get();
            Rail rail = getRail(train.getRail());
            rail.removeTrain();
            getRailSwitch(train.getNearRailSwitch()).setTrainNear(false);
        }
    }

    public void deleteRailSwitch(String railSwitch) {
        graph.deleteVertex(railSwitch);
    }

    public Train getTrain(String trainName) {
        return getTrains().stream().filter(train -> train.getName().equals(trainName)).findFirst().get();
    }

    public void editTrain(String key, Train value) {
        Train train = getTrain(key);
        Rail rail = getRail(train.getRail());
        addTrain(value);
        if (!value.getRail().equals(rail.getName())) {
            rail.setTrain(null);
        }
    }

    public RailSwitch getRailSwitch(String railSwitchKey) {
        return graph.getVertexValue(railSwitchKey);
    }

    public void editRailSwitch(String key, RailSwitch value) {
        renameRailSwitch(key, value.getName());
    }

    private void renameRailSwitch(String oldName, String newName) {
        RailSwitch railSwitch = getRailSwitch(oldName);
        railSwitch.setName(newName);
        graph.renameVertex(oldName, newName);
        List<Train> trains = getTrains().stream().filter(train -> train.getNearRailSwitch().equals(oldName)).toList();
        trains.forEach(train -> train.setNearRailSwitch(newName));
    }

    public void editRail(String key, Rail value) {
        if (value.getLength() < value.getTrain().getLength()) {
            throw new IllegalArgumentException("Cannot make rail smaller then train on rail.");
        }
        graph.setEdge(key, value.getName(), value);
        Rail rail = getRail(value.getName());
        rail.getTrain().setRail(rail.getName());
    }
}
