package aiPrograms;

import java.util.ArrayList;
import java.util.Random;

import utils.AIUtils;
import utils.BoardPos;

import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.MoveInfo;

public class MinimaxAI implements AIProgram {
	
	private int levelsDeep;
	private Random r;
	private int offenseWeight;
	private int defenseWeight;
	
	public MinimaxAI(int n, int o, int d) {
		levelsDeep = n;
		offenseWeight = o;
		defenseWeight = d;
		r = new Random();
	}
	
	@Override
	public MoveInfo makeMove(int playerNum, Board board) {
		ArrayList<Integer> maxColList = new ArrayList<Integer>();
		int maxVal = Integer.MIN_VALUE;
		for(int i = 0; i < board.getSize().cols; i++) {
			int emptyRow = board.getEmptyRow(i);
			if(emptyRow != -1) {
				Board newBoard = board.deepCopy();
				newBoard.setState(i, emptyRow, playerNum);
				int minimaxResult = AIUtils.minimax(playerNum, newBoard, new BoardPos(emptyRow, i), 1, levelsDeep, 
						false, offenseWeight, defenseWeight);
				if(minimaxResult > maxVal) {
					maxVal = minimaxResult;
					maxColList.clear();
					maxColList.add(i);
				}
				else if(minimaxResult == maxVal) {
					maxColList.add(i);
				}
			}
		}
		int moveCol = maxColList.get(r.nextInt(maxColList.size()));
		MoveInfo moveInfo = board.makeMove(moveCol, playerNum);
		return moveInfo;
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
