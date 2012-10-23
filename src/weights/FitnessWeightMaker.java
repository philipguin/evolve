package weights;

import java.util.List;


import fitness.IFitnessed;



public final class FitnessWeightMaker<T extends IFitnessed> implements IWeightMaker<T>
{
	private List<? extends T> associated;
	private float tempTotalWeight;
	private int currentWeight;
	
	@Override
	public final void setup(List<? extends T> associated)
	{
		this.associated = associated;
		
		tempTotalWeight = 0f;
		
		for (int i = 0; i < associated.size(); ++i)
			tempTotalWeight += associated.get(i).getFitness();
		
		currentWeight = 0;
	}
	
	@Override
	public final float getTotalWeight()
	{
		return tempTotalWeight;
	}
	
	@Override
	public final float getNextWeight()
	{
		return associated.get(currentWeight++).getFitness();
	}
}