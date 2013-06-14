package pacmanpp;

import java.awt.Graphics;
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
	
	// ghost timers
	protected int shot_timer;
	protected int split_timer;
	
	// ghost coliding
	protected Ghost collideGhost;
	
	
	/******************************************
	 * 
	 * Initialization Methods
	 * 
	 ******************************************/
	public Ghost()
	{
		
		collideGhost = null;
		
		must_think = false;
	
		sprite_index = 0;
		
		this.current_state = Constants.SPAWN;
		
		Util.simpleTrace("Randomizing Energy Here");
		if (Util.random(2) == 0)
		{
			Util.simpleTrace("	is max energy");
			this.current_energy = Constants.MAX_ENERGY;
		}
		else
		{
			Util.simpleTrace("	is 0 energy");
			this.current_energy = 0;
		}
		
		// initialization of the movement direction
		this.current_direction = 0;
		this.desired_direction = 0;
		
		// initialization of the sprites
		this.initSprites();

		this.setVisible(true);
		
		Util.simpleTrace("Floor -> SpecFloor");
		this.tile_type = Constants.FLOOR;
		//this.tile_type = Constants.SPEC_FLOOR;
		
		shot_timer = 0;
		split_timer = 0;
	}
	
	public void setTileType (int type)
	{
		this.tile_type = type;
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
	
	public int [] getPosition()
	{
		int [] ret = { position_x, position_y };
		return ret;
	}
	
	public void setDirection(int direction)
	{
		this.current_direction = direction;
	}
	
	public void setDirection(int[] position)
	{
		if (this.position_x > position[0])
			this.current_direction = Constants.LEFT;
		else if (this.position_x < position[0])
			this.current_direction = Constants.RIGHT;
		
		if (this.position_y > position[1])
			this.current_direction = Constants.UP;
		else if (this.position_y < position[1])
			this.current_direction = Constants.DOWN;
	}
	
	public void setState(int newState)
	{
		Util.simpleTrace("new state");
		this.current_state = newState;
	}
	
	public void setEnergy(int energy)
	{
		this.current_energy = energy;
	}
	
	public void getShot ()
	{
		this.current_energy = this.current_energy + Constants.SHOT_ENERGY_GAIN;
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
	
	public int randomize_direction()
	{
		int decide_direction;
		for(;;)
		{
			decide_direction = 1+Util.random(4);
			if (checkDirection(decide_direction))
				return decide_direction;
		}
	}
	
	public int getEnergy()
	{
		return this.current_energy;
	}
	
	public int getGhostState()
	{
		return this.current_state;
	}
	
	public int getTileType()
	{
		return this.tile_type;
	}
	
	public void isColliding(Ghost g)
	{
		this.collideGhost = g;
	}
	
	public int distanceTo(int[] position)
	{
		int distance = 0;
		
		if (this.position_x > position[0])
			distance += (this.position_x - position[0]);
		else
			distance += (position[0] - this.position_x);
		
		if (this.position_y > position[1])
			distance += (this.position_y - position[1]);
		else
			distance += (position[1] - this.position_y);
		
		return distance;
		
	}
	
	/******************************************
	 * 
	 * Actuator Methods
	 * 
	 ******************************************/
	
	public void e_split()
	{
		this.current_energy = this.current_energy - Constants.SPLIT_ENERGY_COST;
		World.getInstance().spawnGhost(this.position_x, this.position_y, this.tile_type);
		this.split_timer = Constants.SPLIT_TIMER;
	}
	
	public void e_shot()
	{
		World.getInstance().shoot(this.position_x, this.position_y, this.current_direction);
		this.shot_timer = Constants.SHOT_TIMER;
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
	
	public Ghost s_see_ghost ()
	{
		return World.getInstance().see_ghost(this.position_x, this.position_y);
	}
	
	public boolean s_can_split()
	{
		if (this.current_energy >= Constants.SPLIT_ENERGY && this.split_timer == 0
				&& this.checkDirection(this.current_direction))
			return true;
		return false;
	}
	
	public boolean s_can_shoot_pacman()
	{
		if (this.current_energy >= Constants.SHOOT_PACMAN_ENERGY && this.shot_timer == 0)
			return true;
		return false;
	}
	
	public boolean s_can_shoot_ghost()
	{
		if (this.current_energy >= Constants.SHOOT_GHOST_ENERGY && this.shot_timer == 0)
			return true;
		return false;
	}
	
	public boolean s_can_shoot_crystal()
	{
		if (this.current_energy >= Constants.SHOOT_PACMAN_ENERGY && this.shot_timer == 0)
			return true;
		return false;
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
				if (this.current_energy == 0)
				{
					if (this.sprite_index == 3)
						this.sprite_index = 0;
					else
						this.sprite_index++;
					break;
				}
				else
				{
					Util.simpleTrace("Turn NORMAL!");
					this.current_state = this.current_direction;
					this.sprite_index = 0;
					break;
				}
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
				if (this.current_energy == 0)
				{
					Util.simpleTrace("turn blue");
					this.current_state = Constants.BLUE;
					this.sprite_index = 0;
					break;
				}
				else
				{
					this.sprite_index = (this.sprite_index+1)%2;
					break;
				}
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
		
		if (newDirection.size() > 0)
			this.current_direction = newDirection.get(Util.random(newDirection.size()));
		else this.current_direction = 0;
		
		if (this.current_state < Constants.BLUE && this.current_direction != this.current_state)
		{
			this.current_state = this.current_direction;
		}
		
	}
	
	public void update_position()
	{
		
		if (tile_type == Constants.SPEC_FLOOR)
			this.current_energy += Constants.BASE_RECHARGE_ENERGY;
		if (this.current_energy > Constants.MAX_ENERGY)
			this.current_energy = Constants.MAX_ENERGY;
		
		int newx = position_x;
		int newy = position_y;
		
		if (collideGhost != null)
		{
			this.position_x = collideGhost.getPosition()[0];
			this.position_y = collideGhost.getPosition()[1];
			
			World.getInstance().killGhost(collideGhost);
			World.getInstance().updateGhostPosition(newx, newy, position_x, position_y, tile_type);
			
			tile_type = collideGhost.getTileType();
			
			collideGhost = null;
			
			this.current_state = Constants.JOIN;
			
			return;
		}
		
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
			
		if (this.current_direction != 0 && this.current_energy > 0)
			this.current_energy--;
	}
	
	public void updateTimer()
	{
		if (this.shot_timer > 0)
			this.shot_timer--;
		if (this.split_timer > 0)
			this.split_timer--;
	}
	
	@Override
	public void paint(Graphics g)
	{
		g.drawImage(this.sprites[this.current_state][this.sprite_index], Util.convertPosition(position_x), Util.convertPosition(position_y), null);
	}
}
