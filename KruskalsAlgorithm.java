
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class KruskalsAlgorithm extends JPanel {

    //For the visualization
    SwingShell parent = null;

    //Stores entire list of vertices
    LinkedList<Vertex> vertices = null;

    //Stores all edges of the graph
    LinkedList<Edge> edges = null;

    //Stores edges not in the MST
    PriorityQueue<Edge> pq = null;

    //Stores the minimum spanning treeEdges
    Tree mst = null;

    //Color for unhighlighted vertices
    Color normalColor = Color.red;

    //Color for highlighted vertices
    Color highlightColor = Color.yellow;

    int n = 0;

    int m = 0;

    public KruskalsAlgorithm(SwingShell _parent) {
	super();
	parent = _parent;
	vertices = parent.vertices;
  edges = parent.edges;

  for(Edge e : edges) {
    pq.add(e);
  }
    }

    //TODO: edit to work with vertex structure
    public void paintComponent(Graphics g) {
	super.paintComponent(g);

	g.setColor(normalColor);

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

    /**
    * Kruskal's Algorithm
    *
    * Input:
    */
    public Tree Kruskal(LinkedList<Vertex> points, PriorityQueue<Edge> pqEdges, Tree mst) {

      LinkedList<LinkedList<Vertex>> clusters = new LinkedList<LinkedList<Vertex>>();

      //Initialize clusters
      for(int i = 0; i < n; i++) {
        clusters.get(i).add(points.get(i));
      }

      while(mst.size() < n-1) {

        //Take the minimum edge
        Edge e = pqEdges.poll();
        Vertex v1 = e.getVertexOne();
        Vertex v2 = e.getVertexTwo();

        //Find their clusters
        int indexOne = find(clusters, v1);
        int indexTwo = find(clusters, v2);

        //Add it to the MST if they are not already in the same cluster
        if(indexOne != indexTwo) {
          mst.addEdge(e);
          clusters = union(clusters, indexOne, indexTwo);        }
      }

      return mst;
    }

    public LinkedList<LinkedList<Vertex>> union(LinkedList<LinkedList<Vertex>> c, int one, int two) {

    LinkedList<Vertex> temp = c.remove(two);
    c.get(one).addAll(temp);
    return c;

    }

    public int find(LinkedList<LinkedList<Vertex>> c, Vertex v) {
      for(int i = 0; i < n; i++) {
        if(clusters.get(i).contains(v)) {
          return i;
        }
      }
      //TODO: handle error here
      return -1;
    }

}
