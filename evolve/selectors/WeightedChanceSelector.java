package selectors;

import java.util.List;
import java.util.Random;

import weights.IWeightMaker;


public class WeightedChanceSelector<T> implements ISelector<T>
{
	private final Random random;
	private final IWeightMaker<T> weights;
	
	public WeightedChanceSelector(Random random, IWeightMaker<T> weights)
	{
		this.random = random;
		this.weights = weights;
	}
	
	public final T select(List<? extends T> choices)
	{
		weights.setup(choices);
		
		int bucket = 0;
		float selection = random.nextFloat() * weights.getTotalWeight();
		float currentWeightSum = 0f, weight;

		while (true)
		{
			weight = weights.getNextWeight();
			currentWeightSum += weight;
			
			if (currentWeightSum >= selection)
				break;
			
			++bucket;
		}
	
		return choices.get(bucket);
	}
}
