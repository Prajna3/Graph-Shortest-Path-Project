package project2;
import java.awt.Color;
import java.util.Scanner;

import javax.swing.JFrame;

public class Project2 {
	
	
	public static void main(String[] args){
	    // Set up application frame
	    JFrame window = new JFrame("Project 2");
	    window.setBounds(0,0,800,1000); // Dimensions of frame
	    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    System.out.println("\nWritten by Charlie Powicki\n");

	    /*
	     * TO DO:
	     * Create Graph and pass into GraphCanvas
	     * Run Bellman-Ford or Dijkstra.
	     * You probably want to just run one of the algorithms at a time.
	     */
	    Graph G = new Graph();
	    System.out.println("Choose an algorithm to run:\n1. Bellman-Ford's Algorithm\n2. Dijkstra's Algorithm\n3. None - Just draw the graph!");	
		Scanner scanner = new Scanner(System.in);
		int choice = scanner.nextInt();
		while (!(choice == 1 || choice == 2 || choice == 3)){
			System.out.println("Your input is invalid. Enter 1 for Bellman-Ford's Algorithm, or 2 for Dijkstra's Algorithm");
			choice = scanner.nextInt();
		}
		if (choice != 3) {
		    System.out.println("Choose starting vertex ID:");
		    for (Vertex i: G.AdjacencyLists.keySet()){
		    	System.out.println("Vertex " + i.ID);
		    }
		    int ID = scanner.nextInt();
		    Vertex start = G.getVertexByID(ID);
		    while (start == null) {
		    	System.out.println("Sorry, the ID " + ID + " does not match an ID in the graph. Please try again.");
		    	ID = scanner.nextInt();
		    	start = G.getVertexByID(ID);
		    }
		    if (choice == 1) {
		    	G.BellmanFord(start);
		    } else {
		    	G.Dijkstra(start);
		    }
		}
	    GraphCanvas canvas = new GraphCanvas(G); 
	    		
	    // Display frame
	    canvas.setBackground(new Color(245,241,222));
	    window.getContentPane().add(canvas); // component added to content pane
	    //window.setResizable(false);
	    window.setAlwaysOnTop(true);
	    window.setVisible(true); // displays the frame 
	}
	
}
