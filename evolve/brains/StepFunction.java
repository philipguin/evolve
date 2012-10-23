package brains;

/**
 * A simple function that returns 1 if x > threshold, 0 otherwise
 * (where threshold is parameterized.)  For a {@link NeuralNetwork}'s
 * activation function, 0.5 is a typical, working value.
 */

public final class StepFunction implements IFunction
{
	private final float threshold;
	
	public StepFunction(float threshold)
	{
		this.threshold = threshold;
	}
	
	@Override
	public final float output(float x)
	{
		return x >= threshold ? 1f : 0f;
	}
}
