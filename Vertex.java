import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class Vertex {

    private Ellipse2D.Double vertexShape;
    private LinkedList<Edge> edges;

    public Vertex(Ellipse2D.Double vertexShape) {
        this.vertexShape = vertexShape;
        this.edges = new LinkedList<Edge>();
    }

    public void addEdge(Edge e) {
        this.edges.add(e);
    }

    public Ellipse2D.Double getVertexShape() {
        return this.vertexShape;
    }

    public LinkedList<Edge> getEdges() {
        return this.edges;
    }
}