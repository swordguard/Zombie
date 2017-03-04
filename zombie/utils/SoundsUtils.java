package zombie.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.CannotRealizeException;
import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

public class SoundsUtils {
	
	public static void playSound(String str){
		File soundFile = new File(str);
		Player player;
        try {
            player = Manager.createRealizedPlayer(soundFile.toURI().toURL());
            player.start();
            //player.addControllerListener(new ControllListener());
        } catch (NoPlayerException e) {
            e.printStackTrace();
        } catch (CannotRealizeException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

}
