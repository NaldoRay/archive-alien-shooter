package shootingAlien;

import java.util.ArrayList;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.awt.Font;
import java.text.AttributedString;
import java.awt.font.TextAttribute;
import java.awt.Point;

public class Board extends JPanel implements Runnable
{	
	private final Bgm bgm;
	private Craft craft;
	private ArrayList <Alien> aliens;
	private final int pos[][] = {{300,20}, {250,150}, {160,80}, {360,230}};
	private boolean isPlay;
	protected final long DELAY  = 10;
	private short LIVES;
	private Thread currentThread;
	private boolean isWin;
	private volatile boolean finishPainting;
	
	private int WIDTH, HEIGHT;
	private static Board board = new Board();
	
	private Board()
	{
		this.setBackground(Color.BLACK);
		this.addKeyListener(new CraftAdapter());
		/*
		addKeyListener(new KeyAdapter() {
			public void keyPressed (KeyEvent ev)
			{}
			public void keyReleased(KeyEvent ev)
			{}
		});
		*/
		this.setFocusable(true);
		//this.setDoubleBuffered(true);
		
		bgm = Bgm.getInstance();
		craft = new Craft(this);
		isPlay = true;
		this.LIVES = 3;
		
		
		initAliens();
		isWin = false;
		currentThread = new Thread(this);
	}
	
	protected static Board getInstance()
	{
		return board;
	}
	
