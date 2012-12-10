package neuralNetComponents;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class NeuralNetNode implements Serializable {

	private static final long serialVersionUID = -2784280108553367549L;
	private ArrayList<NeuralNetEdge> forwardEdges;
	private ArrayList<NeuralNetEdge> backwardEdges;
	private boolean fired;
	private double accumulatedValue;
	
	public NeuralNetNode() {
		forwardEdges = new ArrayList<NeuralNetEdge>();
		backwardEdges = new ArrayList<NeuralNetEdge>();
	}
	
	public boolean hasFired() {
		return fired;
	}
	
	public void reset() {
		accumulatedValue = 0;
		fired = false;
	}
	
	public void addForwardEdge(NeuralNetEdge edge) {
		forwardEdges.add(edge);
	}
	
	public void addBackwardEdge(NeuralNetEdge edge) {
		backwardEdges.add(edge);
	}
	
	public double getValue() {
		return accumulatedValue;
	}
	
	public void setValue(double value) {
		accumulatedValue = value;
	}
	
	public void accumulate(double value) {
		accumulatedValue += value;
	}
	
	public abstract double getFiringValue();
	
	public abstract String getNodeInfo();
	
	public void forwardPropagate() {
		double propagationValue = getFiringValue();
		if(propagationValue == 0) {
			return;
		}
		fired = true;
		for(int i = 0; i < forwardEdges.size(); i++) {
			forwardEdges.get(i).getTo().accumulate(propagationValue*forwardEdges.get(i).getWeight());
		}
	}
	
	public void backPropagate(double reinforcementModifier) {
		if(reinforcementModifier == 1) {
			return;
		}
		int numFiredBack = 0;
		for(int i = 0; i < backwardEdges.size(); i++) {
			if(backwardEdges.get(i).getFrom().hasFired()) {
				numFiredBack++;
			}
		}
		if(numFiredBack == 0) {
			return;
		}
		double modifier = Math.pow(reinforcementModifier, 1./numFiredBack);
		for(int i = 0; i < backwardEdges.size(); i++) {
			if(backwardEdges.get(i).getFrom().hasFired()) {
				backwardEdges.get(i).modifyWeight(modifier);
				backwardEdges.get(i).getFrom().backPropagate(modifier);
			}
		}
	}

	public ArrayList<NeuralNetEdge> getOutEdges() {
		return forwardEdges;
	}
}
