package neuralNetComponents.meganet;

import utils.BoardPos;
import neuralNetComponents.NeuralNetNode;

public class PointNode extends NeuralNetNode {
	
	BoardPos pos;

	//is connected to the two player inputs for the given point
	public PointNode(BoardPos p) {
		super();
		pos = p;
	}
	
	
	@Override
	public double getFiringValue() {
		if(getValue() > 0) {
			return 1;
		}
		else {
			return 0;
		}
	}
	
	public void backPropogate(double modifier) {
		//do nothing, this node should not learn
	}


	@Override
	public String getNodeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
