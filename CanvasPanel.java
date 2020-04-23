
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class CanvasPanel extends JPanel {

	SwingShell parent = null; // Reference to SwingShell
	LinkedList vertices = null; // Vertices in SwingShell
	LinkedList edges = null; // Edges in Swingshell

	// Colors
	Color vertexColor = Color.blue;
	Color edgeColor = Color.green;
	Color highlightColor = Color.orange;
	Color deleteColor = Color.red;

	Vertex highlightVertex = null; // Vertex to be highlighted
	boolean activeLine = false; // Should draw line from vertexOne to pointer
	boolean deleteState = false;
	Point mousePos = null; // Mouse position
	Vertex vertexOne = null; // Origin of activeLine

	public CanvasPanel(SwingShell _parent) {
		super();
		parent = _parent;
		vertices = parent.vertices;
		edges = parent.edges;

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;

		ListIterator iterator = vertices.listIterator(0);
		ListIterator iterator2 = edges.listIterator(0);

		g2.setColor(this.edgeColor);

		// Draw edges
		Edge currentEdge = null;

		for (int i = 0; i < edges.size(); ++i) {
			currentEdge = (Edge) iterator2.next();
			g2.draw(currentEdge.getEdgeShape());
		}

		if (activeLine) {
			g2.drawLine((int) vertexOne.getVertexShape().getCenterX(), (int) vertexOne.getVertexShape().getCenterY(),
					(int) mousePos.getX(), (int) mousePos.getY());
		}

		g2.setColor(this.vertexColor);

		// Draw vertices
		Vertex currentVertex = null;

		for (int i = 0; i < vertices.size(); ++i) {
			currentVertex = (Vertex) iterator.next();

			if (currentVertex == this.highlightVertex && this.deleteState) {
				g2.setColor(this.deleteColor);
				g2.draw(currentVertex.getVertexShape());
				g2.fill(currentVertex.getVertexShape());
				g2.setColor(this.vertexColor);
			} else if (currentVertex == this.highlightVertex) {
				g2.setColor(this.highlightColor);
				g2.draw(currentVertex.getVertexShape());
				g2.fill(currentVertex.getVertexShape());
				g2.setColor(this.vertexColor);
			} else {
				g2.setColor(this.vertexColor);
				g2.draw(currentVertex.getVertexShape());
				g2.fill(currentVertex.getVertexShape());
			}

		}
	}

}
