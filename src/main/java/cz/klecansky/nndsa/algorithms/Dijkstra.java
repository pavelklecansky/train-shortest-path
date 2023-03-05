package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import javafx.util.Pair;

import java.util.*;

public class Dijkstra {

    public List<ShortestPathDisplay> computePath(RailSwitch sourceVertex, Rail startRail, RailSwitch targetVertex, Rail endRail, RailwayInfrastructure infrastructure, double trainLength) {
        Map<String, List<String>> reversePaths = new HashMap<>();
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
                        Pair<List<String>, Boolean> reversePath = getReversePath(vertex.getPreviosRailSwitch(), vertex, target, trainLength, infrastructure);
                        System.out.println(reversePath.getKey());
                        reversePaths.put(vertex.getName(), new ArrayList<>(reversePath.getKey()));
                        if (!reversePath.getValue()) {
                            continue;
                        }
                        vertex.setMinDistance(vertex.getMinDistance() + trainLength);
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
            path.add(new ShortestPathDisplay(vertex.getName(), vertex.getMinDistance(), trainLength));
        }

        Collections.reverse(path);

        List<String> illegalCrossing = getIllegalCrossingThatTrainTaked(path, infrastructure);

        for (String vertexKey : illegalCrossing) {
            System.out.println(vertexKey);
            if (reversePaths.containsKey(vertexKey)) {
                List<String> reversePathList = reversePaths.get(vertexKey);
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

    private Pair<List<String>, Boolean> getReversePath(RailSwitch from, RailSwitch currentVertex, RailSwitch destination, double threshold, RailwayInfrastructure infrastructure) {
        Stack<RailSwitch> stack = new Stack<>();
        Set<RailSwitch> visited = new HashSet<>();
        Stack<Rail> moveOver = new Stack<>();

        Map<RailSwitch, DFSTable> dfsTableMap = new HashMap<>();

        stack.push(currentVertex);

        while (!stack.isEmpty()) {
            if (isPathOverThreshold(threshold, dfsTableMap)) {
                break;
            }
            RailSwitch current = stack.pop();
            dfsTableMap.putIfAbsent(current, new DFSTable(currentVertex));
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
                    if (isPathOverThreshold(threshold, dfsTableMap)) {
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
                        dfsTableMap.putIfAbsent(neighbor, new DFSTable(neighbor, current, dfsTableMap.get(current).getPosition() + 1, dfsTableMap.get(current).getWeight() + edgeWeight));
                        if (dfsTableMap.containsKey(current)) {
                            dfsTableMap.get(current).addRail(edge);
                        }
                        System.out.println(edge);
                        moveOver.add(edge);
                        stack.push(neighbor);
                    }
                }
            }
        }

        System.out.println("------- DFS Table -----------");
        for (Map.Entry<RailSwitch, DFSTable> vertexDFSTableEntry : dfsTableMap.entrySet()) {
            System.out.println(vertexDFSTableEntry);
        }
        System.out.println("------------------------");
        List<String> strings = trainReversePath(dfsTableMap);
        System.out.println(String.join(", ", strings));
        List<String> railReversePath = trailReversePathRails(trainReversePath(dfsTableMap), infrastructure);
        System.out.println(String.join(", ", railReversePath));
        System.out.println("------------------------");

        return new Pair<>(railReversePath, isPathOverThreshold(threshold, dfsTableMap));
    }

    private List<String> trailReversePathRails(List<String> trainReversePath, RailwayInfrastructure infrastructure) {
        List<String> path = new ArrayList<>();
        int i = 0;
        while (i < trainReversePath.size() - 1) {
            Rail rail = infrastructure.getRail(trainReversePath.get(i), trainReversePath.get(i + 1));
            path.add(rail.getName());
            i++;
        }

        return path;
    }

    private boolean isPathOverThreshold(double threshold, Map<RailSwitch, DFSTable> vertexCalculator) {
        return vertexCalculator.values().stream().anyMatch(table -> table.getWeight() >= threshold);
    }

    private boolean isPartOfOldCrossing(RailSwitch neighbor, RailSwitch from, RailSwitch destination) {
        return neighbor.getName().equals(from.getName()) || neighbor.getName().equals(destination.getName());
    }

    private List<String> trainReversePath(Map<RailSwitch, DFSTable> dfsTableMap) {
        List<String> path = new ArrayList<>();
        DFSTable last = dfsTableMap.values().stream().max(Comparator.comparing(DFSTable::getWeight)).orElseThrow(NoSuchElementException::new);
        for (DFSTable current = last; current != null; current = dfsTableMap.get(dfsTableMap.get(current.getRailSwitch()).getPrevious())) {
            path.add(current.getRailSwitch().getName());
        }
        Collections.reverse(path);
        return path;
    }
}
