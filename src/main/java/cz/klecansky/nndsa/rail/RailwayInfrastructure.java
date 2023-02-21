package cz.klecansky.nndsa.rail;

import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;

public class RailwayInfrastructure {
    private final Graph<String, Rail> graph;

    public RailwayInfrastructure() {
        this.graph = new EdgeWeightedGraph<>();
    }
}
