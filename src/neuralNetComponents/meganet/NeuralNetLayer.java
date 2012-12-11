package neuralNetComponents.meganet;

import java.io.Serializable;
import java.util.ArrayList;

import neuralNetComponents.NeuralNetEdge;
import neuralNetComponents.NeuralNetNode;



public class NeuralNetLayer implements Serializable{
	
	private static final long serialVersionUID = -6227011591188981200L;
	private ArrayList<SoftmaxNode> nodes;
	
	public NeuralNetLayer() {
		nodes = new ArrayList<SoftmaxNode>();
	}
	
	public void addNode(SoftmaxNode node) {
		nodes.add(node);
	}
	
	public void addEdgeToAll(NeuralNetNode from) {
		for(NeuralNetNode to : nodes) {
			NeuralNetEdge e = new NeuralNetEdge(from, to, .01);
			from.addForwardEdge(e);
			to.addBackwardEdge(e);
		}
	}
	
	public double getSoftmaxValue(double value) {
		double num = 0;
		double den = 0;
		
		num = Math.exp(value);
		
		for(SoftmaxNode n : nodes) {
			den += Math.exp(n.getInputValue());
		}
		
		return num / den;
	}
	
	public NeuralNetNode[] getNodeArray() {
		return nodes.toArray(new NeuralNetNode[nodes.size()]);
	}
	
}