	protected void startAnim()
	{
		WIDTH = this.getWidth();
		HEIGHT = this.getHeight();
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run () 
			{
				currentThread.start();
				craft.allowFiring();
			}
		});
		bgm.setBGM("ff7battl.mid");
		bgm.start();
	}
	
	/*
	public void addNotify ()
	{
		super.addNotify();
		WIDTH = MainWindow.getInstance().getWidth();
		HEIGHT = MainWindow.getInstance().getHeight();
	}
	*/

	private synchronized boolean isShouldRun()
	{
		return isPlay;
	}
	
	private synchronized void stopAnim()
	{
		isPlay = false;
	}
	
	private void initAliens()
	{	
		aliens = new ArrayList<Alien>();
		for(int idx = 0; idx < pos.length; idx++)
			aliens.add(new Alien(this, pos[idx][0], pos[idx][1]));
	}
	
	
	public void run()
	{	
		long before, diff;
		ArrayList<Missile> missiles;
		System.out.println("START MAIN THREAD");
	
		while(isShouldRun())
		{	
			if (!isWin)
			{
				before = System.nanoTime();
				//System.out.println(before);
				missiles = craft.getMissiles();
				
				for (int idx = 0; idx < missiles.size(); idx++)
				{
					Missile m = missiles.get(idx);
					if (m.isVisible())
						m.move();
					else
						missiles.remove(idx);
				}
				for (int idx = 0; idx < aliens.size(); idx++)
				{
					//System.out.println("ALIEN");
					Alien a = aliens.get(idx);
					if (a.isAlive())
						a.move();
					else
						aliens.remove(idx);
				}
				//System.out.println("--------------------------------------------------------------");
				craft.move();
				checkCollisions();
				//System.out.println("--------------------------POST COLLISION---------------");
				
				
				repaint();
				//delayThread(1);
				diff = (System.nanoTime() - before)/1000000;
				//System.out.println(diff + "\n---------------------------POST REPAINT--------------------------------");
				diff = DELAY - diff;
				
				if (diff < 0)
					diff = 0;
	
				delayThread(diff);
								
				if(aliens.size() == 0)
				{
					isWin = true;
					repaint();
					delayThread(100);
				}
			}
			else
			{
				craft.resetPos();
				craft.clearMissiles();
				isWin = false;
				initAliens();
				delayThread(3000);
				repaint();
				delayThread(1000);
			}
		}
		bgm.stop();
		//System.out.println(isPlay);
		//i = 2;
		//System.out.println(isShouldRun());
		
		//System.exit(0);
	}
	
	private void delayThread(long diff)
	{
		try
		{//System.out.println("-------------------------- SLEEP--------------------------------");
			Thread.sleep(diff);//System.out.println("---------------------------AFTER SLEEP--------------------------------");
		}
		catch(InterruptedException e)
		{e.printStackTrace();}
	}

	public void paint(Graphics g)
	{
		super.paint(g);
		//System.out.println("REPAINT");
		Graphics2D g2d = (Graphics2D) g;
		AttributedString amsg;
		Point p;
		String msg;
		Font f;
		FontMetrics metric;
		
		if(isShouldRun())
		{
			if (isWin)
			{
				p = craft.getPos();
				g2d.drawImage(craft.getImage(), p.x, p.y, this);
				
				msg = "You Win";
				f = new Font("SansSerif", Font.BOLD + Font.ITALIC, 28);
				metric = this.getFontMetrics(f);
				amsg = new AttributedString(msg);
				amsg.addAttribute(TextAttribute.FONT, f);
				amsg.addAttribute(TextAttribute.FOREGROUND, Color.BLUE);
				
				g2d.drawString(amsg.getIterator(), (WIDTH - metric.stringWidth(msg))/2, HEIGHT/2);
			}
			else
			{
				p = craft.getPos();
				g2d.drawImage(craft.getImage(), p.x, p.y, this);
				
				ArrayList<Missile> missiles = craft.getMissiles();
				
				for(int idx = 0; idx < missiles.size(); idx++)
				{
					Missile m = missiles.get(idx);
					if (m.isVisible())
					{
						p = m.getPos();
						g2d.drawImage(m.getImage(), p.x, p.y, this);
					}
				}
				
				for(int idx = 0; idx < aliens.size(); idx++)
				{
					Alien a = aliens.get(idx);
					if (a.isAlive())
					{
						p = a.getPos();
						g2d.drawImage(a.getImage(), p.x, p.y, this);
					}
				}
				msg = "Aliens Left : " + (aliens.size());
				f = new Font("SansSerif", Font.BOLD, 14);
				metric = this.getFontMetrics(f);
				amsg = new AttributedString(msg);
				amsg.addAttribute(TextAttribute.FONT, f);
				amsg.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
				
				g2d.drawString(amsg.getIterator(), (WIDTH - metric.stringWidth(msg)-10), HEIGHT - 14);
				
				msg = "Lives : " + LIVES;
				amsg = new AttributedString(msg);
				amsg.addAttribute(TextAttribute.FONT, f);
				amsg.addAttribute(TextAttribute.FOREGROUND, Color.WHITE);
				g2d.drawString(amsg.getIterator(), 15, 14);
			}
		}
		else
		{
			for(int idx = 0; idx < aliens.size(); idx++)
			{
				Alien a = aliens.get(idx);
				p = a.getPos();
				g2d.drawImage(a.getImage(), p.x, p.y, this);
			}

			msg = "Game Over";
			f = new Font("SansSerif", Font.BOLD + Font.ITALIC, 28);
			metric = this.getFontMetrics(f);
			
			amsg = new AttributedString(msg);
			amsg.addAttribute(TextAttribute.FONT, f);
			amsg.addAttribute(TextAttribute.FOREGROUND, Color.red);
			
			g2d.drawString(amsg.getIterator(), (WIDTH - metric.stringWidth(msg)) / 2, HEIGHT / 2);
		}
		g.dispose();
		//System.out.println("AFTER REPAINT" + " " + System.nanoTime());
	}
	
	private void checkCollisions()
	{
		ArrayList<Missile> missiles = craft.getMissiles();
		//System.out.println("--------------------------COLLISION---------------");
		Rectangle cr;
		for(int idx = 0; idx < aliens.size(); idx++)
		{
			cr = craft.getBounds();
			Alien a = aliens.get(idx);
			Rectangle ar = a.getBounds();
			if (cr.intersects(ar))
			{
				LIVES--;
				if (LIVES > 0)
					craft.resetPos();
				else
				{
					craft.setVisible(false);
					//a.setVisible(false);
					this.stopAnim();
					
					break;
				}
				//System.out.println(LIVES);
			}
			for(int idx2 = 0; idx2 < missiles.size(); idx2++)
			{
				Missile m = missiles.get(idx2);
				if (m.getBounds().intersects(ar))
				{
					m.setVisible(false);
					a.setVisible(false);
				}
			}
		}	
		//System.out.println("----------------------AFTER----COLLISION---------------");		
	}
	
	private class CraftAdapter extends KeyAdapter
	{
		public void keyPressed (KeyEvent ev)
		{
			craft.keyPressed(ev);
		}
		
		public void keyReleased (KeyEvent ev)
		{
			craft.keyReleased(ev);
		}
	}	
}