/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pacmanpp;

import java.util.ArrayList;

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
		ArrayList<Ghost> ghosts = w.getGhosts();
		ArrayList<NuThread> ghost_ai = w.getGhostAIs();
		
		for(Ghost ghost : ghosts)
		{
			if (ghost.distanceTo(position) <= Constants.MSG_NOTIFY_RADIUS)
			{
				Ghost_AI_BDI gai = (Ghost_AI_BDI)ghost_ai.get(ghosts.indexOf(ghost)).getRunnable();
				switch (msg_type)
				{
					case Constants.MSG_PACMAN:
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
