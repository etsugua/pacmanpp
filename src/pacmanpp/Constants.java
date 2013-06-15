package pacmanpp;

/**
 * Constants.java holds the collection of global variables
 * required for the correct functionality of Pacman++.
 * These are to be used by every class that needs them.
 */
public class Constants
{
	
	// Type of Agent to use
	protected static final int REACTIVE = 0;
	protected static final int DELIBERATIVE = 1;
	protected static final int HYBRID = 2;
	// Current agent type
	protected static final int ACTIVE = REACTIVE;
	
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
	protected static final int MAX_ENERGY = 100;
	protected static final int BASE_RECHARGE_ENERGY = 20;
	protected static final int SPLIT_INIT_ENERGY = 60;
	protected static final int SPLIT_ENERGY = 50;
	protected static final int SPLIT_ENERGY_COST = 20;
	protected static final int SHOT_ENERGY = 5;
	protected static final int SHOT_ENERGY_GAIN = 10;
	protected static final int SHOOT_PACMAN_ENERGY = 5; //necessary energy to shoot
	protected static final int SHOOT_GHOST_ENERGY = 20; //necessary energy to shoot
	protected static final int MOVEMENT_ENERGY = 1; //necessary energy to shoot
	
	// ghost timers
	protected static final int SHOT_TIMER = 6;
	protected static final int SPLIT_TIMER = 20;
	
	// message types
	protected static final int MSG_PACMAN = 0;
	protected static final int MSG_BLUE = 1;
	protected static final int MSG_TIMER = 10;
	// message radius
	protected static final int MSG_NOTIFY_RADIUS = 14;
	
	
	// BDI Deliberation Keys
	protected static final int BDI_SEE_PACMAN = 0;
	protected static final int BDI_SEE_GHOST = 1;
	protected static final int BDI_SEE_BLUE = 2;
	protected static final int BDI_SEE_CRYSTAL = 3;
	protected static final int BDI_PACMAN_CLOSE = 4;
	protected static final int BDI_BLUE_CLOSE = 5;
	// BDI Deliberation Second Key
	protected static final int BDI_ENERGY_HIGH = 0;
	protected static final int BDI_ENERGY_NORMAL = 1;
	protected static final int BDI_ENERGY_LOW = 2;
	// BDI Energy Level Configuration
	protected static final int ENERGY_HIGH = 60;
	protected static final int ENERGY_NORMAL = 15;
	protected static final int ENERGY_LOW = 1;
	// BDI Decision Values
	protected static final int BDI_SPLIT = 0;
	protected static final int BDI_JOIN = 1;
	protected static final int BDI_SHOOT_PACMAN = 2;
	protected static final int BDI_SHOOT_GHOST = 3;
	protected static final int BDI_SHOOT_BLUE = 4;
	protected static final int BDI_SHOOT_CRYSTAL = 5;
	protected static final int BDI_MOVE_PACMAN = 6;
	protected static final int BDI_MOVE_BLUE = 7;
	protected static final int BDI_MOVE = 8;
	
	
	
	
	
	
	
	
}
