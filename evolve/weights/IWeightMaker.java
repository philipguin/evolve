package weights;

import java.util.List;

public interface IWeightMaker<T>
{
	public void setup(List<? extends T> associatedList);
	public float getTotalWeight();
	public float getNextWeight();
}
