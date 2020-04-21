
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class KruskalsAlgorithm extends JPanel {

  private static class Tree{
      /** Edge weight and endpoints */
      private LinkedList<Point> treeVertices;
      private LinkedList<Edge> treeEdges;

      /** Constructor*/
      public Edge(LinkedList<Point> vert, LinkedList<Edge> e) {
        treeVertices = vert;
        treeEdges = e;
      }

      public LinkedList<Point> getVertices() {
        return treeVertices;
      }
      public LinkedList<Edge> getEdges() {
        return treeEdges;
      }

      public void addVertex(Point p) {
        return treeVertices.add(p);
      }
      public void addEdge(Edge e) {
        return treeEdges.add(e);
      }

      public int size() {
        return treeEdges.size();
      }
      public boolean contains(Point p) {
        return treeVertices.contains(p);
      }
  }

    //For the visualization
    SwingShell parent = null;

    //Stores entire list of vertices
    LinkedList vertices = null;

    //Stores edges not in the MST
    PriorityQueue<Edge> edges = null;

    //Stores the minimum spanning treeEdges
    Tree mst = null;

    //Color for unhighlighted vertices
    Color normalColor = Color.red;

    //Color for highlighted vertices
    Color highlightColor = Color.yellow;

    public CanvasPanel(SwingShell _parent) {
	super();
	parent = _parent;
	vertices = parent.vertices;
  edges = parent.edges;
    }

    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	g.setColor(currentColor);

	ListIterator iterator = vertices.listIterator(0);

	Point currentVertex = null;

	for (int i=0; i < vertices.size(); ++i) {
	    currentVertex = (Point) iterator.next();
	    g.fillOval(currentVertex.x - parent.NODE_RADIUS,
		       currentVertex.y - parent.NODE_RADIUS,
		       2*parent.NODE_RADIUS, 2*parent.NODE_RADIUS);
	}
    }

    //TODO: This is where KruskalsAlgorithm methods will go

}
