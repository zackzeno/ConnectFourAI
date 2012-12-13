package neuralNetComponents.meganet;

import neuralNetComponents.NeuralNetNode;

public class SoftmaxLayer extends NeuralNetLayer {

	public double getSoftmaxValue(double value) {
		double num = 0;
		double den = 0;
		
		num = Math.exp(value);
		
		for(NeuralNetNode n : nodes) {
			den += Math.exp(n.getInputValue());
		}
		
		return num / den;
	}
	
}
