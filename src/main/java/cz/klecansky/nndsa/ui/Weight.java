package cz.klecansky.nndsa.ui;

public class Weight {
    private final double weight;

    public Weight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return String.valueOf(weight);
    }
}
