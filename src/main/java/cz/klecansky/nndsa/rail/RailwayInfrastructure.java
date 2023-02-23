package cz.klecansky.nndsa.rail;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.utils.Triplet;

import java.util.List;

public class RailwayInfrastructure {
    private final Graph<String, RailSwitch, Rail> graph;

    public RailwayInfrastructure() {
        this.graph = new EdgeWeightedGraph<>();
    }

    public void addSwitch(RailSwitch railSwitch) {
        graph.addVertex(railSwitch.getName(), railSwitch);
    }

    public void addRail(String switchKey, String secondSwitchKey, Rail rail) {
        graph.addEdge(switchKey, secondSwitchKey, rail);
    }

    public List<RailSwitch> getSwitches() {
        return graph.getVerticesValue();
    }

    public List<Rail> getRails() {
        return graph.getEdgeValue();
    }

    public List<Train> getTrains() {
        return getRails().stream().filter(Rail::hasTrain).map(Rail::getTrain).toList();
    }

    public List<String> getRailsInfo() {
        return graph.getEdges().stream().map(Edge::toString).toList();
    }

    // TODO find better way to get data about edges.
    public List<Triplet<String, String, Rail>> getRailsDetailInfo() {
        return graph.getUndirectedEdges().stream().map(stringRailEdge -> new Triplet<>(stringRailEdge.getStart().getKey(), stringRailEdge.getTarget().getKey(), stringRailEdge.getValue())).toList();
    }

    public void deleteRail(String first, String second) {
        graph.deleteEdge(first, second);
    }
}
