package speciators;

public final class NicheSpeciator<T> implements ISpeciator<T>
{
	private final ISpeciator<? super T> base;
	private final float minSimilarity, curve, similarityRange;
	
	/**
	 * @param base speciator to have output modified by power function.
	 * @param minSimilarity should be between 0 and 1 (exclusive).
	 * @param curve increases base similarity at an decreasing/increasing rate for values less than or greater than 1, respectively.
	 * */
	public NicheSpeciator(float minSimilarity, float curve, ISpeciator<? super T> base)
	{
		this.base = base;
		this.minSimilarity = minSimilarity;
		this.curve = curve;
		
		this.similarityRange = 1f - minSimilarity;
	}
	
	public final float getSimilarity(T a, T b)
	{
		float similarity = base.getSimilarity(a, b);
		
		if (similarity < minSimilarity)
			return 0f;
		
		return (float)Math.pow(similarity / similarityRange, curve);
	}
}