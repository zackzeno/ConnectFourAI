package neuralNetComponents;

import utils.BoardPos;

public class OffDefJoinNode extends NeuralNetNode{
	
	private static final long serialVersionUID = 2658356951335419915L;
	private BoardPos position;
	
	public OffDefJoinNode(BoardPos p) {
		position = p;
	}
	
	@Override
	public double getActivationValue(double value) {
		if(value > 0) {
			return value;
		}
		else {
			return 0;
		}
		
	}

	@Override
	public double getActivationDerivative(double value) {
		if(value > 0) {
			return 1;
		}
		return 0;
	}

	@Override
	public String getNodeInfo() {
		return "Join Node " + position.toString();
	}
}
