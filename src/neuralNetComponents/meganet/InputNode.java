package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class InputNode extends NeuralNetNode {
	
	
	@Override
	public double getFiringValue() {
		// TODO Auto-generated method stub
		return getValue();
	}

	public void fire() {
		setValue(1);
	}
	
	public void unfire() {
		setValue(0);
	}
	
}
