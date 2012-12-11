package neuralNetComponents;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class NeuralNet implements Serializable {

	private static final long serialVersionUID = 417205279392940710L;
	private NeuralNetNode[][] netNodes;
	private int nLayers;
	private static final Object ioLock = new Object();
	
	public NeuralNet(NeuralNetNode[][] nodes) {
		nLayers = nodes.length;
		netNodes = nodes;
	}
	
	public double[] forwardPropagate(double[] inputs) {
		for(int i = 0; i < nLayers; i++) {
			for(int j = 0; j < netNodes[i].length; j++) {
				netNodes[i][j].reset();
			}
		}
		for(int i = 0; i < netNodes[0].length; i++) {
			netNodes[0][i].setInputValue(inputs[i]);
		}
		for(int i = 0; i < nLayers; i++) {
			for(int j = 0; j < netNodes[i].length; j++) {
				netNodes[i][j].forwardPropagate();
			}
		}
		double[] outputValues = new double[netNodes[nLayers-1].length];
		for(int i = 0; i < netNodes[nLayers-1].length; i++) {
			outputValues[i] = netNodes[nLayers-1][i].getActivationValue();
		}
		return outputValues;
	}
	
	public void backPropagate(double[] outputModifiers) {
		for(int i = 0; i < netNodes[nLayers - 1].length; i++) {
			netNodes[nLayers - 1][i].setDelta(outputModifiers[i]);
		}
		for(int i = nLayers - 1; i >= 0; i--) {
			for(int j = 0; j < netNodes[i].length; j++) {
				netNodes[i][j].backPropagate();
			}
		}
	}
	
	public String getNetInfo() {
		String res = "";
		for(int i = 0; i < netNodes.length; i++) {
			res += "Printing neural net statistics for layer " + i;
			res += "\n";
			for(int j = 0; j < netNodes[i].length; j++) {
				res += "Node: " + netNodes[i][j].getNodeInfo();
				res += "\n";
				res += "Value: " + netNodes[i][j].getActivationValue();
				res += "\n";
				res += "Edges:";
				res += "\n";
				for(int k = 0; k < netNodes[i][j].getOutEdges().size(); k++) {
					res += "--> to " + netNodes[i][j].getOutEdges().get(k).getTo().getNodeInfo() 
							+ ", weight " + netNodes[i][j].getOutEdges().get(k).getWeight();
					res += "\n";
				}
			}
		}
		res += "\n\n";
		return res;
	}
	
	public void saveToFile(String filename) {
		synchronized(ioLock) {
			try {
				File f = new File("nets", filename);
				if(!f.exists()) {
					f.createNewFile();
				}
				FileOutputStream s = new FileOutputStream(f);
				ObjectOutputStream os = new ObjectOutputStream(s);
				os.writeObject(this);
				os.close();
			} 
			catch (IOException e) {
				System.exit(0);
			}
		}
	}
	
	public static NeuralNet loadFromFile(String filename) {
		synchronized(ioLock) {
			try {
				File f = new File("nets", filename);
				FileInputStream s = new FileInputStream(f);
				ObjectInputStream os = new ObjectInputStream(s);
				NeuralNet importNet = (NeuralNet)os.readObject();
				os.close();
				return importNet;
			}
			catch(IOException e) {
				System.exit(0);
			}
			catch(ClassNotFoundException e) {
				System.exit(0);
			}
			return null;
		}
	}
}
