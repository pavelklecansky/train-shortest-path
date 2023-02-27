package cz.klecansky.nndsa.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VertexTest {

    @Test
    void containsEdge() {
        Vertex<String, Double, Integer> first = new Vertex<>("first", 100.0);
        Vertex<String, Double, Integer> second = new Vertex<>("second", 100.0);

        Edge<String, Double, Integer> firstEdge = new Edge<>("e1",first, second, 100);
        Edge<String, Double, Integer> secondEdge = new Edge<>("e1",second, first, 100);

        first.addEdge(firstEdge);
        assertTrue(first.containsEdge(secondEdge));
    }
}