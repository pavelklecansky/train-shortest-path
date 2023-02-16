package cz.klecansky.nndsa.graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EdgeWeightedGraph<K, V> implements Graph<K, V> {

    private final Map<K, Vertex<K, V>> vertices;

    public EdgeWeightedGraph() {
        vertices = new HashMap<>();
    }

    @Override
    public void addVertex(Vertex<K, V> vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex is null.");
        }
        if (vertices.containsKey(vertex.getKey())) {
            throw new IllegalArgumentException("Vertex with this key is already in graph.");
        }
        vertices.put(vertex.getKey(), vertex);
    }

    @Override
    public void addEdge(K firstVertexKey, K secondVertexKey, double weight) {
        if (firstVertexKey == null && secondVertexKey == null) {
            throw new IllegalArgumentException("Vertex keys are null.");
        }
        if (!vertices.containsKey(firstVertexKey) && !vertices.containsKey(secondVertexKey)) {
            throw new IllegalArgumentException("Edge vertices are not in graph.");
        }
        Vertex<K, V> firstVertex = vertices.get(firstVertexKey);
        Vertex<K, V> secondVertex = vertices.get(secondVertexKey);

        Edge<K, V> firstEdge = new Edge<>(firstVertex, secondVertex, weight);
        Edge<K, V> secondEdge = new Edge<>(secondVertex, firstVertex, weight);

        firstVertex.addEdge(firstEdge);
        secondVertex.addEdge(secondEdge);
    }

    @Override
    public void addEdge(Edge<K, V> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge is null.");
        }
        addEdge(edge.getFirst().getKey(), edge.getSecond().getKey(), edge.getWeight());
    }

    @Override
    public void deleteEdge(Edge<K, V> edge) {
        if (edge == null) {
            throw new IllegalArgumentException("Edge is null.");
        }
        deleteEdgeFromSingleVertex(edge, edge.getFirst());
        deleteEdgeFromSingleVertex(edge, edge.getSecond());
    }

    @Override
    public Set<K> getVerticesKey() {
        return vertices.keySet();
    }

    @Override
    public List<String> getEdges() {
        return vertices.values().stream().map(Vertex::getEdges).flatMap(List::stream).map(Object::toString).toList();
    }

    private boolean isVertexNotInGraph(Vertex<K, V> vertex) {
        return !vertices.containsKey(vertex.getKey());
    }

    private void deleteEdgeFromSingleVertex(Edge<K, V> edge, Vertex<K, V> vertex) {
        if (isVertexNotInGraph(vertex)) {
            throw new IllegalArgumentException("Edge vertices are not in graph.");
        }
        vertices.get(vertex.getKey()).deleteEdge(edge);
    }
}
