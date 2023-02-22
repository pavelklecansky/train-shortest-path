package cz.klecansky.nndsa.graph;

import java.util.*;

public class EdgeWeightedGraph<Key, VValue, EValue> implements Graph<Key, VValue, EValue> {

    private final Map<Key, Vertex<Key, VValue, EValue>> vertices;
    private final List<Edge<Key, VValue, EValue>> undirectedEdges;

    public EdgeWeightedGraph() {
        vertices = new HashMap<>();
        undirectedEdges = new ArrayList<>();
    }

    @Override
    public void addVertex(Key key, VValue value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("Vertex is null.");
        }
        if (vertices.containsKey(key)) {
            throw new IllegalArgumentException("Vertex with this key is already in graph.");
        }
        vertices.put(key, new Vertex<>(key, value));
    }

    @Override
    public void addEdge(Key firstVertexKey, Key secondVertexKey, EValue value) {
        if (firstVertexKey == null && secondVertexKey == null) {
            throw new IllegalArgumentException("Vertex keys are null.");
        }
        if (!vertices.containsKey(firstVertexKey) && !vertices.containsKey(secondVertexKey)) {
            throw new IllegalArgumentException("Edge vertices are not in graph.");
        }
        Vertex<Key, VValue, EValue> firstVertex = vertices.get(firstVertexKey);
        Vertex<Key, VValue, EValue> secondVertex = vertices.get(secondVertexKey);

        Edge<Key, VValue, EValue> firstEdge = new Edge<>(firstVertex, secondVertex, value);
        Edge<Key, VValue, EValue> secondEdge = new Edge<>(secondVertex, firstVertex, value);

        undirectedEdges.add(firstEdge);
        firstVertex.addEdge(firstEdge);
        secondVertex.addEdge(secondEdge);
    }

    @Override
    public void deleteEdge(Key first, Key second) {
        if (first == null || second == null) {
            throw new IllegalArgumentException("Edge is null.");
        }
        deleteEdgeFromSingleVertex(first, second);
        deleteEdgeFromSingleVertex(second, first);
    }

    @Override
    public Vertex<Key, VValue, EValue> vertexByKey(Key vertexKey) {
        return vertices.get(vertexKey);
    }

    @Override
    public Set<Key> getVerticesKey() {
        return vertices.keySet();
    }

    @Override
    public List<Edge<Key, VValue, EValue>> getEdges() {
        return vertices.values().stream().map(Vertex::getEdges).flatMap(List::stream).toList();
    }

    @Override
    public List<Vertex<Key, VValue, EValue>> getVertices() {
        return vertices.values().stream().toList();
    }

    @Override
    public List<Edge<Key, VValue, EValue>> getUndirectedEdges() {
        return undirectedEdges;
    }

    @Override
    public List<VValue> getVerticesValue() {
         return vertices.values().stream().map(Vertex::getValue).toList();
    }

    private boolean isVertexNotInGraph(Vertex<Key, VValue, EValue> vertex) {
        return isVertexNotInGraph(vertex.getKey());
    }

    private boolean isVertexNotInGraph(Key vertexKey) {
        return !vertices.containsKey(vertexKey);
    }

    private void deleteEdgeFromSingleVertex(Key one, Key other) {
        if (isVertexNotInGraph(one) || isVertexNotInGraph(other)) {
            throw new IllegalArgumentException("Edge vertices are not in graph.");
        }
        vertices.get(one).deleteEdge(other);
    }
}
