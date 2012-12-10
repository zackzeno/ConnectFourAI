package gameComponents;

import java.util.ArrayList;

import aiPrograms.interfaces.Teachable;
import enums.MoveResult;
import uiComponents.GamePanel;
import uiComponents.MainFrame;

public class Game implements Runnable {
	private Player[] players;
	private Teachable observer;
	private Board board;
	private int nRounds;
	private double aiMoveDelay;
	private double aiGameDelay;
	
	private boolean p1Turn;
	private int p1Wins;
	private int p2Wins;
	private int nRoundsPlayed;
	
	private static Thread activeGameThread = null;
	
	public Game(Board b, Player[] p, int n, double am, double ag, Teachable o) {
		players = p;
		observer = o;
		board = b;
		nRounds = n;
		aiMoveDelay = am;
		aiGameDelay = ag;
		
		p1Wins = 0;
		p2Wins = 0;
		p1Turn = true;
		nRoundsPlayed = 0;
	}
	
	public Board getBoard() {
		return board;
	}
	
	public int getNRounds() {
		return nRounds;
	}
	
	public int getNRoundsPlayed() {
		return nRoundsPlayed;
	}
	
	public int getP1Wins() {
		return p1Wins;
	}
	
	public int getP2Wins() {
		return p2Wins;
	}
	
	public void startGame() {
		if(activeGameThread != null && activeGameThread.isAlive()) {
			activeGameThread.interrupt();
			try {
				activeGameThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
				System.exit(0);
			}
		}
		((GamePanel)MainFrame.getMainPanel()).setupNewGame(this);
		new Thread(this).start();
	}
	
	public void handleClick(int clickCol) {
		if(p1Turn) {
			players[0].handleClick(clickCol);
		}
		else {
			players[1].handleClick(clickCol);		
		}
	}

	@Override
	public void run() {
		activeGameThread = Thread.currentThread();
		int winningPlayer = -1;
		players[0].startGame();
		players[1].startGame();
		for(int n = 1; n <= nRounds; n++) {
			ArrayList<MoveInfo> moveList = null;
			if(observer != null) {
				moveList = new ArrayList<MoveInfo>();
			}
			players[0].startRound();
			players[1].startRound();
			((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setGameScoreLabel("P1: " + p1Wins + " wins, P2: " 
					+ p2Wins + " wins, R: " + n + "/" + nRounds);
			switch(winningPlayer) {
				case 0:
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("Last round tied.");
					break;
				case 1:
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("Last round won by P1.");
					break;
				case 2:
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("Last round won by P2.");
					break;
				default:
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("No previous round.");
					break;
			}
			((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastMoveLabel("No last move.");
			if(p1Turn) {
				((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setProgressLabel("Player 1's Turn.");
			}
			else {
				((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setProgressLabel("Player 2's Turn.");
			}
			MainFrame.getMainFrame().repaint();
			while(true) {
				if(Thread.interrupted()) {
					return;
				}
				if(((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().checkPause()) {
					return;
				}
				double nmd = ((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().getAIMoveDelay();
				if(nmd >= 0) {
					aiMoveDelay = nmd;
				}
				double ngd = ((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().getAIGameDelay();
				if(ngd >= 0) {
					aiGameDelay = ngd;
				}
				MoveInfo result;
				if(p1Turn) {
					result = players[0].makeMove(board, aiMoveDelay);
				}
				else {
					result = players[1].makeMove(board, aiMoveDelay);
				}
				if(result == null) {
					return;
				}
				if(observer != null) {
					moveList.add(result);
				}
				p1Turn = !p1Turn;
				((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastMoveLabel(result.getMoveText());
				if(result.getResult() == MoveResult.WIN || result.getResult() == MoveResult.FULL_BOARD) {
					if(result.getResult() == MoveResult.FULL_BOARD) {
						winningPlayer = 0;
					}
					else if(!p1Turn) {
						winningPlayer = 1;
					}
					else {
						winningPlayer = 2;
					}
					break;
				}
				if(p1Turn) {
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setProgressLabel("Player 1's Turn");
				}
				else {
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setProgressLabel("Player 2's Turn");
				}
				MainFrame.getMainFrame().repaint();
			}
			players[0].endRound(winningPlayer);
			players[1].endRound(winningPlayer);
			nRoundsPlayed++;
			if(observer != null) {
				observer.teachNeuralNet(moveList.toArray(new MoveInfo[0]), winningPlayer);
			}
			switch(winningPlayer) {
				case 1:
					p1Wins++;
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setProgressLabel("Player 1 wins!");
					break;
				case 2:
					p2Wins++;
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setProgressLabel("Player 2 wins!");
					break;
				default:
					((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setProgressLabel("Game tied.");
					break;
			}
			MainFrame.getMainFrame().repaint();
			if(!(players[0].isAI() && players[1].isAI())) {
				if(((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().waitForNextRoundClick()) {
					return;
				}
			}
			else {
				try {
					Thread.sleep((long)(aiGameDelay*1000));
				} 
				catch (InterruptedException e) {
					return;
				}
			}
			if(n != nRounds) {
				board.resetBoard();
			}
		}
		((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setGameScoreLabel("P1: " + p1Wins + " wins, P2: " 
				+ p2Wins + " wins, complete (" + nRounds + ")");
		switch(winningPlayer) {
			case 0:
				((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("Last round tied.");
				break;
			case 1:
				((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("Last round won by P1.");
				break;
			case 2:
				((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("Last round won by P2.");
				break;
			default:
				((GamePanel)MainFrame.getMainPanel()).getOptionsPanel().setLastRoundLabel("No previous round.");
				break;
		}
		players[0].endGame();
		players[1].endGame();
		MainFrame.getMainFrame().repaint();
	}
}