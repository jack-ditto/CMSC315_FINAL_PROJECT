
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class KruskalsAlgorithm extends JPanel {

    SwingShell parent = null;
    LinkedList vertices = null;
    Color normalColor = Color.red;

    // TODO: define adjacency list (double check that it should be adjacency list) of edges

    //Added: highlightColor
    Color highlightColor = Color.yellow;

    public CanvasPanel(SwingShell _parent) {
	super();
	parent = _parent;
	vertices = parent.vertices;
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
