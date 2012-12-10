package utils;

import java.util.ArrayList;
import java.util.Calendar;

import java.io.File;
import java.io.IOException;

import neuralNetComponents.FourGroupNode;
import enums.Direction;
import gameComponents.Board;

public class AIUtils {
	public static final Direction[] evalDirections = {Direction.SOUTHEAST, Direction.EAST, Direction.NORTHEAST, Direction.NORTH};
	
	public static FourGroupNode[] createGroupNodes(Board board) {
		ArrayList<FourGroupNode> list = new ArrayList<FourGroupNode>();
		for(int i = 0; i < board.getSize().cols; i++) {
			for(int j = board.getSize().rows - 1; j >= 0; j--) {
				BoardPos currentPos = new BoardPos(j, i);
				for(int k = 0; k < evalDirections.length; k++) {
					BoardPos[] positions = new BoardPos[4];
					BoardPos checkPos = currentPos;
					boolean possibleGroup = true;
					for(int n = 0; n < 4; n++) {
						if(checkPos == null) {
							possibleGroup = false;
							break;
						}
						positions[n] = checkPos;
						checkPos = board.moveInDirection(checkPos, evalDirections[k]);
					}
					if(possibleGroup) {
						list.add(new FourGroupNode(positions));
					}
				}
			}
		}
		return list.toArray(new FourGroupNode[0]);
	}
	
	public static int evaluateBoard(Board board, int playerNum, int offenseWeight, int defenseWeight) {
		int value = 0;
		for(int i = 0; i < board.getSize().cols; i++) {
			for(int j = board.getSize().rows - 1; j >= 0; j--) {
				BoardPos currentPos = new BoardPos(j, i);
				boolean rowFinishedFlag = false;
				for(int k = 0; k < evalDirections.length; k++) {
					int numPlayerPieces = 0;
					int numOpponentPieces = 0;
					BoardPos checkPos = currentPos;
					boolean possibleGroup = true;
					for(int n = 0; n < 4; n++) {
						if(checkPos == null) {
							possibleGroup = false;
							break;
						}
						if(board.getPosState(checkPos) == playerNum) {
							numPlayerPieces++;
						}
						else if(board.getPosState(checkPos) != 0) {
							numOpponentPieces++;
						}
						checkPos = board.moveInDirection(checkPos, evalDirections[k]);
					}
					if(possibleGroup) {
						if(k == 0 && numPlayerPieces == 0 && numOpponentPieces == 0) {
							rowFinishedFlag = true;
							break;
						}
						else if(numPlayerPieces == 4) {
							return Integer.MAX_VALUE;
						}
						else if(numOpponentPieces == 4) {
							return Integer.MIN_VALUE;
						}
						else if(numPlayerPieces > 0 && numOpponentPieces == 0) {
							value += Math.pow(numPlayerPieces, 2)*offenseWeight;
						}
						else if(numOpponentPieces > 0  && numPlayerPieces == 0) {
							value -= Math.pow(numOpponentPieces, 2)*defenseWeight;
						}
					}
				}
				if(rowFinishedFlag) {
					break;
				}
			}
		}
		return value;
	}
	
	public static int getBoardStatus(Board board) {
		for(int i = 0; i < board.getSize().cols; i++) {
			for(int j = board.getSize().rows - 1; j >= 0; j--) {
				BoardPos currentPos = new BoardPos(j, i);
				if(board.getPosState(currentPos) == 0) {
					continue;
				}
				for(int k = 0; k < evalDirections.length; k++) {
					int num1Pieces = 0;
					int num2Pieces = 0;
					BoardPos checkPos = currentPos;
					boolean possibleGroup = true;
					for(int n = 0; n < 4; n++) {
						if(checkPos == null) {
							possibleGroup = false;
							break;
						}
						if(board.getPosState(checkPos) == 1) {
							num1Pieces++;
						}
						else if(board.getPosState(checkPos) != 0) {
							num2Pieces++;
						}
						checkPos = board.moveInDirection(checkPos, evalDirections[k]);
					}
					if(possibleGroup) {
						if(num1Pieces == 4) {
							return 1;
						}
						else if(num2Pieces == 4) {
							return 2;
						}
					}
				}
			}
		}
		for(int i = 0; i < board.getSize().cols; i++) {
			if(board.getPosState(i, 0) == 0) {
				return 0;
			}
		}
		return -1;
	}
	
	public static int minimax(int playerNum, Board board, BoardPos lastMove, int level, int levelsDeep, 
			boolean maximize, int offWeight, int defWeight) {
		if(lastMove != null) {
			int status = board.checkGameComplete(lastMove);
			if(status == 1 && !maximize) {
				return Integer.MAX_VALUE;
			}
			else if(status == 1) {
				return Integer.MIN_VALUE;
			}
			else if(status != 0) {
				return 0;
			}
		}
		if(level == levelsDeep) {
			return AIUtils.evaluateBoard(board, playerNum, offWeight, defWeight);
		}
		ArrayList<Board> nextBoards = new ArrayList<Board>();
		ArrayList<BoardPos> nextMoves = new ArrayList<BoardPos>();
		int currentPlayer;
		if(maximize) {
			currentPlayer = playerNum;
		}
		else if(playerNum == 1) {
			currentPlayer = 2;
		}
		else {
			currentPlayer = 1;
		}
		for(int i = 0; i < board.getSize().cols; i++) {
			int emptyRow = board.getEmptyRow(i);
			if(emptyRow != -1) {
				Board newBoard = board.deepCopy();
				newBoard.setState(i, emptyRow, currentPlayer);
				nextBoards.add(newBoard);
				nextMoves.add(new BoardPos(emptyRow, i));
			}
		}
		if(maximize) {
			int maxValue = Integer.MIN_VALUE;
			for(int i = 0; i < nextBoards.size(); i++) {
				int minimaxResult = minimax(playerNum, nextBoards.get(i), nextMoves.get(i), level + 1, levelsDeep, 
						!maximize, offWeight, defWeight);
				if(minimaxResult > maxValue) {
					maxValue = minimaxResult;
				}
			}
			return maxValue;
		}
		else {
			int minValue = Integer.MAX_VALUE;
			for(int i = 0; i < nextBoards.size(); i++) {
				int minimaxResult = minimax(playerNum, nextBoards.get(i), nextMoves.get(i), level + 1, levelsDeep, 
						!maximize,  offWeight, defWeight);
				if(minimaxResult < minValue) {
					minValue = minimaxResult;
				}
			}
			return minValue;
		}
	}
	
	public static File getOrCreateLogfile(String prefix) {
		Calendar calendar = Calendar.getInstance();
		File f = new File("logs", prefix + "_y" + calendar.get(Calendar.YEAR) + "_d" + 
				calendar.get(Calendar.DAY_OF_YEAR) + "_h" + calendar.get(Calendar.HOUR_OF_DAY) + "_m" +
				calendar.get(Calendar.MINUTE) + "_s" + calendar.get(Calendar.SECOND) + ".log");
		if(!f.exists()) {
			try {
				f.createNewFile();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}
}