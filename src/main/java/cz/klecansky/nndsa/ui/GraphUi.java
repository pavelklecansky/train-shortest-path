package cz.klecansky.nndsa.ui;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import cz.klecansky.nndsa.graph.Edge;

public class GraphUi {
    public static SmartGraphPanel<String, Weight> getGraphUi(cz.klecansky.nndsa.graph.Graph<String, Integer> graph) {
        Graph<String, Weight> distances = new GraphEdgeList<>();

        for (String vertex : graph.getVerticesKey()) {
            distances.insertVertex(vertex);
        }

        for (Edge<String, Integer> edge : graph.getUndirectedEdges()) {
            distances.insertEdge(edge.getTarget().getKey(), edge.getStart().getKey(), new Weight(edge.getWeight()));
        }

        /* Only Java 15 allows for multi-line strings */
        String customProps = "edge.label = true" + "\n" + "edge.arrow = false";

        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        SmartGraphPanel<String, Weight> cityDistanceSmartGraphPanel = new SmartGraphPanel<>(distances, properties, new SmartCircularSortedPlacementStrategy());
        cityDistanceSmartGraphPanel.setMinWidth(400);
        cityDistanceSmartGraphPanel.setMinHeight(400);
        cityDistanceSmartGraphPanel.prefWidth(1300);
        cityDistanceSmartGraphPanel.prefHeight(1000);

        return cityDistanceSmartGraphPanel;
    }

    public static SmartGraphPanel<String, Weight> getEmptyGraphUi() {
        Graph<String, Weight> distances = new GraphEdgeList<>();

        /* Only Java 15 allows for multi-line strings */
        String customProps = "edge.label = true" + "\n" + "edge.arrow = false";

        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        SmartGraphPanel<String, Weight> cityDistanceSmartGraphPanel = new SmartGraphPanel<>(distances, properties, new SmartCircularSortedPlacementStrategy());
        cityDistanceSmartGraphPanel.setMinWidth(400);
        cityDistanceSmartGraphPanel.setMinHeight(400);
        cityDistanceSmartGraphPanel.prefWidth(1300);
        cityDistanceSmartGraphPanel.prefHeight(1000);

        return cityDistanceSmartGraphPanel;
    }
}
