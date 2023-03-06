package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import javafx.util.Pair;

import java.util.*;

public class Dijkstra {

    public List<ShortestPathDisplay> computePath(RailSwitch sourceRailSwitch, Rail startRail, RailSwitch targetRailSwitch, Rail endRail, RailwayInfrastructure infrastructure, double trainLength) {
        Map<String, List<String>> reversePaths = new HashMap<>();
        sourceRailSwitch.setMinDistance(0);
        PriorityQueue<RailSwitch> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(sourceRailSwitch);

        while (!priorityQueue.isEmpty()) {
            RailSwitch current = priorityQueue.poll();

            for (Rail rail : infrastructure.getRailNeighbours(current.getName())) {
                RailSwitch target = infrastructure.getRailTarget(current.getName(), rail.getName());
                if (target.isTrainNear()) {
                    continue;
                }
                if (target.equals(targetRailSwitch) && (rail.equals(endRail) && infrastructure.getRailNeighbours(target.getName()).size() != 1)) {
                    System.out.printf("Bad path: %s via %s\n", target.getName(), rail.getName());
                    continue;
                }
                double weight = rail.getLength();

                if (current.getPreviosRailSwitch() != null) {
                    if (infrastructure.isPartOfIllegalPath(current.getPreviosRailSwitch().getName(), current.getName(), target.getName())) {
                        System.out.printf("Illegal Path: %s->%s->%s%n", current.getPreviosRailSwitch().getName(), current.getName(), target.getName());
                        Pair<List<String>, Boolean> reversePath = getReversePath(current.getPreviosRailSwitch(), current, target, trainLength, infrastructure);
                        reversePaths.put(current.getName(), new ArrayList<>(reversePath.getKey()));
                        if (!reversePath.getValue()) {
                            continue;
                        }
                        current.setMinDistance(current.getMinDistance() + trainLength);
                    }
                }

                double minDistance = current.getMinDistance() + weight;
                if (minDistance < target.getMinDistance()) {
                    priorityQueue.remove(current);
                    target.setPreviosRailSwitch(current);
                    target.setMinDistance(minDistance);
                    priorityQueue.add(target);
                }
            }
        }
        List<ShortestPathDisplay> path = new ArrayList<>();

        for (RailSwitch railSwitch = targetRailSwitch; railSwitch != null; railSwitch = railSwitch.getPreviosRailSwitch()) {
            path.add(new ShortestPathDisplay(railSwitch.getName(), railSwitch.getMinDistance(), trainLength));
        }

        Collections.reverse(path);

        List<String> illegalCrossing = getIllegalCrossingThatTrainUsed(path, infrastructure);

        for (String railSwitchName : illegalCrossing) {
            if (reversePaths.containsKey(railSwitchName)) {
                List<String> reversePathList = reversePaths.get(railSwitchName);
                path.forEach(shortestPathDisplay -> {
                    if (shortestPathDisplay.getRailSwitchName().equals(railSwitchName)) {
                        shortestPathDisplay.setReversePath(reversePathList);
                    }
                });
            }
        }

        return path;
    }

    private Pair<List<String>, Boolean> getReversePath(RailSwitch from, RailSwitch currentRailSwitch, RailSwitch destination, double threshold, RailwayInfrastructure infrastructure) {
        Stack<RailSwitch> stack = new Stack<>();
        Set<RailSwitch> visited = new HashSet<>();
        Map<RailSwitch, DFSTable> dfsTableMap = new HashMap<>();

        stack.push(currentRailSwitch);

        while (!stack.isEmpty()) {
            if (isPathOverThreshold(threshold, dfsTableMap)) {
                break;
            }
            RailSwitch current = stack.pop();
            dfsTableMap.putIfAbsent(current, new DFSTable(currentRailSwitch));
            if (!visited.contains(current)) {
                visited.add(current);
                for (Rail rail : infrastructure.getRailNeighbours(current.getName())) {
                    RailSwitch neighbor = infrastructure.getRailTarget(current.getName(), rail.getName());
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

                    double railVacancy = rail.getVacancy();

                    if (!visited.contains(neighbor)) {
                        dfsTableMap.putIfAbsent(neighbor, new DFSTable(neighbor, current, dfsTableMap.get(current).getPosition() + 1, dfsTableMap.get(current).getWeight() + railVacancy));
                        if (dfsTableMap.containsKey(current)) {
                            dfsTableMap.get(current).addRail(rail);
                        }
                        stack.push(neighbor);
                    }
                }
            }
        }

        List<String> railReversePath = trailReversePathRails(trainReversePath(dfsTableMap), infrastructure);

        return new Pair<>(railReversePath, isPathOverThreshold(threshold, dfsTableMap));
    }

    private List<String> getIllegalCrossingThatTrainUsed(List<ShortestPathDisplay> path, RailwayInfrastructure infrastructure) {
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

    private boolean isPathOverThreshold(double threshold, Map<RailSwitch, DFSTable> dfsTableMap) {
        return dfsTableMap.values().stream().anyMatch(table -> table.getWeight() >= threshold);
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
