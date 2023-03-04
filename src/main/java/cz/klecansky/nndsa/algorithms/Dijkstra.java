package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Crossing;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import javafx.util.Pair;

import java.util.*;
import java.util.stream.Collectors;

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
                        Stack<Edge<String, RailSwitch, Rail>> reversePath = getReversePath(vertex.getPreviosVertex(), vertex, edge.getTarget(), trainLength, infrastructure);
                        System.out.println(reversePath);
                        reversePaths.put(vertex.getKey(), new HashSet<>(reversePath));
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

    private Stack<Edge<String, RailSwitch, Rail>> getReversePath(Vertex<String, RailSwitch, Rail> from, Vertex<String, RailSwitch, Rail> currentVertex, Vertex<String, RailSwitch, Rail> destination, double threshold, RailwayInfrastructure infrastructure) {
        Stack<Vertex<String, RailSwitch, Rail>> stack = new Stack<>();
        Set<Vertex<String, RailSwitch, Rail>> visited = new HashSet<>();
        Stack<Edge<String, RailSwitch, Rail>> moveOver = new Stack<>();
        Stack<Pair<Edge<String, RailSwitch, Rail>, List<Edge<String, RailSwitch, Rail>>>> test = new Stack<>();
        List<List<Edge<String, RailSwitch, Rail>>> allPaths = new ArrayList<>();
        int totalWeight = 0;

        List<Edge<String, RailSwitch, Rail>> currentList = new ArrayList<>();

        stack.push(currentVertex);

        // double remainingThreshold = threshold;

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
                    if (threshold <= totalWeight) {
                        break;
                    }

//                    if (current.getPreviosVertex() != null) {
//                        System.out.printf("Illegal Path: %s->%s->%s%n", current.getPreviosVertex().getKey(), current.getKey(), neighbor.getKey());
//                        if (infrastructure.isPartOfIllegalPath(current.getPreviosVertex().getKey(), current.getKey(), neighbor.getKey())) {
//                            continue;
//                        }
//                    }
                    double edgeWeight = e.getValue().getLength();

                    if (!visited.contains(neighbor)) {
                        System.out.println(e);
                        currentList.add(e);
                        allPaths.add(currentList);
                        currentList = new ArrayList<>(currentList);
                        moveOver.add(e);
                        totalWeight += edgeWeight;
                        stack.push(neighbor);

                    }
                }
            }
        }

//        System.out.println("------- Test -----------");
//        for (Pair<Edge<String, RailSwitch, Rail>, List<Edge<String, RailSwitch, Rail>>> edgeListPair : test) {
//            System.out.println(edgeListPair);
//        }
//        System.out.println("------------------------");
        System.out.println("------- Test -----------");
        for (List<Edge<String, RailSwitch, Rail>> allPath : allPaths) {
            System.out.println(allPath);
        }
        System.out.println("------------------------");
        return moveOver;
    }

    private boolean isPartOfOldCrossing(Vertex<String, RailSwitch, Rail> neighbor, Vertex<String, RailSwitch, Rail> from, Vertex<String, RailSwitch, Rail> destination) {
        return neighbor.getKey().equals(from.getKey()) || neighbor.getKey().equals(destination.getKey());
    }

    private List<Vertex<String, RailSwitch, Rail>> getNeighborVertices(Vertex<String, RailSwitch, Rail> currentVertex) {
        return currentVertex.getEdges().stream().map(Edge::getTarget).toList();
    }
}
