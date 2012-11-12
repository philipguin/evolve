package weights;

import java.util.ArrayList;
import java.util.List;

import speciators.ISpeciator;



public final class AbsoluteSpeciatedWeightMaker<T> implements IWeightMaker<T>
{
	private final ISpeciator<? super T> speciator;
	private final ArrayList<Float> tempWeights = new ArrayList<Float>();
	private final IWeightMaker<? super T> base;

	private float totalWeight;
	private int currentWeight;
	
	public AbsoluteSpeciatedWeightMaker(ISpeciator<? super T> speciator, IWeightMaker<? super T> base)
	{
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
		
		for (T member : population)
		{
			float similaritySum = 0f;
			
			for (T other : population)
				similaritySum += speciator.getSimilarity(member, other);
			
			weight = base.getNextWeight() / similaritySum;
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
	
	@Override
	public final void reset()
	{
		currentWeight = 0;
	}
}