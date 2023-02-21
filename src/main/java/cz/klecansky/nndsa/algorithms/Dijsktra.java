package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.rail.Rail;

import java.util.*;

public class Dijsktra {

    public void computePath(Vertex<String, Rail> sourceVertex) {
        sourceVertex.setMinDistance(0);
        PriorityQueue<Vertex<String, Rail>> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(sourceVertex);
        double allWeight = 0;

        while (!priorityQueue.isEmpty()) {
            Vertex<String, Rail> vertex = priorityQueue.poll();

            for (Edge<String, Rail> edge : vertex.getEdges()) {
                Vertex<String, Rail> v = edge.getTarget();
                double weight = edge.getValue().getLength();
                double minDistance = vertex.getMinDistance() + weight;

                if (minDistance < v.getMinDistance()) {
                    allWeight += weight;
                    priorityQueue.remove(vertex);
                    v.setPreviosVertex(vertex);
                    v.setMinDistance(minDistance);
                    priorityQueue.add(v);
                }
            }
        }
        System.out.println(allWeight);
    }

    public List<Vertex<String, Integer>> getShortestPathTo(Vertex<String, Integer> targetVerte) {
        List<Vertex<String, Integer>> path = new ArrayList<>();

        for (Vertex<String, Integer> vertex = targetVerte; vertex != null; vertex = vertex.getPreviosVertex()) {
            path.add(vertex);
        }

        Collections.reverse(path);
        return path;
    }

}
