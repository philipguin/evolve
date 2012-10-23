package weights;

import java.util.ArrayList;
import java.util.List;

import selectors.IMultiselector;
import speciators.ISpeciator;



public final class CelebrityBiasedSpeciatedWeightMaker<T> implements IWeightMaker<T>
{
	private final IMultiselector<T> celebritySelector;
	private final int celebCount;
	private final ISpeciator<? super T> speciator;
	private final ArrayList<Float> tempWeights = new ArrayList<Float>();
	private final IWeightMaker<? super T> base;

	private float totalWeight;
	private int currentWeight;
	
	public CelebrityBiasedSpeciatedWeightMaker(IMultiselector<T> celebritySelector, int celebCount, ISpeciator<? super T> speciator, IWeightMaker<? super T> base)
	{
		this.celebritySelector = celebritySelector;
		this.celebCount = celebCount;
		this.speciator = speciator;
		this.base = base;
	}

	@Override
	public final void setup(List<? extends T> population)
	{
		base.setup(population);
		
		tempWeights.ensureCapacity(population.size());
		tempWeights.clear();
		
		totalWeight = 0f;
		float weight;
		
		List<? extends T> celebrities = celebritySelector.select(population, celebCount);
		
		for (T member : population)
		{
			float similaritySum = 0f;
			
			for (T celeb : celebrities)
				similaritySum += speciator.getSimilarity(member, celeb);
			
			weight = base.getNextWeight() * similaritySum;
			totalWeight += weight;
			tempWeights.add(weight);
		}
		
		currentWeight = 0;
	}
	
	@Override
	public final float getTotalWeight()
	{
		return totalWeight;
	}

	@Override
	public final float getNextWeight()
	{
		return tempWeights.get(currentWeight++);
	}
}