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
				tick();
				render();
				lastSystemTime = currentTime;
			}
		}
	}

	private static final int createInterval = 3000;
	private long lastCreate = System.currentTimeMillis() - createInterval;

	private void tick()
	{
		boolean leftPressed = isLeftPressed();
		boolean rightPressed = isRightPressed();

		if (leftPressed)
		{
			if (playerX > 1)
			{
				playerX--;
				System.out.println("LEFT");
			}
		}

		if (rightPressed)
		{
			if (playerX < 4)
			{
				playerX++;
				System.out.println("RIGHT");
			}
		}

		long currentTime = System.currentTimeMillis();

		// Creates a new box every createInterval
		if ((lastCreate + createInterval) < currentTime)
		{
			boxes.add(new FallingBox(this));
			lastCreate = currentTime;
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
	}
}