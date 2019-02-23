package shootingAlien;

import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Point;

public class Missile
{
	private final String IMGPATH = "sprites/missile.PNG";
	private final Image img;

	private Point coord;
	private final int WIDTH, HEIGHT;
	private boolean isVisible;
	private final int SPEED = 2;
	private int BOARD_WIDTH;
	
	protected Missile(int bw, int x, int y)
	{
		this.BOARD_WIDTH = bw;
		//this.BOARD_HEIGHT = bh;
		
		coord = new Point (x, y);
		img = new ImageIcon(this.getClass().getResource(IMGPATH)).getImage();
		WIDTH = img.getWidth(null);
		HEIGHT = img.getHeight(null);
		
		isVisible = true;
	}
	
	protected Rectangle getBounds()
	{
		return new Rectangle(coord.x,coord.y, WIDTH, HEIGHT);
	}
		
	protected Image getImage()
	{
		return img;
	}

	protected Point getPos()
	{
		return new Point (coord.x, coord.y);
	}
	
	protected boolean isVisible()
	{	
		return isVisible;
	}
	
	protected void setVisible(boolean flag)
	{
		isVisible = flag;
	}
	
	protected void move()
	{
		coord.x += SPEED;
		if (coord.x > BOARD_WIDTH - 15)
			setVisible(false);
	}	
}