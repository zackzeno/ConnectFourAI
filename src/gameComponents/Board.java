package gameComponents;

import enums.Direction;
import enums.MoveResult;

import utils.BoardPos;
import utils.BoardSize;

public class Board {
	private BoardSize size;
	private int nToWin;
	private Game assGame;
	
	public static final Direction[][] WINCHECKARR = {{Direction.NORTHEAST, Direction.SOUTHWEST}, 
			{Direction.NORTHWEST, Direction.SOUTHEAST}, {Direction.WEST, Direction.EAST}, {Direction.SOUTH}};
	
	/*
	 * NOTE: this is indexed by column first, then row
	 * i.e. boardState[2][5] is 3rd column, 6th row
	 * done to better fit game parameters 
	 */
	private int[][] boardState;
	
	public Board(int rows, int cols, int n) {
		size = new BoardSize(rows, cols);
		boardState = new int[cols][rows];
		for(int i = 0; i < cols; i++) {
			for(int j = 0; j < rows; j++) {
				boardState[i][j] = 0;
			}
		}
		nToWin = n;
	}
	
	public Board deepCopy() {
		Board copy = new Board(size.rows, size.cols, nToWin);
		for(int i = 0; i < size.cols; i++) {
			for(int j = 0; j < size.rows; j++) {
				copy.setState(i, j, boardState[i][j]);
			}
		}
		return copy;
	}
	
	public int getEmptyRow(int colNum) {
		for(int r = size.rows - 1; r >= 0; r--) {
			if(getPosState(colNum, r) == 0) {
				return r;
			}
		}
		return -1;
	}
	
	public void setState(int col, int row, int value) {
		boardState[col][row] = value;
	}
	
	public void setGame(Game g) {
		assGame = g;
	}
	
	public Game getGame() {
		return assGame;
	}
	
	public BoardSize getSize() {
		return size;
	}
	
	public int getPosState(BoardPos pos) {
		return boardState[pos.colIndex][pos.rowIndex];
	}
	
	public int getPosState(int colIndex, int rowIndex) {
		return boardState[colIndex][rowIndex];
	}
	
	public void resetBoard() {
		boardState = new int[size.cols][size.rows];
		for(int i = 0; i < size.cols; i++) {
			for(int j = 0; j < size.rows; j++) {
				boardState[i][j] = 0;
			}
		}
	}
	
	public boolean isFeasibleMove(int colIndex, int rowIndex) {
		BoardPos currentPos = new BoardPos(rowIndex, colIndex);
		if(getPosState(currentPos) == 0) {
			BoardPos oneDown = moveInDirection(currentPos, Direction.SOUTH);
			if(oneDown == null || getPosState(oneDown) != 0) {
				return true;
			}
		}
		return false;
	}
	
	public int checkGameComplete(BoardPos lastMove) {
		int consec;
		BoardPos currentPos;
		int playerNum = boardState[lastMove.colIndex][lastMove.rowIndex];
		for(int i = 0; i < WINCHECKARR.length; i++) {
			consec = 1;
			for(int j = 0; j < WINCHECKARR[i].length; j++) {
				currentPos = lastMove;
				while(true) {
					currentPos = moveInDirection(currentPos, WINCHECKARR[i][j]);
					if(currentPos == null || getPosState(currentPos) != playerNum) {
						break;
					}
					consec++;
				}
			}
			if(consec >= nToWin) {
				return 1;
			}
		}
		for(int i = 0; i < size.cols; i++) {
			if(boardState[i][0] == 0) {
				return 0;
			}
		}
		return -1;
	}
	
	public BoardPos moveInDirection(BoardPos currentPos, Direction dir) {
		switch (dir) {
			case NORTH: 
				if(currentPos.rowIndex == 0) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex - 1, currentPos.colIndex);
			case NORTHEAST:
				if(currentPos.rowIndex == 0 || currentPos.colIndex == size.cols - 1) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex - 1, currentPos.colIndex + 1);
			case EAST:
				if(currentPos.colIndex == size.cols - 1) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex, currentPos.colIndex + 1);
			case SOUTHEAST:
				if(currentPos.rowIndex == size.rows - 1 || currentPos.colIndex == size.cols - 1) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex + 1, currentPos.colIndex + 1);
			case SOUTH:
				if(currentPos.rowIndex == size.rows - 1) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex + 1, currentPos.colIndex);
			case SOUTHWEST:
				if(currentPos.rowIndex == size.rows - 1 || currentPos.colIndex == 0) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex + 1, currentPos.colIndex - 1);
			case WEST:
				if(currentPos.colIndex == 0) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex, currentPos.colIndex - 1);
			case NORTHWEST:
				if(currentPos.rowIndex == 0 || currentPos.colIndex == 0) {
					return null;
				}
				return new BoardPos(currentPos.rowIndex - 1, currentPos.colIndex - 1);
		}
		return null;
	}
	
	/*
	 * Called when a given player makes a move (drops a piece) in the given column
	 */
	public MoveInfo makeMove(int colNum, int playerNum) {
		if(boardState[colNum][0] != 0) {
			return new MoveInfo(MoveResult.INVALID, playerNum, colNum);
		}
		int pos = size.rows - 1;
		while(true) {
			if(boardState[colNum][pos] == 0) {
				boardState[colNum][pos] = playerNum;
				break;
			}
			pos--;
		}
		//check to see if player has won or if board is full
		BoardPos movePos = new BoardPos(pos, colNum);
		int consec;
		BoardPos currentPos;
		for(int i = 0; i < WINCHECKARR.length; i++) {
			consec = 1;
			for(int j = 0; j < WINCHECKARR[i].length; j++) {
				currentPos = movePos;
				while(true) {
					currentPos = moveInDirection(currentPos, WINCHECKARR[i][j]);
					if(currentPos == null || getPosState(currentPos) != playerNum) {
						break;
					}
					consec++;
				}
			}
			if(consec >= nToWin) {
				return new MoveInfo(MoveResult.WIN, playerNum, colNum);
			}
		}
		for(int i = 0; i < size.cols; i++) {
			if(boardState[i][0] == 0) {
				return new MoveInfo(MoveResult.DEFAULT, playerNum, colNum);
			}
		}
		return new MoveInfo(MoveResult.FULL_BOARD, playerNum, colNum);
	}	
}
