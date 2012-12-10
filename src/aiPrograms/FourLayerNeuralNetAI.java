package aiPrograms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import neuralNetComponents.FourGroupNode;
import neuralNetComponents.NeuralNet;
import neuralNetComponents.NeuralNetEdge;
import neuralNetComponents.NeuralNetNode;
import neuralNetComponents.PositionNode;
import utils.AIUtils;
import utils.BoardPos;
import utils.BoardSize;
import gameComponents.Board;
import gameComponents.MoveInfo;

public class FourLayerNeuralNetAI extends AbstractNeuralNet  {
	
	private Random r;
	private double epsilon;
	private BoardSize boardSize;
	private int previousBoardValue;
	private BoardPos previousMovePos;
	
	public FourLayerNeuralNetAI(String loadFile, String saveFile, boolean logging) {
		super(loadFile, saveFile, logging);
		r = new Random();
		epsilon = .01;
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
		int currentBoardValue = AIUtils.minimax(playerNum, board, null, 1, 3, true, 1, 1);
		if(!initiated) {
			boardSize = board.getSize();
			if(net == null) {
				initiateNeuralNet(board);
			}
			initiated = true;
		}
		if(previousMovePos != null) {
			int difference;
			if(currentBoardValue == Integer.MAX_VALUE && previousBoardValue == Integer.MAX_VALUE) {
				difference = 0;
			}
			else if(currentBoardValue == Integer.MAX_VALUE) {
				difference = 10;
			}
			else if(previousBoardValue == Integer.MAX_VALUE) {
				difference = -10;
			}
			else if(currentBoardValue == Integer.MIN_VALUE && previousBoardValue == Integer.MIN_VALUE) {
				difference = 0;
			}
			else if(currentBoardValue == Integer.MIN_VALUE) {
				difference = -10;
			}
			else if(previousBoardValue == Integer.MIN_VALUE) {
				difference = 10;
			}
			else {
				difference = currentBoardValue - previousBoardValue;
			}
			updateNeuralNet(difference);
		}
		BoardPos movePosArr[] = getNeuralNetMove(board, playerNum);
		int moveInd = r.nextInt(movePosArr.length);
		MoveInfo moveInfo = board.makeMove(movePosArr[moveInd].colIndex, playerNum);
		previousBoardValue = currentBoardValue;
		previousMovePos = movePosArr[moveInd];
		if(logOut != null) {
			try {
				logOut.write(net.getNetInfo());
				logOut.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return moveInfo;
	}
	
	private double normalizeDifference(double difference) {
		boolean negFlag = (difference < 0);
		if(negFlag) {
			difference = difference*(-1);
		}
		double nDiff = (difference/10) + 1;
		if(negFlag) {
			nDiff = 1/nDiff;
		}
		return nDiff;
	}
	
	//initiate for 4-layer
	public void initiateNeuralNet(Board board) {
		NeuralNetNode[][] nodes = new NeuralNetNode[4][];
		PositionNode[] inputLayer = new PositionNode[boardSize.cols*boardSize.rows];
		for(int i = 0; i < inputLayer.length; i++) {
			inputLayer[i] = new PositionNode(indexToPos(i));
		}
		FourGroupNode[] middleLeftLayer = AIUtils.createGroupNodes(board);
		FourGroupNode[] middleRightLayer = AIUtils.createGroupNodes(board);
		PositionNode[] outputLayer = new PositionNode[boardSize.cols*boardSize.rows];
		for(int i = 0; i < inputLayer.length; i++) {
			outputLayer[i] = new PositionNode(indexToPos(i));
		}
		for(int i = 0; i < middleLeftLayer.length; i++) { 
			for(int j = 0; j < middleLeftLayer[i].getPositions().length; j++) {
				int indexOfPos = posToIndex(middleLeftLayer[i].getPositions()[j]);
				NeuralNetEdge firstEdge = new NeuralNetEdge(inputLayer[indexOfPos], middleLeftLayer[i], 1); 	
				inputLayer[indexOfPos].addForwardEdge(firstEdge);
				middleLeftLayer[i].addBackwardEdge(firstEdge);
			}
		}
		for(int i = 0; i < middleLeftLayer.length; i++) {
			for(int j = 0; j < middleRightLayer.length; j++) {
				if(middleLeftLayer[i].overlap(middleRightLayer[j])) {
					NeuralNetEdge conEdge = new NeuralNetEdge(middleLeftLayer[i], middleRightLayer[j], 1);
					middleLeftLayer[i].addForwardEdge(conEdge);
					middleRightLayer[j].addBackwardEdge(conEdge);
				}
			}
		}
		for(int i = 0; i < middleRightLayer.length; i++) { 
			for(int j = 0; j < middleRightLayer[i].getPositions().length; j++) {
				int indexOfPos = posToIndex(middleRightLayer[i].getPositions()[j]);
				NeuralNetEdge lastEdge = new NeuralNetEdge(middleRightLayer[i], outputLayer[indexOfPos], 1); 	
				middleRightLayer[i].addForwardEdge(lastEdge);
				outputLayer[indexOfPos].addBackwardEdge(lastEdge);
			}
		}
		nodes[0] = inputLayer;
		nodes[1] = middleLeftLayer;
		nodes[2] = middleRightLayer;
		nodes[3] = outputLayer;
		net = new NeuralNet(nodes);
	}
	
	public void updateNeuralNet(int valueDifference) {
		if(valueDifference == 0) {
			return;
		}
		double normalizedDifference = normalizeDifference(valueDifference);
		if(logOut != null) {
			try {
				logOut.write("Updating based on previous move in (row, column) (" + 
						previousMovePos.rowIndex + ", " + previousMovePos.colIndex + ") by value of " + normalizedDifference + "\n\n");
				logOut.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		double[] modifiers = new double[boardSize.cols*boardSize.rows];
		for(int i = 0; i < modifiers.length; i++) {
			modifiers[i] = 1;
		}
		modifiers[posToIndex(previousMovePos)] = normalizedDifference;
		net.backPropagate(modifiers);
	}
	
	public int posToIndex(BoardPos pos) {
		return (pos.colIndex*boardSize.rows + pos.rowIndex);
	}
	
	public BoardPos indexToPos(int index) {
		return new BoardPos(index % boardSize.rows, index / boardSize.rows);
	}
	
	public BoardPos[] getNeuralNetMove(Board currentBoard, int playerNum) {
		double[] inputs = new double[boardSize.rows*boardSize.cols];
		double[] outputs = new double[boardSize.rows*boardSize.cols];
		for(int i = 0; i < boardSize.rows; i++) {
			for(int j = 0; j < boardSize.cols; j++) {
				BoardPos currentPos = new BoardPos(i, j);
				inputs[posToIndex(currentPos)] = getNeuralNetInput(currentBoard.getPosState(currentPos), playerNum);
			}
		}
		outputs = net.forwardPropagate(inputs);
		double maxOutput = Double.NEGATIVE_INFINITY;
		ArrayList<BoardPos> maxPosList = new ArrayList<BoardPos>(); 
		for(int i = 0; i < boardSize.rows; i++) {
			for(int j = 0; j < boardSize.cols; j++) {
				BoardPos currentPos = new BoardPos(i, j);
				if(currentBoard.isFeasibleMove(j, i)) {
					if(outputs[posToIndex(currentPos)] > maxOutput + epsilon) {
						maxOutput = outputs[posToIndex(currentPos)];
						maxPosList.clear();
						maxPosList.add(currentPos);
					}
					else if(outputs[posToIndex(currentPos)] >= maxOutput - epsilon) {
						maxPosList.add(currentPos);
					}
				}
			}
		}
		return maxPosList.toArray(new BoardPos[0]);
	}
	
	public int getNeuralNetInput(int state, int playerNum) {
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
}