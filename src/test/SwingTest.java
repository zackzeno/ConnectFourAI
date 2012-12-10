package test;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwingTest {

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame.setSize(300, 300);
		frame.setVisible(true);
		CustomPanelTest panel = new CustomPanelTest();
		panel.setLocation(100, 100);
		frame.add(panel);
	}

}
