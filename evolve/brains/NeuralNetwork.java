package brains;
import java.util.List;
import java.util.Random;


public class NeuralNetwork
{	
	public final class Node
	{
		private final float[] weights;
		
		public Node(float[] theWeights)
		{
			weights = theWeights;
		}
		
		public final float signal(final float[] inputs)
		{
			float activation = weights[weights.length - 1]; //last
			
			for (int i = 0; i < weights.length - 1; ++i)
				activation += inputs[i] * weights[i];
			
			return sigmoid.output(activation);
		}
	}

	private final Node[][] network;
	private final int greatestNodeCount;
	private final IFunction sigmoid;
	
	private NeuralNetwork(int inputCount, float[][][] weights, IFunction sigmoid)
	{
		int greatestNodeCount = 0;
		this.network = new Node[weights.length][];
		
		float[][] weightLayer;
		Node[] layer;
		int n, nodeCount;
		
		for (int i = 0; i < weights.length; ++i)
		{
			nodeCount = weights[i].length;
			
			if (nodeCount > greatestNodeCount)
				greatestNodeCount = nodeCount;
			
			layer = network[i] = new Node[nodeCount];
			weightLayer = weights[i];
			
			for (n = 0; n < nodeCount; ++n)
				layer[n] = new Node(weightLayer[n]);
		}
		
		this.greatestNodeCount = greatestNodeCount;
		this.sigmoid = sigmoid;
	}
	
	public final int getGreatestNodeCount()
	{
		return greatestNodeCount;
	}
	
	/**
	 * Performs one "thought" of the neural network.<P>
	 * 
	 * Both parameters should be of the same length as the network's largest layer.
	 * 
	 * @param inputs should contain the stimuli in it's first slots, whereas the remaining slots can be anything.<P>
	 * @param buffer may contain anything in its slots.<P>
	 * @return One of the two given arrays, with the outputs of the NN in its first slots.
	 * */
	public final float[] process(float[] inputs, float[] buffer)
	{	
		//Flip inputs and processResult with each iteration
		float[] temp;
		int i, n;

		Node[] layerTemp = network[0];
		
		for (n = 0; n < layerTemp.length; ++n)
			buffer[n] = layerTemp[n].signal(inputs);
		
		for (i = 1; i < network.length; ++i)
		{
			temp = inputs;
			inputs = buffer;
			buffer = temp;

			layerTemp = network[i];

			for (n = 0; n < layerTemp.length; ++n)
				buffer[n] = layerTemp[n].signal(inputs);
		}
		
		return buffer;
	}
	
	/** Swaps the node ranges' locations, keeping the actual logic intact
	 * (unless the final layer is selected, in which case, it's the callers responsibility
	 * to ensure the NN's outputs are reinterpreted appropriately)*/
	public final void swapNodes(int layerIndex, int startA, int startB, int range)
	{
		Node[] layer = network[layerIndex];
		
		if (startA < 0)
			throw new Error("Error: startA was less than 0 (was " + startA + ").");
		if (startB < 0)
			throw new Error("Error: startB was less than 0 (was " + startB + ").");
		if (startA+range >= layer.length)
			throw new Error("Error: startA+range was greater than layer " + layerIndex + "'s length of "+layer.length+" (found " + (startA + range) + " instead).");
		if (startB+range >= layer.length)
			throw new Error("Error: startB+range was greater than layer " + layerIndex + "'s length of "+layer.length+" (found " + (startB + range) + " instead).");
		
		Node temp;
		int i;
		
		for (i = 0; i < range; ++i)
		{
			temp = layer[startA + i];
			layer[startA + i] = layer[startB + i];
			layer[startB + i] = temp;
		}
		
		if (layerIndex < network.length - 1)
		{
			float tempWeight;
			layer = network[layerIndex + 1];
			
			for (Node node : layer)
			{
				for (i = 0; i < range; ++i)
				{
					tempWeight = node.weights[startA + i];
					node.weights[startA + i] = node.weights[startB + i];
					node.weights[startB + i] = tempWeight;
				}
			}
		}
	}
	
	public static final class RandomCreator implements spawners.ICreator<NeuralNetwork>
	{
		private final Random random;
		private final boolean isGaussianWeighted;
		private final IFunction activation;
		private final int inputCount;
		private final int[] layerSizes;
		
		public RandomCreator(Random random, boolean isGaussianWeighted, IFunction activation, int inputCount, int[] layerSizes)
		{
			this.random = random;
			this.isGaussianWeighted = isGaussianWeighted;
			this.inputCount = inputCount;
			this.layerSizes = layerSizes;
			this.activation = activation;
		}
		
		public final NeuralNetwork create()
		{
			float[][][] network = new float[layerSizes.length][][];
			int weightCount = inputCount + 1;
			
			for (int i = 0; i < network.length; ++i)
			{
				int nodeCount = layerSizes[i];
				float[][] layer = network[i] = new float[nodeCount][];
				
				for (int n = 0; n < nodeCount; ++n)
				{
					float[] weights = layer[n] = new float[weightCount];
					for (int w = 0; w < weightCount; ++w)
						weights[w] = isGaussianWeighted ? (float)random.nextGaussian() : (-1f + 2f * random.nextFloat());
				}
				
				weightCount = nodeCount + 1;
			}
			
			return new NeuralNetwork(inputCount, network, activation);
		}
	}
	
	
	public static final class Mater implements spawners.IMater<NeuralNetwork>
	{
		private final Random random;
		private final IFunction sigmoid;
		private final float neuronBiasedMutationRate, crossOverFlipRate;
		
		public Mater(Random random, IFunction sigmoid, float neuronBiasedMutationRate, float crossOverFlipRate)
		{
			this.random = random;
			this.sigmoid = sigmoid;
			this.neuronBiasedMutationRate = neuronBiasedMutationRate;
			this.crossOverFlipRate = crossOverFlipRate;
		}
		
		//Assumes networks are of same topology...
		public NeuralNetwork mate(List<? extends NeuralNetwork> parents)
		{
			Node[][] base = parents.get(0).network; //grab topology from this
			
			int inputCount = base[0][0].weights.length;
			
			float[][][] network = new float[base.length][][];
			int weightCount = inputCount;
			
			for (int i = 0; i < network.length; ++i)
			{
				int nodeCount = base[i].length;
				float[][] layer = network[i] = new float[nodeCount][];

				NeuralNetwork parent = parents.get(random.nextInt(parents.size()));
				
				for (int n = 0; n < nodeCount; ++n)
				{
					float[] weights = layer[n] = new float[weightCount];

					for (int w = 0; w < weightCount; ++w)
					{
						weights[w] = parent.network[i][n].weights[w];

						if (random.nextFloat() < neuronBiasedMutationRate)
							weights[w] += random.nextGaussian();

						if (random.nextFloat() < crossOverFlipRate)
							parent = parents.get(random.nextInt(parents.size()));
					}
				}
				
				weightCount = nodeCount + 1;
			}
			
			return new NeuralNetwork(inputCount, network, sigmoid);
		}
	}
}
