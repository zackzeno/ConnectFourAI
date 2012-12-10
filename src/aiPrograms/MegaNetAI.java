package aiPrograms;

import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.MoveInfo;
import neuralNetComponents.NeuralNet;
import neuralNetComponents.NeuralNetEdge;
import neuralNetComponents.NeuralNetNode;
import neuralNetComponents.meganet.OutputNode;
import neuralNetComponents.meganet.PositionInputGroup;
import utils.BoardPos;
import utils.BoardSize;

public class MegaNetAI implements AIProgram {

	
	private BoardSize boardSize;
	private NeuralNet net;
	
	private boolean initiated;
	
	public MegaNetAI() {
		initiated = false;
	}
	
	@Override
	public void startRound(int playerNum) {
		
	}

	@Override
	public void endRound(int playerNum, int winnerNum) {
		
	}
	
	@Override
	public MoveInfo makeMove(int playerNum, Board board) {
		return null;
		
	}
	
	private void addEdgeToAll(NeuralNetNode[] targets, NeuralNetNode source) {
		for(NeuralNetNode n : targets) {
			NeuralNetEdge e = new NeuralNetEdge(source, n, 1);
			source.addForwardEdge(e);
			n.addBackwardEdge(e);
		}
	}
	
	private void addEdge(NeuralNetNode from, NeuralNetNode to, double w) {
		NeuralNetEdge e = new NeuralNetEdge(from, to, w);
		from.addForwardEdge(e);
		to.addBackwardEdge(e);
	}
	
	public void initiateNeuralNet() {
		OutputNode[] outputs = new OutputNode[boardSize.cols];
		for(int i = 0; i < outputs.length; i++) {
			outputs[i] = new OutputNode();
		}
		
		PositionInputGroup[] inputs = new PositionInputGroup[boardSize.cols * boardSize.rows];
		for(int i = 0; i < inputs.length; i++) {
			inputs[i] = new PositionInputGroup();
			addEdgeToAll(outputs, inputs[i].player);
			addEdgeToAll(outputs, inputs[i].empty);
			addEdgeToAll(outputs, inputs[i].opp);
		}
		
		
		
		
		
	}
	
	public void updateNeuralNet(int valueDifference) {
		
	}
	
	public BoardPos getNeuralNetMove(Board currentBoard, int playerNum) {
		return null;
		
	}
	
	public int getNeuralNetInput(int colIndex, int rowIndex, Board currentBoard, int playerNum) {
		return playerNum;
		
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
