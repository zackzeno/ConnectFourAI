package gameComponents;

import aiPrograms.DecisionTreeAI;
import aiPrograms.FiveLayerOffDefAI;
import aiPrograms.FourLayerNeuralNetAI;
import aiPrograms.MegaNetAI;
import aiPrograms.MinimaxAI;
import aiPrograms.MoveValueHeuristicAI;
import aiPrograms.RandomMoveAI;
import aiPrograms.SimpleNeuralNetAI;
import enums.MoveResult;

public class Player {
	// TODO add new AI type(s) here
	public static final String[] playerTypes = {"Human", "Random Mover", "Value Heuristic", "Simple Neural Net", "Four Layer Net", 
		"Five Layer Net", "Mega Net", "Minimax", "Decision Tree"}; 
	public static final String[] observerTypes = {"None", "Mega Net"};
	public static final String[] neuralNetTypes = {"Four Layer Net", "Five Layer Net", "Mega Net", "Simple Neural Net"};
	
	
	private int playerNum;
	private boolean isAI;
	private AIProgram ai;
	private final Object moveLock = new Object();
	private boolean waitForClick;
	private int currentMoveColNum;
	
	public Player(AIProgram a, int n) {
		if(a == null) {
			isAI = false;
		}
		else {
			isAI = true;
		}
		playerNum = n;
		ai = a;
		waitForClick = false;
	}
	
	public static AIProgram createAI(String aiType, Object[] params, boolean logAI) {
		if(aiType.equals("Human")) {
			return null;
		}
		else if(aiType.equals("Random Mover")) {
			return new RandomMoveAI();
		}
		else if(aiType.equals("Value Heuristic")) {
			return new MoveValueHeuristicAI();
		}
		else if(aiType.equals("Simple Neural Net")) {
			return new SimpleNeuralNetAI((String)params[0], (String)params[1], logAI);
		}
		else if(aiType.equals("Four Layer Net")) {
			return new FourLayerNeuralNetAI((String)params[0], (String)params[1], logAI);
		}
		else if(aiType.equals("Five Layer Net")) {
			return new FiveLayerOffDefAI((String)params[0], (String)params[1], logAI);
		}
		else if(aiType.equals("Mega Net")) {
			return new MegaNetAI((String)params[0], (String)params[1], logAI);
		}
		// TODO insert 'else if' for each new ai type here (copy above syntax)
		else if(aiType.equals("Minimax")) {
			return new MinimaxAI((Integer)params[0], (Integer)params[1], (Integer)params[2]);
		}
		else if(aiType.equals("Decision Tree")) {
			return new DecisionTreeAI((String)params[0], (String)params[1], logAI);
		}
		return null;
	}
	
	public boolean isAI() {
		return isAI;
	}
	
	public MoveInfo makeMove(Board b, double aiMoveDelay) {
		if(isAI) {
			try {
				//have AI 'simulate thinking' for x seconds for viewing purposes
				Thread.sleep((long)(aiMoveDelay*1000));
				return ai.makeMove(playerNum, b);
			} 
			catch (InterruptedException e) {
				return null;
			}
		}
		else {
			while(true) {
				if(waitForClick()) {
					return null;
				}
				MoveInfo result = b.makeMove(currentMoveColNum, playerNum);
				if(result.getResult() != MoveResult.INVALID) {
					return result;
				}
			}
			
		}
	}
	
	public void startRound() {
		if(isAI) {
			ai.startRound(playerNum);
		}
	}
	
	public void endRound(int winnerNum) {
		if(isAI) {
			ai.endRound(playerNum, winnerNum);
		}
	}
	
	public void startGame() {
		if(isAI) {
			ai.startGame(playerNum);
		}
	}
	
	public void endGame() {
		if(isAI) {
			ai.endGame(playerNum);
		}
	}
	
	public boolean waitForClick() {
		synchronized(moveLock) {
			waitForClick = true;
			while(waitForClick) {
				try {
					moveLock.wait();
				}
				catch(InterruptedException e) {
					return true;
				}
			}
		}
		return false;
	}
	
	public synchronized void handleClick(int colNum) {
		synchronized(moveLock) {
			if(waitForClick) {
				currentMoveColNum = colNum;
				waitForClick = false;
				moveLock.notify();
			}
		}
	}
}
