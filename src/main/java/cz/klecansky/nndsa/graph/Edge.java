package cz.klecansky.nndsa.graph;

public class Edge<K, V> {
    private final double weight;

    private final Vertex<K, V> first;
    private final Vertex<K, V> second;

    public Edge(Vertex<K, V> first, Vertex<K, V> second, double weight) {
        this.weight = weight;
        this.first = first;
        this.second = second;
    }

    public double getWeight() {
        return weight;
    }

    public Vertex<K, V> getFirst() {
        return first;
    }

    public Vertex<K, V> getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return String.format("%s-%s %.5f", first, second, weight);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Edge<?, ?> edge = (Edge<?, ?>) o;

        if (Double.compare(edge.weight, weight) != 0) return false;
        return (this.first.equals(edge.first) && this.second.equals(edge.second)) || (this.first.equals(edge.second) && this.second.equals(edge.first));
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(weight);
        result = (int) (temp ^ (temp >>> 32));
        result = 31 * result + first.hashCode();
        result = 31 * result + second.hashCode();
        return result;
    }
}
