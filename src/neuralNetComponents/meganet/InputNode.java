package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class InputNode extends NeuralNetNode {

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
