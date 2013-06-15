package pacmanpp;

import java.awt.Color;
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
	private ArrayList<NuThread> ghost_ai;
	
	// instances of shot
	private ArrayList<Shot> shots;
	
	private boolean ghost_ticker;
	
	private boolean new_game;
	
	private int agent_type;
	
	public static World getInstance()
	{
		if (instance == null)
			instance = new World();
		return instance;
	}
	
	protected World() 
	{
		this.agent_type = Constants.ACTIVE;
		
		this.new_game = false;
		
		this.setSize(PREF_SIZE);
		
		gim = GameImages.getInstance();
		gsm = GameSounds.getInstance();
		
		p = new Player();
		this.add(p);

		ghosts = new ArrayList<Ghost>();
		ghost_ai = new ArrayList<NuThread>();
		shots = new ArrayList<Shot>();
		
		this.loadmap();
		
		this.ghost_ticker = false;
		
		this.score = 0;
	}
	
	private synchronized void loadmap ()
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
							Runnable gai = null;
							switch(this.agent_type)
							{
								case Constants.REACTIVE:
									gai = new Ghost_AI(g);
									break;
								case Constants.DELIBERATIVE:
									gai = new Ghost_AI_BDI(g);
									break;
								case Constants.HYBRID:
									Util.simpleTrace("Hybrid - ToDo");
						//			gai = new Ghost_AI_Hybrid(g);
									break;
							}
							
							NuThread t_gai = new NuThread(gai);
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
	
	public synchronized NuThread spawnGhost(int x, int y, int tileType)
	{
		// create the ghost
		Ghost g = new Ghost();
		g.setPosition(x, y);
		g.setEnergy(Constants.SPLIT_INIT_ENERGY);
		ghosts.add(g);
		this.add(g);
		g.setTileType(tileType);
		// create the thread
		Runnable gai = null;
		switch(this.agent_type)
		{
			case Constants.REACTIVE:
				gai = new Ghost_AI(g);
				break;
			case Constants.DELIBERATIVE:
				gai = new Ghost_AI_BDI(g);
				break;
			case Constants.HYBRID:
				Util.simpleTrace("Hybrid - ToDo");
		//		gai = new Ghost_AI_Hybrid(g);
				break;
		}
							
		NuThread t_gai = new NuThread(gai);
		ghost_ai.add(t_gai);
		t_gai.start();
		
		return t_gai;
	}
	
	public synchronized ArrayList<Ghost> getGhosts()
	{
		return ghosts;
	}
	
	public synchronized ArrayList<NuThread> getGhostAIs()
	{
		return ghost_ai;
	}
	
	public int[] getPacmanPosition()
	{
		return p.getPosition();
	}
	
	public synchronized void shoot(int x, int y, int direction)
	{
		Shot s = new Shot(direction, x, y);
		this.add(s);
		shots.add(s);
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
		if (x >= Constants.WORLD_WIDTH || x < 0)
			return false;
		return (worldMap[y][x] == Constants.WALL);
	}
	
	
	
	public boolean isPacman(int x, int y)
	{
		return (worldMap[y][x] == Constants.PACMAN);
	}
	
	public boolean isCrystal(int x, int y)
	{
		return (worldMap[y][x] == Constants.NOMNOM_CRYSTAL);
	}
	
	public boolean isGhost(int x, int y)
	{
		return (worldMap[y][x] == Constants.GHOST);
	}
	
	public synchronized void hitGhost(int x, int y)
	{
		for (Ghost g : ghosts)
		{
			if (g.getPosition()[0] == x && g.getPosition()[1] == y)
			{
				g.getShot();
			}
		}
	}
	
	public void hitPacman()
	{
		Util.simpleTrace("Pacman is DEAD");
		this.new_game = true;
	}
	
	public synchronized Ghost getGhost(int x, int y) 
	{
		for (Ghost ghost : ghosts)
		{
			if (ghost.getPosition()[0] == x && ghost.getPosition()[1] == y)
				return ghost;
		}
		return null;
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
    
	public synchronized Ghost see_ghost(int x, int y)
	{
		int i = -1;
		for (Ghost ghost : ghosts)
		{
			i++;
			if (ghost.getPosition()[0] == x && ghost.getPosition()[1] == y)
				continue;
			
			if (ghost.getPosition()[0] != x && ghost.getPosition()[1] != y)
				continue;
			
			/*System.out.println("Compare with Ghost #"+i);
			System.out.println("x Compare: ("+ghost.getPosition()[0]+") , ("+x+")");
			System.out.println("y Compare: ("+ghost.getPosition()[1]+") , ("+y+")");*/
			
			if (ghost.getPosition()[0] > x && ghost.getPosition()[1] == y)
			{
				int xmax = ghost.getPosition()[0];
				for (int xi = x+1; xi <= xmax; xi++)
				{
					if(xi == xmax)
						return ghost;
					else
					{
						if (this.isWall(xi, y) || this.isGhost(xi, y))
							break;
					}
				}
			}
			else if (ghost.getPosition()[0] < x && ghost.getPosition()[1] == y)
			{
				for (int xi = ghost.getPosition()[0]+1; xi <= x; xi++)
				{
					if(xi == x)
						return ghost;
					else
					{
						if (this.isWall(xi, y) || this.isGhost(xi, y))
							break;
					}
				}
			}
			else if (ghost.getPosition()[0] == x && ghost.getPosition()[1] < y)
			{
				for (int yi = ghost.getPosition()[1]+1; yi <= y; yi++)
				{
					if(yi == y)
						return ghost;
					else
					{
						if (this.isWall(x, yi) || this.isGhost(x, yi))
							break;
					}
				}
			}
			else if (ghost.getPosition()[0] == x && ghost.getPosition()[1] > y)
			{
				int ymax = ghost.getPosition()[1];
				for (int yi = y+1; yi <= ymax; yi++)
				{
					if(yi == ymax)
						return ghost;
					else
					{
						if (this.isWall(x, yi) || this.isGhost(x, yi))
							break;
					}
				}
			}
		}
		
		return null;
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
	
	public synchronized void killGhost(Ghost g)
	{
		int i = ghosts.indexOf(g);
		Thread t = ghost_ai.get(i);
		
		t.interrupt();
		ghost_ai.remove(t);
		ghosts.remove(g);
		
		this.worldMap[g.getPosition()[1]][g.getPosition()[0]] = Constants.FLOOR;
		
		this.remove(g);
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
	
	public synchronized World newGame()
	{
		if ( this.new_game )
		{
			while (!ghosts.isEmpty())
				killGhost(ghosts.get(0));
			
			this.removeAll();
			instance = new World();
		}
		
		
		return instance;
	}
	
	public void cleanGhosts()
	{
		for (int j = 0; j < Constants.WORLD_HEIGHT; j++)
			for (int i = 0; i < Constants.WORLD_WIDTH; i++)
			{
				if (this.worldMap[j][i] == Constants.GHOST)
				{
					boolean foundGhost = false;
					for (Ghost ghost : ghosts)
					{
						if (ghost.getPosition()[0] == i && ghost.getPosition()[1] == j)
						{
							foundGhost = true;
							break;
						}
					}
					if (!foundGhost)
						this.worldMap[j][i] = Constants.FLOOR;
				}
			}
	}
	
	// updates important values like positions and deals with collision events
	public synchronized void update()
	{
		this.cleanGhosts();
		
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

		int i = 0;
		Shot [] removeShot = new Shot [5];
		for (Shot shot : shots)
		{

			shot.update();

			if (shot.hitSomething())
				if (i <= 4)
					removeShot[i++] = shot;
		}

		if (i > 0)
			for (int j = 0; j < i; j++)
			{
				this.remove(removeShot[j]);
				shots.remove(removeShot[j]);
			}
	}
	
	// updates the pacman position in the map
	public void update_pacman_position(int newX, int newY, int prevX, int prevY)
	{
		
		if (worldMap[newY][newX] == Constants.SHOT)
		{
			Util.simpleTrace("Pacman is DEAD");
			this.new_game = true;
		}		
		else if (worldMap[newY][newX] == Constants.GHOST)
		{
			
			Ghost ghost = getGhost(newX,newY);
			
			if(ghost == null)
				worldMap[newY][newX] = Constants.FLOOR;
			
			else
			{
				if(ghost.getGhostState() == Constants.BLUE) 
				{
					this.score += 100;
					this.killGhost(ghost);
				}
				else 
				{
					Util.simpleTrace("Pacman is DEAD");
					this.new_game = true;
				}
			}
		}
		else
		{
			
			if(worldMap[newY][newX] == Constants.NOMNOM_CRYSTAL)
			{
				this.score += 50;
				this.hurtGhosts();
				//Util.simpleTrace("Current Score = " + score);
			}
			
			worldMap[newY][newX] = Constants.PACMAN;
			worldMap[prevY][prevX] = Constants.FLOOR;

			if (nomnoms[newY][newX] == true)
			{
				nomnoms[newY][newX] = false;
				this.score += 5;
			}
		}
	}
	
	public synchronized void hurtGhosts()
	{
		for (Ghost ghost : ghosts)
		{
			int newEnergy = ghost.getEnergy()-(Constants.MAX_ENERGY/2);
			if (newEnergy < 0)
				newEnergy = 0;
			ghost.setEnergy(newEnergy);
			ghost.update_sprite();
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
	public synchronized void paint(Graphics g)
	{
		// paint the world
		g.drawImage(gim.getWorldSprite(), 0, 0, null);
		
		// paint the nomnoms
		for (int i = 0; i < Constants.WORLD_WIDTH; i++)
			for (int j = 0; j < Constants.WORLD_HEIGHT; j++)
			{
				if (nomnoms[j][i])
					g.drawImage(gim.getNomNomSprites()[0], Util.convertPosition(i), Util.convertPosition(j), null);
			}
		
		// paint the crystal
		for (int i = 0; i < Constants.WORLD_WIDTH; i++)
			for (int j = 0; j < Constants.WORLD_HEIGHT; j++)
			{
				if (worldMap[j][i] == Constants.NOMNOM_CRYSTAL)
					g.drawImage(gim.getNomNomSprites()[1], Util.convertPosition(i), Util.convertPosition(j), null);
					
			}
		
		// paint the pacman
		p.paint(g);
		
		// paint the ghosts
		for(Ghost ghost : ghosts)
		{
			ghost.paint(g);
		}
		
		// paint the shots?
		for (Shot shot : shots)
		{
			shot.paint(g);
		}
		
		g.setFont(gim.getScoreFont());
		g.setColor(Color.YELLOW);
		g.drawString("SCORE: "+score, Constants.INFO_TOPX * Constants.GRID_UNIT + 6*Constants.GRID_UNIT, Constants.INFO_TOPY * Constants.GRID_UNIT + 3*Constants.GRID_UNIT/2);
	}

	public synchronized void destroyCrystal(int x, int y)
	{
		for (Ghost ghost : ghosts)
		{
			int energy = ghost.getEnergy();
			energy += Constants.MAX_ENERGY/2;
			if (energy > Constants.MAX_ENERGY)
				energy = Constants.MAX_ENERGY;
			
			ghost.setEnergy(energy);
		}
		
		this.worldMap[y][x] = Constants.FLOOR;
	}
	
	public synchronized void printMAP()
	{
		for (int i = 0; i < Constants.WORLD_HEIGHT; i++)
		{
			for (int j = 0; j < Constants.WORLD_WIDTH; j++)
			{
				System.out.print("  " + this.worldMap[i][j]);
			}
			System.out.println();
		}
		
		int i = 0;
		for (Ghost ghost : ghosts)
		{
			System.out.println("Ghost #"+i++ + "Energy = " + ghost.getEnergy() + " Position = "+ghost.getPosition()[0]+","+ghost.getPosition()[1]);
		}
		
		
	}
}
