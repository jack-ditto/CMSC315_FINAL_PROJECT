import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class Tree {
    /** Edge weight and endpoints */
    private LinkedList<Vertex> treeVertices;
    private LinkedList<Edge> treeEdges;

    /** Constructor*/
    public Tree(LinkedList<Vertex> vert, LinkedList<Edge> e) {
      treeVertices = vert;
      treeEdges = e;
    }

    //Getters
    public LinkedList<Vertex> getVertices() {
      return treeVertices;
    }
    public LinkedList<Edge> getEdges() {
      return treeEdges;
    }

    /*public void addVertex(Vertex v) {
       treeVertices.add(v);
    }*/

    //Add an edge and incident vertices (if necessary)
    public void addEdge(Edge e) {
      treeEdges.add(e);

      if(!treeVertices.contains(e.getVertexOne())) {
        treeVertices.add(e.getVertexOne());
      }

      if(!treeVertices.contains(e.getVertexTwo())) {
        treeVertices.add(e.getVertexTwo());
      }
    }

    public int size() {
      return treeEdges.size();
    }
    public boolean contains(Vertex v) {
      return treeVertices.contains(v);
    }
}
