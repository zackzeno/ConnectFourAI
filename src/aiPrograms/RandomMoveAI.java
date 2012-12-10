package aiPrograms;

import java.util.Random;

import enums.MoveResult;

import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.MoveInfo;

public class RandomMoveAI implements AIProgram {
	private final Random random = new Random();
	
	@Override
	public MoveInfo makeMove(int playerNum, Board b) {
		while(true) {
			int movePos = random.nextInt(b.getSize().cols);
			MoveInfo result = b.makeMove(movePos, playerNum);
			if(result.getResult() != MoveResult.INVALID) {
				return result;
			}
		}
	}

	@Override
	public void startRound(int playerNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound(int playerNum, int winnerNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startGame(int playerNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endGame(int playerNum) {
		// TODO Auto-generated method stub
		
	}
}
