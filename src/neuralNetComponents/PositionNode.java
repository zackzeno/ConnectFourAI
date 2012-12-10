package neuralNetComponents;

import utils.BoardPos;

public class PositionNode extends NeuralNetNode {

	private static final long serialVersionUID = 2198328508374798581L;
	private BoardPos position;
	
	public PositionNode(BoardPos p) {
		position = p;
	}
	
	@Override
	public double getActivationValue(double value) {
		return value;
	}

	@Override
	public double getActivationDerivative(double value) {
		return 1;
	}
	
	@Override
	public String getNodeInfo() {
		return "Position Node " + position.toString();
	}

}
