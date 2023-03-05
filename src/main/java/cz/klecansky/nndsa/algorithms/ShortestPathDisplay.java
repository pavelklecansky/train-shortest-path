package cz.klecansky.nndsa.algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class ShortestPathDisplay {
    private String railSwitchName;
    private double minDistance;
    private List<String> reversePath = new ArrayList<>();

    private double trainLength;

    public ShortestPathDisplay(String railSwitchName, double minDistance, List<String> reversePath, double trainLength) {
        this.railSwitchName = railSwitchName;
        this.minDistance = minDistance;
        this.reversePath = reversePath;
        this.trainLength = trainLength;
    }

    public ShortestPathDisplay(String railSwitchName, double minDistance, double trainLength) {
        this.railSwitchName = railSwitchName;
        this.minDistance = minDistance;
        this.trainLength = trainLength;
    }

    public String getRailSwitchName() {
        return railSwitchName;
    }

    public void setRailSwitchName(String railSwitchName) {
        this.railSwitchName = railSwitchName;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public List<String> getReversePath() {
        return reversePath;
    }

    public void setReversePath(List<String> reversePath) {
        this.reversePath = reversePath;
    }

    @Override
    public String toString() {
        String reversePathFormat = "";
        double minDistanceCalc = minDistance;
        if (!reversePath.isEmpty()) {
            reversePathFormat = String.format("%s (%1.1f)", String.join(" -> ", reversePath), trainLength);
            minDistanceCalc = minDistance - trainLength;
        }
        return String.format("%s: %1.1f | %s", railSwitchName, minDistanceCalc, reversePathFormat);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (ShortestPathDisplay) obj;
        return Objects.equals(this.railSwitchName, that.railSwitchName) &&
                Double.doubleToLongBits(this.minDistance) == Double.doubleToLongBits(that.minDistance) &&
                Objects.equals(this.reversePath, that.reversePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(railSwitchName, minDistance, reversePath);
    }

}
