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
    public Point getOne() {
        int x = (int) this.vertexOne.getVertexShape().getCenterX();
        int y = (int) this.vertexOne.getVertexShape().getCenterY();
        return new Point(x, y);
    }

    // Get the center of vertex two
    public Point getTwo() {
        int x = (int) this.vertexTwo.getVertexShape().getCenterX();
        int y = (int) this.vertexTwo.getVertexShape().getCenterY();
        return new Point(x, y);
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