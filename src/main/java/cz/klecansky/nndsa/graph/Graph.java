package cz.klecansky.nndsa.graph;

import java.util.List;
import java.util.Set;

public interface Graph<Key, VValue, EValue> {
    void addVertex(Key key, VValue value);

    void addEdge(Key key, Key firstVertexKey, Key secondVertexKey, EValue value);

    void deleteEdge(Key first, Key second);

    Vertex<Key, VValue, EValue> vertexByKey(Key vertexKey);

    Set<Key> getVerticesKey();

    List<Edge<Key, VValue, EValue>> getEdges();

    List<Vertex<Key, VValue, EValue>> getVertices();

    List<Edge<Key, VValue, EValue>> getUndirectedEdges();

    List<VValue> getVerticesValue();
    List<EValue> getEdgeValue();

    void clearDijkstra();

    VValue getVertexValue(Key railNear);

    Edge<Key,VValue,EValue> edgeByKey(Key from);

    List<Key> getVertexEdgeKeys(Key newValue);

    EValue getEdgeValue(Key railSwitchStart);
}
