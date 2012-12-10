package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;
import utils.BoardPos;

public class FourTupleNode extends NeuralNetNode {

	private BoardPos[] positions;
	
	public FourTupleNode(BoardPos[] p) {
		super();
		positions = p;
	}
	
	@Override
	public double getFiringValue() {
		return (getValue() / 4);
	}
	
	public BoardPos[] getPositions() {
		return positions;
	}
	
	public boolean contains(BoardPos pos) {
		for(int i = 0; i < positions.length; i++) {
			if(positions[i].equals(pos)) {
				return true;
			}
		}
		return false;
		
	}
	
	public void backPropogate(double m) {
		//do nothing, this node doesnt learn
	}

	@Override
	public String getNodeInfo() {
		// TODO Auto-generated method stub
		return null;
	}

}
