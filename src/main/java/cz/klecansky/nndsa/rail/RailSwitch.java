package cz.klecansky.nndsa.rail;

public class RailSwitch implements Comparable<RailSwitch> {
    private String name;
    private boolean trainNear;
    private RailSwitch previosRailSwitch;
    private double minDistance = Double.MAX_VALUE;

    public RailSwitch(String name) {
        this(name, false);
    }

    public RailSwitch(String name, boolean trainNear) {
        this.name = name;
        this.trainNear = trainNear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isTrainNear() {
        return trainNear;
    }

    public void setTrainNear(boolean trainNear) {
        this.trainNear = trainNear;
    }

    public RailSwitch getPreviosRailSwitch() {
        return previosRailSwitch;
    }

    public void setPreviosRailSwitch(RailSwitch previosRailSwitch) {
        this.previosRailSwitch = previosRailSwitch;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public void clearDijkstra() {
        previosRailSwitch = null;
        minDistance = Double.MAX_VALUE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RailSwitch that = (RailSwitch) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(RailSwitch otherRailSwitch) {
        return Double.compare(this.minDistance, otherRailSwitch.minDistance);
    }

    @Override
    public String toString() {
        return name;
    }
}
