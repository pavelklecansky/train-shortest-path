package cz.klecansky.nndsa.io;

import cz.klecansky.nndsa.graph.Edge;
import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.utils.Utils;

import java.io.*;

public class ExporterCsv {

    // TODO ještě se rozhodnout jak to bude s tím exportem hran(edges)
    public void exportGraph(File file, Graph<String, Integer> graph) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("File is null.");
        }
        if (graph == null) {
            throw new IllegalArgumentException("Graph is null.");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String vertexKey : graph.getVerticesKey()) {
            stringBuilder.append(vertexKey).append("\n");
        }
        stringBuilder.append(Utils.VERTICES_EDGES_DIVIDER).append("\n");
        for (String edge : graph.getEdges().stream().map(Edge::toString).toList()) {
            stringBuilder.append(edge).append("\n");
        }
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(stringBuilder.toString());
        }
    }
}
