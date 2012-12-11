package aiPrograms;

import gameComponents.Board;
import gameComponents.MoveInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import neuralNetComponents.NeuralNet;
import neuralNetComponents.NeuralNetNode;
import neuralNetComponents.meganet.NeuralNetLayer;
import neuralNetComponents.meganet.PositionInputGroup;
import neuralNetComponents.meganet.SigmoidNode;
import neuralNetComponents.meganet.SoftmaxLayer;
import neuralNetComponents.meganet.SoftmaxNode;
import utils.BoardSize;
import aiPrograms.interfaces.Teachable;

public class MegaNetAI extends AbstractNeuralNet implements Teachable{

	
	private BoardSize boardSize;
	
	
	private PositionInputGroup[] inputLayer;
	private List<NeuralNetLayer> hiddenLayers;
	private SoftmaxLayer outputLayer;
	
	
	public MegaNetAI(String loadFile, String saveFile, boolean logging) {
		super(loadFile, saveFile, logging);
	}
	
	@Override
	public void startRound(int playerNum) {
		
	}

	@Override
	public void endRound(int playerNum, int winnerNum) {
		
	}
	
	@Override
	public MoveInfo makeMove(int playerNum, Board board) {
		if(!initiated) {
			boardSize = board.getSize();
			if(net == null) {
				initiateNeuralNet();
			}
			initiated = true;
		}
		
		double[] outputs = net.forwardPropagate(getInputsFromBoard(board, playerNum));
		
		int bestMove = -1;
		double bestScore = -1;
		for(int i = 0; i < outputs.length; i++) {
			if(board.isFeasibleMove(i) && outputs[i] > bestScore) {
				bestMove = i;
				bestScore = outputs[i];
			}
		}
		
		
		return board.makeMove(bestMove, playerNum);
		
	}
	
	public static double[] getInputsFromBoard(Board b, int playerNum) {
		BoardSize boardSize = b.getSize();
		double[] inputs = new double[boardSize.cols * boardSize.rows * 3];
		
		for(int i = 0; i < boardSize.cols; i++) {
			int colIndex = i * (boardSize.rows * 3);
			for(int j = 0; j < boardSize.rows; j++) {
				int index = colIndex + (j * 3);
				
				if(b.getPosState(i, j) == playerNum) {
					inputs[index] = 1;
				}
				else if(b.getPosState(i, j) == 0) {
					inputs[index + 1] = 1;
				}
				else {
					inputs[index + 2] = 1;
				}
			}
		}
		
		return inputs;
	}
	
	@Override
	public void teachNeuralNet(BoardSize boardSize, MoveInfo[] moves, int winnerNum) {
		Board b = new Board(boardSize.rows, boardSize.cols, 4);
		
		if(!initiated) {
			this.boardSize = boardSize;
			if(net == null) {
				initiateNeuralNet();
			}
			initiated = true;
		}
		
		for(int i = 0; i < moves.length; i++) {

			double[] inputs = getInputsFromBoard(b, moves[i].getPlayerNo());
			
			double[] outputs = net.forwardPropagate(inputs);
			
			double[] expected = new double[outputs.length];
			double[] error = new double[outputs.length];
			
			expected[moves[i].getColumn()] = 1;
			for(int j = 0; j < error.length; j++) {
				error[j] = expected[j] - outputs[j];
			}
			
			if(moves[i].getPlayerNo() == winnerNum) {
				net.backPropagate(error);
			}
			
			
			b.makeMove(moves[i].getColumn(), moves[i].getPlayerNo());
		}
		
	}
	
	public List<NeuralNetLayer> createHiddenLayers(NeuralNetLayer outputLayer, int ... sizes) {
		ArrayList<NeuralNetLayer> hidden = new ArrayList<NeuralNetLayer>();
		
		NeuralNetLayer prev = outputLayer;
		for(int i = sizes.length - 1; i >= 0; i--) {
			NeuralNetLayer l = new NeuralNetLayer();
			for(int j = 0; j < sizes[i]; j++) {
				SigmoidNode n = new SigmoidNode(l);
				prev.addEdgeToAll(n);
			}
			prev = l;
			hidden.add(0, l);
		}
		
		return hidden;
	}
	
	public void initiateNeuralNet() {
		outputLayer = new SoftmaxLayer();
		for(int i = 0; i < boardSize.cols; i++) {
			new SoftmaxNode(outputLayer);
		}
		
		int numPositions = boardSize.cols * boardSize.rows * 3;
		
		hiddenLayers = createHiddenLayers(outputLayer, numPositions * 2);
		
		inputLayer = new PositionInputGroup[boardSize.cols * boardSize.rows];
		for(int i = 0; i < inputLayer.length; i++) {
			inputLayer[i] = new PositionInputGroup();

			hiddenLayers.get(0).addEdgeToAll(inputLayer[i].player);
			hiddenLayers.get(0).addEdgeToAll(inputLayer[i].empty);
			hiddenLayers.get(0).addEdgeToAll(inputLayer[i].opp);
		}
		
		NeuralNetNode[][] nodes = new NeuralNetNode[1 + hiddenLayers.size() + 1][];
		
		nodes[0] = new NeuralNetNode[inputLayer.length * 3];
		for(int i = 0; i < inputLayer.length; i++) {
			nodes[0][3 * i] = inputLayer[i].player;
			nodes[0][3 * i + 1] = inputLayer[i].empty;
			nodes[0][3 * i + 2] = inputLayer[i].opp;
		}
		
		for(int i = 0; i < hiddenLayers.size(); i++) {
			nodes[i + 1] = hiddenLayers.get(i).getNodeArray();
		}
		
		nodes[nodes.length - 1] = outputLayer.getNodeArray();
		
		net = new NeuralNet(nodes);
	}

	@Override
	public void startGame() {
		super.startGame(0);
		
	}

	@Override
	public void endGame() {
		super.endGame(0);
		
		if(logOut != null) {
			try {
				System.out.println("start log");
				logOut.write(net.getNetInfo());
				logOut.flush();
				System.out.println("log printed");
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("no log");
		}
	}
}
