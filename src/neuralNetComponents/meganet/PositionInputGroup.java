package neuralNetComponents.meganet;

import java.io.Serializable;

public class PositionInputGroup implements Serializable{

	private static final long serialVersionUID = 5883926885652635190L;
	public InputNode player;
	public InputNode empty;
	public InputNode opp;
	
	public PositionInputGroup() {
		player = new InputNode();
		empty = new InputNode();
		opp = new InputNode();
	}
	
	
}
