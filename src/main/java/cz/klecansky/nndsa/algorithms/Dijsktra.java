package cz.klecansky.nndsa.algorithms;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.Vertex;

import java.util.*;

public class Dijsktra {

    public void computePath(Vertex<String, Integer> sourceVertex) {
        sourceVertex.setMinDistance(0);
        PriorityQueue<Vertex<String, Integer>> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(sourceVertex);
        double allWeight = 0;

        while (!priorityQueue.isEmpty()) {
            Vertex<String, Integer> vertex = priorityQueue.poll();

            for (Edge<String, Integer> edge : vertex.getEdges()) {
                Vertex<String, Integer> v = edge.getTarget();
                double weight = edge.getWeight();
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
