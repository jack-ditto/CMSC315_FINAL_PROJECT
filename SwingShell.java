
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class SwingShell extends JFrame
    implements ActionListener, MouseListener {

    // The radius in pixels of the circles drawn in graph_panel

    final int NODE_RADIUS = 3;

    // GUI stuff
    CanvasPanel canvas = null;

    JPanel buttonPanel = null;
    JButton drawButton, clearButton;

    // Data Structures for the Points

    /*This holds the set of vertices, all
     * represented as type Point.
     */
    LinkedList<Point> vertices = null;

    //TODO: adjacency list of edges
    //TODO: state variable (string??)

    // Event handling stuff
    Dimension panelDim = null;

    public SwingShell() {
	super("Generic Swing App");
	setSize(new Dimension(700,575));

	// Initialize main data structures
	initializeDataStructures();

	//The content pane
	Container contentPane = getContentPane();
	contentPane.setLayout(new BoxLayout(contentPane,
					    BoxLayout.Y_AXIS));

	//Create the drawing area
	canvas = new CanvasPanel(this);
	canvas.addMouseListener(this);

	Dimension canvasSize = new Dimension(700,500);
	canvas.setMinimumSize(canvasSize);
	canvas.setPreferredSize(canvasSize);
	canvas.setMaximumSize(canvasSize);
	canvas.setBackground(Color.black);

	// Create buttonPanel and fill it
	buttonPanel = new JPanel();
	Dimension panelSize = new Dimension(700,75);
	buttonPanel.setMinimumSize(panelSize);
	buttonPanel.setPreferredSize(panelSize);
	buttonPanel.setMaximumSize(panelSize);
	buttonPanel.setLayout(new BoxLayout(buttonPanel,
					    BoxLayout.X_AXIS));
	buttonPanel.
	    setBorder(BorderFactory.
		      createCompoundBorder(BorderFactory.
					   createLineBorder(Color.red),
					   buttonPanel.getBorder()));

  //TODO: add all appropriate buttons, may need to adjust size
  //TODO: update state maybe according to action command

	Dimension buttonSize = new Dimension(175,50);
	drawButton = new JButton("Toggle Color");
	drawButton.setMinimumSize(buttonSize);
	drawButton.setPreferredSize(buttonSize);
	drawButton.setMaximumSize(buttonSize);
	drawButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	drawButton.setActionCommand("toggleColor");
	drawButton.addActionListener(this);
	drawButton.
	    setBorder(BorderFactory.
		      createCompoundBorder(BorderFactory.
					   createLineBorder(Color.green),
					   drawButton.getBorder()));

	clearButton = new JButton("Clear");
	clearButton.setMinimumSize(buttonSize);
	clearButton.setPreferredSize(buttonSize);
	clearButton.setMaximumSize(buttonSize);
	clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
	clearButton.setActionCommand("clearDiagram");
	clearButton.addActionListener(this);
	clearButton.
	    setBorder(BorderFactory.
		      createCompoundBorder(BorderFactory.
					   createLineBorder(Color.blue),
					   clearButton.getBorder()));

	buttonPanel.add(Box.createHorizontalGlue());
	buttonPanel.add(drawButton);
	buttonPanel.add(Box.createHorizontalGlue());
	buttonPanel.add(clearButton);
	buttonPanel.add(Box.createHorizontalGlue());

	contentPane.add(canvas);
	contentPane.add(buttonPanel);
    }

    public static void main(String[] args) {

	SwingShell project = new SwingShell();
	project.addWindowListener(new WindowAdapter() {
		public void
		    windowClosing(WindowEvent e) {
		    System.exit(0);
		}
	    }
				  );
	project.pack();
	project.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

	String buttonIdentifier = e.getActionCommand();

  //TODO: update with other states (i.e. add edge, add vertex, compute MST...)

  if (buttonIdentifier.equals("toggleColor")) {
	    // toggle the color
	    canvas.changeColor();
	    canvas.repaint();
	} else if (buttonIdentifier.equals("clearDiagram")) {
	    vertices.clear();
	    canvas.repaint();
	}
    }

    public void mouseClicked(MouseEvent e) {

      //TODO: cases based on state

	Point click_point = e.getPoint();
	vertices.add(click_point);
	canvas.repaint();
    }

    public void initializeDataStructures() {

      //TODO: initialize any new data structures

	vertices = new LinkedList<Point>();
    }

    public void mouseExited(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mousePressed(MouseEvent e) {}

      //TODO: this will be how we get the edge to follow the mouse
    public void mouseDragged(MouseEvent e) {}
}
