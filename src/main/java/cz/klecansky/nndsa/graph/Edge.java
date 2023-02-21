package cz.klecansky.nndsa.graph;

public class Edge<K, V> {
    private final V value;
    private final Vertex<K, V> start;
    private final Vertex<K, V> target;

    public Edge(Vertex<K, V> start, Vertex<K, V> target, V rail) {
        this.value = rail;
        this.start = start;
        this.target = target;
    }

    public V getValue() {
        return value;
    }

    public Vertex<K, V> getStart() {
        return start;
    }

    public Vertex<K, V> getTarget() {
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

        Edge<?, ?> edge = (Edge<?, ?>) o;

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
