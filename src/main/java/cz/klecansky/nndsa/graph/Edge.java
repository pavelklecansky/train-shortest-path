package cz.klecansky.nndsa.graph;

public class Edge<Key, VValue, EValue> {
    private final EValue value;
    private final Vertex<Key, VValue, EValue> start;
    private final Vertex<Key, VValue, EValue> target;

    public Edge(Vertex<Key, VValue, EValue> start, Vertex<Key, VValue, EValue> target, EValue rail) {
        this.value = rail;
        this.start = start;
        this.target = target;
    }

    public EValue getValue() {
        return value;
    }

    public Vertex<Key, VValue, EValue> getStart() {
        return start;
    }

    public Vertex<Key, VValue, EValue> getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return String.format("%s-%s %s", start, target, value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?, ?, ?> edge = (Edge<?, ?, ?>) o;

        return (this.start.equals(edge.start) && this.target.equals(edge.target)) || (this.start.equals(edge.target) && this.target.equals(edge.start));
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }
}
