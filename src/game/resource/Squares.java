package game.resource;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class Squares extends JPanel
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
		for (Square square : squares)
		{
			if (square.id == id) return square;
		}
		return null;
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
	public void paintComponent(Graphics g)
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
