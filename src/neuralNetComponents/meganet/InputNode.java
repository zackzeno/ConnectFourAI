package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class InputNode extends NeuralNetNode {

	
	private static final long serialVersionUID = -1162698683086513081L;

	@Override
	public String getNodeInfo() {
		return "Input Node";
	}

	@Override
	public double getActivationValue(double value) {
		return value;
	}

	@Override
	public double getActivationDerivative(double value) {
		return 1;
	}
	
}
