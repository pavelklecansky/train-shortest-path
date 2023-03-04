package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import javafx.util.Pair;

import java.util.*;

public class Dijkstra {

    public List<ShortestPathDisplay> computePath(Vertex<String, RailSwitch, Rail> sourceVertex, Vertex<String, RailSwitch, Rail> targetVertex, RailwayInfrastructure infrastructure, double trainLength) {
        Map<String, Set<Edge<String, RailSwitch, Rail>>> reversePaths = new HashMap<>();
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
                    if (infrastructure.isPartOfIllegalPath(vertex.getPreviosVertex().getKey(), vertex.getKey(), edge.getTarget().getKey())) {
                        System.out.printf("Illegal Path: %s->%s->%s%n", vertex.getPreviosVertex().getKey(), vertex.getKey(), edge.getTarget().getKey());
                        Pair<Stack<Edge<String, RailSwitch, Rail>>, Boolean> reversePath = getReversePath(vertex.getPreviosVertex(), vertex, edge.getTarget(), trainLength, infrastructure);
                        System.out.println(reversePath.getKey());
                        reversePaths.put(vertex.getKey(), new HashSet<>(reversePath.getKey()));
                        if (!reversePath.getValue()) {
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
        List<ShortestPathDisplay> path = new ArrayList<>();

        for (Vertex<String, RailSwitch, Rail> vertex = targetVertex; vertex != null; vertex = vertex.getPreviosVertex()) {
            path.add(new ShortestPathDisplay(vertex.getKey(), vertex.getMinDistance()));
        }

        Collections.reverse(path);

        List<String> illegalCrossing = getIllegelCrossingThatTrainTaked(path, infrastructure);

        for (String vertexKey : illegalCrossing) {
            System.out.println(vertexKey);
            if (reversePaths.containsKey(vertexKey)) {
                List<String> reversePathList = new ArrayList<>(reversePaths.get(vertexKey).stream().map(Edge::getKey).toList());
                path.forEach(shortestPathDisplay -> {
                    if (shortestPathDisplay.getRailSwitchName().equals(vertexKey)) {
                        shortestPathDisplay.setReversePath(reversePathList);
                    }
                });
            }
        }

        return path;
    }

    private List<String> getIllegelCrossingThatTrainTaked(List<ShortestPathDisplay> path, RailwayInfrastructure infrastructure) {
        List<String> crossings = new ArrayList<>();
        int i = 0;
        while (i < path.size() - 2) {
            if (infrastructure.isPartOfIllegalPath(path.get(i).getRailSwitchName(), path.get(i + 1).getRailSwitchName(), path.get(i + 2).getRailSwitchName())) {
                crossings.add(path.get(i + 1).getRailSwitchName());
            }
            i++;
        }
        return crossings;
    }

    private Pair<Stack<Edge<String, RailSwitch, Rail>>, Boolean> getReversePath(Vertex<String, RailSwitch, Rail> from, Vertex<String, RailSwitch, Rail> currentVertex, Vertex<String, RailSwitch, Rail> destination, double threshold, RailwayInfrastructure infrastructure) {
        Stack<Vertex<String, RailSwitch, Rail>> stack = new Stack<>();
        Set<Vertex<String, RailSwitch, Rail>> visited = new HashSet<>();
        Stack<Edge<String, RailSwitch, Rail>> moveOver = new Stack<>();

        Map<Vertex<String, RailSwitch, Rail>, Double> pathWeight = new HashMap<>();

        stack.push(currentVertex);

        while (!stack.isEmpty()) {
            if (isPathOverThreshold(threshold, pathWeight)) {
                break;
            }
            Vertex<String, RailSwitch, Rail> current = stack.pop();
            pathWeight.putIfAbsent(current, 0.0);
            if (!visited.contains(current)) {
                visited.add(current);
                for (Edge<String, RailSwitch, Rail> e : current.getEdges()) {
                    Vertex<String, RailSwitch, Rail> neighbor = e.getTarget();
                    if (isPartOfOldCrossing(neighbor, from, destination)) {
                        continue;
                    }
                    if (current.getValue().isTrainNear()) {
                        continue;
                    }
                    if (isPathOverThreshold(threshold, pathWeight)) {
                        break;
                    }

                    double edgeWeight = e.getValue().getVacancy();

                    if (!visited.contains(neighbor)) {
                        pathWeight.putIfAbsent(neighbor, pathWeight.get(current) + edgeWeight);
                        System.out.println(e);
                        moveOver.add(e);
                        stack.push(neighbor);
                    }
                }
            }
        }

        System.out.println("------- Test -----------");
        for (Map.Entry<Vertex<String, RailSwitch, Rail>, Double> vertexDoubleEntry : pathWeight.entrySet()) {
            System.out.println(vertexDoubleEntry);
        }
        System.out.println("------------------------");
        return new Pair<>(moveOver, isPathOverThreshold(threshold, pathWeight));
    }

    private boolean isPathOverThreshold(double threshold, Map<Vertex<String, RailSwitch, Rail>, Double> vertexCalculator) {
        return vertexCalculator.values().stream().anyMatch(aDouble -> aDouble >= threshold);
    }

    private boolean isPartOfOldCrossing(Vertex<String, RailSwitch, Rail> neighbor, Vertex<String, RailSwitch, Rail> from, Vertex<String, RailSwitch, Rail> destination) {
        return neighbor.getKey().equals(from.getKey()) || neighbor.getKey().equals(destination.getKey());
    }

    private List<Vertex<String, RailSwitch, Rail>> getNeighborVertices(Vertex<String, RailSwitch, Rail> currentVertex) {
        return currentVertex.getEdges().stream().map(Edge::getTarget).toList();
    }
}
