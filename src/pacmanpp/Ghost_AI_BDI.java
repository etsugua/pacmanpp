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
public class Ghost_AI_BDI implements Runnable
{
	/*
     * Variables
     */
    Ghost ghost;
	
	public Ghost_AI_BDI(Ghost g)
	{
		ghost = g;
	}
	
	
	@Override
    public void run()
	{
		
	}
}

