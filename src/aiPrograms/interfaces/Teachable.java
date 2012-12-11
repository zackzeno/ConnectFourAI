package aiPrograms.interfaces;

import gameComponents.MoveInfo;
import utils.BoardSize;

public interface Teachable {

	public void teachNeuralNet(BoardSize boardSize, MoveInfo[] moves, int winnerNum);
	
	public void startGame();
	
	public void endGame();
	
}
