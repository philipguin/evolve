package weights;

import java.util.List;


public final class ExponentialWeightMaker<T> implements IWeightMaker<T>
{
	private final float toRaise;
	private float tempTotalWeight, previousWeight;
	
	public ExponentialWeightMaker(float toRaise)
	{
		this.toRaise = toRaise;
	}
	
	@Override
	public final void setup(List<? extends T> associated)
	{
		tempTotalWeight = 1f;
		
		for (int i = 0; i < associated.size(); ++i)
			tempTotalWeight += tempTotalWeight * toRaise;
		
		previousWeight = 1f;
	}
	
	@Override
	public final float getTotalWeight()
	{
		return tempTotalWeight;
	}
	
	@Override
	public final float getNextWeight()
	{
		return previousWeight * toRaise;
	}
	
	@Override
	public final void reset()
	{
		previousWeight = 1f;
	}
}