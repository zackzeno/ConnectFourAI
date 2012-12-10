package uiComponents;

import java.awt.Graphics;

import gameComponents.Game;

import javax.swing.JPanel;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private GameOptionsPanel leftPanel;
	private BoardPanel boardPanel;
	
	public GamePanel(Game g) {
		leftPanel = new GameOptionsPanel();
		boardPanel = new BoardPanel(g.getBoard());
		this.add(leftPanel);
		this.add(boardPanel);
	}
	
	public void setupNewGame(Game g) {
		this.removeAll();
		leftPanel.resetOptionsPanel();
		this.add(leftPanel);
		boardPanel = new BoardPanel(g.getBoard());
		this.add(boardPanel);
		revalidate();
		repaint();
	}
	
	public GameOptionsPanel getOptionsPanel() {
		return leftPanel;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		this.setBounds(0, 0, this.getParent().getWidth(), this.getParent().getHeight());
		leftPanel.setBounds(0, 0, this.getParent().getWidth()/5, this.getParent().getHeight());
		boardPanel.setBounds(this.getParent().getWidth()/5, 0, 4*(this.getParent().getWidth()/5), this.getParent().getHeight()); 
	}
	
}
