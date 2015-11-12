package game.main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import game.resource.FallingBox;
import game.resource.Squares;

public class Game extends JFrame implements KeyListener
{
	public static final int PIXEL_INTERVAL = 64;

	private static final long serialVersionUID = 5767179268856809354L;

	public static final int PANEL_SIZE_X = 263; // 263
	public static final int PANEL_SIZE_Y = 286; // 286

	public static final int BOX_X_OFFSET = 0;
	public static final int BOX_Y_OFFSET = 0;

	private int playerX = 2;

	public static final int MAX_SCORE = 185;
	private int score = 0;
	private int displayScore = 0;

	private Squares pixels;

	private List<FallingBox> boxes;
	private List<FallingBox> boxesToRemove;

	public Game()
	{
		super("Arduino Game Prototype");

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(PANEL_SIZE_X, PANEL_SIZE_Y);
		setResizable(false);
		setLocationRelativeTo(null); // Starts window in centre of screen

		addKeyListener(this);

		pixels = new Squares();
		// Generates the background
		for (int iRow = 0; iRow < 4; iRow++)
		{
			for (int iCol = 0; iCol < 4; iCol++)
			{
				pixels.addSquare((iRow * 4) + iCol, iCol, iRow);
			}
		}
		getContentPane().add(pixels);

		boxes = new ArrayList<FallingBox>();
		boxesToRemove = new ArrayList<FallingBox>();

		// Makes this visible
		setVisible(true);

		run();
	}

	private void run()
	{
		final int TICKS_PER_SECOND = 20;

		long lastSystemTime = System.currentTimeMillis();
		int interval = 1000 / TICKS_PER_SECOND;

		while (true)
		{
			long currentTime = System.currentTimeMillis();

			if ((lastSystemTime + interval) < currentTime)
			{
				if (!isDed)
				{
					tick();
					render();
				}
				else
				{
					ded();
				}
				lastSystemTime = currentTime;
			}
		}
	}

	public static final int scoreInterval = 100;
	private long scoreNext = System.currentTimeMillis();
	private int frameScore = 0;
	private boolean showScore = false;

	public static final int dedInterval = 1000;
	private long dedNext = System.currentTimeMillis();
	private int frame = 0;

	private void ded()
	{
		pixels.clear();

		long currentTime = System.currentTimeMillis();

		if ((dedNext + dedInterval) < currentTime)
		{
			dedNext = currentTime;
			frame++;
		}

		if ((scoreNext + scoreInterval) < currentTime && showScore)
		{
			scoreNext = currentTime;

			if (frameScore < displayScore) 
			{
				frameScore++;
			}
		}

		switch (frame)
		{
			case 1: // Draws an e
				pixels.getSquare(1, 4).fill();
				pixels.getSquare(2, 4).fill();
				pixels.getSquare(3, 4).fill();
				pixels.getSquare(1, 3).fill();
				pixels.getSquare(2, 3).fill();
				pixels.getSquare(3, 3).fill();
				pixels.getSquare(1, 2).fill();
				pixels.getSquare(2, 1).fill();
				pixels.getSquare(3, 1).fill();
				break;

			case 2: // Draws an n
				pixels.getSquare(1, 1).fill();
				pixels.getSquare(1, 2).fill();
				pixels.getSquare(1, 3).fill();
				pixels.getSquare(2, 3).fill();
				pixels.getSquare(3, 2).fill();
				pixels.getSquare(3, 1).fill();
				break;

			case 3: // Draws a d
				pixels.getSquare(1, 1).fill();
				pixels.getSquare(2, 1).fill();
				pixels.getSquare(3, 1).fill();
				pixels.getSquare(1, 2).fill();
				pixels.getSquare(2, 3).fill();
				pixels.getSquare(3, 2).fill();
				pixels.getSquare(3, 3).fill();
				pixels.getSquare(3, 4).fill();
				break;
			case 4:
				showScore = true;
				scoreNext = currentTime;
				break;
		}

		if (showScore)
		{
			switch (frameScore)
			{
				case 16:
					pixels.getSquare(15).fill();
				case 15:
					pixels.getSquare(14).fill();
				case 14:
					pixels.getSquare(13).fill();
				case 13:
					pixels.getSquare(12).fill();
				case 12:
					pixels.getSquare(11).fill();
				case 11:
					pixels.getSquare(10).fill();
				case 10:
					pixels.getSquare(9).fill();
				case 9:
					pixels.getSquare(8).fill();
				case 8:
					pixels.getSquare(7).fill();
				case 7:
					pixels.getSquare(6).fill();
				case 6:
					pixels.getSquare(5).fill();
				case 5:
					pixels.getSquare(4).fill();
				case 4:
					pixels.getSquare(3).fill();
				case 3:
					pixels.getSquare(2).fill();
				case 2:
					pixels.getSquare(1).fill();
				case 1:
					pixels.getSquare(0).fill();
					break;
			}
		}

		pixels.paintComponent(getContentPane().getGraphics());
	}

