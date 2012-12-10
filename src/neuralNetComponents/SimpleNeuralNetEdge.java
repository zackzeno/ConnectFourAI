package neuralNetComponents;

import utils.BoardPos;

public class SimpleNeuralNetEdge {
	public BoardPos edgeFrom;
	public BoardPos edgeTo;
	public double weight;
	
	public SimpleNeuralNetEdge(BoardPos f, BoardPos t, double w) {
		edgeFrom = f;
		edgeTo = t;
		weight = w;
	}
}
