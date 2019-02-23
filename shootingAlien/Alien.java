package shootingAlien;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

public class Alien
{
	private final Image img;
	private final String imgPath = "sprites/alien.png";
	
	private final int WIDTH, HEIGHT;
	private Point coord;
	
	private Board board;
	private boolean isAlive;
	private boolean dir;
	private int counter;
	private Random rand;
	
	protected Alien (Board board, int x, int y)
	{
		this.board = board;
		coord = new Point (x, y);
		
		img = new ImageIcon(this.getClass().getResource(imgPath)).getImage();
		
		WIDTH = img.getWidth(null);
		HEIGHT = img.getHeight(null);
		
		rand = new Random();
		isAlive = true;
		counter = 3;
	}
	
	protected Point getPos()
	{
		return new Point (coord.x, coord.y);
	}
	
	protected boolean isAlive()
	{
		return this.isAlive;
	}
	
	protected void move()
	{
		int limitY = board.getHeight() - HEIGHT;
		int backX = board.getWidth()-1;
		
		if (dir)
		{
			coord.x -= 1;
			coord.y += 1;
		}
		else
		{
			coord.x -= 1;
			coord.y -= 1;
		}
		if (coord.y < 1)
			coord.y = 1;
		else if (coord.y > limitY)
			coord.y = limitY;
		if (coord.x < 1)
			coord.x = backX;
		counter--;
		if (counter == 0)
			changeDir();
	}
	
	protected Rectangle getBounds()
	{
		return new Rectangle(coord.x,coord.y, WIDTH, HEIGHT);
	}
	
	protected Image getImage()
	{
		return this.img;
	}
	
	protected void setVisible(boolean flag)
	{
		isAlive = flag;
	}
	
	private void changeDir ()
	{
		dir = !dir;
		counter = rand.nextInt(15) + 10; 
	}
}