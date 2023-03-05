package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import javafx.util.Pair;

import java.util.*;

public class Dijkstra {

    public List<ShortestPathDisplay> computePath(RailSwitch sourceVertex, Rail startRail, RailSwitch targetVertex, Rail endRail, RailwayInfrastructure infrastructure, double trainLength) {
        Map<String, Set<Rail>> reversePaths = new HashMap<>();
        sourceVertex.setMinDistance(0);
        PriorityQueue<RailSwitch> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(sourceVertex);

        while (!priorityQueue.isEmpty()) {
            RailSwitch vertex = priorityQueue.poll();

            for (Rail edge : infrastructure.getRailNeighbours(vertex.getName())) {
                RailSwitch target = infrastructure.getRailTarget(vertex.getName(), edge.getName());
                if (target.isTrainNear()) {
                    continue;
                }
                if (target.equals(targetVertex) && edge.equals(endRail)) {
                    System.out.printf("Bad path: %s via %s\n", target.getName(), edge.getName());
                    continue;
                }
                double weight = edge.getLength();

                if (vertex.getPreviosRailSwitch() != null) {
                    if (infrastructure.isPartOfIllegalPath(vertex.getPreviosRailSwitch().getName(), vertex.getName(), target.getName())) {
                        System.out.printf("Illegal Path: %s->%s->%s%n", vertex.getPreviosRailSwitch().getName(), vertex.getName(), target.getName());
                        Pair<Stack<Rail>, Boolean> reversePath = getReversePath(vertex.getPreviosRailSwitch(), vertex, target, trainLength, infrastructure);
                        System.out.println(reversePath.getKey());
                        reversePaths.put(vertex.getName(), new HashSet<>(reversePath.getKey()));
                        if (!reversePath.getValue()) {
                            continue;
                        }
                        weight += trainLength;
                    }
                    System.out.println(vertex.getPreviosRailSwitch().getName() + "->" + vertex.getName() + " -> " + target.getName());
                }

                double minDistance = vertex.getMinDistance() + weight;
                if (minDistance < target.getMinDistance()) {
                    priorityQueue.remove(vertex);
                    target.setPreviosRailSwitch(vertex);
                    target.setMinDistance(minDistance);
                    priorityQueue.add(target);
                }
            }
        }
        List<ShortestPathDisplay> path = new ArrayList<>();

        for (RailSwitch vertex = targetVertex; vertex != null; vertex = vertex.getPreviosRailSwitch()) {
            path.add(new ShortestPathDisplay(vertex.getName(), vertex.getMinDistance()));
        }

        Collections.reverse(path);

        List<String> illegalCrossing = getIllegalCrossingThatTrainTaked(path, infrastructure);

        for (String vertexKey : illegalCrossing) {
            System.out.println(vertexKey);
            if (reversePaths.containsKey(vertexKey)) {
                List<String> reversePathList = new ArrayList<>(reversePaths.get(vertexKey).stream().map(Rail::getName).toList());
                path.forEach(shortestPathDisplay -> {
                    if (shortestPathDisplay.getRailSwitchName().equals(vertexKey)) {
                        shortestPathDisplay.setReversePath(reversePathList);
                    }
                });
            }
        }

        return path;
    }

    private List<String> getIllegalCrossingThatTrainTaked(List<ShortestPathDisplay> path, RailwayInfrastructure infrastructure) {
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

    private Pair<Stack<Rail>, Boolean> getReversePath(RailSwitch from, RailSwitch currentVertex, RailSwitch destination, double threshold, RailwayInfrastructure infrastructure) {
        Stack<RailSwitch> stack = new Stack<>();
        Set<RailSwitch> visited = new HashSet<>();
        Stack<Rail> moveOver = new Stack<>();

        Map<RailSwitch, DFSTable> dfsTableMap = new HashMap<>();

        Map<RailSwitch, Double> pathWeight = new HashMap<>();

        stack.push(currentVertex);

        while (!stack.isEmpty()) {
            if (isPathOverThreshold(threshold, pathWeight)) {
                break;
            }
            RailSwitch current = stack.pop();
            dfsTableMap.putIfAbsent(current, new DFSTable(currentVertex));
            pathWeight.putIfAbsent(current, 0.0);
            if (!visited.contains(current)) {
                visited.add(current);
                for (Rail edge : infrastructure.getRailNeighbours(current.getName())) {
                    RailSwitch neighbor = infrastructure.getRailTarget(current.getName(), edge.getName());
                    if (isPartOfOldCrossing(neighbor, from, destination)) {
                        continue;
                    }
                    if (current.isTrainNear()) {
                        continue;
                    }
                    if (isPathOverThreshold(threshold, pathWeight)) {
                        break;
                    }

                    if (dfsTableMap.containsKey(current) && dfsTableMap.get(current).getPrevious() != null) {
                        if (infrastructure.isPartOfIllegalPath(dfsTableMap.get(current).getPrevious().getName(), current.getName(), neighbor.getName())) {
                            System.out.printf("Illegal Path inside DFS: %s->%s->%s%n", dfsTableMap.get(current).getPrevious(), current.getName(), neighbor.getName());
                            continue;
                        }
                    }

                    double edgeWeight = edge.getVacancy();

                    if (!visited.contains(neighbor)) {
                        pathWeight.putIfAbsent(neighbor, pathWeight.get(current) + edgeWeight);
                        dfsTableMap.putIfAbsent(neighbor, new DFSTable(neighbor, current));
                        System.out.println(edge);
                        moveOver.add(edge);
                        stack.push(neighbor);
                    }
                }
            }
        }

        System.out.println("------- Test -----------");
        for (Map.Entry<RailSwitch, Double> vertexDoubleEntry : pathWeight.entrySet()) {
            System.out.println(vertexDoubleEntry);
        }
        System.out.println("------- Test -----------");
        for (Map.Entry<RailSwitch, DFSTable> vertexDFSTableEntry : dfsTableMap.entrySet()) {
            System.out.println(vertexDFSTableEntry);
        }
        System.out.println("------------------------");
        return new Pair<>(moveOver, isPathOverThreshold(threshold, pathWeight));
    }

    private boolean isPathOverThreshold(double threshold, Map<RailSwitch, Double> vertexCalculator) {
        return vertexCalculator.values().stream().anyMatch(aDouble -> aDouble >= threshold);
    }

    private boolean isPartOfOldCrossing(RailSwitch neighbor, RailSwitch from, RailSwitch destination) {
        return neighbor.getName().equals(from.getName()) || neighbor.getName().equals(destination.getName());
    }
}
