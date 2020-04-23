
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class KruskalsAlgorithm extends JPanel {

  private static class Tree {
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

      //TODO: probably dont need
      public void addVertex(Point p) {
         treeVertices.add(p);
      }

      public void addEdge(Edge e) {
        treeEdges.add(e);

        if(!treeVertices.contains(e.getOne())) {
          treeVertices.add(e.getOne());
        }

        if(!treeVertices.contains(e.getTwo())) {
          treeVertices.add(e.getTwo());
        }
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
    LinkedList<Point> vertices = null;

    //Stores edges not in the MST
    PriorityQueue<Edge> edges = null;

    //Stores the minimum spanning treeEdges
    Tree mst = null;

    //Color for unhighlighted vertices
    Color normalColor = Color.red;

    //Color for highlighted vertices
    Color highlightColor = Color.yellow;

    int n = 0;

    int m = 0;

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

  n = vertices.size();
  m = edges.size();
    }

    public Tree Kruskal(LinkedList<Point> points, PriorityQueue<Edge> pqEdges, Tree mst) {

      LinkedList<LinkedList<Point>> clusters = new LinkedList<LinkedList<Point>>();

      //Initialize clusters
      for(int i = 0; i < n; i++) {
        clusters.get(i).add(points.get(i));
      }

      while(mst.size() < n-1) {
        Edge e = pqEdges.poll();
        Point p1 = e.getOne();
        Point p2 = e.getTwo();

        int indexOne = find(clusters, p1);
        int indexTwo = find(clusters, p2);

        if(indexOne != indexTwo) {
          mst.addEdge(e);
          clusters = merge(clusters, indexOne, indexTwo);
        }
      }

      return mst;
    }

    public LinkedList<LinkedList<Point>> union(LinkedList<LinkedList<Point>> c, int one, int two) {

    LinkedList<Point> temp = c.get(two).remove();
    c.addAll(one, temp);
    return c;

    }

    public int find(LinkedList<LinkedList<Point>> c, Point p) {
      for(int i = 0; i < n; i++) {
        if(clusters.get(i).contains(p)) {
          return i;
        }
      }
      //TODO: handle error here
      return -1;
    }

}
