package project2;

import java.io.*;
import java.util.*;

public class Graph {
	// This class is used to pass into the GraphCanvas. 
	// Variables stored in this class can be used for drawing on the screen.
	
	// Class variables
    protected int n; // Number of vertices in the graph
    protected HashMap<Vertex, ArrayList<Edge>> AdjacencyLists;
    protected ArrayList<Edge> Edges;
    protected HashMap<Vertex, Edge> PredecessorArray;
    protected boolean negCycle; //True if there is a negative cycle in this graph 
    protected String algorithm; //Stores the name of the last algorithm we ran.
    protected String graphTitle;
    protected Vertex source; //Stores the last source vertex

    public Graph(){
    	AdjacencyLists = new HashMap<Vertex, ArrayList<Edge>>();
    	Edges = new ArrayList<Edge>();
    	PredecessorArray = new HashMap<Vertex, Edge>();
    	readGraphFromFile();
    	negCycle = false;
    	algorithm = "None";
    }
     
    
	public void readGraphFromFile(){
		// Optional; asks for keyboard input.
		String filename;
		System.out.print("Please enter name for graph file: ");	
		Scanner scanner = new Scanner(System.in);
		filename = scanner.nextLine();
		graphTitle = filename;
        readGraphFromFile(filename);
	}
    
    public void readGraphFromFile(String filename){
		Scanner scanner;
	    try{
	        scanner = new Scanner(new File(filename));
	        int graph_size = scanner.nextInt();
	        int ID; int y; int x;
	        Vertex new_vertex;
	        Edge new_edge;
	        //Loop through the .dat file to create graph
	        for (int i = 0; i < graph_size; i++){
	        	ID = scanner.nextInt();
	        	x = scanner.nextInt();
	        	y = scanner.nextInt();
	        	if (getVertexByID(ID) != null){ //if we've already created the vertex
	        		new_vertex = getVertexByID(ID);
	        		new_vertex.setY(y); //Update coordinates
	        		new_vertex.setX(x); //Update coordinates
	        	} else {
	        		new_vertex = new Vertex(ID, x, y); //Otherwise, create a new vertex
	        		AdjacencyLists.put(new_vertex, new ArrayList<Edge>()); //Map to an empty list of edges
	        	}
	        	int AJ_size = scanner.nextInt();
	        	Vertex edgeVertex;
	        	int weight;
	        	for (int j = 0; j < AJ_size; j++){
	        		ID = scanner.nextInt();
	        		if (getVertexByID(ID) == null){
		        		edgeVertex = new Vertex(ID, 0, 0);
		        		AdjacencyLists.put(edgeVertex, new  ArrayList<Edge>()); 
	        		} else {
	        			edgeVertex = getVertexByID(ID); //Just get the vertex if we've already created it. 
	        		}
	        		weight = scanner.nextInt();
	        		new_edge = new Edge(new_vertex,edgeVertex, weight);
	        		edgeVertex.inDegree += 1;
	        		AdjacencyLists.get(new_vertex).add(new_edge);
	        		Edges.add(new_edge);
	        	}
	        }
	    } catch(FileNotFoundException e){
	        System.err.println("Could not find file "+filename+". "+e);
	        System.exit(0);
	    } catch(IOException e){
	        System.err.println("Error reading integer from file "+filename+". "+e);
	        System.exit(0);
	    }	   
	}
    
    public void print(){
    	for (Vertex i : AdjacencyLists.keySet()){
    		System.out.println(i.printString());
    	}
    }
    
    
    public void BellmanFord(Vertex s) {
    	algorithm = "BF"; source = s;
    	PredecessorArray.clear();
    	for (Vertex i : AdjacencyLists.keySet()){
    		i.dValue = Integer.MAX_VALUE;
    	}
    	s.dValue = 0;
    	for (int i = 1; i < AdjacencyLists.keySet().size() - 1; i ++) {
    		for (Edge j: Edges) {
    			//Standard check to see if the dValue can be reduced, and also ensures that we don't
    			//get the wrap around error from adding to the Integer.MAX_VALUE, which goes negative.
    			if (j.end.dValue > j.start.dValue + j.weight && j.start.dValue != Integer.MAX_VALUE){ 
    				j.end.dValue = j.start.dValue + j.weight;
    				PredecessorArray.put(j.end, j);
    			}
    		}
    	}
    	//The Negative cycle check
    	for (Edge j: Edges) {
    		if (j.end.dValue > j.start.dValue + j.weight && j.start.dValue != Integer.MAX_VALUE) {
    			negCycle = true;
    			return;
    		}
    	}
    	negCycle = false;
    }
    
    public void Dijkstra(Vertex s) {
    	algorithm = "D";source = s; //Note which algorithm we're using and the source vertex
    	ArrayList<Vertex> S = new ArrayList<Vertex>();
    	PredecessorArray.clear();
    	for (Vertex i : AdjacencyLists.keySet()){
    		i.dValue = Integer.MAX_VALUE;
    	}
    	s.dValue = 0;
    	ArrayListHeap Q = new ArrayListHeap();
    	for (Vertex i : AdjacencyLists.keySet()){
    		Q.insert(i);
    	}
    	Vertex u;
    	while (!Q.isEmpty()){
    		u = Q.extractMin();
    		S.add(u);
    		for (Edge i : AdjacencyLists.get(u)) {
    			//If check to see if we can update the d-value, and make sure that
    			//that we're not allowing the Integer.Max_Value to wrap around to a negative
    			//number when it's added to. 
    			if (i.end.dValue > u.dValue + i.weight && u.dValue != Integer.MAX_VALUE) {
    				if (i.end.indexInHeap != -1){ //If it's in the heap, decrease it's key
    					Q.decreaseKey(i.end.indexInHeap, u.dValue + i.weight);
    				} else { //Otherwise, directly decrease the d-value.
    					i.end.dValue = u.dValue + i.weight;
    				}
    				PredecessorArray.put(i.end, i);
    			}
    		}
    	}
    }
    //A function for grabbing a particular vertex if it exists. 
    public Vertex getVertexByID(int search_id){
    	for (Vertex i: AdjacencyLists.keySet()){
    		if (i.ID == search_id) {
    			return i;
    		}
    	}
    	return null;
    }
}

