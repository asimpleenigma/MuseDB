/*
 */
package musedb;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.stage.Stage;
//import java.beans.EventHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
//import java.awt.event.ActionEvent;

//import java.nio.file.Files;
import java.io.File;
import javafx.stage.FileChooser;
import java.util.*;


/**
 *
 * @author Lloyd Cloer
 */

public class MuseDB extends Application {
    Player player;
    VBox song_listing;
    
    public static void main(String[] args) {
        MuseDB muse = new MuseDB();
        muse.launch();
    }
    
    public void start(Stage stage) {
        
        // *** Initialize Model Classes *** //
        player = new Player();
        
        
        stage.setTitle("MuseDB");
        
        BorderPane root = new BorderPane();
        
        
        
        VBox control_panel = new VBox();
        root.setLeft(control_panel);
        
        song_listing = new VBox();
        //root.setCenter(song_listing);
        
        ScrollPane sp = new ScrollPane();
        sp.setContent(song_listing);
        root.setCenter(sp);
        
        // **** File Import *** // 
        FileChooser file_chooser = new FileChooser();
        Button import_button = new Button();
        import_button.setText("Import");
        import_button.setOnAction(
            new EventHandler<ActionEvent>() {
                @Override
                public void handle(final ActionEvent e) {
                    List<File> list = file_chooser.showOpenMultipleDialog(stage);
                    FileCoordinator.importFiles(list);
                    listSongs(list);
                }
            });
        control_panel.getChildren().add(import_button);
        
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
        
        control_panel.getChildren().add(play_button);
        
        // Test playing music
        
        //player.selectSong();
       // player.play();
        
        // Test moving files
        String a1 = "C:\\Users\\Lloyd Cloer\\Documents\\Java Test\\Green Neuron.jpg";
        String a2 = "C:\\Users\\Lloyd Cloer\\Documents\\Java Test\\the folder\\Green Neuron.jpg";
      //  FileCoordinator.moveFile(a2, a1);
      
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
        
    }
    
    public void listSongs(List<File> list){
        for (File f : list){
            SongButton b = new SongButton();
            b.setText(f.getName());
            song_listing.getChildren().add(b);
        }
    }
    public class SongButton extends Button{
        
        public SongButton(){
            setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override 
                    public void handle(ActionEvent e) {
                        File f = FileCoordinator.song_table.get(getText());
                        player.selectSong(f);
                    }
                }
            );
        }
    }
    
    
}
