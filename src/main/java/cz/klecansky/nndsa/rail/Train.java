package cz.klecansky.nndsa.rail;

import java.util.Objects;

public final class Train implements Comparable<Train> {
    private String name;
    private double length;
    private String nearRailSwitch;
    private String rail;

    public Train(String name, double length, String nearRail, String rail) {
        this.name = name;
        this.length = length;
        this.nearRailSwitch = nearRail;
        this.rail = rail;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public String getNearRailSwitch() {
        return nearRailSwitch;
    }

    public String getRail() {
        return rail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setNearRailSwitch(String nearRailSwitch) {
        this.nearRailSwitch = nearRailSwitch;
    }

    public void setRail(String rail) {
        this.rail = rail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Train train = (Train) o;

        return Objects.equals(name, train.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, length);
    }

    @Override
    public String toString() {
        return String.format("%s: length: %1.2f | rail: %s | near: %s", name, length, rail, nearRailSwitch);
    }

    @Override
    public int compareTo(Train o) {
        return this.name.compareTo(o.name);
    }
}
