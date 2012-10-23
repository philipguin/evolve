package genome;

import speciators.ISpeciator;

public final class DifferenceGenomeSpeciator implements ISpeciator<float[]>
{
	private final float geneRange;
	
	public DifferenceGenomeSpeciator(float geneRange)
	{
		this.geneRange = geneRange;
	}
	
	@Override
	public float getSimilarity(float[] a, float[] b)
	{
		if (a.length != b.length)
			throw new Error("a and b must be of same length!");
		
		if (a.length == 0)
			return 1f;
		
		float difference = 0f;
		
		for (int i = 0; i < a.length; ++i)
		{
			difference += Math.abs(a[i] - b[i]);
		}
		
		return 1f - difference / (geneRange * a.length);
	}
}