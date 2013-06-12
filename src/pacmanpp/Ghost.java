package pacmanpp;

import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;

/**
 * Ghost refers to the an object of the movable entity ghost.
 * An instance of this object will hold all variables
 * and implements specific needed sensors and effectors.
 * The decision process for Ghost is to be implemented in a
 * specified AI class.
 */
public class Ghost extends MovableEntity
{

	// holds the current state of this instance of ghost
	protected int current_state;
	
	// holds the world tile type - for marking
	protected int tile_type;
	
	// flag used for the threaded update
	protected boolean must_think;
        
    // ghost energy
    protected int current_energy;
	
	/******************************************
	 * 
	 * Initialization Methods
	 * 
	 ******************************************/
	public Ghost()
	{
		
		must_think = false;
	
		sprite_index = 0;
		
		this.current_state = Constants.SPAWN;
                this.current_energy = Constants.START_ENERGY;
		
		// initialization of the movement direction
		this.current_direction = 0;
		this.desired_direction = 0;
		
		// initialization of the sprites
		this.initSprites();

		this.setVisible(true);
		
		this.tile_type = Constants.SPEC_FLOOR;
	}
	
	private void initSprites()
	{
		sprites = GameImages.getInstance().getGhostSprites();
	}
	
	public void setPosition(int x, int y)
	{
		position_x = x;
		position_y = y;
	}
	
	public boolean getMustThink()
	{
		return must_think;
	}
	
	public void setMustThink(boolean b)
	{
		must_think = b;
	}
	
	/******************************************
	 * 
	 * Generic Methods
	 * 
	 ******************************************/
	
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
		
		boolean retVal = !World.getInstance().isWall(check_x, check_y) &&
						 !World.getInstance().isGhost(check_x, check_y);
		
		
		return retVal;
	}
	
	private int randomize_direction()
	{
		int decide_direction = 0;
		for(;;)
		{
			decide_direction = 1+Util.random(4);
			if (checkDirection(decide_direction))
				return decide_direction;
		}
	}
	
	public int getGhostState()
	{
		return this.current_state;
	}
	
	/******************************************
	 * 
	 * Actuator Methods
	 * 
	 ******************************************/
	
	public void e_split()
	{
		// energy = energy - Constants.SPLIT_COST;
		World.getInstance().spawnGhost(this.position_x, this.position_y, Constants.SPLIT_ENERGY);
	}
	
	/******************************************
	 * 
	 * Sensor Methods
	 * 
	 ******************************************/
	public int s_see_pacman()
	{
		return World.getInstance().see_pacman(this.position_x, this.position_y);
	}
        
        public int s_see_crystal ()
	{
		return World.getInstance().see_crystal(this.position_x, this.position_y);
	}
		
	public boolean s_can_split()
	{
		//if (energy >= Constants.SPLIT_ENERGY)
			//return true;
		//return false;
		return true;
	}
	
	/******************************************
	 * 
	 * Update Methods
	 * 
	 ******************************************/
	public void update_sprite()
	{
		switch (this.current_state)
		{
			case Constants.SPAWN:
				if (this.sprite_index == 2)
				{
					this.sprite_index = 0;
					this.current_state = this.randomize_direction();
					this.current_direction = this.current_state;
					this.desired_direction = this.current_state;
				}
				else
					this.sprite_index++;
				break;
			case Constants.BLUE:
				if (this.sprite_index == 3)
					this.sprite_index = 0;
				else
					this.sprite_index++;
				break;
			case Constants.JOIN:
				if (this.sprite_index == 2)
				{
					this.sprite_index = 0;
					this.current_state = this.randomize_direction();
					this.current_direction = this.current_state;
					this.desired_direction = this.current_state;
				}
				else
					this.sprite_index++;
				break;
			// else ghost is moving
			default:
				this.sprite_index = (this.sprite_index+1)%2;
				break;
		}
	}
	
	public void update_direction()
	{
		ArrayList<Integer> newDirection = new ArrayList<Integer>();
		switch (this.current_direction)
		{
			case Constants.UP:
				if (checkDirection(Constants.UP))
					newDirection.add(Constants.UP);
				else if (checkDirection(Constants.DOWN))
					newDirection.add(Constants.DOWN);
				if (checkDirection(Constants.LEFT))
					newDirection.add(Constants.LEFT);
				if (checkDirection(Constants.RIGHT))
					newDirection.add(Constants.RIGHT);
				break;
			case Constants.DOWN:
				if (checkDirection(Constants.DOWN))
					newDirection.add(Constants.DOWN);
				else if (checkDirection(Constants.UP))
					newDirection.add(Constants.UP);
				if (checkDirection(Constants.LEFT))
					newDirection.add(Constants.LEFT);
				if (checkDirection(Constants.RIGHT))
					newDirection.add(Constants.RIGHT);
				break;
			case Constants.LEFT:
				if (checkDirection(Constants.LEFT))
					newDirection.add(Constants.LEFT);
				else if (checkDirection(Constants.RIGHT))
					newDirection.add(Constants.RIGHT);
				if (checkDirection(Constants.UP))
					newDirection.add(Constants.UP);
				if (checkDirection(Constants.DOWN))
					newDirection.add(Constants.DOWN);
				break;
			case Constants.RIGHT:
				if (checkDirection(Constants.RIGHT))
					newDirection.add(Constants.RIGHT);
				else if (checkDirection(Constants.LEFT))
					newDirection.add(Constants.LEFT);
				if (checkDirection(Constants.UP))
					newDirection.add(Constants.UP);
				if (checkDirection(Constants.DOWN))
					newDirection.add(Constants.DOWN);
				break;
		}
		
		this.current_direction = newDirection.get(Util.random(newDirection.size()));
		
		if (this.current_state < Constants.BLUE && this.current_direction != this.current_state)
		{
			this.current_state = this.current_direction;
		}
		
	}
	
	public void update_position()
	{
		int newx = position_x;
		int newy = position_y;
		
		switch (this.current_direction)
        {
            case Constants.UP:
                    newy = newy-1;
                    break;
            case Constants.DOWN:
                    newy = newy+1;
                    break;
            case Constants.LEFT:
                    newx = newx-1;
                    if (newx < 0)
                            newx = Constants.WORLD_WIDTH-1;
                    break;
            case Constants.RIGHT:
                    newx = newx+1;
                    if (newx == Constants.WORLD_WIDTH)
                            newx = 0;
                    break;
        }
		
            tile_type = World.getInstance().updateGhostPosition(newx, newy, position_x, position_y, tile_type);
		
            position_x = newx;
            position_y = newy;
			
			this.current_energy--;
        }
	
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(this.sprites[this.current_state][this.sprite_index], Util.convertPosition(position_x), Util.convertPosition(position_y), null);
	}
}
