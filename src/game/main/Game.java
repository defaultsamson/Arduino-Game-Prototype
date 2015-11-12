package game.main;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

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
	}
	
	private void render()
	{
		pixels.clear();
		
		pixels.getSquare(playerX, 1).fill();
		
		pixels.paintComponent(getContentPane().getGraphics());
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

class Square extends Rectangle
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
		super(x * Game.PIXEL_INTERVAL + Game.BOX_X_OFFSET, y * Game.PIXEL_INTERVAL + Game.BOX_Y_OFFSET, Game.PIXEL_INTERVAL, Game.PIXEL_INTERVAL);

		this.id = id;
		this.filled = filled;
	}
}

class Squares extends JPanel
{
	private static final long serialVersionUID = 6667228252442519364L;

	private static final int PREF_W = 500;
	private static final int PREF_H = PREF_W;
	private List<Square> squares = new ArrayList<Square>();

	public void addSquare(int id, int x, int y)
	{
		Square square = new Square(id, x, y);
		squares.add(square);
	}
	
	public void clear()
	{
		for (Square square : squares)
		{
			square.setFill(false);
		}
	}
	
	public void addSquare(Square square)
	{
		squares.add(square);
	}

	public Square getSquare(int id)
	{
		return squares.get(id);
	}

	public Square getSquare(int x, int y)
	{
		// Allows coords parameters from bottom left from 1-4
		// Instead of top right from 0-3
		y = 4 - y;
		x -= 1;

		int id = (y * 4) + x;

		return getSquare(id);
	}

	@Override
	public Dimension getPreferredSize()
	{
		return new Dimension(PREF_W, PREF_H);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		for (Square rect : squares)
		{
			if (rect.filled)
			{
				g2.fillRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
			}
			else
			{
				g2.draw(rect);
			}
		}
	}

}