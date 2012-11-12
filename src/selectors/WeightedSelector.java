package selectors;

import java.util.List;
import java.util.Random;

import weights.IWeightMaker;


public class WeightedSelector<T> implements ISelector<T>
{
	private final Random random;
	private final IWeightMaker<T> weights;
	
	public WeightedSelector(Random random, IWeightMaker<T> weights)
	{
		this.random = random;
		this.weights = weights;
	}
	
	public final T select(List<? extends T> choices)
	{
		weights.setup(choices);

		float selection = random.nextFloat() * weights.getTotalWeight();
		float currentWeightSum = 0f, weight;
		
		for (T choice : choices)
		{
			weight = weights.getNextWeight();
			currentWeightSum += weight;
			
			if (currentWeightSum >= selection)
				return choice;
		}
	
		throw new Error("Error: weighted selector did not find its weight selection!  Perhaps the weight maker's total weight was inaccurately high?");
	}
}
