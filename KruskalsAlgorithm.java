
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class KruskalsAlgorithm extends JPanel {

    SwingShell parent = null;

    //List of vertices
    LinkedList<Vertex> vertices = null;

    //All edges of the graph
    LinkedList<Edge> edges = null;

    //Stores edges not in the MST
    PriorityQueue<Edge> pq = null;

    public KruskalsAlgorithm(SwingShell _parent) {
	     super();
	      parent = _parent;
	       vertices = parent.vertices;
         edges = parent.edges;

         for(Edge e : edges) {
           pq.add(e);
         }
    }

    /**
    * Kruskal's Algorithm - computes the minimum spanning tree of a graph
    *
    * Input: A set of vertices, a set of edges
    * Output: A minimum spannining tree
    */
    public Tree Kruskal(LinkedList<Vertex> points, PriorityQueue<Edge> pqEdges) {

      //Stores the minimum spanning treeEdges
      Tree mst = null;

      //Stores the temporary clusters
      LinkedList<LinkedList<Vertex>> clusters = new LinkedList<LinkedList<Vertex>>();

      int n = points.size();

      //Initialize clusters
      for(int i = 0; i < n; i++) {
        clusters.get(i).add(points.get(i));
      }

      //Add appropriate edges to the MouseMotionListener
      while(mst.size() < n-1) {

        //Take the minimum edge
        Edge e = pqEdges.poll();
        Vertex v1 = e.getVertexOne();
        Vertex v2 = e.getVertexTwo();

        //Find their clusters
        int indexOne = find(clusters, v1);
        int indexTwo = find(clusters, v2);

        if (indexOne == -1 || indexTwo == -1) {
          System.out.println("Vertex not found in clusters ??? (Error)")
          throw new ArrayOutOfBoundsException("Vertex not found in clusters");
        }

        //Add it to the MST if they are not already in the same cluster
        if(indexOne != indexTwo) {
          mst.addEdge(e);
          clusters = union(clusters, indexOne, indexTwo);
        }
      }

      return mst;
    }

    /* Union - merges two clusters and deletes one of the two
    *
    * Input: The vertex clusters and the indices of the two clusters to be merged
    * Output: The vertex clusters after merging
    */
    public LinkedList<LinkedList<Vertex>> union(LinkedList<LinkedList<Vertex>> c, int one, int two) {

    LinkedList<Vertex> temp = c.remove(two);
    c.get(one).addAll(temp);
    return c;

    }

    /* Find - finds the index of the cluster of a given vertex
    *
    * Input: The vertex clusters and the vertex to find (or -1 if it is not in any cluster)
    * Output: The index of the vertex cluster
    */
    public int find(LinkedList<LinkedList<Vertex>> c, Vertex v) {
      for(int i = 0; i < n; i++) {
        if(c.get(i).contains(v)) {
          return i;
        }
      }
      //Should never happen -- error
      return -1;
    }

}
