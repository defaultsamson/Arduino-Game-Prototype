package game.resource;

import java.util.Random;

import game.main.Game;

public class FallingBox
{
	private int x;
	private int y;
	
	public static final int MIN_INTERVAL = 100;
	private static int progressInterval = 500;
	private long lastProgress;
	private boolean hang = true;
	
	private Game game;
	
	public FallingBox(Game game)
	{
		this(game, new Random().nextInt(4) + 1); // range 1 - 4
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
	
	public static int getBoxSpeed()
	{
		return FallingBox.progressInterval;
	}
	
	public static void addBoxSpeed(int speed)
	{
		FallingBox.progressInterval += speed;
	}
	
	public void progressY()
	{
		long currentTime = System.currentTimeMillis();
		
		if ((lastProgress + FallingBox.progressInterval) < currentTime)
		{
			if (!hang)
			{
				y -= 1;
				
				if (y < 1)
				{
					game.removeBox(this);
				}
			}
			else
			{
				hang = false;
			}
			
			lastProgress = currentTime;
		}
	}
}
