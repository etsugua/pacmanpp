/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanpp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommunicationModule
{
	private static CommunicationModule instance = null;
	
	public static CommunicationModule getInstance()
	{
		if (instance == null)
			instance = new CommunicationModule();
		return instance;
	}
	
	public synchronized void notifyGhosts(int msg_type, int[] position)
	{
		World w = World.getInstance();
		List<Ghost> ghosts = Collections.synchronizedList(w.getGhosts());
		List<NuThread> ghost_ai = Collections.synchronizedList(w.getGhostAIs());
		
		for(Ghost ghost : ghosts)
		{
			if (ghost.distanceTo(position) <= Constants.MSG_NOTIFY_RADIUS)
			{
				Ghost_AI_BDI gai = (Ghost_AI_BDI)ghost_ai.get(ghosts.indexOf(ghost)).getRunnable();
				switch (msg_type)
				{
					case Constants.MSG_PACMAN:
						if (ghost.s_see_pacman() == 0)
							gai.notifyPacmanPosition(position);
						break;
					case Constants.MSG_BLUE:
						gai.notifyBluePosition(position);
						break;
				}
			}
		}
	}
}
