package neuralNetComponents;

import java.io.Serializable;

public class NeuralNetEdge implements Serializable {

	private static final long serialVersionUID = -5719781289539870765L;
	private NeuralNetNode from;
	private NeuralNetNode to;
	private double weight;
	
	public NeuralNetEdge(NeuralNetNode f, NeuralNetNode t, double w) {
		from = f;
		to = t;
		weight = w;
	}
	
	public NeuralNetNode getTo() {
		return to;
	}
	
	public NeuralNetNode getFrom() {
		return from;
	}
	
	public double getWeight() {
		return weight;
	}
	
	public void modifyWeight(double increment) {
		weight = weight += increment;
	}
}