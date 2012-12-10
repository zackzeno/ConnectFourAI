package test;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class CustomPanelTest extends JPanel {
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(Color.RED);
		g.drawRect(0, 0, 50, 50);
	}
}
