package pacmanpp;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Ghost_AI refers to the intelligence component of the movable
 * entity ghost. This class will run on a separate thread
 * to ensure that all ghosts thought process will occur at the
 * same time.
 */
public class Ghost_AI implements Runnable
{
	/*
     * Variables
     */
    Ghost ghost;
	
	public Ghost_AI(Ghost g)
	{
		ghost = g;
	}
	
	
	@Override
    public void run()
	{
		
		while (true)
		{
			if(ghost.getMustThink())
			{
				ghost.updateTimer();
				int state = ghost.getGhostState();

					// this ghost is NOT blue

				if (state < Constants.BLUE)
				{
					int direction = ghost.s_see_pacman();
					if (direction != 0)
					{
						ghost.setDirection(direction);
						ghost.update_sprite();
						if (ghost.s_can_split())
						{
							ghost.e_split();
							ghost.update_position();
						}
						else if (ghost.s_can_shoot_pacman())
						{
							ghost.e_shot();
						}
					}
					else
					{
						direction = ghost.s_see_crystal();
						if (direction != 0)
						{
							ghost.setDirection(direction);
							ghost.update_sprite();
							if (ghost.s_can_shoot_crystal())
							{
								ghost.e_shot();
							}
							else
							{
								ghost.update_position();
							}
						}
						else
						{
							Ghost g = ghost.s_see_ghost();
							
							if (g != null)
							{
								
							}
							
						
					

					// else if see ghost
						// if that ghost is blue
							// if distance > 1
								// move towards ghost
							// else
								// call for join
						// if that ghost is close to blue and this one can shoot
							// shoot once - update sprite (MUST HAVE SHOOT TIMER!)
						// if that ghost is energy good and this one is close to blue
							// wait for him - update sprite

							else
							{
								ghost.update_direction();
								ghost.update_position();
								ghost.update_sprite();
							}
						}
					}
				}

					// this ghost is blue
				else
				{
					// if see pacman
						// move opposite direction - update sprite
					// if see ghost
						// if that ghost is not blue
							// if distance >= 1
								// move towards him - update sprite
				}

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
				}
			}
		}
	}
}

