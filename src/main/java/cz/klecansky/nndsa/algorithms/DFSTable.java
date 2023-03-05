package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.rail.RailSwitch;

public class DFSTable {
    private RailSwitch vertex;
    private RailSwitch previous;

    public DFSTable(RailSwitch vertex) {
        this(vertex, null);
    }

    public DFSTable(RailSwitch vertex, RailSwitch previous) {
        this.vertex = vertex;
        this.previous = previous;
    }

    public RailSwitch getVertex() {
        return vertex;
    }

    public void setVertex(RailSwitch vertex) {
        this.vertex = vertex;
    }

    public RailSwitch getPrevious() {
        return previous;
    }

    public void setPrevious(RailSwitch previous) {
        this.previous = previous;
    }

    @Override
    public String toString() {
        return "vertex=" + vertex + ", previous=" + previous;
    }
}
