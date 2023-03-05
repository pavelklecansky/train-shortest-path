package cz.klecansky.nndsa.rail;

public record Crossing(RailSwitch firstOuter, RailSwitch middle, RailSwitch secondOuter) {

    @Override
    public String toString() {
        return "Crossing{" +
                "firstOuter=" + firstOuter +
                ", middle=" + middle +
                ", secondOuter=" + secondOuter +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Crossing crossing = (Crossing) o;

        return (this.firstOuter.equals(crossing.firstOuter) && this.secondOuter.equals(crossing.secondOuter)) || (this.firstOuter.equals(crossing.secondOuter) && this.secondOuter.equals(crossing.firstOuter));
    }

}
