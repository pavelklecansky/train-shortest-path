package cz.klecansky.nndsa.rail;

public class RailSwitch {
    private String name;
    private RailSwitchType type;
    private boolean trainNear;

    public RailSwitch(String name, RailSwitchType type) {
        this(name, type, false);
    }

    public RailSwitch(String name, RailSwitchType type, boolean trainNear) {
        this.name = name;
        this.type = type;
        this.trainNear = trainNear;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RailSwitchType getType() {
        return type;
    }

    public void setType(RailSwitchType type) {
        this.type = type;
    }

    public boolean isTrainNear() {
        return trainNear;
    }

    public void setTrainNear(boolean trainNear) {
        this.trainNear = trainNear;
    }

    @Override
    public String toString() {
        return name + ": Type:" + type;
    }
}
