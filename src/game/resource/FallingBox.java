package game.resource;

import java.util.Random;

import game.main.Game;

public class FallingBox
{
	private int x;
	private int y;
	
	private static final int progressInterval = 500;
	private long lastProgress;
	
	private Game game;
	
	public FallingBox(Game game)
	{
		this(game, new Random().nextInt(3) + 1); // range 1 - 4
	}
	
	public FallingBox(Game game, int x)
	{
		this.game = game;
		lastProgress = System.currentTimeMillis();
		this.x = x;
		this.y = 4;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public void progressY()
	{
		long currentTime = System.currentTimeMillis();
		
		if ((lastProgress + progressInterval) < currentTime)
		{
			lastProgress = currentTime;
			y -= 1;
			
			if (y < 1)
			{
				game.removeBox(this);
			}
		}
	}
}
