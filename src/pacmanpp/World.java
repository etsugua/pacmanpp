package pacmanpp;

import java.awt.Dimension;
import java.awt.Graphics;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * World is the class that holds the representation of the environment.
 * For the purposed effect, this class will have a memory map
 * of the positions of the hold and will be responsible by
 * correctly mapping all objects in the scene.
 */
public class World extends JPanel
{
	private static final Dimension PREF_SIZE = new Dimension(600, 680);
	
	// nomnoms represent the points that pacman eats
	private boolean [][] nomnoms;
	// worldMap represents the entities on the map
	private int [][] worldMap;

	// score is defined by the number of nomnoms eaten
	private int score;
	// nomnomsLeft is a preventive counter
	private int nomnomsLeft;
	
	// instance of the World to be used by the other classes
	private static World instance = null;
	
	// instances of the sound and image managers
	private GameImages gim = null;
	private GameSounds gsm = null;
	
	// instance of player
	private Player p;
	
	// instances of ghosts and their threads
	private ArrayList<Ghost> ghosts;
	private ArrayList<Thread> ghost_ai;
	
	private boolean ghost_ticker;
	
	public static World getInstance()
	{
		if (instance == null)
			instance = new World();
		return instance;
	}
	
	protected World() 
	{
		
		this.setSize(PREF_SIZE);
		
		gim = GameImages.getInstance();
		gsm = GameSounds.getInstance();
		
		p = new Player();
		this.add(p);

		ghosts = new ArrayList<Ghost>();
		ghost_ai = new ArrayList<Thread>();
		
		this.loadmap();
		
		this.ghost_ticker = true;
	}
	
	private void loadmap ()
	{
		try
		{	
			nomnoms = new boolean[Constants.WORLD_HEIGHT][Constants.WORLD_WIDTH];
			worldMap = new int[Constants.WORLD_HEIGHT][Constants.WORLD_WIDTH];
			Scanner in = new Scanner(new FileReader("resources/maps/map.mpp"));
			
			int j = 0;
			String line = new String();
			while (in.hasNext())
			{
				line = in.next();
				int type;
				for(int i = 0; i < Constants.WORLD_WIDTH; i++)
				{
					type = Integer.parseInt(String.valueOf(line.charAt(i)));
					worldMap[j][i] = type;
					if (type == Constants.FLOOR)
					{
						nomnoms[j][i] = true;
						nomnomsLeft++;
					}
					else
					{
						nomnoms[j][i] = false;
						if (type == Constants.PACMAN)
							p.setPosition(i,j);
						else if (type == Constants.GHOST)
						{
							// create the ghost
							Ghost g = new Ghost();
							g.setPosition(i, j);
							ghosts.add(g);
							this.add(g);
							// create the thread
							Ghost_AI gai = new Ghost_AI(g);
							Thread t_gai = new Thread(gai);
							ghost_ai.add(t_gai);
							t_gai.start();
						}
							
					}
				}
				j++;
				if (j == Constants.WORLD_HEIGHT)
					break;
			}
			in.close();
		}
		catch (Exception ex)
		{
			Util.simpleTrace(ex.getMessage());
		}
	}
	
	public void spawnGhost(int x, int y, int energy)
	{
		// create the ghost
		Ghost g = new Ghost();
		g.setPosition(x, y);
		g.setEnergy(energy);
		ghosts.add(g);
		this.add(g);
		// create the thread
		Ghost_AI gai = new Ghost_AI(g);
		Thread t_gai = new Thread(gai);
		ghost_ai.add(t_gai);
		t_gai.start();
	}
	
	// safeguard mesure - for pack call in MainApp.java
	@Override
	public Dimension getPreferredSize()
	{
		return PREF_SIZE;
	}
	
	/*
	 * required methods for collision confirmation
	 */
	public boolean isWall(int x, int y)
	{
		return (worldMap[y][x] == Constants.WALL);
	}
	
	public boolean isGhost(int x, int y)
	{
		return (worldMap[y][x] == Constants.GHOST);
	}
	
	public boolean isPacman(int x, int y)
	{
		return (worldMap[y][x] == Constants.PACMAN);
	}
        public boolean isCrystal(int x, int y)
	{
		return (worldMap[y][x] == Constants.NOMNOM_CRYSTAL);
	}
	
