package neuralNetComponents.meganet;

import utils.BoardPos;
import neuralNetComponents.NeuralNetNode;

public class LineNode extends NeuralNetNode {
	
	BoardPos[] positions;
	
	public LineNode(BoardPos[] p) {
		super();
		positions = p;
	}

	@Override
	public double getFiringValue() {
		return getValue() / positions.length;
	}

	@Override
	public String getNodeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
