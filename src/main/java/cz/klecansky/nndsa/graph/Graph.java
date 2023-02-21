package cz.klecansky.nndsa.graph;

import cz.klecansky.nndsa.rail.Rail;

import java.util.List;
import java.util.Set;

public interface Graph<K, V> {
    void addVertex(K key, V value);

    void addEdge(K firstVertex, K secondVertex, V value);

    void deleteEdge(K first, K second);

    Vertex<K, V> vertexByKey(K vertexKey);

    Set<K> getVerticesKey();

    List<Edge<K, V>> getEdges();
    List<Vertex<K, V>> getVertices();

    List<Edge<K, V>> getUndirectedEdges();
}
