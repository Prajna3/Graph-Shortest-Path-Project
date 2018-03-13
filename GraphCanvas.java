package project2;

import java.awt.*;
import java.util.ArrayList;
public class GraphCanvas extends Canvas {
	
	static final int diameter = 30;
	static final int arrowWidth = 6;
	static final int arrowLength =15;
	
	protected Graph G;
	
	public GraphCanvas(Graph G){
	  this.G=G;
	}
	
	public void drawDValue(Graphics graphics, Vertex v){
	  /* Draws dValue of vertex in upper left corner */
	  graphics.setColor(Color.RED);
	  graphics.drawString(Integer.toString(v.dValue), v.x-diameter/2, v.y-diameter/2);	  
	}
	  
	public void drawVertex(Graphics graphics, Vertex v){
	  /* Draws vertexID in circle centered at (vertex.x,vertex.y) */
	  graphics.setColor(Color.BLACK);
	  graphics.drawOval(v.x-diameter/2, v.y-diameter/2, diameter, diameter);
	  graphics.drawString(Integer.toString(v.ID), v.x-diameter/4, v.y+diameter/4);
	}
	  
	public void drawEdge(Graphics graphics, Vertex v, Vertex u, int weight){
	  /* Draws a weighted DIRECTED edge v->u as an arrow from v to u . 
	   * Note that edges v->u and u->v will be side-to-side and not right on top of each other. 
	   */
	  double l = Math.sqrt((u.x-v.x)*(u.x-v.x)+(u.y-v.y)*(u.y-v.y));
	  graphics.setColor(new Color(110,60,240));
      // edge    	        
	  graphics.drawLine((int)(u.x+(l-diameter/2)*(v.x-u.x)/l-arrowWidth*(u.y-v.y)/l), 
	      (int)(u.y+(l-diameter/2)*(v.y-u.y)/l+arrowWidth*(u.x-v.x)/l),
	      (int)(v.x+(l-diameter/2)*(u.x-v.x)/l-arrowWidth*(u.y-v.y)/l), 
	      (int)(v.y+(l-diameter/2)*(u.y-v.y)/l+arrowWidth*(u.x-v.x)/l)); 

	  // arrow
	  Polygon arrowhead = new Polygon();
	  arrowhead.addPoint((int)(v.x+(l-diameter/2)*(u.x-v.x)/l-arrowWidth*(u.y-v.y)/l), 
	      (int)(v.y+(l-diameter/2)*(u.y-v.y)/l+arrowWidth*(u.x-v.x)/l)); 
	  arrowhead.addPoint((int)(v.x+(l-diameter/2-arrowLength)*(u.x-v.x)/l-2*arrowWidth*(u.y-v.y)/l), 
	      (int)(v.y+(l-diameter/2-arrowLength)*(u.y-v.y)/l+2*arrowWidth*(u.x-v.x)/l)); 
	  arrowhead.addPoint((int)(v.x+(l-diameter/2-arrowLength)*(u.x-v.x)/l+0*arrowWidth*(u.y-v.y)/l), 
	      (int)(v.y+(l-diameter/2-arrowLength)*(u.y-v.y)/l-0*arrowWidth*(u.x-v.x)/l)); 
	  graphics.fillPolygon(arrowhead);
	      
	  // weight
	  graphics.drawString(Integer.toString(weight), 
	       (int)(v.x+(l-diameter/2-2*arrowLength)*(u.x-v.x)/l-2*arrowWidth*(u.y-v.y)/l), 
	       (int)(v.y+(l-diameter/2-2*arrowLength)*(u.y-v.y)/l+2*arrowWidth*(u.x-v.x)/l));
		  
	}
	
	public void highlightEdge(Graphics graphics, Vertex v, Vertex u){
	  /* Highlights the line segment of the DIRECTED edge v->u.  
	   * Note that edges v->u and u->v will be side-to-side and not right on top of each other.
	   */
	  double l = Math.sqrt((u.x-v.x)*(u.x-v.x)+(u.y-v.y)*(u.y-v.y));
	  graphics.setColor(new Color(240,20,110));
	  // edge    	        
	  graphics.drawLine((int)(u.x+(l-diameter/2)*(v.x-u.x)/l-arrowWidth*(u.y-v.y)/l), 
	       (int)(u.y+(l-diameter/2)*(v.y-u.y)/l+arrowWidth*(u.x-v.x)/l),
	       (int)(v.x+(l-diameter/2)*(u.x-v.x)/l-arrowWidth*(u.y-v.y)/l), 
	       (int)(v.y+(l-diameter/2)*(u.y-v.y)/l+arrowWidth*(u.x-v.x)/l)); 
	  }
	  
