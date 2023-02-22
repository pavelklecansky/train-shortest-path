package cz.klecansky.nndsa.rail;

import java.util.Objects;

public class Rail {
    private String name;
    private double length;
    private Train train;

    public Rail(double length) {
        this.length = length;
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

    @Override
    public String toString() {
        return String.valueOf(length);
    }
}
