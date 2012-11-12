package weights;

import java.util.List;

public interface IWeightMaker<T>
{
	/** Prepares for one or more iterations over the weights of an associated list. */
	public void setup(List<? extends T> associatedList);
	
	/** Reset the iteration, such that the next call to "getNextWeight" will return the first weight.
	 * Callers have the option of calling this after "setup", but it is not required of them. */
	public void reset();
	
	/** Return the sum of all weights in the iteration.
	 * This may be called as often as desired during an iteration,
	 * so if necessary, implementors should calculate this during
	 * "setup" for good performance. */
	public float getTotalWeight();
	
	/** Returns the next weight of the iteration.  Should not be called once
	 * the sum of its returned values is greater than or equal to "getTotalWeight,"
	 * or bad things might happen. */
	public float getNextWeight();
}