	public int see_pacman(int x, int y)
	{
		
		// check up
		for (int i = 1; y-i >= 0; i++ )
		{
			if (this.isWall(x, y-i) || this.isGhost(x, y-i) || this.isCrystal(x, y-i))
				break;
			else if (this.isPacman(x, y-i))
				return Constants.UP;
		}
			
		// check down
		for (int i = 1; y+i < Constants.WORLD_HEIGHT; i++ )
		{
			if (this.isWall(x, y+i) || this.isGhost(x, y+i) || this.isCrystal(x, y+i))
				break;
			else if (this.isPacman(x, y+i))
				return Constants.DOWN;
		}
		
		// check left
		for (int i = 1; x-i >= 0; i++ )
		{
			if (this.isWall(x-i, y) || this.isGhost(x-i, y)  || this.isCrystal(x-i, y))
				break;
			else if (this.isPacman(x-i, y))
				return Constants.LEFT;
		}
			
		// check right
		for (int i = 1; x+i < Constants.WORLD_WIDTH; i++ )
		{
			if (this.isWall(x+i, y) || this.isGhost(x+i, y) || this.isCrystal(x+i, y))
				break;
			else if (this.isPacman(x+i, y))
				return Constants.RIGHT;
		}
		
		return 0;
	}
        
        public int see_crystal(int x, int y)
	{
		
		// check up
		for (int i = 1; y-i >= 0; i++ )
		{
			if (this.isWall(x, y-i) || this.isGhost(x, y-i) || this.isPacman(x, y-i))
				break;
			else if (this.isCrystal(x, y-i))
				return Constants.UP;
		}
			
		// check down
		for (int i = 1; y+i < Constants.WORLD_HEIGHT; i++ )
		{
			if (this.isWall(x, y+i) || this.isGhost(x, y+i) || this.isPacman(x, y+i))
				break;
			else if (this.isCrystal(x, y+i))
				return Constants.DOWN;
		}
		
		// check left
		for (int i = 1; x-i >= 0; i++ )
		{
			if (this.isWall(x-i, y) || this.isGhost(x-i, y) || this.isPacman(x-i, y))
				break;
			else if (this.isCrystal(x-i, y))
				return Constants.LEFT;
		}
			
		// check right
		for (int i = 1; x+i < Constants.WORLD_WIDTH; i++ )
		{
			if (this.isWall(x+i, y) || this.isGhost(x+i, y) || this.isPacman(x+i, y))
				break;
			else if (this.isCrystal(x+i, y))
				return Constants.RIGHT;
		}
		
		return 0;
	}
	
	/*
	 * 
	 * input treatment methods
	 * 
	 */
	
	public void updatePlayerDirection(int new_direction)
	{
		p.setDesiredDirection(new_direction);
	}
	
	
	/*
	 * 
	 * update game state methods
	 * 
	 */
	
	// updates important values like positions and deals with collision events
	public void update()
	{
		// player update
		p.update();
		
		// ghost update
		if (ghost_ticker)
		{
			for(Ghost ghost : ghosts)
			{
				switch (ghost.getGhostState())
				{
					case Constants.SPAWN:
					case Constants.JOIN:
						ghost.update_sprite();
						break;
					default:
						ghost.setMustThink(true);
				}
			}
		}
		ghost_ticker = !ghost_ticker;
	}
	
	// updates the pacman position in the map
	public void update_pacman_position(int newX, int newY, int prevX, int prevY)
	{
		
		
		if (worldMap[newY][newX] == Constants.GHOST || worldMap[newY][newX] == Constants.SHOT)
		{
			Util.simpleTrace("Pacman is DEAD");
		}
		else
		{	
			worldMap[newY][newX] = Constants.PACMAN;
			worldMap[prevY][prevX] = Constants.FLOOR;

			if (nomnoms[newY][newX] == true)
			{
				nomnoms[newY][newX] = false;
				this.score += 5;
				Util.simpleTrace("Current Score = " + score);
			}
		}
	}
	
	// updates a ghost position in the map
	public int updateGhostPosition(int newX, int newY, int prevX, int prevY, int prevType)
	{
		int retVal = worldMap[newY][newX];
		worldMap[newY][newX] = Constants.GHOST;
		worldMap[prevY][prevX] = prevType;
		return retVal;
	}
	
	// calls the draws to every component in this frame
	@Override
	public void paint(Graphics g)
	{
		// paint the world
		g.drawImage(gim.getWorldSprite(), 0, 0, null);
		// paint the nomnoms
		for (int i = 0; i < Constants.WORLD_WIDTH; i++)
			for (int j = 0; j < Constants.WORLD_HEIGHT; j++)
			{
				if (nomnoms[j][i])
					g.drawImage(gim.getNomNomSprites()[0], Util.convertPosition(i), Util.convertPosition(j), instance);
			}
		//Util.simpleTrace("Paint NomNoms");
		
		// paint the pacman
		p.paint(g);
		
		// paint the ghosts
		for(Ghost ghost : ghosts)
		{
			ghost.paint(g);
		}
		
		// paint the shots?
		//Util.simpleTrace("Paint Shots?");
	}

	void destroyCrystal(int newx, int newy) {
		Util.simpleTrace("TO DO AUGUSTE");
	}

	void incGhostEnergy(int newx, int newy) {
		Util.simpleTrace("TO DO AUGUSTE");
	}

	void hitPacman() {
		Util.simpleTrace("TO DO AUGUSTE");
	}

}
