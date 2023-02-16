package cz.klecansky.nndsa.graph;

import java.util.List;
import java.util.Set;

public interface Graph<K, V> {
    void addVertex(Vertex<K, V> vertex);

    void addEdge(K firstVertex, K secondVertex, double weight);

    void addEdge(Edge<K, V> edge);

    void deleteEdge(Edge<K, V> edge);

    Set<K> getVerticesKey();

    List<String> getEdges();
}
