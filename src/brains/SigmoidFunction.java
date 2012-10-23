package brains;


/** 
 * A thread safe sigmoid function whose values are cached in a lookup table.
 * 
 * The sigmoid is defined by the function
 * {@code (float)(1d / (1d + Math.exp(6d * (sigmoidBegin + sigmoidEnd - 2d * x) / (sigmoidEnd - sigmoidBegin))))},
 * where {@code x} is the input, and {@code sigmoidBegin} and {@code sigmoidEnd} are the approximate beginning
 * and end of the 'S', respectively.
 * */
public final class SigmoidFunction implements IFunction
{
	private final float[] table;
	private final float floor, ceiling;
	private final float tableMultiply;
	private final int tableAdjust;
	
	/**
	 * @param tableSize The size of the cached sigmoid table (a {@code float} array).
	 * 0x10000 is the highest recommended value (it's very precise, and if the table is large enough,
	 * it may result in the OS paging memory in and out for successive calls...
	 * that's slow, and you don't want that.)<P>
	 * @param sigmoidBegin Where the 'S' should approximately begin curving upward.  It must not be equal to {@code sigmoidEnd}.<P>
	 * @param sigmoidEnd Where the 'S' should approximately begin curving downward.  It must not be equal to {@code sigmoidBegin}.
	 */
	public SigmoidFunction(int tableSize, double sigmoidBegin, double sigmoidEnd)
	{
		if (sigmoidBegin < sigmoidEnd)
		{
			this.floor = (float)sigmoidBegin;
			this.ceiling = (float)sigmoidEnd;
		}
		else if (sigmoidEnd < sigmoidBegin)
		{
			this.floor = (float)sigmoidEnd;
			this.ceiling = (float)sigmoidBegin;
		}
		else
			throw new Error("assert sigmoidEnd != sigmoidBegin failed!  Both were '" + sigmoidBegin + "'.");
		
		this.table = new float[tableSize];
		
		double range = sigmoidEnd - sigmoidBegin;
		
		this.tableAdjust = (int)(.5d - sigmoidBegin * (tableSize - 1) / range);
		this.tableMultiply = (float)((tableSize - 1) / range);
		
        for (int i = 0; i < tableSize; ++i)
        {
            table[i] = (float)(1d / (1d + Math.exp(6d * (sigmoidBegin + sigmoidEnd - 2d * (sigmoidBegin + range * i / (tableSize - 1))) / range)));
        }
	}
	
	@Override
	public final float output(float x)
	{
		if (x <= floor)
			return 0f;
		
		if (x >= ceiling)
			return 1f;
		
		return table[(int)(x*tableMultiply) + tableAdjust];
	}
}
