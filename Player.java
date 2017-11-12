/*
 */
package musedb;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Lloyd Cloer
 */
public class Player {
    MediaPlayer media_player;
    
    
    public void selectSong(){
        File file = new File("C:\\Users\\Lloyd Cloer\\Music\\Aesthetic Perfection\\Close To Human\\03 Architech.mp3");
        try {
            Media media = new Media(file.toURI().toString());
            media_player = new MediaPlayer(media);
          //  media_player.play();
        } catch(Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    public void play(){
        media_player.play();
    }    
    public void pause(){
        media_player.pause();
    }
    
}
