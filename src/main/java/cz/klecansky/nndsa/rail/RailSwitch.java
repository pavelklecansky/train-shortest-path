package cz.klecansky.nndsa.rail;

public class RailSwitch {
    private String name;
    private boolean crossing;
    private boolean trainNear;

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

    public boolean isCrossing() {
        return crossing;
    }

    public void setCrossing(boolean crossing) {
        this.crossing = crossing;
    }

    @Override
    public String toString() {
        return name;
    }
}
