package pacmanpp;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;

/*
 * MainApp contains the calls needed to start the Pacman++ Game
 */
public class MainApp extends JFrame implements MouseListener, KeyListener
{
	// rate of the update
	private int step = 75;
	
	// this timer is used to determine the next updatestep
	private long timer = -1;

	// This timer is used to do request new frames be drawn
	javax.swing.Timer frameTimer;
	
	
	// game entities
	private World w = World.getInstance();
	
	
	public MainApp() {}
    
	public void init()
	{
		
		this.setTitle("PACMAN++");
		
		// need to get the board to pack the JFrame and get a correct windowsize
		
		this.setVisible(true);
		this.pack();
		this.setResizable(true);
		
		// adding the world to the scene
		this.add(w);
		this.pack();
		
		this.addMouseListener(this);
		this.addKeyListener(this);
		
		
		while (true)
		{
			if (timer == -1)
			{
				timer = System.nanoTime();
			}
			else
			{
				long newTimer = System.nanoTime();
				if ((int)(newTimer - timer) / 1000000 > step)
				{
					World w1 = w.newGame();
					if (w1 != w)
					{
						Util.simpleTrace("NEW GAME!");
						this.remove(w);
						w = w1;
						this.add(w);
					}
					w.update();
					w.repaint();
					
					try
					{
						//Thread.sleep(step);
						Thread.sleep(step-(long)((System.nanoTime()-newTimer)/1000000.f));
					}
					catch (InterruptedException ex)
					{
						Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
					}
					// reset the timer
					timer = -1;
				}
			}
		}
	}
	
	public static void main(String[] args)
	{
		MainApp app = new MainApp();
		app.init();
	}
	
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void mousePressed(MouseEvent e)
	{
		//w.mouseAction(e.getX(), e.getY());
	}
	@Override
	public void keyPressed(KeyEvent e)
	{
		switch (e.getKeyCode())
		{
			case KeyEvent.VK_UP:
				w.updatePlayerDirection(Constants.UP);
				break;
			case KeyEvent.VK_DOWN:
				w.updatePlayerDirection(Constants.DOWN);
				break;
			case KeyEvent.VK_LEFT:
				w.updatePlayerDirection(Constants.LEFT);
				break;
			case KeyEvent.VK_RIGHT:
				w.updatePlayerDirection(Constants.RIGHT);
				break;
			case KeyEvent.VK_M:
			//	w.switchSound();
				break;
			case KeyEvent.VK_Q:
				w.printMAP();
				break;
		}
	}
}