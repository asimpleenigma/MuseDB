/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musedb;

import java.io.File;
import javafx.scene.layout.VBox;
import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.input.ClipboardContent;

/**
 *
 * @author Lloyd Cloer
 */
public class View {
    MuseDB muse;
    String[] music_fields = new String[]{"Title", "Artist", "Album"};
    
    public View(MuseDB muse){
        this.muse = muse;
    }
    
    public Region libraryDisplay(){
   //     if (muse.org.listing_name == "Music"){
            System.out.println(muse.org.listing);
            Region md = new MusicDisplay(muse.org.listing);
           // System.out.println("md: "+ md);
            return md;
 //       } else {
 //           throw new Error("Invalid Medium");
  //      }
            
    }
    
    
    public class MusicDisplay extends ScrollPane{
        
        
        public MusicDisplay(List<Integer> song_list){
            super();
            
            VBox v_box = new VBox();
            HBox field_titles = new HBox();
            String text;
            for (String field : music_fields){
                text = field;
                while (text.length() < 30) text+= " ";
                text += "\t";
                Label label = new Label(text);
                field_titles.getChildren().add(label);
            }
            v_box.getChildren().add(field_titles);
            
            for (int songID : song_list){
                v_box.getChildren().add(new SongButton(songID));
            }
            this.setContent(v_box);
        }
    }
    
    
    public class SongButton extends HBox{
        private Hashtable<String, String> song_data;
        //Label title_label; Label artist_label; Label album_label;
        Label label;
        int ID;
        
        
        public SongButton(int songID){
            super();
            this.ID = songID;
            song_data = muse.db.getSong(songID);
            this.setStyle("-fx-border-color: black");
            
            String text;
            for (String field : music_fields){
                text = (String) song_data.get(field.toLowerCase());
                while (text.length() < 30) text+= " ";
                text += "\t";
                label = new Label(text);
                this.getChildren().add(label);
            }
            
            this.setOnMouseClicked(new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent t) {
                    File f = new File(song_data.get("file path"));
                    System.out.println(song_data.get("file path"));
                    muse.player.selectSong(f);
                }
            });
            SongButton source = this;
            setOnDragDetected(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    Dragboard board = source.startDragAndDrop(TransferMode.ANY);
                    ClipboardContent content = new ClipboardContent();
                    content.putString(""+source.ID);
                    board.setContent(content);
                    event.consume();
                }    
            });
            
        }
    }
}