	private static final int MIN_INTERVAL = 200;
	public static int createInterval = 1000;
	private long lastCreate = System.currentTimeMillis() - createInterval;

	private boolean isDed = false;

	private void tick()
	{
		boolean leftPressed = isLeftPressed();
		boolean rightPressed = isRightPressed();

		if (leftPressed)
		{
			if (playerX > 1)
			{
				playerX--;
			}
		}

		if (rightPressed)
		{
			if (playerX < 4)
			{
				playerX++;
			}
		}

		long currentTime = System.currentTimeMillis();

		// Creates a new box every createInterval
		if ((lastCreate + createInterval) < currentTime)
		{
			score++;

			boxes.add(new FallingBox(this));
			lastCreate = currentTime;

			if (FallingBox.getBoxSpeed() > FallingBox.MIN_INTERVAL)
			{
				FallingBox.addBoxSpeed(-5);
			}

			if (Game.createInterval > MIN_INTERVAL)
			{
				createInterval -= 5;
			}
		}

		// Makes all the boxes fall my one pixel
		for (FallingBox box : boxes)
		{
			box.progressY();
		}

		// Progresses all boxes
		for (FallingBox box : boxes)
		{
			box.progressY();
		}

		// Removes all the boxes that are set to fall.
		for (FallingBox box : boxesToRemove)
		{
			boxes.remove(box);
		}
		boxesToRemove.clear();

		boolean isColliding = false;

		// Tests for player collision
		for (FallingBox box : boxes)
		{
			// If the box has the same coordinates of the player
			if (box.getX() == playerX && box.getY() == 1)
			{
				isColliding = true;
			}
		}

		if (isColliding)
		{
			// Gets the display score. (Maximum score is 100)
			displayScore = (int) Math.min((float) score / (float) MAX_SCORE * 16F, 16);
			isDed = true;
			System.out.println("SCORE: " + displayScore + "/16");
		}
	}

	private void render()
	{
		pixels.clear();

		pixels.getSquare(playerX, 1).fill();

		for (FallingBox box : boxes)
		{
			pixels.getSquare(box.getX(), box.getY()).fill();
		}

		pixels.paintComponent(getContentPane().getGraphics());
	}

	public void removeBox(FallingBox box)
	{
		boxesToRemove.add(box);
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	boolean leftPressed = false;
	boolean leftPressedFiltered = false;

	private boolean isLeftPressed()
	{
		boolean toReturn = leftPressedFiltered;

		// Sets it false so that it can only be used once.
		leftPressedFiltered = false;

		return toReturn;
	}

	boolean rightPressed = false;
	boolean rightPressedFiltered = false;

	private boolean isRightPressed()
	{
		boolean toReturn = rightPressedFiltered;

		// Sets it false so that it can only be used once.
		rightPressedFiltered = false;

		return toReturn;
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT && !leftPressed)
		{
			leftPressed = true;
			leftPressedFiltered = true;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT && !rightPressed)
		{
			rightPressed = true;
			rightPressedFiltered = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
		{
			leftPressed = false;
		}

		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			rightPressed = false;
		}
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER)
		{
			isDed = false;
		}
	}
}