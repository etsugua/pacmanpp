package pacmanpp;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.DISCARDING;

/**
 * Ghost_AI refers to the intelligence component of the movable
 * entity ghost. This class will run on a separate thread
 * to ensure that all ghosts thought process will occur at the
 * same time.
 */
public class Ghost_AI_BDI implements Runnable
{
	/*
     * Variables
     */
    Ghost ghost;
	
	ArrayList<int[]> path;
	
	// message pairs
		// msg pacman
	boolean knowPacman;
	int[] pacmanPosition;
	int pacmanTicker;
		// msg blue
	boolean knowBlue;
	int[] bluePosition;
	int blueTicker;
	
	int previousState;
	
	
	public Ghost_AI_BDI(Ghost g)
	{
		previousState = g.getGhostState();
		
		knowPacman = false;
		pacmanTicker = 0;
		knowBlue = false;
		blueTicker = 0;
		
		path = new ArrayList<int[]>();
		
		ghost = g;
	}
	
	public void setInitValues(int[] pmPos,int pmTick,int[] bluePos, int blueTick, ArrayList<int[]> parentPath)
	{
		if(pmTick > 0)
		{
			knowPacman = true;
			pacmanPosition = pmPos;
			pacmanTicker = pmTick;
		}
		
		if(blueTick > 0)
		{
			knowBlue = true;
			bluePosition = bluePos;
			blueTicker = blueTick;
		}
		path = new ArrayList<int[]>(parentPath);
	}
	
	public void notifyPacmanPosition(int[] position)
	{
		knowPacman = true;
		pacmanPosition = position;
		pacmanTicker = Constants.MSG_TIMER;
	}
	
	public void notifyBluePosition(int[] position)
	{
		knowBlue = true;
		bluePosition = position;
		blueTicker = Constants.MSG_TIMER;
	}
	
	public void resetPacmanPosition(int[] position)
	{
		knowPacman = false;
		pacmanPosition = position;
		pacmanTicker = 0;
	}
	
	public void resetBluePosition(int[] position)
	{
		knowBlue = false;
		bluePosition = position;
		blueTicker = 0;
	}
	
	public void messageTicker()
	{
		if (blueTicker > 0)
			blueTicker--;
		if (pacmanTicker > 0)
			pacmanTicker--;
	}
	
	public int energyLevel()
	{
		if (ghost.getEnergy() >= Constants.ENERGY_HIGH)
			return Constants.BDI_ENERGY_HIGH;
		else if (ghost.getEnergy() >= Constants.ENERGY_NORMAL)
			return Constants.BDI_ENERGY_NORMAL;
		else if (ghost.getEnergy() >= Constants.ENERGY_LOW)
			return Constants.BDI_ENERGY_LOW;
		else
			return -1;
	}
	
	public int checkEnergyLevel(int energy)
	{
		if (energy >= Constants.ENERGY_HIGH)
			return Constants.BDI_ENERGY_HIGH;
		else if (energy >= Constants.ENERGY_NORMAL)
			return Constants.BDI_ENERGY_NORMAL;
		else if (energy >= Constants.ENERGY_LOW)
			return Constants.BDI_ENERGY_LOW;
		else
			return -1;
	}
	
