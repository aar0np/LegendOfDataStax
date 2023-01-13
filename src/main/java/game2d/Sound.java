package game2d;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound {
	Clip clip;
	URL soundURL[] = new URL[30];
	
	public Sound (boolean musicOnly) {

		if (musicOnly) {
			//soundURL[0] = getClass().getResource("/sounds/nintendo_song_short5-25171.wav");
			soundURL[0] = getClass().getResource("/sounds/dr_dreamchip_music.wav");
		} else {
			soundURL[0] = null;
			soundURL[1] = getClass().getResource("/sounds/coin.wav");
			soundURL[2] = getClass().getResource("/sounds/powerup.wav");
			soundURL[3] = getClass().getResource("/sounds/unlock.wav");
			soundURL[4] = getClass().getResource("/sounds/fanfare.wav");
			soundURL[5] = getClass().getResource("/sounds/blocked.wav");
			//soundURL[6] = getClass().getResource("/sounds/cuttree.wav");
			soundURL[7] = getClass().getResource("/sounds/dooropen.wav");
			soundURL[8] = getClass().getResource("/sounds/hitmonster.wav");
			//soundURL[9] = getClass().getResource("/sounds/parry.wav");
			soundURL[10] = getClass().getResource("/sounds/receivedamage.wav");
			soundURL[11] = getClass().getResource("/sounds/speak.wav");
			//soundURL[12] = getClass().getResource("/sounds/stairs.wav");
			soundURL[13] = getClass().getResource("/sounds/swingweapon.wav");
			soundURL[14] = getClass().getResource("/sounds/levelup.wav");
			soundURL[15] = getClass().getResource("/sounds/cursor.wav");
			soundURL[16] = getClass().getResource("/sounds/burning.wav");
		}
	}
	
	public void setFile(int index) {
		try {
			
			AudioInputStream audioInStream = AudioSystem.getAudioInputStream(soundURL[index]);
			clip = AudioSystem.getClip();
			clip.open(audioInStream);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void play() {
		clip.start();
	}
	
	public void loop() {
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}
	
	public void stop() {
		clip.stop();
	}
}
