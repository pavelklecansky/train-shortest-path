package cz.klecansky.nndsa.io;

import cz.klecansky.nndsa.graph.EdgeWeightedGraph;
import cz.klecansky.nndsa.graph.Graph;
import cz.klecansky.nndsa.graph.Vertex;
import cz.klecansky.nndsa.utils.Utils;

import java.io.*;

public class ImporterCsv {

    public Graph<String, Integer> importGraph(File file) throws IOException {
        if (file == null || !file.exists()) {
            throw new IllegalArgumentException("File is null or not existing.");
        }
        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
        Graph<String, Integer> graph = new EdgeWeightedGraph<>();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            if (line.equals(Utils.VERTICES_EDGES_DIVIDER)) {
                break;
            }
            graph.addVertex(new Vertex<>(line.trim(), 122));
        }
        while ((line = bufferedReader.readLine()) != null) {
            String[] split = line.split(",");
            graph.addEdge(split[0], split[1], Double.parseDouble(split[2]));
        }

        return graph;
    }
}
