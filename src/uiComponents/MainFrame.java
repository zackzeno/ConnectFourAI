package uiComponents;

import java.awt.Dimension;

import gameComponents.Board;
import gameComponents.Game;
import gameComponents.Player;

import javax.swing.JFrame;
import javax.swing.JPanel;

import aiPrograms.RandomMoveAI;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static JPanel openPanel;
	private static MainFrame instance;
	
	public static MainFrame getMainFrame() {
		return instance;
	}
	
	public static JPanel getMainPanel() {
		return openPanel;
	}
	
	public static void initiateFrame() {
		instance = new MainFrame();
		instance.setSize(1200, 750);
		instance.setMinimumSize(new Dimension(1000, 600));
		instance.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		instance.setVisible(true);
		Game firstGame = new Game(new Board(6, 6, 4), 
				new Player[] { new Player(null, 1), new Player(new RandomMoveAI(), 2) }, 1, 3, 5);
		openPanel = new GamePanel(firstGame);
		instance.add(openPanel);
		firstGame.getBoard().setGame(firstGame);
		firstGame.startGame();
	}
}
