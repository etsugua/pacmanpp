package pacmanpp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 * GameSounds purpose is to hold the various game sounds in one single point,
 * working as a Manager of sorts.
 */
public class GameSounds{
    
    ContinuousAudioDataStream  nomNom;
    AudioStream  newGame;
	AudioStream  death;
    /* Keeps track of whether or not the eating sound is playing*/
    boolean stopped;
	
	private static GameSounds instance = null;
       

/* Initialize audio files */ 
    public GameSounds(){
        stopped=true; 
        URL url;
        AudioInputStream audioIn;
        
        try{
			// Pacman eating sound
			InputStream in = new FileInputStream("resources/sounds/nomnom.wav");
			
			AudioData data = (new AudioStream(in)).getData();
			// Create ContinuousAudioDataStream.
			nomNom = new ContinuousAudioDataStream (data);
			
			//nomNom = new AudioStream(in);
			
            // newGame        
			in = new FileInputStream("resources/sounds/newGame.wav");
            newGame = new AudioStream(in);
            
            // death        
            in = new FileInputStream("resources/sounds/death.wav");
            death = new AudioStream(in);

        }catch(Exception e){}
    }
	
	public static GameSounds getInstance()
	{
		if (instance == null)
			instance = new GameSounds();
		return instance;
	}
    
    /* Play pacman eating sound */
    public void nomNom()
	{
        AudioPlayer.player.start(nomNom);
    }

	public void stopNomNom()
	{
		AudioPlayer.player.stop(nomNom);
	}
    
    /* Play new game sound */
    public void newGame()
	{
        //AudioPlayer.player.start(newGame);
    }
    
    /* Play pacman death sound */
    public void death()
	{
		//AudioPlayer.player.start(death);
    }
}