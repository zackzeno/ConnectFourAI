package gameComponents;

import enums.MoveResult;

public class MoveInfo {

	private MoveResult moveResult;
	private int playerNo;
	private int column;
	
	public MoveInfo(MoveResult r, int p, int c) {
		moveResult = r;
		playerNo = p;
		column = c;
	}
	
	public MoveResult getResult() {
		return moveResult;
	}
	
	public int getPlayerNo() {
		return playerNo;
	}
	
	public int getColumn() {
		return column;
	}
	
	public String getMoveText() {
		return "Last move: Player " + playerNo + ", Column " + column;
	}

}
