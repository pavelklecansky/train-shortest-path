package cz.klecansky.nndsa.graph;

import java.util.List;
import java.util.Set;

public interface Graph<Key, VValue, EValue> {
    void addVertex(Key key, VValue value);

    void addEdge(Key firstVertex, Key secondVertex, EValue value);

    void deleteEdge(Key first, Key second);

    Vertex<Key, VValue, EValue> vertexByKey(Key vertexKey);

    Set<Key> getVerticesKey();

    List<Edge<Key, VValue, EValue>> getEdges();

    List<Vertex<Key, VValue, EValue>> getVertices();

    List<Edge<Key, VValue, EValue>> getUndirectedEdges();
}
