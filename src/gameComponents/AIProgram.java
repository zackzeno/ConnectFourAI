package gameComponents;

public interface AIProgram {
	MoveInfo makeMove(int playerNum, Board board);
	void startRound(int playerNum);
	void endRound(int playerNum, int winnerNum);
	void startGame(int playerNum);
	void endGame(int playerNum);
}
