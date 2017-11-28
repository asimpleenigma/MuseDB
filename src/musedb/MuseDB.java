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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

/**
 *
 * @author Lloyd Cloer
 */

public class MuseDB extends Application {
    Player player;
    VBox song_listing;
    
    public static void main(String[] args) {
        String s = "C:\\Users\\Lloyd Cloer\\Music\\Aesthetic Perfection\\Close To Human\\03 Architech.mp3";
        File file = new File(s);
    //    Media media = new Media(s);
        
       // System.out.print
     //   file = new File("file:/C:/Users/Lloyd%20Cloer/Music/AFI/AFI/01%20The%20Lost%20Souls.mp3");
     //   Player p = new Player();
       // p.selectSong(file);
       // p.play();
       // FileCoordinator.copyFile(file, "C:\\Users\\Lloyd Cloer\\Music\\");
      //  System.out.println(FileCoordinator.extractMetadata(file));
     //   Media media = new Media(file.toURI().toString());
     //MediaPlayer media_player = new MediaPlayer(media);
       //     System.out.println(media.getMetadata());
        
        if (true){
            
            MuseDB muse = new MuseDB();
            muse.launch();
        }
    }
    
    public void start(final Stage stage) {
        
        // *** Initialize Model Classes *** //
        player = new Player();
//        File file = new File("C:/Users/Lloyd%20Cloer/Music/AFI/AFI/01%20The%20Lost%20Souls.mp3");
  //      player.selectSong(file);
    //    player.play();
        final FileCoordinator fc = new FileCoordinator();
        
        
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
        final FileChooser file_chooser = new FileChooser();
        
        
        
        // *** Menu Bar *** //
        final Menu file_menu = new Menu("File");
        MenuItem import_item = new MenuItem("Import");
        import_item.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                List<File> list = file_chooser.showOpenMultipleDialog(stage);
                fc.importFiles(list);
                listSongs(list);
            }
        });
        file_menu.getItems().add(import_item);
        
        final Menu menu2 = new Menu("Options");
        final Menu menu3 = new Menu("Help");

        MenuBar menu_bar = new MenuBar();
        menu_bar.getMenus().addAll(file_menu, menu2, menu3);
        
        root.setTop(menu_bar);
        //root
        
        //******* Test Player *******/
        
        final Button play_button = new Button();
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