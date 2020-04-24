
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;
import java.lang.*;

public class KruskalsAlgorithm extends JPanel {

  SwingShell parent = null;

  // List of vertices
  LinkedList<Vertex> vertices = null;

  // All edges of the graph
  LinkedList<Edge> edges = null;

  // Stores edges not in the MST
  PriorityQueue<Edge> pq = null;

  // The minimum spanning tree
  Tree mst = null;

  // To store all the clusters
  LinkedList<LinkedList<Vertex>> clusters = null;

  // The number of vertices
  int n = 0;


  public KruskalsAlgorithm(LinkedList<Vertex> v, LinkedList<Edge> e) {
    vertices = v;
    edges = e;

    pq = new PriorityQueue<Edge>();
    for (Edge ed : edges) {
      pq.add(ed);
    }

    n = vertices.size();
    mst = new Tree();
    clusters = new LinkedList<LinkedList<Vertex>>();

    // Initialize clusters
    for (int i = 0; i < n; i++) {
      LinkedList<Vertex> temporary = new LinkedList<Vertex>();
      temporary.add(vertices.get(i));
      clusters.add(i,temporary);
    }

  }

  public Tree run() {
    return Kruskal();
  }

  /**
   * Kruskal's Algorithm - computes the minimum spanning tree of a graph
   *
   * Input: A set of vertices, a set of edges
   * Output: A minimum spannining tree
   */
  public Tree Kruskal() {

    // Add appropriate edges to the MST
    while (mst.size() < n - 1 && pq.size() > 0) {

      // Take the minimum edge
      Edge e = pq.poll();
      Vertex v1 = e.getVertexOne();
      Vertex v2 = e.getVertexTwo();

      // Find their clusters
      int indexOne = find(v1);
      int indexTwo = find(v2);

      if (indexOne == -1 || indexTwo == -1) {
        throw new ArrayIndexOutOfBoundsException("Vertex not found in clusters");
      }

      // Add it to the MST if they are not already in the same cluster
      if (indexOne != indexTwo) {
        mst.addEdge(e);
        union(indexOne, indexTwo);
      }
    }

    return mst;
  }

  /*
   * Union - merges two clusters and deletes one of the two
   *
   * Input: The vertex clusters and the indices of the two clusters to be merged
   * Output: The vertex clusters after merging
   */
  public void union(int one, int two) {
    if(one < two){
      LinkedList<Vertex> temp = clusters.remove(two);
      clusters.get(one).addAll(temp);
    }

    else {
      LinkedList<Vertex> temp = clusters.remove(one);
      clusters.get(two).addAll(temp);
    }

  }

  /*
   * Find - finds the index of the cluster of a given vertex
   *
   * Input: The vertex clusters and the vertex to find (or -1 if it is not in any
   * cluster)
   * Output: The index of the vertex cluster
   */
  public int find(Vertex v) {
    for (int i = 0; i < clusters.size(); i++) {
      if (clusters.get(i).contains(v)) {
        return i;
      }
    }
    // Should never happen -- error
    return -1;
  }

}
