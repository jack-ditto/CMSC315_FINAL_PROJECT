
import java.awt.*;
import java.awt.font.*;
import javax.swing.*;
import java.util.*;
import java.awt.geom.*;

public class CanvasPanel extends JPanel {

	SwingShell parent = null; // Reference to SwingShell
	LinkedList vertices = null; // Vertices in SwingShell
	LinkedList edges = null; // Edges in Swingshell

	// Colors
	Color vertexColor = Color.blue;
	Color edgeColor = Color.green;
	Color highlightColor = Color.orange;
	Color deleteColor = Color.red;
	Color textColor = Color.white;

	Vertex highlightVertex = null; // Vertex to be highlighted
	Edge highlightEdge = null; // Edge to be highlighted
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

		Font font = new Font("Monotype Corsiva", Font.PLAIN, 20);
		g2.setFont(font);
		FontRenderContext fontRenderContext = g2.getFontRenderContext();

		for (int i = 0; i < edges.size(); ++i) {
			currentEdge = (Edge) iterator2.next();

			double changeInY = (currentEdge.getEdgeShape().getY2() - currentEdge.getEdgeShape().getY1());
			double changeInX = (currentEdge.getEdgeShape().getX2() - currentEdge.getEdgeShape().getX1());

			double slope = changeInY / changeInX;
			// double offset = currentEdge.getEdgeShape().getY1() - (slope *
			// currentEdge.getEdgeShape().getX1());

			System.out.println(slope);

			// Angle text same as line
			AffineTransform affineTransform = new AffineTransform();
			affineTransform.rotate(Math.atan(slope));
			Font rotatedFont = font.deriveFont(affineTransform);
			g2.setFont(rotatedFont);

			// Get midpoint of line
			float x = (float) ((currentEdge.getEdgeShape().getX1() + currentEdge.getEdgeShape().getX2()) / 2);
			float y = (float) ((currentEdge.getEdgeShape().getY1() + currentEdge.getEdgeShape().getY2()) / 2);

			g2.setColor(this.textColor);
			if (slope <= 0) {
				g2.drawString(String.valueOf(currentEdge.getWeight()), x, y - 15);
			} else {
				g2.drawString(String.valueOf(currentEdge.getWeight()), x, y + 10);
			}
			g2.setColor(this.edgeColor);

			if (currentEdge == this.highlightEdge && this.deleteState) {
				g2.setColor(this.deleteColor);
				g2.draw(currentEdge.getEdgeShape());
				g2.fill(currentEdge.getEdgeShape());
				g2.setColor(this.edgeColor);
			} else if (currentEdge == this.highlightEdge) {
				g2.setColor(this.highlightColor);
				g2.draw(currentEdge.getEdgeShape());
				g2.fill(currentEdge.getEdgeShape());
				g2.setColor(this.edgeColor);
			} else {
				g2.setColor(this.edgeColor);
				g2.draw(currentEdge.getEdgeShape());
				g2.fill(currentEdge.getEdgeShape());
			}

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
