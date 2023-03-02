package cz.klecansky.nndsa.rail;

import java.util.Objects;

public class Rail implements Comparable<Rail> {
    private final String name;
    private double length;
    private Train train;

    public Rail(String name, double length) {
        this(name, length, null);
    }

    public Rail(String name, double length, Train train) {
        this.name = name;
        this.length = length;
        this.train = train;
    }

    public boolean hasTrain() {
        return Objects.nonNull(train);
    }

    public Train getTrain() {
        return train;
    }

    public void setTrain(Train train) {
        this.train = train;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public void removeTrain() {
        train = null;
    }

    @Override
    public String toString() {
        return name + ": " + length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rail rail = (Rail) o;

        return name.equals(rail.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int compareTo(Rail o) {
        return this.name.compareTo(o.name);
    }
}
