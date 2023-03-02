package cz.klecansky.nndsa.graph;

import java.util.Objects;

public class Edge<Key extends Comparable<Key>, VValue, EValue> implements Comparable<Edge<Key, VValue, EValue>> {
    private Key key;
    private EValue value;
    private final Vertex<Key, VValue, EValue> start;
    private final Vertex<Key, VValue, EValue> target;

    public Edge(Key key, Vertex<Key, VValue, EValue> start, Vertex<Key, VValue, EValue> target, EValue rail) {
        this.key = key;
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

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public void setValue(EValue value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s: %s-%s %s", key, start, target, value);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?, ?, ?> edge = (Edge<?, ?, ?>) o;

        return Objects.equals(key, edge.key);
    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + start.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }

    @Override
    public int compareTo(Edge<Key, VValue, EValue> o) {
        return this.key.compareTo(o.key);
    }
}
