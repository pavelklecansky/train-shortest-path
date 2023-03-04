package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;

public class DFSTable {
    private Vertex<String, RailSwitch, Rail> vertex;
    private Vertex<String, RailSwitch, Rail> previous;

    public DFSTable(Vertex<String, RailSwitch, Rail> vertex) {
        this(vertex, null);
    }

    public DFSTable(Vertex<String, RailSwitch, Rail> vertex, Vertex<String, RailSwitch, Rail> previous) {
        this.vertex = vertex;
        this.previous = previous;
    }

    public Vertex<String, RailSwitch, Rail> getVertex() {
        return vertex;
    }

    public void setVertex(Vertex<String, RailSwitch, Rail> vertex) {
        this.vertex = vertex;
    }

    public Vertex<String, RailSwitch, Rail> getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex<String, RailSwitch, Rail> previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        return "vertex=" + vertex + ", previous=" + previous;
    }
}
