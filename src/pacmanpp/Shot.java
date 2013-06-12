/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanpp;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 *
 * @author João Araújo
 */
public class Shot extends JComponent {

    private int direction;
	private int position_x;
	private int position_y;
	
	// game images for shot
	private GameImages gim = null;
	
	// directions
    private static final int UP = 1;
    private static final int DOWN = 2;
    private static final int LEFT = 3;
    private static final int RIGHT = 4;
	
	private int grid_unit;
	
	World w;
	Image shot_sprite;
    
    public Shot(int dir, int x, int y)
	{
        direction = dir;
		gim = GameImages.getInstance();
		
		w = World.getInstance();
		w.add(this);
		
		this.setVisible(true);
		this.setPosition(dir, x, y);
    }
	
	public void setPosition(int dir, int x, int y)
	{
		int newx = x;
		int newy = y;
		switch(dir)
		{
			case UP:
				newy = y-1;
				break;
			case DOWN:
				newy = y+1;
				break;
			case LEFT:
				newx = x-1;
				break;
			case RIGHT:
				newx = x+1;
				break;
		}
		
		if (newx < 0 || newx > Constants.WORLD_WIDTH || w.isWall(newx,newy))
		{
			w.remove(this);
		}
		else if (w.isCrystal(newx, newy))
		{
			w.remove(this);
			w.destroyCrystal(newx,newy);
		}
		else if(w.isGhost(newx, newy))
		{
			w.remove(this);
			w.incGhostEnergy(newx, newy);
		}
		else
		{
			if (w.isPacman(newx,newy))
			{
				w.remove(this);
				w.hitPacman();
			}
			else
			{
				position_x = newx;
				position_y = newy;
			}
		}
		
	}
	
	public void move()
	{
		setPosition(direction, position_x, position_y);
	}
    
    public void paint(Graphics g) {
            this.move();
			g.drawImage(gim.getShotSprite(), 0, 0, null);
    }   
    
}
