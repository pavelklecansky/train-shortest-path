package cz.klecansky.nndsa.graph;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EdgeTest {
    @Test
    void edgesAreEqualWhenVertexSwapped() {
        Vertex<String, Double> first = new Vertex<>("first", 100.0);
        Vertex<String, Double> second = new Vertex<>("second", 100.0);

        Edge<String, Double> firstEdge = new Edge<>(first, second, 100);
        Edge<String, Double> secondEdge = new Edge<>(second, first, 100);

        assertEquals(firstEdge, secondEdge);
    }

    @Test
    void edgesAreEqualWhenVertexSwapped2() {
        Vertex<String, Double> first = new Vertex<>("v13", 40.0);
        Vertex<String, Double> second = new Vertex<>("v14", 40.0);

        Edge<String, Double> firstEdge = new Edge<>(first, second, 100);
        Edge<String, Double> secondEdge = new Edge<>(second, first, 100);

        List<Edge<String, Double>> firstEdge1 = List.of(firstEdge, secondEdge);

        assertEquals(firstEdge, secondEdge);
    }
}