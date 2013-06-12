package pacmanpp;

import java.awt.Image;
import javax.swing.JComponent;

/**
 * MovableEntity is to be extended by any and all movable
 * objects in the Pacman++'s game.
 * This means this class will hold the required basic
 * methods to be implemented in those movable classes
 * in order for the game to proceed without issues.
 */
public abstract class MovableEntity extends JComponent
{
	// defines the direction which the entity is facing
	protected int current_direction;
	// represents a desired change in direction when possible
	protected int desired_direction;
	
	// defines the position where the entity is
	protected int position_x;
	protected int position_y;
	
	// the following ints and images are used for animation purpose
	protected int sprite_index;
	protected int animation_direction;
	
	protected Image [][] sprites;
	
	// moves the entity in the direction it is facing
	protected void move()
	{
		switch(current_direction)
		{
			case Constants.UP:
				position_y--;
				break;
			case Constants.DOWN:
				position_y++;
				break;
			case Constants.LEFT:
				position_x--;
				break;
			case Constants.RIGHT:
				position_x++;
				break;
		}
		if (position_x == -1)
			position_x += Constants.WORLD_WIDTH;
		else if (position_x == Constants.WORLD_WIDTH)
			position_x -= Constants.WORLD_WIDTH;
	}
	
	// position conversion - from map view to world view
	protected int convertPosition(int index)
	{
		int retValue = (index + 1) * Constants.GRID_UNIT;
		return retValue;
	}
	
	// checks if a defined direction is free to move or not
		// must be implemented in below classes as it may
		// have different behaviours for each of them
	abstract boolean checkDirection(int checkDir);
	
}
