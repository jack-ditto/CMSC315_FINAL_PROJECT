
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;
import java.util.*;

public class SwingShell extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

	// The radius in pixels of the circles drawn in graph_panel
	final int NODE_RADIUS = 10;
	// References the canvas object where actual drawing will take place
	CanvasPanel canvas = null;

	JPanel buttonPanel = null;

	JButton addVertexButton, addEdgeButton, deleteButton, clearButton;

	// Data Structures for the Points

	/*
	 * This holds the set of vertices, all represented as type Point.
	 */
	LinkedList<Vertex> vertices = null;

	// This holds the set of all edges
	// PriorityQueue<Edge> edges = null;
	LinkedList<Edge> edges = null;

	// Implement a FSA using an enum w/ methods
	public enum State {

		/**
		 * INITIAL state
		 * 
		 * The user can click any of the buttons to enter a state. Allows for movement
		 * into ADD_VERTEX state or ADD_EDGE_1 state. Otherwise, self loop into the same
		 * state.
		 */
		INITIAL {
			@Override
			State handleAction(String actionIdentifier) {
				if (actionIdentifier.equals("addVertex")) {
					System.out.println("In ADD_VERTEX state");
					return ADD_VERTEX;
				} else if (actionIdentifier.equals("addEdge")) {
					System.out.println("In ADD_EDGE_1 state");
					return ADD_EDGE_1;
				} else if (actionIdentifier.equals("delete")) {
					System.out.println("In DELETE state");
					return DELETE;
				}

				return this;
			}

		},

		/**
		 * ADD_VERTEX state
		 * 
		 * The user can add vertices to the canvas. Alls only for movement into
		 * ADD_EDGE_1 state. Otherwise self loop
		 */
		ADD_VERTEX {
			@Override
			State handleAction(String actionIdentifier) {
				if (actionIdentifier.equals("addEdge")) {
					System.out.println("In ADD_EDGE_1 state");
					return ADD_EDGE_1;
				}

				return this;
			}

		},
		/**
		 * ADD_EDGE_1 state
		 * 
		 * The user can select a node to attach an edge to. Allows for movement into
		 * ADD_EDGE_2 state once the verex is selected.
		 */
		ADD_EDGE_1 {
			@Override
			State handleAction(String actionIdentifier) {

				if (actionIdentifier.equals("mouseClicked")) {
					System.out.println("In ADD_EDGE_2 state");
					return ADD_EDGE_2;
				}
				return this;
			}

		},

		/**
		 * ADD_EDGE_2 state
		 * 
		 * The user can select a second vertex to connect the active edge to. Allows for
		 * return to INITIAL state after selecting.
		 */
		ADD_EDGE_2 {
			@Override
			State handleAction(String actionIdentifier) {
				if (actionIdentifier.equals("connectEdge")) {
					System.out.println("In INITIAL state");
					return INITIAL;
				}
				return this;
			}

		},

		// TODO: Delete nodes or edges
		DELETE

		{
			@Override
			State handleAction(String actionIdentifier) {
				if (actionIdentifier.equals("mouseClicked")) {
					System.out.println("In INITIAL state");
					return INITIAL;
				}
				return INITIAL;
			}

		},
		// TODO: change edge weights
		CHANGE_EDGE_WEIGHT {
			@Override
			State handleAction(String actionIdentifier) {
				return this;
			}

		};

		abstract State handleAction(String actionIdentifier);

	}

	// Store the current state
	private State state;

	// Event handling stuff
	Dimension panelDim = null;

	public SwingShell() {
		super("Kruskal's Algorithm");
		setSize(new Dimension(700, 575));

		// Initialize main data structures
		initializeDataStructures();

		// The content pane
		Container contentPane = getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		// Create the drawing area
		canvas = new CanvasPanel(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);

		// Define canvas
		Dimension canvasSize = new Dimension(700, 500);
		canvas.setMinimumSize(canvasSize);
		canvas.setPreferredSize(canvasSize);
		canvas.setMaximumSize(canvasSize);
		canvas.setBackground(Color.black);

		// Create button panel
		buttonPanel = new JPanel();
		Dimension panelSize = new Dimension(700, 75);
		buttonPanel.setMinimumSize(panelSize);
		buttonPanel.setPreferredSize(panelSize);
		buttonPanel.setMaximumSize(panelSize);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				buttonPanel.getBorder()));

		// Set button dimensions
		Dimension buttonSize = new Dimension(150, 50);

		// Add vertex button
		addVertexButton = new JButton("Add Vertex");
		addVertexButton.setMinimumSize(buttonSize);
		addVertexButton.setPreferredSize(buttonSize);
		addVertexButton.setMaximumSize(buttonSize);
		addVertexButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addVertexButton.setActionCommand("addVertex");
		addVertexButton.addActionListener(this);
		addVertexButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				addVertexButton.getBorder()));

		// Add edge button
		addEdgeButton = new JButton("Add Edge");
		addEdgeButton.setMinimumSize(buttonSize);
		addEdgeButton.setPreferredSize(buttonSize);
		addEdgeButton.setMaximumSize(buttonSize);
		addEdgeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		addEdgeButton.setActionCommand("addEdge");
		addEdgeButton.addActionListener(this);
		addEdgeButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				addEdgeButton.getBorder()));

		// Delete button (for both vertex and edges)
		deleteButton = new JButton("Delete");
		deleteButton.setMinimumSize(buttonSize);
		deleteButton.setPreferredSize(buttonSize);
		deleteButton.setMaximumSize(buttonSize);
		deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(this);
		deleteButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				deleteButton.getBorder()));

		// Clear button
		clearButton = new JButton("Clear");
		clearButton.setMinimumSize(buttonSize);
		clearButton.setPreferredSize(buttonSize);
		clearButton.setMaximumSize(buttonSize);
		clearButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		clearButton.setActionCommand("clear");
		clearButton.addActionListener(this);
		clearButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				clearButton.getBorder()));

		// Add buttons to panel
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(addVertexButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(addEdgeButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(deleteButton);
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(clearButton);
		buttonPanel.add(Box.createHorizontalGlue());

		// Add everything
		contentPane.add(canvas);
		contentPane.add(buttonPanel);

		// Set state to initial on constuction
		this.state = State.INITIAL;
	}

	public static void main(String[] args) {

		SwingShell project = new SwingShell();
		project.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		project.pack();
		project.setVisible(true);
	}

	/**
	 * Triggered when a button is pressed
	 */
	public void actionPerformed(ActionEvent e) {

		String actionIdentifier = e.getActionCommand();

		if (actionIdentifier.equals("clear")) {
			this.vertices.clear();
			this.edges.clear();
			this.canvas.repaint();
		}

		// Allow the state to handle a button press
		this.state = state.handleAction(actionIdentifier);
	}

	/**
	 * Triggered when the mouse is clicked
	 */
	public void mouseClicked(MouseEvent e) {

		// If we are in the ADD_VERTEX state, clicking should draw a vertex
		if (this.state == State.ADD_VERTEX) {

			// Create circle shape
			Ellipse2D.Double vertexShape = new Ellipse2D.Double(e.getX() - NODE_RADIUS, e.getY() - NODE_RADIUS,
					2 * NODE_RADIUS, 2 * NODE_RADIUS);

			// Create vertex object
			Vertex vertex = new Vertex(vertexShape);

			vertices.add(vertex);
			canvas.repaint();
		}
		// If we are in the ADD_EDGE_1 state, clicking a vertex should connect to it
		else if (this.state == State.ADD_EDGE_1) {

			// Find the vertex that the mouse is over
			for (Vertex v : this.vertices) {
				if (v.getVertexShape().contains(e.getPoint())) {
					// Logic for linking vertex to node
					canvas.vertexOne = v;
					canvas.activeLine = true;
				}
			}
		}
		// If we are in the ADD_EDGE_2 state, clicking should add an edge btwn two
		// vertices
		else if (this.state == State.ADD_EDGE_2) {

			// Find the vertex that the mouse is over
			for (Vertex v : this.vertices) {
				if (v.getVertexShape().contains(e.getPoint())) {

					// Create edge shape
					Line2D.Double edgeShape = new Line2D.Double();
					edgeShape.setLine(canvas.vertexOne.getVertexShape().getCenterX(),
							canvas.vertexOne.getVertexShape().getCenterY(), v.getVertexShape().getCenterX(),
							v.getVertexShape().getCenterY());

					// Create Edge object
					Edge edge = new Edge(edgeShape, canvas.vertexOne, v);
					// Add edge
					this.edges.push(edge);
					canvas.vertexOne.addEdge(edge);
					v.addEdge(edge);

					canvas.activeLine = false; // Turn off line to cursor
					canvas.highlightVertex = null; // Turn off highlighting
					canvas.repaint();
					this.state = state.handleAction("connectEdge"); // Tell the FNM that we've connected an edge
				}
			}
		} else if (this.state == State.DELETE) {
			for (Vertex v : this.vertices) {
				if (v.getVertexShape().contains(e.getPoint())) {
					this.edges.removeAll(v.getEdges());
					this.vertices.remove(v);
					break;
				}
			}
			canvas.deleteState = false;
			canvas.repaint();
		}
		// Allow FSM to handle mouse clicked
		this.state = state.handleAction("mouseClicked");
	}

	public void initializeDataStructures() {

		vertices = new LinkedList<Vertex>();
		edges = new LinkedList<Edge>();

		// NOTE: I took out PQ since I need a LinkedList for edges in order to
		// draw them.

		// edges = new PriorityQueue<Edge>();

	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {

	}

	/**
	 * Triggered when the mouse is moved.
	 */
	public void mouseMoved(MouseEvent e) {

		// Allow canvas to know mouse position
		canvas.mousePos = e.getPoint();

		// Highlighting should happen when in ADD_EDGE_1 or ADD_EDGE_2
		if (this.state == State.ADD_EDGE_1 || this.state == State.ADD_EDGE_2 || this.state == State.DELETE) {

			// Check if the pointer is currently over a vertex
			boolean overPoint = false;
			for (Vertex v : this.vertices) {
				if (v.getVertexShape().contains(e.getPoint())) {
					canvas.highlightVertex = v;
					overPoint = true;
				}
			}

			// Remove highlight if no vertex is being hovered over
			if (!overPoint) {
				canvas.highlightVertex = null;
			}

			if (this.state == State.DELETE) {
				canvas.deleteState = true;
			}

			canvas.repaint();
		}

	}
}
