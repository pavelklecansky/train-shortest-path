package cz.klecansky.nndsa.rail;

import java.util.Objects;

public final class Train implements Comparable<Train> {
    private final String name;
    private final double length;
    private final String nearRail;

    public Train(String name, double length) {
        this(name, length, null);
    }

    public Train(String name, double length, String nearRail) {
        this.name = name;
        this.length = length;
        this.nearRail = nearRail;
    }

    public String getName() {
        return name;
    }

    public double getLength() {
        return length;
    }

    public String getNearRail() {
        return nearRail;
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
        return "Train{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", nearRail='" + nearRail + '\'' +
                '}';
    }

    @Override
    public int compareTo(Train o) {
        return this.name.compareTo(o.name);
    }
}
