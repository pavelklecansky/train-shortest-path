package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;

import java.util.*;

public class Dijkstra {

    public void computePath(Vertex<String, RailSwitch, Rail> sourceVertex) {
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

    public List<Vertex<String, RailSwitch, Rail>> getShortestPathTo(Vertex<String, RailSwitch, Rail> targetVertex) {
        List<Vertex<String, RailSwitch, Rail>> path = new ArrayList<>();

        for (Vertex<String, RailSwitch, Rail> vertex = targetVertex; vertex != null; vertex = vertex.getPreviosVertex()) {
            path.add(vertex);
        }

        Collections.reverse(path);
        return path;
    }

}
