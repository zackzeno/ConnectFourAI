package aiPrograms;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import neuralNetComponents.NeuralNet;
import utils.AIUtils;
import gameComponents.AIProgram;
import gameComponents.Board;
import gameComponents.MoveInfo;

//extend this for a new neural net AI, see FourLayerNeuralNet for example implementation
public abstract class AbstractNeuralNet implements AIProgram {

	protected NeuralNet net;	
	protected boolean initiated;
	protected String saveFileName;
	protected FileWriter logOut;
	
	public AbstractNeuralNet(String loadFile, String saveFile, boolean logging) {
		if(loadFile != null) {
			net = NeuralNet.loadFromFile(loadFile);
		}
		else {
			net = null;
		}
		saveFileName = saveFile;
		initiated = false;
		if(logging) {
			File logFile = AIUtils.getOrCreateLogfile("FourLayerNet");
			try {
				logOut = new FileWriter(logFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			logOut = null;
		}
	}
	
	@Override
	public abstract MoveInfo makeMove(int playerNum, Board board); 

	@Override
	public abstract void startRound(int playerNum);

	@Override
	public abstract void endRound(int playerNum, int winnerNum);

	@Override
	public void startGame(int playerNum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void endGame(int playerNum) {
		if(saveFileName != null) {
			net.saveToFile(saveFileName);
		}
	}
}