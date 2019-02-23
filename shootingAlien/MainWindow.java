package shootingAlien;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame
{
	private static MainWindow wind;
	private JMenuBar menuBar;
	
	private Board board;
	
	private MainWindow ()
	{
		init();
	}
	
	public void startGame()
	{
		setVisible(true);
		board.startAnim();
	}
	
	private void init ()
	{
		board = Board.getInstance();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Alien Shooter Beta v.001");
		
		this.add(board);
		
		/*
		menuBar = new JMenuBar();
		JMenu file = new JMenu("File");
		JMenuItem newG = new JMenuItem ("New Game");
		newG.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev)
			{
				restart();
			}
		});
		file.add(newG);
		menuBar.add(file);
		
		this.setJMenuBar (menuBar);
		//*/
		this.setSize(400,300);
		
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
	
	/*private void restart()
	{
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run (){
				board.restartGame();
			}
		});
	}*/
	
	public static MainWindow getInstance()
	{
		if (wind == null)
			wind = new MainWindow();
		return wind;
	}
}