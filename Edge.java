import java.awt.*;
import java.awt.geom.*;

public class Edge implements Comparable<Edge> {
    /** Edge weight and endpoints */
    private double weight;
    private Vertex vertexOne;
    private Vertex vertexTwo;
    private Line2D.Double edgeShape;

    /** Constructor */
    public Edge(Line2D.Double edgeShape, Vertex first, Vertex second) {
        this.edgeShape = edgeShape;
        this.weight = 1;
        this.vertexOne = first;
        this.vertexTwo = second;
    }

    // Get the weight of the Edge
    public double getWeight() {
        return weight;
    }

    // Get the center of vertex one
    public Vertex getVertexOne() {
        return this.vertexOne;
    }

    // Get the center of vertex two
    public Vertex getVertexTwo() {
        return this.vertexTwo;
    }

    public Line2D.Double getEdgeShape() {
        return this.edgeShape;
    }

    // Set the weight of the Edge
    public void setWeight(double w) {
        weight = w;
    }

    public int compareTo(Edge other) {
        if (this.weight > other.weight) {
            return 1;
        } else if (this.weight == other.weight) {
            return 0;
        }
        return -1;
    }
}
