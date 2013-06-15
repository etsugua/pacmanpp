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
	
	private int grid_unit;
	
	private World w;
	private Image shot_sprite;
	
	private boolean hit_something;
    
    public Shot(int dir, int x, int y)
	{
        direction = dir;
		gim = GameImages.getInstance();
		
		w = World.getInstance();
		w.add(this);
		
		this.hit_something = false;
		
		this.setVisible(true);
		
		position_x = x;
		position_y = y;
    }
	
	public int [] getPosition()
	{
		int [] ret = { this.position_x , this.position_y };
		return ret;
	}
	
	public boolean hitSomething()
	{
		return hit_something;
	}
	
	public void updatePosition()
	{
		int newx = position_x;
		int newy = position_y;
		switch(direction)
		{
			case Constants.UP:
				newy = newy-1;
				break;
			case Constants.DOWN:
				newy = newy+1;
				break;
			case Constants.LEFT:
				newx = newx-1;
				break;
			case Constants.RIGHT:
				newx = newx+1;
				break;
		}
		
		if (newx < 0 || newx >= Constants.WORLD_WIDTH || w.isWall(newx,newy))
		{
			this.hit_something = true;
		}
		else if (w.isCrystal(newx, newy))
		{
			this.hit_something = true;
			w.destroyCrystal(newx,newy);
		}
		else if(w.isGhost(newx, newy))
		{
			this.hit_something = true;
			w.hitGhost(newx, newy);
		}
		else
		{
			if (w.isPacman(newx,newy))
			{
				this.hit_something = true;
				w.hitPacman();
			}
			else
			{
				position_x = newx;
				position_y = newy;
			}
		}
		
	}
	
	public void update()
	{
		updatePosition();
	}
    
    public void paint(Graphics g) {
			g.drawImage(gim.getShotSprite(), Util.convertPosition(position_x), Util.convertPosition(position_y), null);
    }   
    
}
