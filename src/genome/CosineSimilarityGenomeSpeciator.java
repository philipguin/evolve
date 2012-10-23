package genome;

import speciators.ISpeciator;

public final class CosineSimilarityGenomeSpeciator implements ISpeciator<float[]>
{
	private final float similarityWhenNoMagnitude;
	
	public CosineSimilarityGenomeSpeciator()
	{
		this(Float.NaN);
	}
	
	public CosineSimilarityGenomeSpeciator(float similarityWhenNoMagnitude)
	{
		this.similarityWhenNoMagnitude = similarityWhenNoMagnitude;
	}
	
	@Override
	public float getSimilarity(float[] a, float[] b)
	{
		if (a.length != b.length)
			throw new Error("a and b must be of same length!");
		
		if (a.length == 0)
			return 1f;
		
		float sum = 0f, magnitudeASquared = 0f, magnitudeBSquared = 0f;
		
		for (int i = 0; i < a.length; ++i)
		{
			sum += a[i] * b[i];
			magnitudeASquared += a[i] * a[i];
			magnitudeBSquared += b[i] * b[i];
		}
		
		if (magnitudeASquared == 0f || magnitudeBSquared == 0f)
			return similarityWhenNoMagnitude;
		
		return sum / ((float)Math.sqrt(magnitudeASquared) * (float)Math.sqrt(magnitudeBSquared));
	}
}