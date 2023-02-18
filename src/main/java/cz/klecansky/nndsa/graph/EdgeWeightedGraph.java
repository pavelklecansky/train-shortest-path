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
        addEdge(edge.getStart().getKey(), edge.getTarget().getKey(), edge.getWeight());
    }

    @Override
    public void deleteEdge(K first, K second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Edge is null.");
        }
        deleteEdgeFromSingleVertex(first, second);
        deleteEdgeFromSingleVertex(second, first);
    }

    @Override
    public Vertex<K, V> vertexByKey(K vertexKey) {
        return vertices.get(vertexKey);
    }

    @Override
    public Set<K> getVerticesKey() {
        return vertices.keySet();
    }

    @Override
    public List<Edge<K, V>> getEdges() {
        return vertices.values().stream().map(Vertex::getEdges).flatMap(List::stream).toList();
    }

    @Override
    public List<Vertex<K, V>> getVertices() {
        return vertices.values().stream().toList();
    }

    private boolean isVertexNotInGraph(Vertex<K, V> vertex) {
        return isVertexNotInGraph(vertex.getKey());
    }

    private boolean isVertexNotInGraph(K vertexKey) {
        return !vertices.containsKey(vertexKey);
    }

    private void deleteEdgeFromSingleVertex(K one, K other) {
        if (isVertexNotInGraph(one) || isVertexNotInGraph(other)) {
            throw new IllegalArgumentException("Edge vertices are not in graph.");
        }
        vertices.get(one).deleteEdge(other);
    }
}
