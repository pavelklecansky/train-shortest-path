package cz.klecansky.nndsa.algorithms;

public record ShortestPathDisplay(String railSwitchName, double minDistance) {
    @Override
    public String toString() {
        return railSwitchName + ": " + minDistance;
    }
}
