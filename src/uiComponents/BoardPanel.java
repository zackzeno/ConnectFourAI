package uiComponents;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import gameComponents.Board;

import javax.swing.JPanel;

public class BoardPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Board board;
	
	public BoardPanel(Board b) {
		board = b;
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				int squareWidth = getSize().width / board.getSize().cols;
				board.getGame().handleClick(arg0.getX()/squareWidth);
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
		});
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillRect(0, 0, getSize().width, getSize().height);
		int squareWidth = getSize().width / board.getSize().cols;
		int squareHeight = getSize().height / board.getSize().rows;
		int circleDiameter = (int)(Math.min(squareWidth, squareHeight)*(.8));
		int widthPadding = (squareWidth - circleDiameter)/2;
		int heightPadding = (squareHeight - circleDiameter)/2;;
		for(int i = 0; i < board.getSize().cols; i++) {
			for(int j = 0; j < board.getSize().rows; j++) {
				int xCoord = (i*squareWidth)+widthPadding;
				int yCoord = (j*squareHeight)+heightPadding;
				switch(board.getPosState(i, j)) {
					case 0:
						g.setColor(Color.WHITE);
						break;
					case 1:
						g.setColor(Color.RED);
						break;
					case 2:
						g.setColor(Color.BLACK);
						break;
				}
				g.fillOval(xCoord, yCoord, circleDiameter, circleDiameter);
			}
		}
	}

}
