package aiPrograms;

import java.util.ArrayList;
import java.util.Random;

import enums.Direction;
import enums.MoveResult;
import utils.BoardPos;
import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.MoveInfo;

public class MoveValueHeuristicAI implements AIProgram {

	private static final Direction[][] DIRCHECKARR = {{Direction.NORTHEAST, Direction.SOUTHWEST}, 
		{Direction.NORTHWEST, Direction.SOUTHEAST}, {Direction.WEST, Direction.EAST}, {Direction.SOUTH, Direction.NORTH}};
	
	
	@Override
	public MoveInfo makeMove(int playerNum, Board b) {
		int topValue = Integer.MIN_VALUE;
		int[] moveValues = new int[b.getSize().cols];
		int oppNum;
		if(playerNum == 1) {
			oppNum = 2;
		}
		else {
			oppNum = 1;
		}
		for(int i = 0; i < moveValues.length; i++) {
			int emptyRow = b.getEmptyRow(i);
			if(emptyRow == -1) {
				moveValues[i] = Integer.MIN_VALUE;
				continue;
			}
			int colMoveVal = getMoveValue(playerNum, b, i, emptyRow);
			if(colMoveVal == Integer.MAX_VALUE) {
				moveValues[i] = Integer.MAX_VALUE;
				topValue = Integer.MAX_VALUE;
				continue;
			}
			Board nextBoard = b.deepCopy();
			nextBoard.setState(i, emptyRow, playerNum);
			int maxOppMove = -1;
			for(int j = 0; j < b.getSize().cols; j++) {
				int oppEmptyRow = nextBoard.getEmptyRow(j);
				if(oppEmptyRow == -1) {
					continue;
				}
				int oppColMoveVal = getMoveValue(oppNum, nextBoard, j, oppEmptyRow);
				if(oppColMoveVal > maxOppMove) {
					maxOppMove = oppColMoveVal;
				}
			}
			if(maxOppMove == -1) {
				moveValues[i] = colMoveVal;
			}
			else if(maxOppMove == Integer.MAX_VALUE) {
				moveValues[i] = Integer.MIN_VALUE + 1;
			}
			else {
				moveValues[i] = colMoveVal - maxOppMove;
			}
			if(moveValues[i] > topValue) {
				topValue = moveValues[i];
			}
		}
		ArrayList<Integer> bestMoves = new ArrayList<Integer>(); 
		for(int i = 0; i < moveValues.length; i++) {
			if(moveValues[i] == topValue) {
				bestMoves.add(i);
			}
		}
		Random random = new Random();
		int moveCol = bestMoves.get(random.nextInt(bestMoves.size()));
		MoveInfo result = b.makeMove(moveCol, playerNum);
		if(result.getResult() == MoveResult.INVALID) {
			System.out.println("Impossible, should not make invalid move.");
			System.exit(0);
			return null;
		}
		return result;
	}
			
