package pacmanpp;

import java.awt.Graphics;
import java.awt.Image;

/**
 * Player refers to the an object of the movable entity player.
 * An instance of this class will hold the representation of the
 * player character, including variables, methods and other
 * related info.
 */
public class Player extends MovableEntity
{	
	// represents the number of lives left for pacman
	protected int numLives;
	
	/******************************************
	 * 
	 * Initialization Methods
	 * 
	 ******************************************/
	
	
	public Player()
	{
	
		sprite_index = 0;
		animation_direction = 1;
		
		// initialization of the movement direction
		this.current_direction = Constants.RIGHT;
		this.desired_direction = this.current_direction;
		
		// initialization of the sprites
		this.initSprites();
		
		// initialization of the number of lives
		this.numLives = 3;
		
		this.setVisible(true);
	}
	
	private void initSprites()
	{
		sprites = GameImages.getInstance().getPacmanSprite();
	}
	
	/******************************************
	 * 
	 * Generic Methods
	 * 
	 ******************************************/
	
	// position setup
	public void setPosition(int x, int y)
	{
		this.position_x = x;
		this.position_y = y;
	}
	
	public int[] getPosition()
	{
		int [] retValue = {this.position_x, this.position_y};
		return retValue;
	}
	
	
	// direction updates
	public void setDesiredDirection(int new_direction)
	{
		this.desired_direction = new_direction;
	}
	
	@Override
	protected boolean checkDirection(int checkDir)
	{
		int check_x = position_x;
		int check_y = position_y;
		
		switch(checkDir)
		{
			case Constants.UP:
				check_y--;
				break;
			case Constants.DOWN:
				check_y++;
				break;
			case Constants.LEFT:
				check_x--;
				break;
			case Constants.RIGHT:
				check_x++;
				break;
		}
		
		if (check_x == -1)
			check_x += Constants.WORLD_WIDTH;
		else if (check_x == Constants.WORLD_WIDTH)
			check_x -= Constants.WORLD_WIDTH;
		
		return !World.getInstance().isWall(check_x, check_y);
	}
	
	
	public void update()
	{
		// can and wants to change direction?
		if (desired_direction != current_direction && checkDirection(desired_direction))
		{
			this.resetDirection(desired_direction);
			
			int[] prevPosition = this.getPosition();
			
			this.move();
			
			World.getInstance().update_pacman_position(position_x, position_y, prevPosition[0], prevPosition[1]);
		}
		// else, can continue to move forward?
		else if (checkDirection(current_direction))
		{
			this.updateSprite();
			
			int[] prevPosition = this.getPosition();
			
			this.move();
			
			World.getInstance().update_pacman_position(position_x, position_y, prevPosition[0], prevPosition[1]);
		}
		// else, hit a wall
	}
	
	private void resetDirection(int new_direction)
	{
		this.current_direction = this.desired_direction;
		
		this.sprite_index = 1;
		this.animation_direction = 1;
	}
	
	private void updateSprite()
	{
		if (this.sprite_index == 2)
			this.animation_direction = -1;
		else if (this.sprite_index == 0)
			this.animation_direction = 1;
		
		this.sprite_index += this.animation_direction;
	}
	
	public void paint(Graphics g)
	{
		g.drawImage(this.sprites[this.current_direction][this.sprite_index], convertPosition(position_x), convertPosition(position_y), null);
	}
}
