package pacmanpp;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.omg.PortableInterceptor.DISCARDING;

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

				//if (state < Constants.BLUE)
				if (ghost.getEnergy() > 0)
				{
					// if see pacman
					int direction = ghost.s_see_pacman();
					if (direction != 0)
					{
						Util.simpleTrace("Saw Pacman");
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
						// else if see crystal
						direction = ghost.s_see_crystal();
						if (direction != 0)
						{
							Util.simpleTrace("Saw Crystal");
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
							// else if see ghost
							Ghost g = ghost.s_see_ghost();

							if (g != null)
							{
								
								Util.simpleTrace("Saw Ghost");
								
								if (g.getEnergy() == 0)
								//if (g.getGhostState() == Constants.BLUE)
								{
									
									Util.simpleTrace("Saw Blue Ghost");
									
									int distance = ghost.distanceTo(g.getPosition());
									
									if (distance > 1)
									{
										Util.simpleTrace("dist > 1");
										ghost.setDirection(g.getPosition());
										ghost.update_position();
										ghost.update_sprite();
									}
									else if (distance == 1)
									{
										Util.simpleTrace("dist = 1");
										ghost.setDirection(g.getPosition());
										ghost.isColliding(g);
										ghost.update_position();
										ghost.update_sprite();
									}
								}
								else
								{
									Util.simpleTrace("Normal Ghost");
									
									if (ghost.s_can_shoot_ghost())
									{
										ghost.setDirection(g.getPosition());
										ghost.e_shot();
										ghost.update_sprite();
									}
									else
									{
										ghost.update_direction();
										ghost.update_position();
										ghost.update_sprite();
									}
								}
							}
							else
							{
								Util.simpleTrace("NONE");
								// else normal behavior
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
					Ghost g = ghost.s_see_ghost();
					if (g != null)
					{
						if (g.getEnergy() > 0)
						//if(g.getGhostState() < Constants.BLUE)
						{
							Util.simpleTrace("Blue Ghost saw Normal Ghost");
							if (ghost.distanceTo(g.getPosition()) > 1)
							{
								Util.simpleTrace(">	the normal ghost is at > 1");
								ghost.setDirection(g.getPosition());
								ghost.update_position();
								ghost.update_sprite();
							}
							else
							{
								Util.simpleTrace(">	the normal ghost is = 1");
								ghost.update_sprite();
							}
						}
						else
						{
							Util.simpleTrace("Blue Ghost saw Blue Ghost");
							ghost.update_direction();
							ghost.update_position();
							ghost.update_sprite();
						}
					}
					else
					{
						Util.simpleTrace("NONE - 2");
						ghost.update_direction();
						ghost.update_position();
						ghost.update_sprite();
					}
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
					return;
				}
			}
		}
	}
}

