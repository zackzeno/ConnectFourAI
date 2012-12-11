package aiPrograms;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import neuralNetComponents.NeuralNet;
import neuralNetComponents.NeuralNetEdge;
import neuralNetComponents.NeuralNetNode;
import neuralNetComponents.OffDefJoinNode;
import neuralNetComponents.PositionNode;
import utils.AIUtils;
import utils.BoardPos;
import utils.BoardSize;
import gameComponents.Board;
import gameComponents.MoveInfo;

public class SimpleNeuralNetAI extends AbstractNeuralNet {

	private Random r;
	private double epsilon;
	private BoardSize boardSize;
	private int previousBoardValue;
	private BoardPos previousMovePos;
	
	public SimpleNeuralNetAI(String loadFile, String saveFile, boolean logging) {
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
	
	//initiate for 3-layer
	public void initiateNeuralNet(Board board) {
		NeuralNetNode[][] nodes = new NeuralNetNode[3][];
		PositionNode[] inputLayerOff = new PositionNode[boardSize.cols*boardSize.rows];
		for(int i = 0; i < inputLayerOff.length; i++) {
			inputLayerOff[i] = new PositionNode(indexToPos(i));
		}
		OffDefJoinNode[] outputLayerOff = new OffDefJoinNode[boardSize.cols*boardSize.rows];
		for(int i = 0; i < outputLayerOff.length; i++) {
			outputLayerOff[i] = new OffDefJoinNode(indexToPos(i));
		}
		for(int i = 0; i < inputLayerOff.length; i++) {
			for(int j = 0; j < outputLayerOff.length; j++) {
				NeuralNetEdge conEdge = new NeuralNetEdge(inputLayerOff[i], outputLayerOff[j], 0);
				inputLayerOff[i].addForwardEdge(conEdge);
				outputLayerOff[i].addBackwardEdge(conEdge);
			}
		}
		PositionNode[] inputLayerDef = new PositionNode[boardSize.cols*boardSize.rows];
		for(int i = 0; i < inputLayerDef.length; i++) {
			inputLayerDef[i] = new PositionNode(indexToPos(i));
		}
		OffDefJoinNode[] outputLayerDef = new OffDefJoinNode[boardSize.cols*boardSize.rows];
		for(int i = 0; i < outputLayerDef.length; i++) {
			outputLayerDef[i] = new OffDefJoinNode(indexToPos(i));
		}
		for(int i = 0; i < inputLayerDef.length; i++) {
			for(int j = 0; j < outputLayerDef.length; j++) {
				NeuralNetEdge conEdge = new NeuralNetEdge(inputLayerDef[i], outputLayerDef[j], 0);
				inputLayerDef[i].addForwardEdge(conEdge);
				outputLayerDef[i].addBackwardEdge(conEdge);
			}
		}
		PositionNode[] outputLayer = new PositionNode[boardSize.cols*boardSize.rows];
		for(int i = 0; i < outputLayer.length; i++) {
			outputLayer[i] = new PositionNode(indexToPos(i));
		}
		for(int i = 0; i < outputLayerOff.length; i++) {
			NeuralNetEdge combineEdge = new NeuralNetEdge(outputLayerOff[i], outputLayer[i], 0);
			outputLayerOff[i].addForwardEdge(combineEdge);
			outputLayer[i].addBackwardEdge(combineEdge);
		}
		for(int i = 0; i < outputLayerDef.length; i++) {
			NeuralNetEdge combineEdge = new NeuralNetEdge(outputLayerDef[i], outputLayer[i], 0);
			outputLayerDef[i].addForwardEdge(combineEdge);
			outputLayer[i].addBackwardEdge(combineEdge);
		}
		nodes[0] = (NeuralNetNode[])AIUtils.arrayConcatenate(new PositionNode[2*inputLayerOff.length], inputLayerOff, inputLayerDef);
		nodes[1] = (NeuralNetNode[])AIUtils.arrayConcatenate(new OffDefJoinNode[2*outputLayerOff.length], outputLayerOff, outputLayerDef);
		nodes[2] = outputLayer;
		net = new NeuralNet(nodes);
	}
	
	public void updateNeuralNet(int valueDifference) {
		if(valueDifference == 0) {
			return;
		}
		if(logOut != null) {
			try {
				logOut.write("Updating based on previous move in (row, column) (" + 
						previousMovePos.rowIndex + ", " + previousMovePos.colIndex + ") by value of " + valueDifference + "\n\n");
				logOut.flush();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		double[] modifiers = new double[boardSize.cols*boardSize.rows];
		for(int i = 0; i < modifiers.length; i++) {
			modifiers[i] = 0;
		}
		modifiers[posToIndex(previousMovePos)] = valueDifference;
		net.backPropagate(modifiers);
	}
	
	public int posToIndex(BoardPos pos) {
		return (pos.colIndex*boardSize.rows + pos.rowIndex);
	}
	
	public BoardPos indexToPos(int index) {
		return new BoardPos(index % boardSize.rows, index / boardSize.rows);
	}
	
	public BoardPos[] getNeuralNetMove(Board currentBoard, int playerNum) {
		double[] inputs = new double[2*boardSize.rows*boardSize.cols];
		double[] outputs = new double[boardSize.rows*boardSize.cols];
		for(int i = 0; i < boardSize.rows; i++) {
			for(int j = 0; j < boardSize.cols; j++) {
				BoardPos currentPos = new BoardPos(i, j);
				int posState = currentBoard.getPosState(currentPos);
				inputs[posToIndex(currentPos)] = getNeuralNetInput(posState, playerNum, true);
				inputs[posToIndex(currentPos) + boardSize.rows*boardSize.cols] = getNeuralNetInput(posState, playerNum, false); 
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
	
	public int getNeuralNetInput(int state, int playerNum, boolean offense) {
		if(offense) {
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
		else {
			if(state == playerNum) {
				return -1;
			}
			else if(state == 0) {
				return 0;
			}
			else {
				return 1;
			}
		}
	}
}