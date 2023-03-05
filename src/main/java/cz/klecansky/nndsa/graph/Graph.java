package cz.klecansky.nndsa.graph;

import cz.klecansky.nndsa.utils.Triplet;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface Graph<Key extends Comparable<Key>, VValue, EValue> {
    void addVertex(Key key, VValue value);

    void addEdge(Key key, Key firstVertexKey, Key secondVertexKey, EValue value);

    List<VValue> getVerticesValue();

    List<EValue> getEdgeValue();

    VValue getVertexValue(Key vertexKey);

    List<Key> getVertexEdgeKeys(Key newValue);

    List<EValue> getVertexEdges(Key newValue);

    List<Triplet<Key, Key, EValue>> getDistinctDetailEdgeValues();

    EValue getEdgeValue(Key edgeKey);

    List<Triplet<Key, Key,EValue>> getDetailEdgeValues();

    void deleteEdge(Key edgeKey);

    void deleteVertex(Key vertexKey);

    void renameVertex(Key oldName, Key newName);

    void setEdge(Key oldKey, Key newKey, EValue eValue);

    VValue getVertexEdgeTarget(Key railSwitch, Key rail);

    class Edge<Key extends Comparable<Key>, VValue, EValue> implements Comparable<Edge<Key, VValue, EValue>> {
        private Key key;
        private EValue value;
        private final Vertex<Key, VValue, EValue> start;
        private final Vertex<Key, VValue, EValue> target;

        public Edge(Key key, Vertex<Key, VValue, EValue> start, Vertex<Key, VValue, EValue> target, EValue rail) {
            this.key = key;
            this.value = rail;
            this.start = start;
            this.target = target;
        }

        public EValue getValue() {
            return value;
        }

        public Vertex<Key, VValue, EValue> getStart() {
            return start;
        }

        public Vertex<Key, VValue, EValue> getTarget() {
            return target;
        }

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public void setValue(EValue value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return String.format("%s: %s-%s %s", key, start, target, value);
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Edge<?, ?, ?> edge = (Edge<?, ?, ?>) o;

            return Objects.equals(key, edge.key);
        }

        @Override
        public int hashCode() {
            int result = value.hashCode();
            result = 31 * result + start.hashCode();
            result = 31 * result + target.hashCode();
            return result;
        }

        @Override
        public int compareTo(Edge<Key, VValue, EValue> o) {
            return this.key.compareTo(o.key);
        }
    }

    class Vertex<Key extends Comparable<Key>, VValue, EValue> {

        private Key key;
        private final VValue value;

        private final List<Edge<Key, VValue, EValue>> edges;

        public Vertex(Key key, VValue value) {
            this.key = key;
            this.value = value;
            edges = new ArrayList<>();
        }

        public void addEdge(Edge<Key, VValue, EValue> edge) {
            if (edge == null) {
                throw new IllegalArgumentException("Edge is null.");
            }
            if (containsEdge(edge)) {
                throw new IllegalArgumentException(String.format("Edge is already in vertex: %s", edge));
            }
            edges.add(edge);
        }

        public void deleteEdge(Key other) {
            if (other == null) {
                throw new IllegalArgumentException("Other vertex key is null.");
            }
            edges.removeIf(edge -> edge.getTarget().getKey().equals(other));
        }

        public void deleteEdgeExact(Key edgeKey) {
            edges.removeIf(edge -> edge.getKey().equals(edgeKey));
        }

        public boolean containsEdge(Edge<Key, VValue, EValue> newEdge) {
            return this.edges.contains(newEdge);
        }

        public List<Edge<Key, VValue, EValue>> getEdges() {
            return edges;
        }

        public Key getKey() {
            return key;
        }

        public VValue getValue() {
            return value;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public boolean hasEdge(Key edgeKey) {
            return edges.stream().anyMatch(edge -> edge.getKey().equals(edgeKey));
        }

        @Override
        public String toString() {
            return key.toString();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Vertex<?, ?, ?> vertex = (Vertex<?, ?, ?>) o;

            return key.equals(vertex.key);
        }

        @Override
        public int hashCode() {
            return key.hashCode();
        }
    }
}
