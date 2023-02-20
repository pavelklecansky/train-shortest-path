package cz.klecansky.nndsa.graph;

public class Edge<K, V> implements Comparable<Edge<K, V>> {
    private final double weight;
    private final Vertex<K, V> start;
    private final Vertex<K, V> target;

    public Edge(Vertex<K, V> start, Vertex<K, V> target, double weight) {
        this.weight = weight;
        this.start = start;
        this.target = target;
    }

    public double getWeight() {
        return weight;
    }

    public Vertex<K, V> getStart() {
        return start;
    }

    public Vertex<K, V> getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return String.format("%s-%s %.5f", start, target, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?, ?> edge = (Edge<?, ?>) o;

        if (Double.compare(edge.weight, weight) != 0) return false;
        return (this.start.equals(edge.start) && this.target.equals(edge.target)) || (this.start.equals(edge.target) && this.target.equals(edge.start));
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(weight);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + start.hashCode();
        result = 31 * result + target.hashCode();
        return result;
    }

    @Override
    public int compareTo(Edge other) {
        if (other == null) {
            return 1;
        } else {
            return Double.compare(other.weight, this.weight);
        }
    }
}
