package neuralNetComponents;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class NeuralNetNode implements Serializable {

	private static final long serialVersionUID = -2784280108553367549L;
	private ArrayList<NeuralNetEdge> forwardEdges;
	private ArrayList<NeuralNetEdge> backwardEdges;
	private double accumulatedDelta;
	private double accumulatedValue;
	
	public NeuralNetNode() {
		forwardEdges = new ArrayList<NeuralNetEdge>();
		backwardEdges = new ArrayList<NeuralNetEdge>();
	}
	
	public void reset() {
		accumulatedDelta = 0;
		accumulatedValue = 0;
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
	
	public void setDelta(double value) {
		accumulatedDelta = value;
	}
	
	public void accumulate(double value) {
		accumulatedValue += value;
	}
	
	public void backAccumulate(double value) {
		accumulatedDelta += value;
	}
	
	public abstract double getActivationValue(double value);
	
	public abstract double getActivationDerivative(double value);
	
	public abstract String getNodeInfo();
	
	public void forwardPropagate() {
		accumulatedValue = getActivationValue(accumulatedValue);
		for(int i = 0; i < forwardEdges.size(); i++) {
			forwardEdges.get(i).getTo().accumulate(accumulatedValue*forwardEdges.get(i).getWeight());
		}
	}
	
	public void backPropagate() {
		accumulatedDelta = accumulatedDelta*getActivationDerivative(accumulatedValue);
		for(int i = 0; i < backwardEdges.size(); i++) {
			NeuralNetNode bNode = backwardEdges.get(i).getFrom();
			bNode.backAccumulate(accumulatedDelta*backwardEdges.get(i).getWeight());
			backwardEdges.get(i).modifyWeight(accumulatedDelta*bNode.accumulatedValue);
		}
	}
	
	/*
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
	*/

	public ArrayList<NeuralNetEdge> getOutEdges() {
		return forwardEdges;
	}
}
