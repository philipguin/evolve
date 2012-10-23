package brains;

import java.awt.Graphics2D;

public abstract class NeuralNetworkHelper
{
	private NeuralNetworkHelper() {}
	
	/**
	 *  Helpful for testing implementations of IFunction (Is it outputting the range and shape I was expecting?  Is my function precise enough?)
	 *  @param precision - The distance in terms of {@code x} between each point in the drawn line.
	 */
	public static final void drawActivationFunction(Graphics2D g, IFunction function, int x, int y, int width, int height, float funcStart, float funcEnd, int precision)
	{
		int curX, curY;
		int lastX = x;
		int lastY = y + (int)(function.output(funcStart) * height + .5f);
		float sigRange = funcEnd - funcStart;
		
		for (int i = precision; i < width; i += precision)
		{
			curX = x+i;
			curY = y+(int)(function.output(funcStart + sigRange * i / width) * height + .5f);
			
			g.drawLine(lastX, lastY, curX, curY);
			
			lastX = curX;
			lastY = curY;
		}

		curX = x + width;
		curY = y + (int)(function.output(funcStart + sigRange) * height + .5f);
		
		g.drawLine(lastX, lastY, curX, curY);
	}
}
