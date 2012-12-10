package uiComponents;

import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.Game;
import gameComponents.Player;

import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameOptionsPanel extends JPanel {
	
	private final Object nextGameLock = new Object();
	private final Object pauseLock = new Object();
	private boolean nextGameClicked;
	private boolean pauseClicked;
	private String invalidMessage;
	
	private static final long serialVersionUID = 1L;
	private JPanel optionsPanel;
	private JPanel infoPanel;
	
	private JButton newGameButton;
	private JLabel heightLabel;
	private JLabel widthLabel;
	private JTextField heightField;
	private JTextField widthField;
	private JLabel p1Label;
	private JLabel p2Label;
	private JComboBox<String> p1Select;
	private JComboBox<String> p2Select;
	private JLabel nRoundsLabel;
	private JTextField nRoundsField;
	private JLabel aiMoveDelayLabel;
	private JTextField aiMoveDelayField;
	private JLabel aiGameDelayLabel;
	private JTextField aiGameDelayField;
	private JButton pauseButton;
	private JLabel p1MMLevelLabel;
	private JTextField p1MMLevelField;
	private JLabel p2MMLevelLabel;
	private JTextField p2MMLevelField;
	private JLabel p1MMOffLabel;
	private JLabel p1MMDefLabel;
	private JTextField p1MMOffField;
	private JTextField p1MMDefField;
	private JLabel p2MMOffLabel;
	private JLabel p2MMDefLabel;
	private JTextField p2MMOffField;
	private JTextField p2MMDefField;
	private JCheckBox p1NNLoadCheck;
	private JTextField p1NNLoadField;
	private JCheckBox p1NNSaveCheck;
	private JTextField p1NNSaveField;
	private JCheckBox p2NNLoadCheck;
	private JTextField p2NNLoadField;
	private JCheckBox p2NNSaveCheck;
	private JTextField p2NNSaveField;
	private JCheckBox loggingCheck;
	
	private JLabel gameInfoHeader;
	private JLabel roundInfoHeader;
	private JLabel progressLabel;
	private JLabel lastMoveLabel;
	private JLabel gameScoreLabel;
	private JLabel lastRoundLabel;
	private JButton nextGameButton;
	
	public GameOptionsPanel() {
		pauseClicked = false;
		
		optionsPanel = new JPanel();
		infoPanel = new JPanel();
		
		newGameButton = new JButton("New Game");
		heightLabel = new JLabel("Board height:");
		heightField = new JTextField("6");
		widthLabel = new JLabel("Board width:");
		widthField = new JTextField("6");
		p1Label = new JLabel("Player 1:");
		p1Select = new JComboBox<String>(Player.playerTypes);
		p1Select.setSelectedIndex(0);
		p2Label = new JLabel("Player 2:");
		p2Select = new JComboBox<String>(Player.playerTypes);
		p2Select.setSelectedIndex(1);
		nRoundsLabel = new JLabel("Number of rounds:");
		nRoundsField = new JTextField("1");
		aiMoveDelayLabel = new JLabel("AI move delay (s):");
		aiMoveDelayField = new JTextField("3");
		aiGameDelayLabel = new JLabel("AI game delay (s):");
		aiGameDelayField = new JTextField("5");
		pauseButton = new JButton("Pawse");
		p1MMLevelLabel = new JLabel("Depth:");
		p1MMLevelField = new JTextField("5");
		p1MMOffLabel = new JLabel("Off Weight:");
		p1MMDefLabel = new JLabel("Def Weight:");
		p1MMOffField = new JTextField("1");
		p1MMDefField = new JTextField("1");
		p2MMOffLabel = new JLabel("Off Weight:");
		p2MMDefLabel = new JLabel("Def Weight:");
		p2MMOffField = new JTextField("1");
		p2MMDefField = new JTextField("1");
		p2MMLevelLabel = new JLabel("Depth:");
		p2MMLevelField = new JTextField("5");
		p1MMLevelLabel.setVisible(false);
		p1MMLevelField.setVisible(false);
		p2MMLevelLabel.setVisible(false);
		p2MMLevelField.setVisible(false);
		p1MMOffLabel.setVisible(false);
		p1MMDefLabel.setVisible(false);
		p1MMOffField.setVisible(false);
		p1MMDefField.setVisible(false);
		p2MMOffLabel.setVisible(false);
		p2MMDefLabel.setVisible(false);
		p2MMOffField.setVisible(false);
		p2MMDefField.setVisible(false);
		p1NNLoadCheck = new JCheckBox("Load file:");
		p1NNLoadField = new JTextField("");
		p1NNSaveCheck = new JCheckBox("Save file:");
		p1NNSaveField = new JTextField("");
		p2NNLoadCheck = new JCheckBox("Load file:");
		p2NNLoadField = new JTextField("");
		p2NNSaveCheck = new JCheckBox("Save file:");
		p2NNSaveField = new JTextField("");
		p1NNLoadCheck.setVisible(false);
		p1NNLoadField.setVisible(false);
		p1NNSaveCheck.setVisible(false);
		p1NNSaveField.setVisible(false);
		p2NNLoadCheck.setVisible(false);
		p2NNLoadField.setVisible(false);
		p2NNSaveCheck.setVisible(false);
		p2NNSaveField.setVisible(false);
		loggingCheck = new JCheckBox("Enable AI logging");
		
		gameInfoHeader = new JLabel("Game Info:");
		roundInfoHeader = new JLabel("Round Info:");
		progressLabel = new JLabel("");
		lastMoveLabel = new JLabel("");
		lastRoundLabel = new JLabel("");
		gameScoreLabel = new JLabel("");
		nextGameButton = new JButton("Next Round");
		nextGameButton.setVisible(false);
		
		p1Select.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(((String)p1Select.getSelectedItem()).equals("Minimax")) {
					p1MMLevelField.setVisible(true);
					p1MMLevelLabel.setVisible(true);
					p1MMOffLabel.setVisible(true);
					p1MMDefLabel.setVisible(true);
					p1MMOffField.setVisible(true);
					p1MMDefField.setVisible(true);
					p1NNLoadCheck.setVisible(false);
					p1NNLoadField.setVisible(false);
					p1NNSaveCheck.setVisible(false);
					p1NNSaveField.setVisible(false);
				}
				else if(((String)p1Select.getSelectedItem()).equals("Four Layer Net")) {
					p1NNLoadCheck.setVisible(true);
					p1NNLoadField.setVisible(true);
					p1NNSaveCheck.setVisible(true);
					p1NNSaveField.setVisible(true);
					p1MMLevelField.setVisible(false);
					p1MMLevelLabel.setVisible(false);
					p1MMOffLabel.setVisible(false);
					p1MMDefLabel.setVisible(false);
					p1MMOffField.setVisible(false);
					p1MMDefField.setVisible(false);
				}
				else {
					p1MMLevelField.setVisible(false);
					p1MMLevelLabel.setVisible(false);
					p1NNLoadCheck.setVisible(false);
					p1NNLoadField.setVisible(false);
					p1NNSaveCheck.setVisible(false);
					p1NNSaveField.setVisible(false);
					p1MMOffLabel.setVisible(false);
					p1MMDefLabel.setVisible(false);
					p1MMOffField.setVisible(false);
					p1MMDefField.setVisible(false);
				}
			}
		});
		
		p2Select.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				if(((String)p2Select.getSelectedItem()).equals("Minimax")) {
					p2MMLevelField.setVisible(true);
					p2MMLevelLabel.setVisible(true);
					p2MMOffLabel.setVisible(true);
					p2MMDefLabel.setVisible(true);
					p2MMOffField.setVisible(true);
					p2MMDefField.setVisible(true);
					p2NNLoadCheck.setVisible(false);
					p2NNLoadField.setVisible(false);
					p2NNSaveCheck.setVisible(false);
					p2NNSaveField.setVisible(false);
				}
				else if(((String)p2Select.getSelectedItem()).equals("Four Layer Net")) {
					p2NNLoadCheck.setVisible(true);
					p2NNLoadField.setVisible(true);
					p2NNSaveCheck.setVisible(true);
					p2NNSaveField.setVisible(true);
					p2MMLevelField.setVisible(false);
					p2MMLevelLabel.setVisible(false);
					p2MMOffLabel.setVisible(false);
					p2MMDefLabel.setVisible(false);
					p2MMOffField.setVisible(false);
					p2MMDefField.setVisible(false);
				}
				else {
					p2MMLevelField.setVisible(false);
					p2MMLevelLabel.setVisible(false);
					p2NNLoadCheck.setVisible(false);
					p2NNLoadField.setVisible(false);
					p2NNSaveCheck.setVisible(false);
					p2NNSaveField.setVisible(false);
					p2MMOffLabel.setVisible(false);
					p2MMDefLabel.setVisible(false);
					p2MMOffField.setVisible(false);
					p2MMDefField.setVisible(false);
				}
			}
		});
		
		newGameButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(validateGameConfiguration()) {
					boolean logging = loggingCheck.isSelected();
					AIProgram a1;
					if(((String)p1Select.getSelectedItem()).equals("Minimax")) {
						a1 = Player.createAI("Minimax", 
								new Object[] { Integer.parseInt(p1MMLevelField.getText()), 
									Integer.parseInt(p1MMOffField.getText()), Integer.parseInt(p1MMDefField.getText()) }, logging );
					}
					else if(((String)p1Select.getSelectedItem()).equals("Four Layer Net")) {
						a1 = Player.createAI("Four Layer Net", 
								new Object[] { p1NNLoadCheck.isSelected() ? p1NNLoadField.getText() + ".data" : null, 
										p1NNSaveCheck.isSelected() ? p1NNSaveField.getText() + ".data" : null }, logging );
					}
					// TODO insert 'else if' for each new neural net here (copy above syntax)
					else {
						a1 = Player.createAI((String)p1Select.getSelectedItem(), null, logging);
					}
					AIProgram a2;
					if(((String)p2Select.getSelectedItem()).equals("Minimax")) {
						a2 = Player.createAI("Minimax", 
								new Object[] { Integer.parseInt(p2MMLevelField.getText()), 
									Integer.parseInt(p2MMOffField.getText()), Integer.parseInt(p2MMDefField.getText()) }, logging);
					}
					else if(((String)p2Select.getSelectedItem()).equals("Four Layer Net")) {
						a2 = Player.createAI("Four Layer Net", 
								new Object[] { p2NNLoadCheck.isSelected() ? p2NNLoadField.getText() + ".data" : null, 
										p2NNSaveCheck.isSelected() ? p2NNSaveField.getText() + ".data" : null }, logging);
					}
					else {
						a2 = Player.createAI((String)p2Select.getSelectedItem(), null, logging);
					}
					Game newGame = new Game(new Board(Integer.parseInt(heightField.getText()), Integer.parseInt(widthField.getText()), 4), 
						new Player[] { new Player(a1, 1), 
						new Player(a2, 2) }, Integer.parseInt(nRoundsField.getText()),
						Double.parseDouble(aiMoveDelayField.getText()), Double.parseDouble(aiGameDelayField.getText()) );
					newGame.getBoard().setGame(newGame);
					newGame.startGame();
				}
				else {
					JOptionPane.showMessageDialog(MainFrame.getMainFrame(), invalidMessage);
				}
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
		
		nextGameButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				handleNextRoundClick();
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
		
		pauseButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				handlePauseClick();
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

		
		this.add(optionsPanel);
		this.add(infoPanel);
		
		optionsPanel.add(newGameButton);
		optionsPanel.add(heightLabel);
		optionsPanel.add(heightField);
		optionsPanel.add(widthLabel);
		optionsPanel.add(widthField);
		optionsPanel.add(p1Label);
		optionsPanel.add(p1Select);
		optionsPanel.add(p2Label);
		optionsPanel.add(p2Select);
		optionsPanel.add(nRoundsLabel);
		optionsPanel.add(nRoundsField);
		optionsPanel.add(aiMoveDelayLabel);
		optionsPanel.add(aiMoveDelayField);
		optionsPanel.add(aiGameDelayLabel);
		optionsPanel.add(aiGameDelayField);
		optionsPanel.add(pauseButton);
		optionsPanel.add(p1MMLevelLabel);
		optionsPanel.add(p1MMLevelField);
		optionsPanel.add(p2MMLevelLabel);
		optionsPanel.add(p2MMLevelField);
		optionsPanel.add(p1MMOffLabel);
		optionsPanel.add(p1MMDefLabel);
		optionsPanel.add(p1MMOffField);
		optionsPanel.add(p1MMDefField);
		optionsPanel.add(p2MMOffLabel);
		optionsPanel.add(p2MMDefLabel);
		optionsPanel.add(p2MMOffField);
		optionsPanel.add(p2MMDefField);
		optionsPanel.add(p1NNLoadCheck);
		optionsPanel.add(p1NNLoadField);
		optionsPanel.add(p1NNSaveCheck);
		optionsPanel.add(p1NNSaveField);
		optionsPanel.add(p2NNLoadCheck);
		optionsPanel.add(p2NNLoadField);
		optionsPanel.add(p2NNSaveCheck);
		optionsPanel.add(p2NNSaveField);
		optionsPanel.add(loggingCheck);
		
		infoPanel.add(gameInfoHeader);
		infoPanel.add(progressLabel);
		infoPanel.add(lastMoveLabel);
		infoPanel.add(roundInfoHeader);
		infoPanel.add(gameScoreLabel);
		infoPanel.add(lastRoundLabel);
		infoPanel.add(nextGameButton);
	}
	
	public boolean checkPause() {
		synchronized(pauseLock) {
			while(pauseClicked) {
				try {
					pauseLock.wait();
				} 
				catch (InterruptedException e) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void handlePauseClick() {
		synchronized(pauseLock) {
			if(pauseClicked) {
				pauseButton.setText("Pawse");
				pauseLock.notify();
			}
			else {
				pauseButton.setText("Resume");
			}
			pauseClicked = !pauseClicked;
			MainFrame.getMainFrame().repaint();
		}
	}
	
	public boolean waitForNextRoundClick() {
		synchronized(nextGameLock) {
			nextGameClicked = false;
			nextGameButton.setVisible(true);
			MainFrame.getMainFrame().repaint();
			while(!nextGameClicked) {
				try {
					nextGameLock.wait();
				} catch (InterruptedException e) {
					return true;
				}
			}
			nextGameButton.setVisible(false);
			MainFrame.getMainFrame().repaint();
			return false;
		}
	}
	
	private void handleNextRoundClick() {
		synchronized(nextGameLock) {
			nextGameClicked = true;
			nextGameLock.notify();
		}
	}
	
	
	public void setProgressLabel(String text) {
		progressLabel.setText(text);
	}
	
	public void setLastMoveLabel(String text) {
		lastMoveLabel.setText(text);
	}
	
	public void setGameScoreLabel(String text) {
		gameScoreLabel.setText(text);
	}
	
	public void setLastRoundLabel(String text) {
		lastRoundLabel.setText(text);
	}
	
	public double getAIMoveDelay() {
		try {
			double amd = Double.parseDouble(aiMoveDelayField.getText());
			if(amd >= 0 && amd <= 60) {
				return amd;
			}
			return -1;
		}
		catch(NumberFormatException e) {
			return -1;
		}
	}
	
	public double getAIGameDelay() {
		try {
			double agd = Double.parseDouble(aiGameDelayField.getText());
			if(agd >= 0 && agd <= 60) {
				return agd;
			}
			return agd;
		}
		catch(NumberFormatException e) {
			return -1;
		}
	}
	
	private boolean validateGameConfiguration() {
		try {
			int h = Integer.parseInt(heightField.getText());
			if(h < 4 || h > 100) {
				invalidMessage = "Invalid Configuration: Board height must be an integer between 4 and 100";
				return false;
			}
		}
		catch(NumberFormatException e) {
			invalidMessage = "Invalid Configuration: Board height must be an integer between 4 and 20";
			return false;
		}
		try {
			int w = Integer.parseInt(widthField.getText());
			if(w < 4 || w > 100) {
				invalidMessage = "Invalid Configuration: Board width must be an integer between 4 and 100";
				return false;
			}
		}
		catch(NumberFormatException e) {
			invalidMessage = "Invalid Configuration: Board width must be an integer between 4 and 20";
			return false;
		}
		try {
			int n = Integer.parseInt(nRoundsField.getText());
			if(n < 1) {
				invalidMessage = "Invalid Configuration: Number of rounds must be an integer greater than 0";
				return false;
			}
		}
		catch(NumberFormatException e) {
			invalidMessage = "Invalid Configuration: Number of rounds must be an integer greater than 0";
			return false;
		}
		if(((String)p1Select.getSelectedItem()).equals("Minimax")) {
			try {
				int n = Integer.parseInt(p1MMLevelField.getText());
				if(n < 1 || n > 10) {
					invalidMessage = "Invalid Configuration: Minimax level must be an integer between 1 and 10";
					return false;
				}
			}
			catch(NumberFormatException e) {
				invalidMessage = "Invalid Configuration: Minimax level must be an integer between 1 and 10";
				return false;
			}
			try {
				int o = Integer.parseInt(p1MMOffField.getText());
				int d = Integer.parseInt(p1MMDefField.getText());
				if(o < 0 || o > 5 || d < 0 || d > 5) {
					invalidMessage = "Invalid Configuration: Offense/defense weight must be an integer between 0 and 5";
					return false;
				}
			}
			catch(NumberFormatException e) {
				invalidMessage = "Invalid Configuration: Offense/defense weight must be an integer between 0 and 5";
				return false;
			}
		}
		if(((String)p2Select.getSelectedItem()).equals("Minimax")) {
			try {
				int n = Integer.parseInt(p2MMLevelField.getText());
				if(n < 1 || n > 10) {
					invalidMessage = "Invalid Configuration: Minimax level must be an integer between 1 and 10";
					return false;
				}
			}
			catch(NumberFormatException e) {
				invalidMessage = "Invalid Configuration: Minimax level must be an integer between 1 and 10";
				return false;
			}
			try {
				int o = Integer.parseInt(p2MMOffField.getText());
				int d = Integer.parseInt(p2MMDefField.getText());
				if(o < 0 || o > 5 || d < 0 || d > 5) {
					invalidMessage = "Invalid Configuration: Offense/defense weight must be an integer between 0 and 5";
					return false;
				}
			}
			catch(NumberFormatException e) {
				invalidMessage = "Invalid Configuration: Offense/defense weight must be an integer between 0 and 5";
				return false;
			}
		}
		if(((String)p1Select.getSelectedItem()).equals("Four Layer Net")) {
			if(p1NNLoadCheck.isSelected()) {
				String fname = p1NNLoadField.getText();
				File f = new File("nets", fname + ".data");
				if(!f.exists()) {
					invalidMessage = "Invalid Configuration: Load file not found";
					return false;
				}
			}
			if(p1NNSaveCheck.isSelected()) {
				String fname = p1NNSaveField.getText();
				if(fname.trim().isEmpty()) {
					invalidMessage = "Invalid Configuration: Save file invalid";
					return false;
				}
			}
		}
		if(((String)p2Select.getSelectedItem()).equals("Four Layer Net")) {
			if(p2NNLoadCheck.isSelected()) {
				String fname = p2NNLoadField.getText();
				File f = new File("nets", fname + ".data");
				if(!f.exists()) {
					invalidMessage = "Invalid Configuration: Load file not found";
					return false;
				}
			}
			if(p2NNSaveCheck.isSelected()) {
				String fname = p2NNSaveField.getText();
				if(fname.trim().isEmpty()) {
					invalidMessage = "Invalid Configuration: Save file invalid";
					return false;
				}
			}
		}
		try {
			double am = Double.parseDouble(aiMoveDelayField.getText());
			if(am < 0 || am > 60) {
				invalidMessage = "Invalid Configuration: AI move delay must be a real number between 0 and 60";
				return false;
			}
		}
		catch(NumberFormatException e) {
			invalidMessage = "Invalid Configuration: AI move delay must be a real number between 0 and 60";
			return false;
		}
		try {
			double ag = Double.parseDouble(aiGameDelayField.getText());
			if(ag < 0 || ag > 60) {
				invalidMessage = "Invalid Configuration: AI game delay must be a real number between 0 and 60";
				return false;
			}
		}
		catch(NumberFormatException e) {
			invalidMessage = "Invalid Configuration: AI game delay must be a real number between 0 and 60";
			return false;
		}
		return true;
	}
	
	public void resetOptionsPanel() {
		nextGameButton.setVisible(false);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		optionsPanel.setBounds(0, 0, getWidth(), 7*(getHeight()/10));
		infoPanel.setBounds(0, 7*(getHeight()/10), getWidth(), 3*(getHeight()/10));
		
		newGameButton.setBounds(10, 10, 100, 20);
		heightLabel.setBounds(10, 40, 120, 20);
		heightField.setBounds(130, 40, 50, 20);
		widthLabel.setBounds(10, 70, 120, 20);
		widthField.setBounds(130, 70, 50, 20);
		p1Label.setBounds(10, 100, 60, 20);
		p1Select.setBounds(70, 100, 140, 20);
		p1MMLevelLabel.setBounds(10, 130, 60, 20);
		p1MMLevelField.setBounds(70, 130, 30, 20);
		p1MMOffLabel.setBounds(10, 160, 70, 20);
		p1MMOffField.setBounds(80, 160, 30, 20);
		p1MMDefLabel.setBounds(120, 160, 70, 20);
		p1MMDefField.setBounds(190, 160, 30, 20);
		p1NNLoadCheck.setBounds(10, 130, 100, 20);
		p1NNLoadField.setBounds(110, 130, 100, 20);
		p1NNSaveCheck.setBounds(10, 160, 100, 20);
		p1NNSaveField.setBounds(110, 160, 100, 20);
		p2Label.setBounds(10, 190, 60, 20);
		p2Select.setBounds(70, 190, 140, 20);
		p2MMLevelLabel.setBounds(10, 220, 60, 20);
		p2MMLevelField.setBounds(70, 220, 30, 20);
		p2MMOffLabel.setBounds(10, 250, 70, 20);
		p2MMOffField.setBounds(80, 250, 30, 20);
		p2MMDefLabel.setBounds(120, 250, 70, 20);
		p2MMDefField.setBounds(190, 250, 30, 20);
		p2NNLoadCheck.setBounds(10, 220, 100, 20);
		p2NNLoadField.setBounds(110, 220, 100, 20);
		p2NNSaveCheck.setBounds(10, 250, 100, 20);
		p2NNSaveField.setBounds(110, 250, 100, 20);
		nRoundsLabel.setBounds(10, 280, 120, 20);
		nRoundsField.setBounds(130, 280, 50, 20);
		aiMoveDelayLabel.setBounds(10, 310, 120, 20);
		aiMoveDelayField.setBounds(130, 310, 50, 20);
		aiGameDelayLabel.setBounds(10, 340, 120, 20);
		aiGameDelayField.setBounds(130, 340, 50, 20);
		pauseButton.setBounds(10, 370, 100, 20);
		loggingCheck.setBounds(10, 400, 200, 20);
		
		roundInfoHeader.setBounds(10, 10, getWidth(), 20);
		progressLabel.setBounds(10, 30, getWidth(), 20);
		lastMoveLabel.setBounds(10, 50, getWidth(), 20);
		
		gameInfoHeader.setBounds(10, 90, getWidth(), 20);
		gameScoreLabel.setBounds(10, 110, getWidth(), 20);
		lastRoundLabel.setBounds(10, 130, getWidth(), 20);
		
		nextGameButton.setBounds(10, 170, 100, 20);	
	}
}