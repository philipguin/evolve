package genome;

import java.util.Random;

import spawners.ICreator;

public class GaussianRandomGenomeCreator implements ICreator<float[]>
{
	private final Random random;
	private final int genomeLength;
	private final float floor, ceiling, center, multiplier;
	
	public GaussianRandomGenomeCreator(Random random, int genomeLength, float floor, float ceiling, float center, float multiplier)
	{
		this.random = random;
		this.genomeLength = genomeLength;
		this.floor = floor;
		this.ceiling = ceiling;
		this.center = center;
		this.multiplier = multiplier;
	}
	
	@Override
	public float[] create()
	{
		float[] result = new float[genomeLength];
		
		for (int i = 0; i < genomeLength; ++i)
			result[i] = Math.max(floor, Math.min(ceiling, center + (float)random.nextGaussian() * multiplier));
		
		return result;
	}

}