	@Override
    public void run()
	{
		while(true)
		{
			if (ghost.getMustThink())
			{
				this.messageTicker();
				
				int[] keys = new int [2];
				int[] directions = new int [2];
				
				int key = 0;
				
				int energyLevel = this.energyLevel();
			
					// if ghost not blue
				if (energyLevel >= 0)
				{
					
					// if see pacman
					int direction = ghost.s_see_pacman();
					if (direction != 0)
					{
						CommunicationModule.getInstance().notifyGhosts(Constants.MSG_PACMAN, World.getInstance().getPacmanPosition());
						this.resetPacmanPosition(World.getInstance().getPacmanPosition());
						keys[key] = Constants.BDI_SEE_PACMAN;
						directions[key++] = direction;
					}
					// if see crystal
					direction = ghost.s_see_crystal();
					if (direction != 0)
					{
						keys[key] = Constants.BDI_SEE_CRYSTAL;
						directions[key++] = direction;
					}
					
					if (key < 2)
					{
						// see blue ghost
						Ghost g = ghost.s_see_ghost();
						if (g != null)
						{
							if (g.getGhostState() == Constants.BLUE)
							{
								this.resetBluePosition(g.getPosition());
								keys[key] = Constants.BDI_SEE_BLUE;
								directions[key++] = direction;
							}
						}	
						if (key < 2)
						{
							// pacman close
							if (knowPacman && ghost.isCloseTo(pacmanPosition))
							{
								keys[key] = Constants.BDI_PACMAN_CLOSE;
								directions[key++] = ghost.getDirection(pacmanPosition);
							}
							if (key < 2)
							{
								// blue close
								if (knowBlue && ghost.isCloseTo(bluePosition))
								{
									keys[key] = Constants.BDI_BLUE_CLOSE;
									directions[key++] = ghost.getDirection(bluePosition);
								}
								if (key < 2)
								{
									// see ghost
									if (g != null && checkEnergyLevel(g.getEnergy()) == Constants.BDI_ENERGY_LOW)
									{
										keys[key] = Constants.BDI_SEE_GHOST;
										directions[key++] = ghost.getDirection(g.getPosition());										
									}
								}
							}
						}
						
						if(key == 1)
						{
							keys[1] = keys[0];
							directions[1] = directions[0];
							if (this.execute(keys,directions,this.energyLevel()) == -1)
								this.failsafe();
						}
						else if (key == 0)
						{
							this.failsafe();
						}	
						else
							if (this.execute(keys,directions,this.energyLevel()) == -1)
								this.failsafe();
					
					}
					
					if (ghost.getTileType() == Constants.SPEC_FLOOR)
						path = new ArrayList<int[]>();
					
					
					int pathSize = path.size();
					for (int i = 0; i < pathSize; i++ )
					{
						if (path.get(i)[0] == ghost.getPosition()[0] && path.get(i)[1] == ghost.getPosition()[1])
						{
							path = new ArrayList<int[]>(path.subList(0, i));
							break;
						}
					}
					path.add(ghost.getPosition());
					
				}
				// else ghost is blue
				else
				{
					Util.simpleTrace("GHOST is BLUE");
					
					this.resetBluePosition(null);
					this.resetPacmanPosition(null);
					
					int limit = path.size()-1;
					
					if (limit > 0)
					{
						int direction = ghost.getDirection(path.get(limit-1));
						if (ghost.getPosition()[0] == 0 && path.get(limit-1)[0] == Constants.WORLD_WIDTH-1)
							direction = Constants.LEFT;
						else if (ghost.getPosition()[0] == Constants.WORLD_WIDTH-1 && path.get(limit-1)[0] == 0)
							direction = Constants.RIGHT;
						
						if (ghost.checkDirection(direction))
						{
							ghost.setDirection(direction);
							ghost.update_position();
							path = new ArrayList<int[]>(path.subList(0, limit));
						}
					}
					else if (ghost.tile_type == Constants.SPEC_FLOOR)
						this.failsafe();
					ghost.update_sprite();
				}

				// notify if ghost turned blue
				if (previousState != Constants.BLUE && ghost.getGhostState() == Constants.BLUE)
					CommunicationModule.getInstance().notifyGhosts(Constants.MSG_BLUE, ghost.getPosition());
				// update previous ghost state
				previousState = ghost.getGhostState();
				ghost.setMustThink(false);
			}
			else
			{
				try
				{
					Thread.sleep(75);
				}
				catch (InterruptedException ex)
				{
					Util.simpleTrace(""+ex);
					return;
				}
			}
		}
	}
	
