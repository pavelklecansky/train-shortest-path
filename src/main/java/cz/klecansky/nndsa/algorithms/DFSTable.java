package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;

import java.util.ArrayList;
import java.util.List;

public class DFSTable {
    private RailSwitch railSwitch;
    private RailSwitch previous;

    private List<Rail> railsToNextRailSwitch = new ArrayList<>();
    private int position;

    private double weight;

    public DFSTable(RailSwitch railSwitch) {
        this(railSwitch, null, 0, 0.0);
    }

    public DFSTable(RailSwitch railSwitch, RailSwitch previous, int position, double weight) {
        this.railSwitch = railSwitch;
        this.previous = previous;
        this.position = position;
        this.weight = weight;
    }

    public RailSwitch getRailSwitch() {
        return railSwitch;
    }

    public void setRailSwitch(RailSwitch railSwitch) {
        this.railSwitch = railSwitch;
    }

    public RailSwitch getPrevious() {
        return previous;
    }

    public void setPrevious(RailSwitch previous) {
        this.previous = previous;
    }

    public List<Rail> getRailsToNextRailSwitch() {
        return railsToNextRailSwitch;
    }

    public void addRail(Rail rail) {
        railsToNextRailSwitch.add(rail);
    }

    public int getPosition() {
        return position;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "rail switch=" + railSwitch + ", previous=" + previous + ", position=" + position + ", weight=" + weight + ", rails= " + String.join(", ", railsToNextRailSwitch.stream().map(Rail::getName).toList());
    }
}
