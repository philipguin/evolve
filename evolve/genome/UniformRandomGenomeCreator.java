package genome;

import java.util.Random;

import spawners.ICreator;

public class UniformRandomGenomeCreator implements ICreator<float[]>
{
	private final Random random;
	private final int genomeLength;
	private final float min, range;
	
	public UniformRandomGenomeCreator(Random random, int genomeLength, float min, float max)
	{
		this.random = random;
		this.genomeLength = genomeLength;
		this.min = min;
		this.range = max - min;
	}
	
	@Override
	public float[] create()
	{
		float[] result = new float[genomeLength];
		
		for (int i = 0; i < genomeLength; ++i)
			result[i] = min + random.nextFloat() * range;
		
		return result;
	}

}
