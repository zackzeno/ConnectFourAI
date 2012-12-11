package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class SoftmaxNode extends NeuralNetNode{
	
	private static final long serialVersionUID = 7120298997303025449L;
	private SoftmaxLayer layer;
	
	public SoftmaxNode(SoftmaxLayer layer) {
		this.layer = layer;
		layer.addNode(this);
	}
	
	@Override
	public double getActivationDerivative(double value) {
		double s = getActivationValue(value);
		return s * (1 - s);
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
