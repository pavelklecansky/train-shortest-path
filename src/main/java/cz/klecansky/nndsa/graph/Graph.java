package cz.klecansky.nndsa.graph;

import cz.klecansky.nndsa.utils.Triplet;

import java.util.List;

public interface Graph<Key extends Comparable<Key>, VValue, EValue> {
    void addVertex(Key key, VValue value);

    void addEdge(Key key, Key firstVertexKey, Key secondVertexKey, EValue value);

    void deleteEdge(Key first, Key second);

    Vertex<Key, VValue, EValue> vertexByKey(Key vertexKey);

    List<VValue> getVerticesValue();

    List<EValue> getEdgeValue();

    void clearDijkstra();

    VValue getVertexValue(Key vertexKey);

    List<Key> getVertexEdgeKeys(Key newValue);

    List<Triplet<Key, Key, EValue>> getDistinctDetailEdgeValues();

    EValue getEdgeValue(Key edgeKey);

    List<Triplet<Key, Key,EValue>> getDetailEdgeValues();

    void deleteEdge(Key edgeKey);

    void deleteVertex(Key vertexKey);

    void renameVertex(Key oldName, Key newName);

    void setEdge(Key oldKey, Key newKey, EValue eValue);
}
