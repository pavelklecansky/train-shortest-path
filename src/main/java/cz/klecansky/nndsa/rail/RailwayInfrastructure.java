package cz.klecansky.nndsa.rail;

import cz.klecansky.nndsa.algorithms.Dijkstra;
import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
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
        return getRails().stream().filter(Rail::hasTrain).map(Rail::getTrain).toList();
    }

    public List<String> getRailsInfo() {
        return graph.getEdges().stream().map(Edge::toString).toList();
    }

    public List<String> getRailKeys() {
        return graph.getEdgeValue().stream().map(Rail::getName).sorted().distinct().toList();
    }


    // TODO find better way to get data about edges.
    public List<Triplet<String, String, Rail>> getRailsDetailInfo() {
        return graph.getUndirectedEdges().stream().map(stringRailEdge -> new Triplet<>(stringRailEdge.getStart().getKey(), stringRailEdge.getTarget().getKey(), stringRailEdge.getValue())).toList();
    }

    public void deleteRail(String first, String second) {
        graph.deleteEdge(first, second);
    }

    public List<Vertex<String, RailSwitch, Rail>> shortestPath(String fromVia, String railSwitchStart, String toVia, String railSwitchEnd, double trainLength) {
        graph.clearDijkstra();
        Rail startRail = graph.getEdgeValue(railSwitchStart);
        if (startRail.getLength() < trainLength) {
            throw new IllegalArgumentException("Train cannot have bigger length than starting rail.");
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
}
