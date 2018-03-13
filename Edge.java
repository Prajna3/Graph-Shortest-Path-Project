package project2;

public class Edge {
	protected Vertex start;
	protected Vertex end;
	protected int weight;
	
	public Edge (Vertex s, Vertex e, int w) {
		start = s;
		end = e;
		weight = w;
	}
}