	/*
	@Override
	public MoveInfo makeMove(int playerNum, Board b) {
		int[] moveValues = new int[b.getSize().cols];
		int topValue = 0;
		for(int c = 0; c < b.getSize().cols; c++) {
			int emptyR = -1;
			for(int r = b.getSize().rows - 1; r >= 0; r--) {
				if(b.getPosState(c, r) == 0) {
					emptyR = r;
					break;
				}
			}
			if(emptyR == -1) {
				moveValues[c] = -1;
				continue;
			}
			BoardPos cMovePos = new BoardPos(emptyR, c);
			int value = 0;
			for(int i = 0; i < DIRCHECKARR.length; i++) {
				int nPConsec = 0;
				int nOConsec = 0;
				int nPMax = 0;
				int nOMax = 0;
				for(int j = 0; j < DIRCHECKARR[i].length; j++) {
					BoardPos nextOver = cMovePos;
					int consecFlag = -1;
					int currentState  = -1;
					for(int n = 0; n < 3; n++) {
						nextOver = b.moveInDirection(nextOver, DIRCHECKARR[i][j]);
						if(nextOver == null) {
							break;
						}
						int s = b.getPosState(nextOver);
						if(currentState == -1) {
							currentState = s;
							consecFlag = s;
							if(s == playerNum) {
								nPConsec++;
								nPMax++;
							}
							else if(s != 0) {
								nOConsec++;
								nOMax++;
							}
							else {
								nPMax++;
								nOMax++;
							}
						}
						else {
							if(s == 0) {
								consecFlag = 0;
								if(currentState == 0) {
									nPMax++;
									nOMax++;
								}
								else if(currentState == playerNum) {
									nPMax++;
								}
								else {
									nOMax++;
								}
							}
							else if (s == playerNum) {
								if(consecFlag == playerNum) {
									nPConsec++;
								}
								if(currentState == 0) {
									currentState = s;
									nPMax++;
								}
								else if(currentState == playerNum) {
									nPMax++;
								}
								else {
									break;
								}
							}
							else {
								if(consecFlag != playerNum && consecFlag != 0) {
									nOConsec++;
								}
								if(currentState == 0) {
									currentState = s;
									nOMax++;
								}
								else if(currentState != playerNum) {
									nOMax++;
								}
								else {
									break;
								}
							}
						}
					}					
				}
				if(nPConsec == 3) {
					value = Integer.MAX_VALUE;
					break;
				}
				else if(nOConsec >= 3) {
					value = Integer.MAX_VALUE - 1;
					break;
				}
				if(nPConsec == 2 && nPMax >= 3) {
					value += 5;
				}
				else if(nPConsec == 1 && nPMax >= 3) {
					value += 3;
				}
				else if(nPMax >= 3) {
					value += 1;
				}
				if(nOConsec == 2 && nOMax >= 3) {
					value += 4;
				}
				else if(nOConsec == 1 && nOMax >= 3) {
					value += 2;
				}
			}
			
			cMovePos = b.moveInDirection(cMovePos, Direction.NORTH);
			//check and adjust for one-up values
			
			
			moveValues[c] = value;
			if(value > topValue) {
				topValue = value;
			}
		}
		ArrayList<Integer> bestMoves = new ArrayList<Integer>(); 
		for(int i = 0; i < moveValues.length; i++) {
			if(moveValues[i] == topValue) {
				bestMoves.add(i);
			}
		}
		Random random = new Random();
		int moveCol = bestMoves.get(random.nextInt(bestMoves.size()));
		MoveInfo result = b.makeMove(moveCol, playerNum);
		if(result.getResult() == MoveResult.INVALID) {
			System.out.println("Impossible, should not make invalid move.");
			System.exit(0);
			return null;
		}
		return result;
	}
	*/

	@Override
	public void startRound(int playerNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endRound(int playerNum, int winnerNum) {
		// TODO Auto-generated method stub
		
	}
	
	public int getMoveValue(int playerNum, Board b, int colNum, int rowNum) {
		BoardPos cMovePos = new BoardPos(rowNum, colNum);
		int value = 0;
		for(int i = 0; i < DIRCHECKARR.length; i++) {
			int nPConsec = 0;
			int nOConsec = 0;
			int nPMax = 0;
			int nOMax = 0;
			for(int j = 0; j < DIRCHECKARR[i].length; j++) {
				BoardPos nextOver = cMovePos;
				int consecFlag = -1;
				int currentState  = -1;
				for(int n = 0; n < 3; n++) {
					nextOver = b.moveInDirection(nextOver, DIRCHECKARR[i][j]);
					if(nextOver == null) {
						break;
					}
					int s = b.getPosState(nextOver);
					if(currentState == -1) {
						currentState = s;
						consecFlag = s;
						if(s == playerNum) {
							nPConsec++;
							nPMax++;
						}
						else if(s != 0) {
							nOConsec++;
							nOMax++;
						}
						else {
							nPMax++;
							nOMax++;
						}
					}
					else {
						if(s == 0) {
							consecFlag = 0;
							if(currentState == 0) {
								nPMax++;
								nOMax++;
							}
							else if(currentState == playerNum) {
								nPMax++;
							}
							else {
								nOMax++;
							}
						}
						else if (s == playerNum) {
							if(consecFlag == playerNum) {
								nPConsec++;
							}
							if(currentState == 0) {
								currentState = s;
								nPMax++;
							}
							else if(currentState == playerNum) {
								nPMax++;
							}
							else {
								break;
							}
						}
						else {
							if(consecFlag != playerNum && consecFlag != 0) {
								nOConsec++;
							}
							if(currentState == 0) {
								currentState = s;
								nOMax++;
							}
							else if(currentState != playerNum) {
								nOMax++;
							}
							else {
								break;
							}
						}
					}
				}					
			}
			if(nPConsec >= 3) {
				return Integer.MAX_VALUE;
			}
			if(nPConsec == 2 && nPMax >= 3) {
				value += 25;
			}
			else if(nPConsec == 1 && nPMax >= 3) {
				value += 9;
			}
			else if(nPMax >= 3) {
				value += 1;
			}
			if(nOConsec >= 3) {
				value += 36;
			}
			else if(nOConsec == 2 && nOMax >= 3) {
				value += 16;
			}
			else if(nOConsec == 1 && nOMax >= 3) {
				value += 4;
			}
		}
		return value;
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
