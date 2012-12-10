package neuralNetComponents;

import utils.BoardPos;

public class FourGroupNode extends NeuralNetNode {

	private static final long serialVersionUID = 7544063731575467852L;
	private BoardPos[] positions;
	
	public FourGroupNode(BoardPos[] p) {
		super();
		positions = p;
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
	
	public boolean overlap(FourGroupNode other) {
		for(int i = 0; i < positions.length; i++) {
			if(other.contains(positions[i])) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getNodeInfo() {
		String ret = "Group Node, positions ";
		for(int i = 0; i < positions.length; i++) {
			ret = ret + positions[i].toString() + " ";
		}
		return ret;
	}
	
	@Override
	public double getActivationValue(double value) {
		if(Math.abs(value) >= 1) {
			return 1;
		}
		return 0;
	}

	@Override
	public double getActivationDerivative(double value) {
		return 1;
	}

}
