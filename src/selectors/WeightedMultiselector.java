package selectors;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import weights.IWeightMaker;


public class WeightedMultiselector<T> implements IMultiselector<T>
{
	private final Random random;
	private final IWeightMaker<T> weights;
	private final boolean isDistinct;
	
	public WeightedMultiselector(Random random, IWeightMaker<T> weights, boolean isDistinct)
	{
		this.random = random;
		this.weights = weights;
		this.isDistinct = isDistinct;
	}
	
	public final List<T> select(List<? extends T> choices, int numberToSelect)
	{
		assert !isDistinct || numberToSelect <= choices.size();
		
		List<T> result = new ArrayList<T>(numberToSelect);
		weights.setup(choices);

		while (result.size() < numberToSelect)
		{
			T toAdd = null;
			
			do
			{
				weights.reset();
				float selection = random.nextFloat() * weights.getTotalWeight();
				float currentWeightSum = 0f, weight;
				
				for (T choice : choices)
				{
					weight = weights.getNextWeight();
					currentWeightSum += weight;
					
					if (currentWeightSum >= selection)
					{
						toAdd = choice;
						break;
					}
				}
			}
			while (isDistinct && result.contains(toAdd));
			
			result.add(toAdd);
		}
		
		return result;
	}
}
