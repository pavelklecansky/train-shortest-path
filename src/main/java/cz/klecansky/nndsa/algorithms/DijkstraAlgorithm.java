package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;

import java.util.*;

public class DijkstraAlgorithm {

    private final List<Vertex<String, Integer>> nodes;
    private final List<Edge<String, Integer>> edges;
    private Set<Vertex<String, Integer>> settledNodes;
    private Set<Vertex<String, Integer>> unSettledNodes;
    private Map<Vertex<String, Integer>, Vertex<String, Integer>> predecessors;
    private Map<Vertex<String, Integer>, Integer> distance;

    public DijkstraAlgorithm(Graph<String, Integer> graph) {
        // create a copy of the array so that we can operate on this array
        this.nodes = new ArrayList<>(graph.getVertices());
        this.edges = new ArrayList<>(graph.getEdges());
    }

    public void execute(Vertex<String, Integer> source) {
        settledNodes = new HashSet<>();
        unSettledNodes = new HashSet<>();
        distance = new HashMap<>();
        predecessors = new HashMap<>();
        distance.put(source, 0);
        unSettledNodes.add(source);
        while (unSettledNodes.size() > 0) {
            Vertex<String, Integer> node = getMinimum(unSettledNodes);
            settledNodes.add(node);
            unSettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Vertex<String, Integer> node) {
        List<Vertex<String, Integer>> adjacentNodes = getNeighbors(node);
        for (Vertex<String, Integer> target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node)
                    + getDistance(node, target)) {
                distance.put(target, getShortestDistance(node)
                        + getDistance(node, target));
                predecessors.put(target, node);
                unSettledNodes.add(target);
            }
        }

    }

    private int getDistance(Vertex<String, Integer> node, Vertex<String, Integer> target) {
        for (Edge<String, Integer> edge : edges) {
            if (edge.getStart().equals(node)
                    && edge.getTarget().equals(target)) {
                return (int) edge.getWeight();
            }
        }
        throw new RuntimeException("Should not happen");
    }

    private List<Vertex<String, Integer>> getNeighbors(Vertex<String, Integer> node) {
        List<Vertex<String, Integer>> neighbors = new ArrayList<>();
        for (Edge<String, Integer> edge : edges) {
            if (edge.getStart().equals(node)
                    && !isSettled(edge.getTarget())) {
                neighbors.add(edge.getTarget());
            }
        }
        return neighbors;
    }

    private Vertex<String, Integer> getMinimum(Set<Vertex<String, Integer>> vertexes) {
        Vertex<String, Integer> minimum = null;
        for (Vertex<String, Integer> vertex : vertexes) {
            if (minimum == null) {
                minimum = vertex;
            } else {
                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
                    minimum = vertex;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Vertex<String, Integer> vertex) {
        return settledNodes.contains(vertex);
    }

    private int getShortestDistance(Vertex<String, Integer> destination) {
        Integer d = distance.get(destination);
        return Objects.requireNonNullElse(d, Integer.MAX_VALUE);
    }

    /*
     * This method returns the path from the source to the selected target and
     * NULL if no path exists
     */
    public LinkedList<Vertex<String, Integer>> getPath(Vertex<String, Integer> target) {
        LinkedList<Vertex<String, Integer>> path = new LinkedList<>();
        Vertex<String, Integer> step = target;
        // check if a path exists
        if (predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }
        // Put it into the correct order
        Collections.reverse(path);
        return path;
    }

}
