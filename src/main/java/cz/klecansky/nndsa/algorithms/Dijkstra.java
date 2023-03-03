package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;

import java.util.*;

public class Dijkstra {

    public void computePath(Vertex<String, RailSwitch, Rail> sourceVertex, RailwayInfrastructure infrastructure, double trainLength) {
        sourceVertex.setMinDistance(0);
        PriorityQueue<Vertex<String, RailSwitch, Rail>> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(sourceVertex);

        while (!priorityQueue.isEmpty()) {
            Vertex<String, RailSwitch, Rail> vertex = priorityQueue.poll();

            for (Edge<String, RailSwitch, Rail> edge : vertex.getEdges()) {
                Vertex<String, RailSwitch, Rail> v = edge.getTarget();
                if (v.getValue().isTrainNear()) {
                    continue;
                }
                double weight = edge.getValue().getLength();

                if (vertex.getPreviosVertex() != null) {
                    if (infrastructure.isCrossing(vertex.getPreviosVertex().getKey(), vertex.getKey(), edge.getTarget().getKey())) {
                        System.out.printf("Illegal Path: %s->%s->%s%n", vertex.getPreviosVertex().getKey(), vertex.getKey(), edge.getTarget().getKey());
                        Set<Edge<String, RailSwitch, Rail>> reversePath = getReversePath(vertex.getPreviosVertex(), vertex, edge.getTarget(), trainLength);
                        System.out.println(reversePath);
                        double reversePathSum = reversePath.stream().map(Edge::getValue).map(Rail::getLength).mapToDouble(Double::doubleValue).sum();
                        if (reversePathSum < trainLength) {
                            continue;
                        }
                        weight += trainLength;
                    }
                    System.out.println(vertex.getPreviosVertex().getKey() + "->" + vertex.getKey() + " -> " + edge.getTarget().getKey());
                }

                double minDistance = vertex.getMinDistance() + weight;
                if (minDistance < v.getMinDistance()) {
                    priorityQueue.remove(vertex);
                    v.setPreviosVertex(vertex);
                    v.setMinDistance(minDistance);
                    priorityQueue.add(v);
                }
            }
        }
    }

    private Set<Edge<String, RailSwitch, Rail>> getReversePath(Vertex<String, RailSwitch, Rail> from, Vertex<String, RailSwitch, Rail> currentVertex, Vertex<String, RailSwitch, Rail> destination, double threshold) {
        Stack<Vertex<String, RailSwitch, Rail>> stack = new Stack<>();
        Set<Vertex<String, RailSwitch, Rail>> visited = new HashSet<>();
        Set<Edge<String, RailSwitch, Rail>> moveOver = new HashSet<>();
        int totalWeight = 0;

        stack.push(currentVertex);

        while (!stack.isEmpty()) {
            if (threshold <= totalWeight) {
                break;
            }
            Vertex<String, RailSwitch, Rail> current = stack.pop();
            if (!visited.contains(current)) {
                visited.add(current);

                for (Edge<String, RailSwitch, Rail> e : current.getEdges()) {
                    Vertex<String, RailSwitch, Rail> neighbor = e.getTarget();
                    if (isPartOfOldCrossing(neighbor, from, destination)) {
                        continue;
                    }
                    if (neighbor.getValue().isTrainNear()) {
                        continue;
                    }
                    double edgeWeight = e.getValue().getLength();

                    if (!visited.contains(neighbor)) {
                        moveOver.add(e);
                        totalWeight += edgeWeight;
                        stack.push(neighbor);
                    }
                }
            }
        }
        return moveOver;
    }

    private boolean isPartOfOldCrossing(Vertex<String, RailSwitch, Rail> neighbor, Vertex<String, RailSwitch, Rail> from, Vertex<String, RailSwitch, Rail> destination) {
        return neighbor.getKey().equals(from.getKey()) || neighbor.getKey().equals(destination.getKey());
    }

    private List<Vertex<String, RailSwitch, Rail>> getNeighborVertices(Vertex<String, RailSwitch, Rail> currentVertex) {
        return currentVertex.getEdges().stream().map(Edge::getTarget).toList();
    }

    public List<ShortestPathDisplay> getShortestPathTo(Vertex<String, RailSwitch, Rail> targetVertex) {
        List<ShortestPathDisplay> path = new ArrayList<>();

        for (Vertex<String, RailSwitch, Rail> vertex = targetVertex; vertex != null; vertex = vertex.getPreviosVertex()) {
            path.add(new ShortestPathDisplay(vertex.getKey(), vertex.getMinDistance()));
        }

        Collections.reverse(path);
        return path;
    }

}
