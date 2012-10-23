package speciators;

import java.util.Iterator;
import java.util.List;

public class WeightedAverageSpeciator<T> implements ISpeciator<T>
{
	private final List<Float> weights;
	private final List<? extends ISpeciator<? super T>> speciators;
	private final float oneOverTotalWeight;
	
	public WeightedAverageSpeciator(List<? extends ISpeciator<? super T>> speciators, List<Float> weights)
	{
		this.speciators = speciators;
		this.weights = weights;
		
		float totalWeight = 0f;
		
		for (float weight : weights)
			totalWeight += weight;
		
		this.oneOverTotalWeight = 1f/totalWeight;
	}
	
	public float getSimilarity(T a, T b)
	{
		float sum = 0f;
		
		Iterator<? extends ISpeciator<? super T>> iterator = speciators.iterator();
		
		for (float weight : weights)
		{
			sum += iterator.next().getSimilarity(a, b) * weight;
		}
		
		return sum * oneOverTotalWeight;
	}
}
