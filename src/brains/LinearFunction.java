package brains;

/**
 * A function whose output is defined by the line {@code x * slope + yIntercept}.
 * A slope of 1f and yIntercept of 0f would simply return x.
 */
public final class LinearFunction implements IFunction
{
	private final float slope, yIntercept;
	
	public LinearFunction(float slope, float yIntercept)
	{
		this.slope = slope;
		this.yIntercept = yIntercept;
	}

	@Override
	public final float output(float x)
	{
		return x * slope + yIntercept;
	}
}
