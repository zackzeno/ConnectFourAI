package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class SoftmaxNode extends NeuralNetNode{
	
	private NeuralNetLayer layer;
	
	public SoftmaxNode(NeuralNetLayer layer) {
		this.layer = layer;
		layer.addNode(this);
	}
	
	@Override
	public double getActivationDerivative(double value) {
		return value * (1 - value);
	}
	
	@Override
	public double getActivationValue(double value) {
		return layer.getSoftmaxValue(value);
	}

	@Override
	public String getNodeInfo() {
		return "Softmax node";
	}
	
}
