
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;
import java.util.*;

public class SwingShell extends JFrame implements ActionListener, MouseListener, MouseMotionListener {

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
				} else if (actionIdentifier.equals("changeEdgeWeight")) {
					System.out.println("In CHANGE_EDGE_WEIGHT state");
					return CHANGE_EDGE_WEIGHT;
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
		ADD_VERTEX

		{

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

		/**
		 * DELETE state
		 * 
		 * The user can select a node or edge to delete. Allows for return to INITIAL
		 * state after click.
		 */
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
				if (actionIdentifier.equals("mouseClicked")) {
					System.out.println("In INITIAL state");
					return INITIAL;
				}

				return this;
			}

		};

		abstract State handleAction(String actionIdentifier);

	}

	// The radius in pixels of the circles drawn in graph_panel
	final int NODE_RADIUS = 10;

	private String INITIAL_INFO_MSG = "Press a button below to perform an action.";
	private String ADD_VERTEX_INFO_MSG = "Click the screen to add a vertex.";
	private String ADD_EDGE_1_INFO_MSG = "Select a vertex 1 for your node.";
	private String ADD_EDGE_2_INFO_MSG = "Select a vertex 2 for your node.";
	private String DELETE_INFO_MSG = "Select a vertex or an edge to delete it.";
	private String CHANGE_EDGE_WEIGHT_MSG = "Select an edge to change its weight.";

	// References the canvas object where actual drawing will take place
	CanvasPanel canvas = null;

	JPanel buttonPanel = null;
	JPanel infoPanel = null;
	JPanel runKruskalsPanel = null;
	JLabel infoText = null;

	JButton addVertexButton, addEdgeButton, deleteButton, clearButton, edgeWeightButton, runKruskalsButton;

	// Data Structures for the Points

	// This holds the set of vertices, all represented as type Point.
	LinkedList<Vertex> vertices = null;

	// This holds the set of all edges
	// PriorityQueue<Edge> edges = null;
	LinkedList<Edge> edges = null;

	// Store the current state
	private State state;

	// Event handling stuff
	Dimension panelDim = null;

	public SwingShell() {
		super("Kruskal's Algorithm");
		setSize(new Dimension(700, 650));

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
		Dimension panelSize = new Dimension(700, 70);
		buttonPanel.setMinimumSize(panelSize);
		buttonPanel.setPreferredSize(panelSize);
		buttonPanel.setMaximumSize(panelSize);
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
		// buttonPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
		// buttonPanel.getBorder()));

		// Set button dimensions
		Dimension buttonSize = new Dimension(120, 50);

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

		// Change edge weight
		Dimension edgeWeightButtonSize = new Dimension(160, 50);
		edgeWeightButton = new JButton("<html><center>" + "Change Edge" + "<br>" + "Weight" + "</center></html>");
		edgeWeightButton.setMinimumSize(edgeWeightButtonSize);
		edgeWeightButton.setPreferredSize(edgeWeightButtonSize);
		edgeWeightButton.setMaximumSize(edgeWeightButtonSize);
		edgeWeightButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		edgeWeightButton.setActionCommand("changeEdgeWeight");
		edgeWeightButton.addActionListener(this);
		edgeWeightButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				edgeWeightButton.getBorder()));

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
		buttonPanel.add(edgeWeightButton);
		buttonPanel.add(Box.createHorizontalGlue());

		// Create button panel
		infoPanel = new JPanel();
		Dimension infoPanelSize = new Dimension(700, 50);
		infoPanel.setMinimumSize(infoPanelSize);
		infoPanel.setPreferredSize(infoPanelSize);
		infoPanel.setMaximumSize(infoPanelSize);
		infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.X_AXIS));
		infoPanel.setBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black), infoPanel.getBorder()));

		this.infoText = new JLabel("<html><center>"
				+ "Welcome to Jack and Rachel's Kruskal Program. </br> Add vertices to the graph to get started"
				+ "</center></html>", SwingConstants.CENTER);
		infoPanel.add(this.infoText);

		// Create panel for run kruskals button
		Dimension kruskalsButtonPanelSize = new Dimension(700, 60);
		runKruskalsPanel = new JPanel();
		runKruskalsPanel.setMinimumSize(kruskalsButtonPanelSize);
		runKruskalsPanel.setPreferredSize(kruskalsButtonPanelSize);
		runKruskalsPanel.setMaximumSize(kruskalsButtonPanelSize);
		runKruskalsPanel.setLayout(new BoxLayout(runKruskalsPanel, BoxLayout.X_AXIS));

		Dimension runKruskalsButtonSize = new Dimension(680, 50);
		runKruskalsButton = new JButton("Find MST with Kruskal's Algorithm");
		runKruskalsButton.setMinimumSize(runKruskalsButtonSize);
		runKruskalsButton.setPreferredSize(runKruskalsButtonSize);
		runKruskalsButton.setMaximumSize(runKruskalsButtonSize);
		runKruskalsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		runKruskalsButton.setActionCommand("runKruskals");
		runKruskalsButton.addActionListener(this);
		runKruskalsButton.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.black),
				runKruskalsButton.getBorder()));

		runKruskalsPanel.add(Box.createHorizontalGlue());
		runKruskalsPanel.add(runKruskalsButton);
		runKruskalsPanel.add(Box.createHorizontalGlue());

		// Add everything
		contentPane.add(infoPanel);
		contentPane.add(canvas);
		contentPane.add(buttonPanel);
		contentPane.add(runKruskalsPanel);

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
		updateInfoMessage();
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
			boolean removedVertex = false;
			for (Vertex v : this.vertices) {
				if (v.getVertexShape().contains(e.getPoint())) {
					this.edges.removeAll(v.getEdges());
					this.vertices.remove(v);
					removedVertex = true;
					break;
				}
			}

			if (!removedVertex) {
				for (Edge edge : this.edges) {
					if (this.withinRadius(edge.getEdgeShape(), e.getPoint())) {
						this.edges.remove(edge);
						break;
					}
				}
			}
			canvas.deleteState = false;
			canvas.repaint();
		} else if (this.state == State.CHANGE_EDGE_WEIGHT) {

			for (Edge edge : this.edges) {
				if (this.withinRadius(edge.getEdgeShape(), e.getPoint())) {
					String newEdgeWeight = JOptionPane.showInputDialog(this, "Please input a new edge weight.",
							edge.getWeight());

					if (newEdgeWeight != null) {
						if (newEdgeWeight.length() > 7) {
							JOptionPane.showMessageDialog(this, "Your input was too large to be parsed");
						} else {
							double newWeightDouble = edge.getWeight();
							try {
								newWeightDouble = Double.parseDouble(newEdgeWeight);
								edge.setWeight(newWeightDouble);
							} catch (NumberFormatException err) {
								JOptionPane.showMessageDialog(this, "The number you entered could not be parsed.");
							}
						}
					}

					// TODO: never trust user input
					// Also handle case if cancelled

				}
			}

			canvas.highlightEdge = null;
			canvas.repaint();
		}
		// Allow FSM to handle mouse clicked
		this.state = state.handleAction("mouseClicked");
		updateInfoMessage();
	}

	private void updateInfoMessage() {
		switch (this.state) {
			case INITIAL:
				this.infoText.setText("<html><center>" + INITIAL_INFO_MSG + "</center></html>");
				break;
			case ADD_VERTEX:
				this.infoText.setText("<html><center>" + ADD_VERTEX_INFO_MSG + "</center></html>");
				break;
			case ADD_EDGE_1:
				this.infoText.setText("<html><center>" + ADD_EDGE_1_INFO_MSG + "</center></html>");
				break;
			case ADD_EDGE_2:
				this.infoText.setText("<html><center>" + ADD_EDGE_2_INFO_MSG + "</center></html>");
				break;
			case DELETE:
				this.infoText.setText("<html><center>" + DELETE_INFO_MSG + "</center></html>");
				break;
			case CHANGE_EDGE_WEIGHT:
				this.infoText.setText("<html><center>" + CHANGE_EDGE_WEIGHT_MSG + "</center></html>");
				break;
		}
	}

	private boolean withinRadius(Line2D.Double line, Point p) {
		double pointX = p.getX();
		double pointY = p.getY();
		int radius = 8;
		return line.intersects(pointX, pointY, radius, radius);

	}

	public void initializeDataStructures() {

		vertices = new LinkedList<Vertex>();
		edges = new LinkedList<Edge>();

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
			boolean overVert = false;
			boolean overEdge = false;
			for (Vertex v : this.vertices) {
				if (v.getVertexShape().contains(e.getPoint())) {
					canvas.highlightVertex = v;
					overVert = true;
				}
			}

			// If not over a vertex, check if over an edge
			if (!overVert && this.state == State.DELETE) {

				for (Edge edge : this.edges) {
					if (this.withinRadius(edge.getEdgeShape(), e.getPoint())) {
						canvas.highlightEdge = edge;
						overEdge = true;
					}
				}

			}

			// Remove highlight if no edge is being hovered over
			if (!overEdge) {
				canvas.highlightEdge = null;
			} else if (!overVert) {
				canvas.highlightVertex = null;
			}

			if (this.state == State.DELETE) {
				canvas.deleteState = true;
			}

			canvas.repaint();
		} else if (this.state == State.CHANGE_EDGE_WEIGHT) {

			// Check if the pointer is currently over an edge
			boolean overPoint = false;
			for (Edge edge : this.edges) {
				if (this.withinRadius(edge.getEdgeShape(), e.getPoint())) {
					canvas.highlightEdge = edge;
					overPoint = true;
				}
			}

			// Remove highlight if no vertex is being hovered over
			if (!overPoint) {
				canvas.highlightEdge = null;
			}

			canvas.repaint();
		}

	}

}