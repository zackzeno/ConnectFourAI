package aiPrograms;

import neuralNetComponents.SimpleNeuralNetEdge;
import utils.AIUtils;
import utils.BoardPos;
import utils.BoardSize;
import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.MoveInfo;

public class SimpleNeuralNetAI implements AIProgram {

	
	private BoardSize boardSize;
	private SimpleNeuralNetEdge[] neuralNetEdges;
	
	private boolean initiated;
	private int previousBoardValue;
	private BoardPos previousMovePos;
	
	public SimpleNeuralNetAI() {
		initiated = false;
	}
	
	@Override
	public void startRound(int playerNum) {
		previousBoardValue = 0;
		previousMovePos = null;
		
	}

	@Override
	public void endRound(int playerNum, int winnerNum) {
		if(playerNum == winnerNum) {
			updateNeuralNet(10);
		}
		else if(winnerNum != 0) {
			updateNeuralNet(-10);
		}
	}
	
	@Override
	public MoveInfo makeMove(int playerNum, Board board) {
		int currentBoardValue = AIUtils.evaluateBoard(board, playerNum, 1, 1);
		if(!initiated) {
			boardSize = board.getSize();
			initiateNeuralNet();
			initiated = true;
		}
		if(previousMovePos != null) {
			updateNeuralNet(currentBoardValue - previousBoardValue);
		}
		BoardPos movePos = getNeuralNetMove(board, playerNum);
		MoveInfo moveInfo = board.makeMove(movePos.colIndex, playerNum);
		previousBoardValue = currentBoardValue;
		previousMovePos = movePos;
		return moveInfo;
	}
	
	//horribly inefficient memory-wise...fuck it
	public void initiateNeuralNet() {
		int arraySize = (int)Math.pow(boardSize.cols*boardSize.rows, 2) - boardSize.cols*boardSize.rows;
		neuralNetEdges = new SimpleNeuralNetEdge[arraySize];
		int n = 0;
		for(int i = 0; i < boardSize.cols; i++) {
			for(int j = 0; j < boardSize.rows; j++) {
				for(int u = 0; u < boardSize.cols; u++) {
					for(int v = 0; v < boardSize.rows; v++) {
						if(!(i == u && j == v)) {
							neuralNetEdges[n] = new SimpleNeuralNetEdge(new BoardPos(j, i), new BoardPos(v, u), 0);
							n++;
						}
					}
				}
			}
		}
	}
	
	public void updateNeuralNet(int valueDifference) {
		System.out.println("Updating node at pos (r, c) " + previousMovePos.rowIndex + ", " + previousMovePos.colIndex + " by " + valueDifference);
		if(valueDifference == 0) {
			return;
		}
		for(int i = 0; i < neuralNetEdges.length; i++) {
			if(neuralNetEdges[i].edgeTo.equals(previousMovePos)) {
				neuralNetEdges[i].weight += valueDifference;
			}
		}
	}
	
	public BoardPos getNeuralNetMove(Board currentBoard, int playerNum) {
		double[][] outputs = new double[boardSize.rows][boardSize.cols];
		for(int i = 0; i < boardSize.rows; i++) {
			for(int j = 0; j < boardSize.cols; j++) {
				outputs[i][j] = 0;
			}
		}
		for(int n = 0; n < neuralNetEdges.length; n++) {
			outputs[neuralNetEdges[n].edgeTo.rowIndex][neuralNetEdges[n].edgeTo.colIndex] 
					+= (getNeuralNetInput(neuralNetEdges[n].edgeFrom.colIndex, neuralNetEdges[n].edgeFrom.rowIndex, currentBoard, playerNum)*
							neuralNetEdges[n].weight); 
		}
		double maxOutput = Double.NEGATIVE_INFINITY;
		BoardPos maxPos = null;
		for(int i = 0; i < boardSize.rows; i++) {
			for(int j = 0; j < boardSize.cols; j++) {
				if(currentBoard.isFeasibleMove(j, i) && outputs[i][j] > maxOutput) {
					maxOutput = outputs[i][j];
					maxPos = new BoardPos(i, j);
				}
			}
		}
		return maxPos;
	}
	
	public int getNeuralNetInput(int colIndex, int rowIndex, Board currentBoard, int playerNum) {
		int state = currentBoard.getPosState(colIndex, rowIndex);
		if(state == playerNum) {
			return 1;
		}
		else if(state == 0) {
			return 0;
		}
		else {
			return -1;
		}
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
