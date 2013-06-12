package pacmanpp;

import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * GameImages purpose is to hold the various game images in one single point,
 * working as a Manager of sorts.
 */
public class GameImages
{
	private static GameImages instance = null;
	
	// movable entities
	private Image [][] pacmanSprites;
	private Image [][] ghostSprites;
	
	// shot
	private Image shotSprite;
	
	// world
	private Image world;
	
	// info options
	private Image [] optionSprites;
	
	private Font scorefont = new Font(Font.MONOSPACED, Font.BOLD, 26);
	
	private GameImages()
	{
		try
		{
			
			// load the pacman sprites
			pacmanSprites = new Image[5][3];
			
			//UP
			pacmanSprites[Constants.UP][0] = ImageIO.read(new File("resources/imgs/pacman/pacman_closed.png"));
			pacmanSprites[Constants.UP][1] = ImageIO.read(new File("resources/imgs/pacman/pacman_up_0.png"));
			pacmanSprites[Constants.UP][2] = ImageIO.read(new File("resources/imgs/pacman/pacman_up_1.png"));
			//DOWN
			pacmanSprites[Constants.DOWN][0] = ImageIO.read(new File("resources/imgs/pacman/pacman_closed.png"));
			pacmanSprites[Constants.DOWN][1] = ImageIO.read(new File("resources/imgs/pacman/pacman_down_0.png"));
			pacmanSprites[Constants.DOWN][2] = ImageIO.read(new File("resources/imgs/pacman/pacman_down_1.png"));
			//LEFT
			pacmanSprites[Constants.LEFT][0] = ImageIO.read(new File("resources/imgs/pacman/pacman_closed.png"));
			pacmanSprites[Constants.LEFT][1] = ImageIO.read(new File("resources/imgs/pacman/pacman_left_0.png"));
			pacmanSprites[Constants.LEFT][2] = ImageIO.read(new File("resources/imgs/pacman/pacman_left_1.png"));
			//RIGHT
			pacmanSprites[Constants.RIGHT][0] = ImageIO.read(new File("resources/imgs/pacman/pacman_closed.png"));
			pacmanSprites[Constants.RIGHT][1] = ImageIO.read(new File("resources/imgs/pacman/pacman_right_0.png"));
			pacmanSprites[Constants.RIGHT][2] = ImageIO.read(new File("resources/imgs/pacman/pacman_right_1.png"));
			
			// load the ghost sprites
			ghostSprites = new Image[7][4];
			ghostSprites[Constants.SPAWN][0] = ImageIO.read(new File("resources/imgs/ghost/split/ghost_split_0.png"));
			ghostSprites[Constants.SPAWN][1] = ImageIO.read(new File("resources/imgs/ghost/split/ghost_split_1.png"));
			ghostSprites[Constants.SPAWN][2] = ImageIO.read(new File("resources/imgs/ghost/split/ghost_split_2.png"));
			//UP
			ghostSprites[Constants.NORMAL_UP][0] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_up_0.png"));
			ghostSprites[Constants.NORMAL_UP][1] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_up_1.png"));
			//DOWN
			ghostSprites[Constants.NORMAL_DOWN][0] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_down_0.png"));
			ghostSprites[Constants.NORMAL_DOWN][1] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_down_1.png"));
			//LEFT
			ghostSprites[Constants.NORMAL_LEFT][0] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_left_0.png"));
			ghostSprites[Constants.NORMAL_LEFT][1] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_left_1.png"));
			//RIGHT
			ghostSprites[Constants.NORMAL_RIGHT][0] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_right_0.png"));
			ghostSprites[Constants.NORMAL_RIGHT][1] = ImageIO.read(new File("resources/imgs/ghost/normal/ghost_move_right_1.png"));
			
			
			
			ghostSprites[Constants.BLUE][0] = ImageIO.read(new File("resources/imgs/ghost/blue/ghost_blue_0.png"));
			ghostSprites[Constants.BLUE][1] = ImageIO.read(new File("resources/imgs/ghost/blue/ghost_blue_1.png"));
			ghostSprites[Constants.BLUE][2] = ImageIO.read(new File("resources/imgs/ghost/blue/ghost_blue_2.png"));
			ghostSprites[Constants.BLUE][3] = ImageIO.read(new File("resources/imgs/ghost/blue/ghost_blue_3.png"));
			

			ghostSprites[Constants.JOIN][0] = ImageIO.read(new File("resources/imgs/ghost/join/ghost_join_0.png"));
			ghostSprites[Constants.JOIN][1] = ImageIO.read(new File("resources/imgs/ghost/join/ghost_join_1.png"));
			ghostSprites[Constants.JOIN][2] = ImageIO.read(new File("resources/imgs/ghost/join/ghost_join_2.png"));			
			
			// load the world sprite
			world = ImageIO.read(new File("resources/imgs/map/world.png"));
			
			// load the option sprites
			optionSprites = new Image [4];
			optionSprites[Constants.RESET_GAME] = ImageIO.read(new File("resources/imgs/system/newgame.png"));
			optionSprites[Constants.MUTE] = ImageIO.read(new File("resources/imgs/system/sound_on.png"));
			optionSprites[Constants.UNMUTE] = ImageIO.read(new File("resources/imgs/system/sound_off.png"));
			optionSprites[Constants.EXIT] = ImageIO.read(new File("resources/imgs/system/exit.png"));
			
			// load the shot sprite
			shotSprite = ImageIO.read(new File("resources/imgs/general/shot.png"));
			
		}
		catch (Exception ex)
		{
			Logger.getLogger(GameImages.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
	public static GameImages getInstance()
	{
		if (instance == null)
			instance = new GameImages();
		return instance;
	}
	
	protected Image[][] getPacmanSprite ()
	{
		return pacmanSprites;
	}
	
	protected Image[][] getGhostSprites ()
	{
		return ghostSprites;
	}
	
	protected Image getShotSprite() {
		return shotSprite;
	}
	
	protected Image getWorldSprite ()
	{
		return world;
	}
	
	protected Image [] getOptionSprites ()
	{
		return optionSprites;
	}
	
	protected Font getScoreFont ()
	{
		return scorefont;
	}
}
