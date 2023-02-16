package cz.klecansky.nndsa.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Vertex<K, V> {

    private final K key;
    private final V value;

    private final List<Edge<K, V>> edges;

    public Vertex(K key, V value) {
        this.key = key;
        this.value = value;
        edges = new ArrayList<>();
    }

    public void addEdge(Edge<K, V> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge is null.");
        }
        if (containsEdge(edge)) {
            throw new IllegalArgumentException(String.format("Edge is already in vertex: %s", edge));
        }
        edges.add(edge);
    }

    public void deleteEdge(Edge<K, V> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge is null.");
        }
        edges.remove(edge);
    }

    public void deleteEdge(K other) {
        if (other == null) {
            throw new IllegalArgumentException("Other vertex key is null.");
        }
        edges.removeIf(edge -> edge.getSecond().getKey().equals(other));
    }

    public boolean containsEdge(Edge<K, V> newEdge) {
        return this.edges.contains(newEdge);
    }

    public List<Edge<K, V>> getEdges() {
        return edges;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex<?, ?> vertex = (Vertex<?, ?>) o;

        return key.equals(vertex.key);
    }

    @Override
    public int hashCode() {
        return key.hashCode();
    }
}
