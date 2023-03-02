package cz.klecansky.nndsa.ui;

import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartGraphProperties;
import cz.klecansky.nndsa.rail.Rail;
import cz.klecansky.nndsa.rail.RailSwitch;
import cz.klecansky.nndsa.rail.RailwayInfrastructure;
import cz.klecansky.nndsa.utils.Triplet;

public class GraphUi {
    public static SmartGraphPanel<String, Weight> getGraphUi(RailwayInfrastructure railwayInfrastructure) {
        Graph<String, Weight> distances = new GraphEdgeList<>();

        for (RailSwitch railSwitch : railwayInfrastructure.getSwitches()) {
            distances.insertVertex(railSwitch.getName());
        }

        for (Triplet<String, String, Rail> triplet : railwayInfrastructure.getDistinctRailsDetailInfo()) {
            distances.insertEdge(triplet.getFirst(), triplet.getSecond(), new Weight(triplet.getThird().getLength()));
        }

        /* Only Java 15 allows for multi-line strings */
        String customProps = "edge.label = true" + "\n" + "edge.arrow = false";

        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        SmartGraphPanel<String, Weight> railwayPanel = new SmartGraphPanel<>(distances, properties, new SmartCircularSortedPlacementStrategy());
        railwayPanel.setMinWidth(400);
        railwayPanel.setMinHeight(400);
        railwayPanel.prefWidth(1300);
        railwayPanel.prefHeight(1000);

        return railwayPanel;
    }

    public static SmartGraphPanel<String, Weight> getEmptyGraphUi() {
        Graph<String, Weight> distances = new GraphEdgeList<>();

        /* Only Java 15 allows for multi-line strings */
        String customProps = "edge.label = true" + "\n" + "edge.arrow = false";

        SmartGraphProperties properties = new SmartGraphProperties(customProps);
        SmartGraphPanel<String, Weight> railwayPanel = new SmartGraphPanel<>(distances, properties, new SmartCircularSortedPlacementStrategy());
        railwayPanel.setMinWidth(400);
        railwayPanel.setMinHeight(400);
        railwayPanel.prefWidth(1300);
        railwayPanel.prefHeight(1000);

        return railwayPanel;
    }
}
