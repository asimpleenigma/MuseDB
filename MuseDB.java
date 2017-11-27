/*
 */
package musedb;

import java.util.*;
import java.io.File;
import javafx.application.Application;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.stage.FileChooser;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.input.Dragboard;
/**
 *
 * @author Lloyd Cloer
 */

public class MuseDB extends Application {
    Player player;
    Organizer org;
    MakeShiftDatabase db;
    Control control;
    FileCoordinator fc;
    View view;
    
    
  //  VBox song_listing;
    Button play_button;
    Region library_display;
    BorderPane root;
  //  ScrollPane sp;
    
    public static void main(String[] args) {
        if (false){
            String s = "C:\\Users\\Lloyd Cloer\\Music\\Aesthetic Perfection\\Close To Human\\03 Architech.mp3";
            File file = new File(s);
            File dir = new File("C:\\Users\\Lloyd Cloer\\Videos\\ThatFolder");
            dir.mkdir();
            System.out.println(dir.getAbsolutePath());
            System.out.println(file.getName());
            String dest = dir.getAbsolutePath()+"\\"+file.getName();
            FileCoordinator.copyFile(file, dest);
        }
        
        
        if (true){
            MuseDB muse = new MuseDB();
            muse.launch();
        }
    }
    
    public void start(Stage stage) {
        
        // *** Initialize Model Classes *** //
        player = new Player();
        db = new MakeShiftDatabase();
        fc = new FileCoordinator(this);
        org = new Organizer(this);
        view = new View(this);
        control = new Control(this);
        
        // *** Initialize GUI *** //
        stage.setTitle("MuseDB");
        
        root = new BorderPane();
        
        // Control Panel //
        VBox control_panel = new VBox();
        root.setLeft(control_panel);
        
    //    System.out.println("hi");
        updateDisplay();
     //   System.out.println("hiiii");
        
        
        // *** Menu Bar *** //
        
        final Menu file_menu = new Menu("File");
        
        
            // ** Import Button ** //
        MenuItem import_item = new MenuItem("Import");
        import_item.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                FileChooser file_chooser = new FileChooser();
                List<File> list = file_chooser.showOpenMultipleDialog(stage);
                fc.importFiles(list);
                
              //  organizer.setMedium(organizer.current_medium);
                updateDisplay();//sp.setContent(view.libraryDisplay());
            }
        });
        file_menu.getItems().add(import_item);
        
            // ** Create Playlist ** //
        MenuItem playlist_item = new MenuItem("Create Playlist");
        playlist_item.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Create Playlist");
                Optional<String> result = dialog.showAndWait();                
                if (result.isPresent()){
                    String pl_name = result.get();
                    control_panel.getChildren().add(new PlaylistButton(pl_name));
                    db.createPlaylist(pl_name);
                }
                
              //  organizer.setMedium(organizer.current_medium);
                updateDisplay();//sp.setContent(view.libraryDisplay());
            }
        });
        file_menu.getItems().add(playlist_item);
        
            // ** Export Playlist ** //
        MenuItem export_item = new MenuItem("Export Playlist");
        export_item.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                
                DirectoryChooser dc = new DirectoryChooser();
                dc.setTitle("Export Playlist: "+org.listing_name);
                File start_dir = new File("C:");
                dc.setInitialDirectory(start_dir);
                File selected_dir = dc.showDialog(stage);
                fc.exportPlaylist(org.listing_name, selected_dir);
            }
        });
        file_menu.getItems().add(export_item);
        
        // *** Tool Menu *** //
        final Menu tool_menu = new Menu("Tools");
        MenuItem sleep_timer_item = new MenuItem("Set Sleep Timer");
        sleep_timer_item.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Set Sleep Timer (minutes)");
                Optional<String> result = dialog.showAndWait();                
                if (result.isPresent()){
                    int time = Integer.parseInt(result.get());
                    control.setSleepTimer(time);
                }
                
              //  organizer.setMedium(organizer.current_medium);
                updateDisplay();//sp.setContent(view.libraryDisplay());
            }
        });
        tool_menu.getItems().add(sleep_timer_item);
        
        
        
        final Menu menu3 = new Menu("Help");

        MenuBar menu_bar = new MenuBar();
        menu_bar.getMenus().addAll(file_menu, tool_menu, menu3);
        
        root.setTop(menu_bar);
        //root
        
        
        
        
        //******* Play Button *******/
        
        play_button = new Button();
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
        
        control_panel.getChildren().add(new Label()); // leave a soace
        
        control_panel.getChildren().add(new Label("Libraries"));
        
        // *** Meidum Panel *** //
        
        control_panel.getChildren().add(new ListingButton("Music"));
        
        
        control_panel.getChildren().add(new Label());
        control_panel.getChildren().add(new Label("Playlists"));
      
        stage.setScene(new Scene(root, 300, 250));
        stage.show();
        
    }
    
    public void updateDisplay(){
        System.out.println("hi");
        root.setCenter(view.libraryDisplay());
        //System.out.println("hii");
    }
    public class ListingButton extends Button{
        
        public ListingButton(String text){
            super(text);
            setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override 
                    public void handle(ActionEvent e) {
                       // organizer.setMedium(getText());
                       control.setListing(getText());
                    }
                }
            );
        }
    }
    public class PlaylistButton extends ListingButton{
        //String text;
        String name;
        public PlaylistButton(String text){
            super(text);
            this.name = text;
            
            PlaylistButton target = this;
            
            target.setOnDragOver(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    if (event.getGestureSource() != target &&
                            event.getDragboard().hasString()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                    event.consume();
                }
            });
            target.setOnDragDropped(new EventHandler<DragEvent>() {
                public void handle(DragEvent event) {
                    Dragboard board = event.getDragboard();
                    boolean success = false;
                    if (board.hasString()) {
                       int songID = Integer.parseInt(board.getString());
                       db.addSongToPlaylist(target.name, songID);
                       success = true;
                    }
                    event.setDropCompleted(success);

                    event.consume();
                 }
            });
        }
                    
    }
    
}
