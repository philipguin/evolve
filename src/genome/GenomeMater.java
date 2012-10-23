package genome;

import java.util.List;
import java.util.Random;

import spawners.IMater;

public final class GenomeMater implements IMater<float[]>
{
	private final Random random;
	private final float geneFloor, geneCeiling, biasedMutationRate, biasedMutationSize, unbiasedMutationRate;
	
	/**
	 * Note that the sum of the "likelihood" parameters is subtracted from 1.00 to arrive at the chance of copying a random parent's gene (without mutation).
	 * 
	 * @param biasedMutationRate The chance out of 1.00 that a biased mutation will occur, causing a gene to "deviate" according to a Gaussian distribution.
	 * @param biasedMutationSize The maximum amount a biased mutation may alter a gene by (iow, half the range of said Gaussian distribution).  The resulting gene will be floored or ceilinged to remain between 0 and 1.
	 * @param unbiasedMutationRate The chance out of 1.00 that an unbiased mutation will occur, replacing a gene with a new one chosen from the initial distribution of genes (in this case, from the uniform distribution between 0f and 1f).
	 * */
	
	public GenomeMater(Random random, float geneFloor, float geneCeiling, float biasedMutationRate, float biasedMutationSize, float unbiasedMutationRate)
	{
		this.random = random;
		this.geneFloor = geneFloor;
		this.geneCeiling = geneCeiling;
		this.biasedMutationRate = biasedMutationRate;
		this.biasedMutationSize = biasedMutationSize;
		this.unbiasedMutationRate = unbiasedMutationRate;
	}
	
	@SuppressWarnings("unchecked")
	private static final <R> R unsafeConvert(Object o)
	{
		return (R)o;
	}
	
	@Override
	public float[] mate(List<? extends float[]> parents)
	{
		float[] genome = new float[parents.get(0).length];
		float roll;
		for (int i = 0; i < genome.length; ++i)
		{
			roll = random.nextFloat();
			if (roll < unbiasedMutationRate)
			{
				//Unbiased mutation
				genome[i] = random.nextFloat();
				continue;
			}

			//Select one parent for this gene
			genome[i] = GenomeMater.<float[]>unsafeConvert(parents.get(random.nextInt(parents.size())))[i];
			
			if (roll < unbiasedMutationRate+biasedMutationRate)
			{
				//Biased mutation
				genome[i] += random.nextGaussian() * biasedMutationSize;
				
				if (genome[i] > geneCeiling)
					genome[i] = geneCeiling;
				
				else if (genome[i] < geneFloor)
					genome[i] = geneFloor;
			}
		}
		return genome;
	}
}