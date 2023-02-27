package cz.klecansky.nndsa.graph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Vertex<Key, VValue, EValue> implements Comparable<Vertex<Key, VValue, EValue>> {

    private final Key key;
    private final VValue value;

    private final List<Edge<Key, VValue, EValue>> edges;

    private Vertex<Key, VValue, EValue> previosVertex;
    private boolean visited;
    private double minDistance = Double.MAX_VALUE;

    public Vertex(Key key, VValue value) {
        this.key = key;
        this.value = value;
        edges = new ArrayList<>();
    }

    public void addEdge(Edge<Key, VValue, EValue> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge is null.");
        }
        if (containsEdge(edge)) {
            throw new IllegalArgumentException(String.format("Edge is already in vertex: %s", edge));
        }
        edges.add(edge);
    }

    public void deleteEdge(Key other) {
        if (other == null) {
            throw new IllegalArgumentException("Other vertex key is null.");
        }
        edges.removeIf(edge -> edge.getTarget().getKey().equals(other));
    }

    public boolean containsEdge(Edge<Key, VValue, EValue> newEdge) {
        return this.edges.contains(newEdge);
    }

    public List<Edge<Key, VValue, EValue>> getEdges() {
        return edges;
    }

    public Key getKey() {
        return key;
    }

    public VValue getValue() {
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

    public Vertex<Key, VValue, EValue> getPreviosVertex() {
        return previosVertex;
    }

    public void setPreviosVertex(Vertex<Key, VValue, EValue> previosVertex) {
        this.previosVertex = previosVertex;
    }

    public void clearDijkstra() {
        previosVertex = null;
        minDistance = Double.MAX_VALUE;
    }

    @Override
    public String toString() {
        return key.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex<?, ?, ?> vertex = (Vertex<?, ?, ?>) o;

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
