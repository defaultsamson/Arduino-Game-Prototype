package game.resource;

import java.awt.Rectangle;

import game.main.Game;

public class Square extends Rectangle
{
	private static final long serialVersionUID = -6941989656876115001L;

	public int id;
	public boolean filled;

	public boolean isFilled()
	{
		return filled;
	}

	public void fill()
	{
		filled = true;
	}

	public void empty()
	{
		filled = false;
	}

	public void setFill(boolean filled)
	{
		this.filled = filled;
	}

	public void toggleFill()
	{
		filled = !filled;
	}

	public Square(int x, int y)
	{
		this(false, x, y);
	}

	public Square(boolean filled, int x, int y)
	{
		this(filled, 0, x, y);
	}

	public Square(int id, int x, int y)
	{
		this(false, id, x, y);
	}

	public Square(boolean filled, int id, int x, int y)
	{
		super(x * Game.PIXEL_INTERVAL + Game.BOX_X_OFFSET, y * Game.PIXEL_INTERVAL + Game.BOX_Y_OFFSET,
				Game.PIXEL_INTERVAL, Game.PIXEL_INTERVAL);

		this.id = id;
		this.filled = filled;
	}
}
