package genome;

import java.util.Random;

import spawners.ICreator;

public class GaussianRandomGenomeCreator implements ICreator<float[]>
{
	private final Random random;
	private final int genomeLength;
	private final float center, magnitude;
	
	public GaussianRandomGenomeCreator(Random random, int genomeLength, float center, float magnitude)
	{
		this.random = random;
		this.genomeLength = genomeLength;
		this.center = center;
		this.magnitude = magnitude;
	}
	
	@Override
	public float[] create()
	{
		float[] result = new float[genomeLength];
		
		for (int i = 0; i < genomeLength; ++i)
			result[i] = center + (float)random.nextGaussian() * magnitude;
		
		return result;
	}

}
