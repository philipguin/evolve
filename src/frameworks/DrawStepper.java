package frameworks;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;
import javax.swing.JPanel;

import draw.IDrawable;

public class DrawStepper implements IStepper
{
	private final JFrame window;
	private final Canvas content = new Canvas();
	private final BufferStrategy bufferStrategy;
	
	@SuppressWarnings("unused")
	private final int windowWidth, windowHeight;
	private final IDrawable<? super Graphics2D> drawable;
	
	private int frameRateCap = 40;
	
	public DrawStepper(
			String title,
			int windowWidth,
			int windowHeight,
			IDrawable<? super Graphics2D> drawable)
	{
		window = new JFrame(title);
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		this.drawable = drawable;
		
		window.setLayout(new FlowLayout());
		JPanel panel = (JPanel) window.getContentPane();
		panel.setPreferredSize(new Dimension(windowWidth, windowHeight));
		panel.setLayout(null);
		
		content.setBounds(0, 0, windowWidth, windowHeight);
		panel.add(content);
		panel.setIgnoreRepaint(true);
		
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setVisible(true);
		
		try {content.createBufferStrategy(2);} catch(Exception e){ e.printStackTrace(); }
		bufferStrategy = content.getBufferStrategy();
	}

	public final JFrame getFrame() { return window; }
	public final Canvas getCanvas() { return content; }
	
	public int getFrameRateCap() { return frameRateCap; }
	public void setFrameRateCap(int f) { frameRateCap = f; }
	
	private long lastTime = 0;
	
	public void step()
	{
		Graphics2D g = (Graphics2D)bufferStrategy.getDrawGraphics();
		
		drawable.draw(g);
		
		g.dispose();
		
		if (frameRateCap > 0)
		{
			long sleepTime = 1000 / frameRateCap - (System.currentTimeMillis() - lastTime);
		
			if (sleepTime > 0)
			{
				try { Thread.sleep(sleepTime); }
				catch (InterruptedException e) { e.printStackTrace(); }
			}
		}

		bufferStrategy.show();
		
		lastTime = System.currentTimeMillis();
	}
}