	public void highlightVertex(Graphics graphics, Vertex v){
	  /* Highlights circle around the vertex v. 
	   */
	  graphics.setColor(Color.GREEN);
	  graphics.drawOval(v.x-diameter*5/12, v.y-diameter*5/12, diameter*10/12, diameter*10/12);
	}
	  
	public void drawTitle(Graphics graphics, String s){
	  /* Draws title in large font on screen.
	   * Could use this method to draw algorithms type on screen (e.g., "Dijkstra") 	  
	   */
		Font oldFont = graphics.getFont();
		graphics.setFont(oldFont.deriveFont((float)40));
	    graphics.setColor(new Color(240,20,110));
	    graphics.drawString(s, 100, 500);
	    graphics.setFont(oldFont);
	  }
	  
	public void paint(Graphics graphics){
	  // Don't confuse "Graphics" (which is used to draw on) with "Graph" (which stores your graph)
      // This method has to contain all the drawing/painting code

		ArrayList<Edge> AJ_List;
	   for (Vertex i : G.AdjacencyLists.keySet()){
		   drawVertex(graphics, i);
		   AJ_List = G.AdjacencyLists.get(i);
		   for (Edge j : AJ_List){
			   drawEdge(graphics, j.start, j.end, j.weight);
		   }
	   }
	   //If we have a negative cycle, print these results...
	   if (G.negCycle) {
		   drawTitle(graphics, G.graphTitle);
		   drawText(graphics, "Bellman-Ford's Results", 100, 525);
		   drawText(graphics, "This graph has a negative cycle, therefore Bellman-Ford's Algorithm", 100, 545);
		   drawText(graphics, "is unsuccessful.", 100, 565);
	   } else {
		   for (Edge i : G.PredecessorArray.values()) {
			   highlightEdge(graphics, i.start, i.end);
		   }
		   highlightVertex(graphics, G.source);
		   //if we ran Bellman-Ford's algorithm, draw these results...
		   if (G.algorithm.compareTo("BF") == 0){
			   drawTitle(graphics, G.graphTitle + ":");
			   drawText(graphics,"Bellman-Ford's Results", 100, 525);
			   int yCoord = 545;
			   for (Vertex i : G.AdjacencyLists.keySet()){
				   if (i.dValue == 0) {
					   drawText(graphics, "Source Vertex: " + i.ID + " dValue: " + i.dValue, 100, yCoord);
				   } else if (i.dValue == Integer.MAX_VALUE){
					   drawText(graphics, "Vertex: " + i.ID + " dValue: Infinite (No path to " + i.ID + " from source!)", 100, yCoord); 
			       } else {
					   drawText(graphics, "Vertex: " + i.ID + " dValue: " + i.dValue, 100, yCoord); 
				   }
				   yCoord += 20;
			   }
			  //If we ran Dijkstra's algorithm, draw these results...
		   } else if (G.algorithm.compareTo("D") == 0) {
			   drawTitle(graphics, G.graphTitle);
			   drawText(graphics, "Dijkstra's Results", 100, 525);
			   int yCoord = 545;
			   for (Vertex i : G.AdjacencyLists.keySet()){
				   if (i.dValue == 0) {
					   drawText(graphics, "Source Vertex: " + i.ID + " dValue: " + i.dValue, 100, yCoord);
				   } else if (i.dValue == Integer.MAX_VALUE){
					   drawText(graphics, "Vertex: " + i.ID + " dValue: Infinite (No path to " + i.ID + " from source!)", 100, yCoord); 
			       } else {
					   drawText(graphics, "Vertex: " + i.ID + " dValue: " + i.dValue, 100, yCoord); 
				   }
				   yCoord += 20;
			   }
			   //If we didn't run any algorithm, just draw the title.
		   } else {
			   drawTitle(graphics, G.graphTitle);
		   }
	   }
	 }
	
	public void drawText(Graphics graphics, String s, int x, int y) {
		Font oldFont = graphics.getFont();
		graphics.setFont(oldFont.deriveFont((float)15));
	    graphics.setColor(new Color(240,20,110));
	    graphics.drawString(s, x, y);
	    graphics.setFont(oldFont);
	}

}
