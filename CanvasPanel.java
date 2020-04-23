
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class CanvasPanel extends JPanel {

	SwingShell parent = null;
	LinkedList vertices = null;
	LinkedList edges = null;
	Color currentColor = Color.blue;
	Ellipse2D.Double highlightVertex = null;

	boolean activeLine = false;
	Point mousePos = null;
	Ellipse2D.Double vertexOne = null;

	public CanvasPanel(SwingShell _parent) {
		super();
		parent = _parent;
		vertices = parent.vertices;
		edges = parent.edges;

	}

	public void paintComponent(Graphics g) {

		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(currentColor);

		ListIterator iterator = vertices.listIterator(0);
		ListIterator iterator2 = edges.listIterator(0);

		g2.setColor(Color.green);
		Line2D.Double currentEdge = null;

		for (int i = 0; i < edges.size(); ++i) {
			currentEdge = (Line2D.Double) iterator2.next();
			g2.draw(currentEdge);
		}

		if (activeLine) {
			g2.drawLine((int) vertexOne.getCenterX(), (int) vertexOne.getCenterY(), (int) mousePos.getX(),
					(int) mousePos.getY());
		}
		g2.setColor(currentColor);

		Ellipse2D.Double currentVertex = null;

		for (int i = 0; i < vertices.size(); ++i) {
			currentVertex = (Ellipse2D.Double) iterator.next();

			if (currentVertex == this.highlightVertex) {
				g2.setColor(Color.yellow);
				g2.draw(currentVertex);
				g2.fill(currentVertex);
				g2.setColor(currentColor);
			} else {
				g2.draw(currentVertex);
				g2.fill(currentVertex);
			}

		}

		// g.setColor(Color.GREEN); // Line colors green

		// // Link all points in the pointsOnHull linkedlist
		// for (int j = 0; j < pointsOnHull.size(); j++) {
		// Point p1 = (Point) pointsOnHull.get(j);
		// Point p2 = (Point) pointsOnHull.get((j + 1) % pointsOnHull.size());
		// int x1 = (int) p1.getX();
		// int y1 = (int) p1.getY();
		// int x2 = (int) p2.getX();
		// int y2 = (int) p2.getY();

		// g.drawLine(x1, y1, x2, y2);
		// }
	}

}
