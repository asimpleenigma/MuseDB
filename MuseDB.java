/*
 */
package musedb;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
import java.io.File;
import java.nio.file.Files;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.nio.file.Paths;
*/
/**
 *
 * @author Lloyd Cloer
 */
public class MuseDB extends Application {
    Player player;
    
    
    public void start(Stage stage) {
        
        stage.setTitle("MuseDB");
        stage.show();
        
        player = new Player();
        player.play();
    }
    
    public static void main(String[] args) {
        MuseDB muse = new MuseDB();
        muse.launch();
    }
    
}
