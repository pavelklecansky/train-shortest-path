package cz.klecansky.nndsa.graph;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    @Test
    void edgesAreEqualWhenVertexSwapped() {
        Graph.Vertex<String, Double, Integer> first = new Graph.Vertex<>("first", 100.0);
        Graph.Vertex<String, Double, Integer> second = new Graph.Vertex<>("second", 100.0);

        Graph.Edge<String, Double, Integer> firstEdge = new Graph.Edge<>("e1", first, second, 100);
        Graph.Edge<String, Double, Integer> secondEdge = new Graph.Edge<>("e1", second, first, 100);

        assertEquals(firstEdge, secondEdge);
    }

    @Test
    void edgesAreEqualWhenVertexSwapped2() {
        Graph.Vertex<String, Double, Integer> first = new Graph.Vertex<>("v13", 40.0);
        Graph.Vertex<String, Double, Integer> second = new Graph.Vertex<>("v14", 40.0);

        Graph.Edge<String, Double, Integer> firstEdge = new Graph.Edge<>("e1", first, second, 100);
        Graph.Edge<String, Double, Integer> secondEdge = new Graph.Edge<>("e1", second, first, 100);

        assertEquals(firstEdge, secondEdge);
    }
}