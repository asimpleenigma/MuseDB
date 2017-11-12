/*
 */
package musedb;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;
//import java.beans.EventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import java.awt.event.ActionEvent;


/**
 *
 * @author Lloyd Cloer
 */

public class MuseDB extends Application {
    Player player;
    
    
    public void start(Stage stage) {
        
        stage.setTitle("MuseDB");
        
        Group root = new Group();
        
        
        
        
        //******* Test Player *******/
        
        Button play_button = new Button();
        play_button.setText("Play");
        play_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (play_button.getText() == "Play"){
                    player.play();
                    play_button.setText("Pause");
                }
                else {
                    player.pause();
                    play_button.setText("Play");
                }
            }
        });
        
        root.getChildren().add(play_button);
        
        // Test playing music
        player = new Player();
        player.selectSong();
       // player.play();
        
        // Test moving files
        String a1 = "C:\\Users\\Lloyd Cloer\\Documents\\Java Test\\Green Neuron.jpg";
        String a2 = "C:\\Users\\Lloyd Cloer\\Documents\\Java Test\\the folder\\Green Neuron.jpg";
      //  FileCoordinator.moveFile(a2, a1);
      
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
        
    }
    
    public static void main(String[] args) {
        MuseDB muse = new MuseDB();
        muse.launch();
    }
    
}
