package cz.klecansky.nndsa.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    @Test
    void edgesAreEqualWhenVertexSwapped() {
        Vertex<String, Double, Integer> first = new Vertex<>("first", 100.0);
        Vertex<String, Double, Integer> second = new Vertex<>("second", 100.0);

        Edge<String, Double, Integer> firstEdge = new Edge<>("e1", first, second, 100);
        Edge<String, Double, Integer> secondEdge = new Edge<>("e1", second, first, 100);

        assertEquals(firstEdge, secondEdge);
    }

    @Test
    void edgesAreEqualWhenVertexSwapped2() {
        Vertex<String, Double, Integer> first = new Vertex<>("v13", 40.0);
        Vertex<String, Double, Integer> second = new Vertex<>("v14", 40.0);

        Edge<String, Double, Integer> firstEdge = new Edge<>("e1", first, second, 100);
        Edge<String, Double, Integer> secondEdge = new Edge<>("e1", second, first, 100);

        List<Edge<String, Double, Integer>> firstEdge1 = List.of(firstEdge, secondEdge);

        assertEquals(firstEdge, secondEdge);
    }
}