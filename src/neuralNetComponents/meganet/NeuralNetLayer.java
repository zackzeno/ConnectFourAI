package neuralNetComponents.meganet;

import java.io.Serializable;
import java.util.ArrayList;

import neuralNetComponents.NeuralNetEdge;
import neuralNetComponents.NeuralNetNode;



public class NeuralNetLayer implements Serializable{
	
	private static final long serialVersionUID = -6227011591188981200L;
	protected ArrayList<NeuralNetNode> nodes;
	
	public NeuralNetLayer() {
		nodes = new ArrayList<NeuralNetNode>();
	}
	
	public void addNode(NeuralNetNode node) {
		nodes.add(node);
	}
	
	public void addEdgeToAll(NeuralNetNode from) {
		for(NeuralNetNode to : nodes) {
			NeuralNetEdge e = new NeuralNetEdge(from, to, Math.random() * .01);
			from.addForwardEdge(e);
			to.addBackwardEdge(e);
		}
	}
	
	public NeuralNetNode[] getNodeArray() {
		return nodes.toArray(new NeuralNetNode[nodes.size()]);
	}
	
}
