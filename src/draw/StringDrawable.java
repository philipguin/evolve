package draw;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class StringDrawable implements IDrawable<Graphics>
{
	private final int x, y, width;

	public static enum Alignment { left, center, right }

	private String string;
	private Font font;
	private Alignment alignment = Alignment.left;
	private Color color = Color.black;
	
	public StringDrawable(int x, int y, int width)
	{
		this(x, y, width, null);
	}
	
	public StringDrawable(int x, int y, int width, String string)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.string = string;
	}

	public String getString() { return this.string; }
	public void setString(String string) { this.string = string; }
	
	public Alignment getAlignment() { return this.alignment; }
	public void setAlignment(Alignment alignment) { this.alignment = alignment == null ? Alignment.left : alignment; }
	
	public Font getFont() { return this.font; }
	public void setFont(Font font) { this.font = font; }
	
	public Color getColor() { return this.color; }
	public void setColor(Color color) { this.color = color; }
	
	public void draw(Graphics g)
	{
		if (string == null)
			return;
		
		int drawX;
		
		switch (alignment)
		{
		default:
		case left:
			drawX = x;
			break;
			
		case center:
			drawX = x + (width - g.getFontMetrics().stringWidth(string)) / 2;
			break;
			
		case right:
			drawX = x + width - g.getFontMetrics().stringWidth(string);
			break;
		}
		
		if (color != null)
			g.setColor(color);
		
		if (font == null)
		{
			g.drawString(string, drawX, y);
		}
		else
		{
			Font oldFont = g.getFont();
			g.setFont(font);
			g.drawString(string, drawX, y);
			g.setFont(oldFont);
		}
	}
}
