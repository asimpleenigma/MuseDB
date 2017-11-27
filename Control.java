/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musedb;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import javafx.stage.FileChooser;
import javafx.application.Platform;

/**
 *
 * @author Lloyd Cloer
 */
public class Control {
    MuseDB muse;
    
    public Control(MuseDB muse){
        this.muse = muse;
    }
    
    public static void importMedia(){
        FileChooser file_chooser = new FileChooser();
     //   List<File> list = file_chooser.showOpenMultipleDialog(stage);
       // file_chooser.importFiles(list);

      //  muse.organizer.setMedium(muse.organizer.current_medium);
       // muse.updateDisplay();//sp.setContent(view.libraryDisplay());
    }
    
    public void setListing(String listing_name){
        muse.org.setListing(listing_name);
        muse.updateDisplay();
    }
    
    public void setSleepTimer(int minutes){
        Timer timer = new Timer();
        TimerTask check_imports = new TimerTask(){   // Checks to see if media is ready with song metadata, transfers to DB if so.
            public void run(){
                if (muse.player.is_playing){
                    Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    muse.play_button.fire();                                }
                            });
                    
                }
            }
        };
        timer.schedule(check_imports, minutes*60*1000);
                
    }
}