	private void failsafe()
	{
		if (ghost.getTileType() != Constants.SPEC_FLOOR)
			ghost.update_direction();
		else
		{
			if (this.energyLevel() == Constants.BDI_ENERGY_HIGH)
				ghost.setDirection(Constants.UP);
			else
				ghost.setDirection(Constants.DOWN);
		}
		ghost.update_position();
		ghost.update_sprite();
	}

	private int execute(int[] keys, int[] directions, int energyKey)
	{
		
		if (energyKey == 3)
			return -1;
		
		switch(BDIDeliberationTable.getInstance().deliberate(keys[0], keys[1], energyKey))
		{
			case Constants.BDI_SPLIT:
				//actions
				ghost.setDirection(directions[0]);
				if (ghost.s_can_split())
				{
					Ghost_AI_BDI son = (Ghost_AI_BDI)ghost.e_split().getRunnable();
					son.setInitValues(pacmanPosition, pacmanTicker, bluePosition, blueTicker, path);
					ghost.update_position();
					ghost.update_sprite();
					return 0;
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
				
			case Constants.BDI_JOIN:
				//actions
				Ghost g = ghost.s_see_ghost();
				if (g != null)
				{
					//if (g.getEnergy() > 0)
					if (g.getGhostState() == Constants.BLUE)
					{									
						int distance = ghost.distanceTo(g.getPosition());

						if (distance > 1)
						{
							ghost.setDirection(g.getPosition());
							ghost.update_position();
							ghost.update_sprite();
						}
						else if (distance == 1)
						{
							ghost.setDirection(g.getPosition());
							ghost.isColliding(g);
							ghost.update_position();
							ghost.update_sprite();
						}
						return 0;
					}
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
				
			case Constants.BDI_SHOOT_PACMAN:
				//actions
				ghost.setDirection(directions[0]);
				if (ghost.s_can_shoot_pacman())
				{
					ghost.e_shot();
					ghost.update_sprite();
					return 0;
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
				
			case Constants.BDI_SHOOT_GHOST:
				//actions
				if (keys[0] == Constants.BDI_SEE_GHOST)
					ghost.setDirection(directions[0]);
				else
					ghost.setDirection(directions[1]);
				if (ghost.s_can_shoot_ghost())
				{
					ghost.e_shot();
					ghost.update_sprite();
					return 0;
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
				
			case Constants.BDI_SHOOT_BLUE:
				//actions
				if (keys[0] == Constants.BDI_SEE_BLUE)
					ghost.setDirection(directions[0]);
				else
					ghost.setDirection(directions[1]);
				if (ghost.s_can_shoot_ghost())
				{
					ghost.e_shot();
					ghost.update_sprite();
					return 0;
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
				
			case Constants.BDI_SHOOT_CRYSTAL:
				//actions
				if (keys[0] == Constants.BDI_SEE_CRYSTAL)
					ghost.setDirection(directions[0]);
				else
					ghost.setDirection(directions[1]);
				if (ghost.s_can_shoot_crystal())
				{
					ghost.e_shot();
					ghost.update_sprite();
					return 0;
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
				
			case Constants.BDI_MOVE_PACMAN:
				//actions
				if (keys[0] == Constants.BDI_PACMAN_CLOSE)
					ghost.setDirection(directions[0]);
				else
					ghost.setDirection(directions[1]);
				if (ghost.s_can_shoot_pacman())
				{
					ghost.e_shot();
					ghost.update_sprite();
					return 0;
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
				
			case Constants.BDI_MOVE_BLUE:
				//actions
				if (keys[0] == Constants.BDI_BLUE_CLOSE)
					ghost.setDirection(directions[0]);
				else
					ghost.setDirection(directions[1]);
				if (ghost.s_can_shoot_pacman())
				{
					ghost.e_shot();
					ghost.update_sprite();
					return 0;
				}
				else if (energyKey == Constants.BDI_ENERGY_LOW)
					return -1;
				return execute(keys, directions, energyKey+1);
			case Constants.BDI_MOVE:
				//actions
				this.failsafe();
				return 0;
				
			default:
				return -1;
		}
	}
}

