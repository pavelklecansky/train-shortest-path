package cz.klecansky.nndsa.utils;

import java.util.Objects;

public record Triplet<A, B, C>(A first, B second, C third) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triplet<?, ?, ?> triplet = (Triplet<?, ?, ?>) o;

        if (!Objects.equals(first, triplet.first)) return false;
        if (!Objects.equals(second, triplet.second)) return false;
        return Objects.equals(third, triplet.third);
    }

    @Override
    public String toString() {
        return "first=" + first +
                ", second=" + second +
                ", third=" + third;
    }
}
