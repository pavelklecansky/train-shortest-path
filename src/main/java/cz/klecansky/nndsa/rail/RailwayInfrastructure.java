package cz.klecansky.nndsa.rail;

import cz.klecansky.nndsa.algorithms.Dijkstra;
import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.utils.Triplet;

import java.util.List;
import java.util.Optional;

public class RailwayInfrastructure {
    private final Graph<String, RailSwitch, Rail> graph;

    public RailwayInfrastructure() {
        this.graph = new EdgeWeightedGraph<>();
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


    public List<Triplet<String, String, Rail>> getDistinctRailsDetailInfo() {
        return graph.getDistinctDetailEdgeValues();
    }

    public List<Triplet<String, String, Rail>> getRailsDetailInfo() {
        return graph.getDetailEdgeValues();
    }

    public List<Vertex<String, RailSwitch, Rail>> shortestPath(String fromVia, String railSwitchStart, String toVia, String railSwitchEnd, double trainLength) {
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
        dijkstra.computePath(sourceVertex);
        Vertex<String, RailSwitch, Rail> targetVertex = graph.vertexByKey(toVia);
        return dijkstra.getShortestPathTo(targetVertex);
    }

    public void setTrainNearFor(String railNear) {
        RailSwitch railSwitch = graph.getVertexValue(railNear);
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
        graph.getEdgeValue(train.getRail()).setTrain(train);
    }

    public void deleteTrain(String trainName) {
        Optional<Train> first = getTrains().stream().filter(train -> train.getName().equals(trainName)).findFirst();
        if (first.isPresent()) {
            Train train = first.get();
            Rail rail = getRail(train.getRail());
            rail.removeTrain();
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

    public RailSwitch getRailSwitch(String railKey) {
        return graph.getVertexValue(railKey);
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
