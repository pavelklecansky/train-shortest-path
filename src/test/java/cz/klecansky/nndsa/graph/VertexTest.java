package cz.klecansky.nndsa.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VertexTest {

    @Test
    void containsEdge() {
        Vertex<String, Double> first = new Vertex<>("first", 100.0);
        Vertex<String, Double> second = new Vertex<>("second", 100.0);

        Edge<String, Double> firstEdge = new Edge<>(first, second, 100);
        Edge<String, Double> secondEdge = new Edge<>(second, first, 100);

        first.addEdge(firstEdge);
        assertTrue(first.containsEdge(secondEdge));
    }
}