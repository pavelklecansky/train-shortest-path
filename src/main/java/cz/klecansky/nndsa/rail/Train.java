package cz.klecansky.nndsa.rail;

import java.util.Objects;

public final class Train {
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
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Train) obj;
        return Objects.equals(this.name, that.name) &&
                Double.doubleToLongBits(this.length) == Double.doubleToLongBits(that.length);
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
}
