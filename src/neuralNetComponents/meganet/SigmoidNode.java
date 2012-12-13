package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class SigmoidNode extends NeuralNetNode{
	
	private static final long serialVersionUID = 7120298997303025449L;
	private NeuralNetLayer layer;
	
	public SigmoidNode(NeuralNetLayer l) {
		this.layer = l;
		l.addNode(this);
	}
	
	@Override
	public double getActivationValue(double value) {
		return 1/(1+Math.exp(-value));
	}

	@Override
	public double getActivationDerivative(double value) {
		double sig = getActivationValue(value);
		return sig*(1-sig);
	}

	@Override
	public String getNodeInfo() {
		return "Sigmoid node";
	}
	
}
