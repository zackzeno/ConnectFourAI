package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class OutputNode extends NeuralNetNode {

	@Override
	public double getFiringValue() {
		return getValue();
	}

	@Override
	public String getNodeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
