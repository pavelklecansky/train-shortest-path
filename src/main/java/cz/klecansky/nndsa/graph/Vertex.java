package cz.klecansky.nndsa.graph;

import java.util.ArrayList;
import java.util.List;

public class Vertex<K, V> implements Comparable<Vertex>  {

    private final K key;
    private final V value;

    private final List<Edge<K, V>> edges;

    private Vertex<K, V> previosVertex;
    private boolean visited;
    private double minDistance = Double.MAX_VALUE;

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
        edges.removeIf(edge -> edge.getTarget().getKey().equals(other));
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

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    public Vertex<K, V> getPreviosVertex() {
        return previosVertex;
    }

    public void setPreviosVertex(Vertex<K, V> previosVertex) {
        this.previosVertex = previosVertex;
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

    @Override
    public int compareTo(Vertex otherVertex) {
        return Double.compare(this.minDistance, otherVertex.minDistance);
    }
}
