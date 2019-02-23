package shootingAlien;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Point;
import javax.swing.ImageIcon;
import java.awt.event.KeyEvent;

import java.util.ArrayList;

public class Craft
{		
	private final String imgPath = "sprites/craft.png";
	private final Image img;
	
	private Point coord;
	private final int WIDTH, HEIGHT;
	
	private int mx, my;
	private boolean isAlive;
	private ArrayList<Missile> missiles;

	private Board board;
	
	private final int STARTX = 10;
	private final int STARTY = 115;
	private boolean isFiring;
	private FiringThread triggerer;
	private int BOARD_WIDTH, BOARD_HEIGHT;
	
	protected Craft(Board board)
	{
		this.board = board;
		
		coord = new Point (STARTX,STARTY);
		
		img = new ImageIcon(this.getClass().getResource(imgPath)).getImage();
		WIDTH = img.getWidth(null);
		HEIGHT = img.getHeight(null);
		
		isFiring = false;
		isAlive = true;
		
		missiles = new ArrayList<Missile>();
		
		triggerer = new FiringThread();
	}
	
	protected void allowFiring()
	{
		BOARD_WIDTH = board.getWidth();
		BOARD_HEIGHT = board.getHeight();
		triggerer.start();
	}
	
	protected void resetPos()
	{
		coord.x = STARTX;
		coord.y = STARTY;
	}
	
	protected void clearMissiles()
	{
		missiles.clear();
	}
	
	protected Point getPos()
	{
		return this.coord;
	}
	
	protected Rectangle getBounds()
	{
		return new Rectangle(coord.x,coord.y, WIDTH, HEIGHT);
	}
	
	protected Image getImage()
	{
		return this.img;
	}
	
	protected ArrayList<Missile> getMissiles()
	{
		return  this.missiles;
	}
	
	protected synchronized boolean isAlive ()
	{
		return isAlive;
	}
	
	protected synchronized void setVisible(boolean flag)
	{
		isAlive = flag;
		if (!isAlive)
			triggerer.stopThread();
	}
	
	protected void move()
	{
		//MainWindow main = MainWindow.getInstance();
		
		//int limitX = main.getWidth() - WIDTH - 5;
		//int limitY = main.getHeight() - HEIGHT - 30;	// karena titik 0,0 JPanel diambil dari content framenya, 
														// sedangkan ukuran tinggi JPanelnya sama kek JFramenya jadi perlu
														// dikurang sama "tinggi title bar" -nya, yaitu 30px.
		
		int limitX = BOARD_WIDTH - WIDTH - 5;
		int limitY = BOARD_HEIGHT - HEIGHT;
		
		coord.x += mx;
		coord.y += my;
		
		if (coord.x < 1)
			coord.x = 1;
		else if (coord.x > limitX)
			coord.x = limitX;
		if (coord.y < 1)
			coord.y = 1;
		else if (coord.y > limitY)
			coord.y = limitY;
	}
	
	private void fire()
	{
		missiles.add(new Missile(BOARD_WIDTH, coord.x + WIDTH, coord.y+(HEIGHT/2) ));
	}
	
	protected void keyPressed(KeyEvent ev)
	{
		int key = ev.getKeyCode();
		switch (key)
		{
			case KeyEvent.VK_SPACE: isFiring = true; break;
			case KeyEvent.VK_LEFT: mx = -1; break;
			case KeyEvent.VK_RIGHT: mx = 1; break;
			case KeyEvent.VK_UP: my = -1; break;
			case KeyEvent.VK_DOWN: my = 1; ;break;
		}		
	}
	
	protected void keyReleased(KeyEvent ev)
	{
		int key = ev.getKeyCode();
		
		if (key == KeyEvent.VK_SPACE)
			isFiring = false;
		else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT)
			mx = 0;
		else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN)
			my = 0;
	}
	
	private class FiringThread extends Thread
	{
		private boolean isRun;
		private int FIRING_DELAY = 500;
		
		private FiringThread ()
		{
			isRun = true;
		}
		
		private synchronized void stopThread()
		{
			isRun = false;
		}
		
		public void run()
		{
			System.out.println("--Start FIRING THREAD-----");
			while(isRun)
			{	
				if (isFiring)
				{
					fire();
					try
					{
						sleep (FIRING_DELAY);
					}
					catch(InterruptedException e)
					{System.out.println(e);}
				}
				//System.out.println("ASD");
			}
			System.out.println("--STOP FIRING THREAD-----");
		}
	}	
}