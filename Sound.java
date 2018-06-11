import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	
	public void playSound() {
		try {
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("sound.wav"));
			Clip clip = AudioSystem.getClip( );
			clip.open(audioInputStream);
			clip.start( );
			//this creates the pop sound you hear every-time a brick merges with another. 
		}
		catch(Exception e)  {
			System.out.println(e);
		} 

	}
	
	public void lostGame() {
		try {
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Failure.wav"));
				Clip clip = AudioSystem.getClip( );
				clip.open(audioInputStream);
				clip.start( );
				//this creates a sound to signal game over.  
			}
			catch(Exception e)  {
				System.out.println(e);
			} 

		}

}
