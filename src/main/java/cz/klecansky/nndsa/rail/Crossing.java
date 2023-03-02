package cz.klecansky.nndsa.rail;

public class Crossing {
    private RailSwitch firstOuter;
    private RailSwitch middle;
    private RailSwitch secondOuter;

    public Crossing(RailSwitch firstOuter, RailSwitch middle, RailSwitch secondOuter) {
        this.firstOuter = firstOuter;
        this.middle = middle;
        this.secondOuter = secondOuter;
    }

    public RailSwitch getFirstOuter() {
        return firstOuter;
    }

    public void setFirstOuter(RailSwitch firstOuter) {
        this.firstOuter = firstOuter;
    }

    public RailSwitch getMiddle() {
        return middle;
    }

    public void setMiddle(RailSwitch middle) {
        this.middle = middle;
    }

    public RailSwitch getSecondOuter() {
        return secondOuter;
    }

    public void setSecondOuter(RailSwitch secondOuter) {
        this.secondOuter = secondOuter;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Crossing crossing = (Crossing) o;

        return (this.firstOuter.equals(crossing.firstOuter) && this.secondOuter.equals(crossing.secondOuter)) || (this.firstOuter.equals(crossing.secondOuter) && this.secondOuter.equals(crossing.firstOuter));
    }

    @Override
    public int hashCode() {
        int result = firstOuter.hashCode();
        result = 31 * result + middle.hashCode();
        result = 31 * result + secondOuter.hashCode();
        return result;
    }
}
