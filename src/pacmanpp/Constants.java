package pacmanpp;

/**
 * Constants.java holds the collection of global variables
 * required for the correct functionality of Pacman++.
 * These are to be used by every class that needs them.
 */
public class Constants
{
	// size of the grid
	protected static final int GRID_UNIT = 20;
	
	// world view settings
	protected static final int WORLD_TOPX = 1;
	protected static final int WORLD_TOPY = 1;
	protected static final int WORLD_WIDTH = 28;
	protected static final int WORLD_HEIGHT = 29;
	
	// info tab settings
	protected static final int INFO_TOPX = 1;
	protected static final int INFO_TOPY = 31;
	protected static final int INFO_WIDTH = 28;
	protected static final int INFO_HEIGHT = 2;
	
	// info tab options available
	protected static final int RESET_GAME = 0;
	protected static final int MUTE = 1;
	protected static final int UNMUTE = 2;
	protected static final int EXIT = 3;
	
	// overall window settings
	protected static final int SCREEN_WIDTH = 30;
	protected static final int SCREEN_HEIGHT = 34;
	
	// possible tiles
	protected static final int WALL = 0;
	protected static final int FLOOR = 1;
	protected static final int PACMAN = 2;
	protected static final int NOMNOM_CRYSTAL = 3;
	protected static final int GHOST = 4;
	protected static final int GHOST_DOOR = 5;
	protected static final int SPEC_FLOOR = 6;
	protected static final int GHOST_BLUE = 7;
	protected static final int SHOT = 8;
	
	// directions
	protected static final int UP = 1;
	protected static final int DOWN = 2;
	protected static final int LEFT = 3;
    protected static final int RIGHT = 4;
	
	// ghost state
	protected static final int SPAWN = 0;
	protected static final int NORMAL_UP= 1;
	protected static final int NORMAL_DOWN = 2;
	protected static final int NORMAL_LEFT = 3;
	protected static final int NORMAL_RIGHT = 4;
	protected static final int BLUE = 5;
	protected static final int JOIN = 6;
	
	// ghost energy
	protected static final int INIT_ENERGY = 100;
	protected static final int SPLIT_INIT_ENERGY = 40;
	protected static final int SPLIT_ENERGY = 50;
	protected static final int SHOT_ENERGY = 1;
	protected static final int SHOOT_PACMAN_ENERGY = 1; //necessary energy to shoot
	protected static final int SHOOT_GHOST_ENERGY = 10; //necessary energy to shoot
}
