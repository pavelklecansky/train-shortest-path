package cz.klecansky.nndsa.rail;

public class RailSwitch {
    private String name;
    private RailSwitchType type;

    public RailSwitch(String name, RailSwitchType type) {
        this.name = name;
        this.type = type;
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
}
