package neuralNetComponents.meganet;

import java.util.ArrayList;

import neuralNetComponents.NeuralNetEdge;
import neuralNetComponents.NeuralNetNode;



public class NeuralNetLayer {
	
	private ArrayList<SoftmaxNode> nodes;
	
	public NeuralNetLayer(NeuralNetLayer prev) {
		nodes = new ArrayList<SoftmaxNode>();
	}
	
	public void addNode(SoftmaxNode node) {
		nodes.add(node);
	}
	
	public double getSoftmaxValue(SoftmaxNode node) {
		double num = 0;
		double den = 0;
		
		num = Math.exp(node.getNetInput());
		
		for(SoftmaxNode n : nodes) {
			den += Math.exp(n.getNetInput());
		}
		
		return num / den;
	}
	
	public ArrayList<NeuralNetEdge> getInEdges() {
		ArrayList<NeuralNetEdge> l = new ArrayList<NeuralNetEdge>();
		for(NeuralNetNode n : nodes) {
			l.addAll(n.getInEdges());
		}
		return l;
	}
	
	
}
