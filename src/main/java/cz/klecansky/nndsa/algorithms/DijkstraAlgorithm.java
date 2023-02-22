package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Rail;

import java.util.*;

public class DijkstraAlgorithm {

//    private final List<Vertex<String, Rail>> nodes;
//    private final List<Edge<String, Rail>> edges;
//    private Set<Vertex<String, Rail>> settledNodes;
//    private Set<Vertex<String, Rail>> unSettledNodes;
//    private Map<Vertex<String, Rail>, Vertex<String, Rail>> predecessors;
//    private Map<Vertex<String, Rail>, Integer> distance;
//
//    public DijkstraAlgorithm(Graph<String, Rail> graph) {
//        // create a copy of the array so that we can operate on this array
//        this.nodes = new ArrayList<>(graph.getVertices());
//        this.edges = new ArrayList<>(graph.getEdges());
//    }
//
//    public void execute(Vertex<String, Rail> source) {
//        settledNodes = new HashSet<>();
//        unSettledNodes = new HashSet<>();
//        distance = new HashMap<>();
//        predecessors = new HashMap<>();
//        distance.put(source, 0);
//        unSettledNodes.add(source);
//        while (unSettledNodes.size() > 0) {
//            Vertex<String, Rail> node = getMinimum(unSettledNodes);
//            settledNodes.add(node);
//            unSettledNodes.remove(node);
//            findMinimalDistances(node);
//        }
//    }
//
//    private void findMinimalDistances(Vertex<String, Rail> node) {
//        List<Vertex<String, Rail>> adjacentNodes = getNeighbors(node);
//        for (Vertex<String, Rail> target : adjacentNodes) {
//            if (getShortestDistance(target) > getShortestDistance(node)
//                    + getDistance(node, target)) {
//                distance.put(target, getShortestDistance(node)
//                        + getDistance(node, target));
//                predecessors.put(target, node);
//                unSettledNodes.add(target);
//            }
//        }
//
//    }
//
//    private int getDistance(Vertex<String, Rail> node, Vertex<String, Rail> target) {
//        for (Edge<String, Rail> edge : edges) {
//            if (edge.getStart().equals(node)
//                    && edge.getTarget().equals(target)) {
//                return (int) edge.getValue().getLength();
//            }
//        }
//        throw new RuntimeException("Should not happen");
//    }
//
//    private List<Vertex<String, Rail>> getNeighbors(Vertex<String, Rail> node) {
//        List<Vertex<String, Rail>> neighbors = new ArrayList<>();
//        for (Edge<String, Rail> edge : edges) {
//            if (edge.getStart().equals(node)
//                    && !isSettled(edge.getTarget())) {
//                neighbors.add(edge.getTarget());
//            }
//        }
//        return neighbors;
//    }
//
//    private Vertex<String, Rail> getMinimum(Set<Vertex<String, Rail>> vertexes) {
//        Vertex<String, Rail> minimum = null;
//        for (Vertex<String, Rail> vertex : vertexes) {
//            if (minimum == null) {
//                minimum = vertex;
//            } else {
//                if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
//                    minimum = vertex;
//                }
//            }
//        }
//        return minimum;
//    }
//
//    private boolean isSettled(Vertex<String, Rail> vertex) {
//        return settledNodes.contains(vertex);
//    }
//
//    private int getShortestDistance(Vertex<String, Rail> destination) {
//        Integer d = distance.get(destination);
//        return Objects.requireNonNullElse(d, Integer.MAX_VALUE);
//    }
//
//    /*
//     * This method returns the path from the source to the selected target and
//     * NULL if no path exists
//     */
//    public LinkedList<Vertex<String, Rail>> getPath(Vertex<String, Rail> target) {
//        LinkedList<Vertex<String, Rail>> path = new LinkedList<>();
//        Vertex<String, Rail> step = target;
//        // check if a path exists
//        if (predecessors.get(step) == null) {
//            return null;
//        }
//        path.add(step);
//        while (predecessors.get(step) != null) {
//            step = predecessors.get(step);
//            path.add(step);
//        }
//        // Put it into the correct order
//        Collections.reverse(path);
//        return path;
//    }

}
