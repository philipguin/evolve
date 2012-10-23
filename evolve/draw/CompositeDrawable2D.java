package draw;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class CompositeDrawable2D<G extends Graphics2D> implements IDrawable<G>
{
	private final List<? extends IDrawable<? super G>> frames;
	private final List<Point> frameOffsets;
	
	public CompositeDrawable2D(List<? extends IDrawable<? super G>> frames, List<Point> frameOffsets)
	{
		if (frames.size() != frameOffsets.size())
			throw new Error("Error: frames' length != frameOffsets' length!");
		
		this.frames = frames;
		this.frameOffsets = frameOffsets;
	}
	
	@Override
	public void draw(G g)
	{
		for (int i = 0; i < frames.size(); ++i)
		{
			Point offset = frameOffsets.get(i);
			g.translate(offset.x, offset.y);
			frames.get(i).draw(g);
			g.translate(-offset.x, -offset.y);
		}
	}
}
