package cz.klecansky.nndsa.graph;

import cz.klecansky.nndsa.utils.Triplet;

import java.util.*;

public class EdgeWeightedGraph<Key extends Comparable<Key>, VValue, EValue> implements Graph<Key, VValue, EValue> {

    private final Map<Key, Vertex<Key, VValue, EValue>> vertices;

    public EdgeWeightedGraph() {
        vertices = new HashMap<>();
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
    public void addEdge(Key key, Key firstVertexKey, Key secondVertexKey, EValue value) {
        if (firstVertexKey == null && secondVertexKey == null) {
            throw new IllegalArgumentException("Vertex keys are null.");
        }
        if (!vertices.containsKey(firstVertexKey) && !vertices.containsKey(secondVertexKey)) {
            throw new IllegalArgumentException("Edge vertices are not in graph.");
        }
        Vertex<Key, VValue, EValue> firstVertex = vertices.get(firstVertexKey);
        Vertex<Key, VValue, EValue> secondVertex = vertices.get(secondVertexKey);

        Edge<Key, VValue, EValue> firstEdge = new Edge<>(key, firstVertex, secondVertex, value);
        Edge<Key, VValue, EValue> secondEdge = new Edge<>(key, secondVertex, firstVertex, value);

        firstVertex.addEdge(firstEdge);
        secondVertex.addEdge(secondEdge);
    }

    @Override
    public void deleteEdge(Key edgeKey) {
        List<Vertex<Key, VValue, EValue>> verticesWithEdgeKey = vertices.values().stream().filter(vertex -> vertex.hasEdge(edgeKey)).toList();
        verticesWithEdgeKey.forEach(vertex -> vertex.deleteEdge(edgeKey));
    }

    @Override
    public void deleteVertex(Key vertexKey) {
        getVertexEdgeKeys(vertexKey).forEach(this::deleteEdge);
        vertices.remove(vertexKey);
    }

    @Override
    public void renameVertex(Key oldName, Key newName) {
        Vertex<Key, VValue, EValue> vertex = vertices.get(oldName);
        vertex.setKey(newName);
        vertices.remove(oldName);
        vertices.put(newName, vertex);
    }

    @Override
    public void setEdge(Key oldKey, Key newKey, EValue eValue) {
        List<Vertex<Key, VValue, EValue>> edgeVertices = getEdgeVertices(oldKey);
        edgeVertices.forEach(vertex -> {
            Edge<Key, VValue, EValue> first = vertex.getEdges().stream().filter(edge -> edge.getKey().equals(oldKey)).findFirst().get();
            first.setKey(newKey);
            first.setValue(eValue);
        });
    }

    @Override
    public List<VValue> getVerticesValue() {
        return vertices.values().stream().map(Vertex::getValue).toList();
    }

    @Override
    public List<EValue> getEdgeValues() {
        return getEdges().stream().map(Edge::getValue).toList();
    }

    @Override
    public VValue getVertexValue(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Vertex key is null.");
        }
        if (!vertices.containsKey(key)) {
            throw new IllegalArgumentException("Vertex with this key is not in graph.");
        }
        return vertices.get(key).getValue();
    }

    @Override
    public List<Key> getVertexEdgeKeys(Key newValue) {
        return vertices.get(newValue).getEdges().stream().map(Edge::getKey).toList();
    }

    @Override
    public List<EValue> getVertexEdges(Key newValue) {
        return vertices.get(newValue).getEdges().stream().map(Edge::getValue).toList();
    }

    @Override
    public List<Triplet<Key, Key, EValue>> getDistinctDetailEdgeValues() {
        return getEdges().stream().sorted().distinct().map(stringRailEdge -> new Triplet<>(stringRailEdge.getStart().getKey(), stringRailEdge.getTarget().getKey(), stringRailEdge.getValue())).toList();
    }

    @Override
    public EValue getEdgeValues(Key railSwitchStart) {
        return edgeByKey(railSwitchStart).getValue();
    }

    @Override
    public List<Triplet<Key, Key, EValue>> getDetailEdgeValues() {
        return getEdges().stream().map(stringRailEdge -> new Triplet<>(stringRailEdge.getStart().getKey(), stringRailEdge.getTarget().getKey(), stringRailEdge.getValue())).toList();
    }

    @Override
    public VValue getVertexEdgeTarget(Key railSwitch, Key rail) {
        Optional<Edge<Key, VValue, EValue>> first = vertices.get(railSwitch).getEdges().stream().filter(edge -> edge.getKey().equals(rail)).findFirst();
        return first.map(edge -> edge.getTarget().getValue()).orElse(null);
    }

    private List<Edge<Key, VValue, EValue>> getEdges() {
        return vertices.values().stream().map(Vertex::getEdges).flatMap(List::stream).toList();
    }

    private Edge<Key, VValue, EValue> edgeByKey(Key from) {
        return getEdges().stream().filter(keyVValueEValueEdge -> keyVValueEValueEdge.getKey().equals(from)).findFirst().get();
    }

    private List<Vertex<Key, VValue, EValue>> getEdgeVertices(Key key) {
        return getVertices().stream().filter(vertex -> vertex.hasEdge(key)).toList();
    }

    private List<Vertex<Key, VValue, EValue>> getVertices() {
        return vertices.values().stream().toList();
    }
}
