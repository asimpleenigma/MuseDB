/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musedb;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.collections.ObservableMap;
import java.nio.file.StandardCopyOption;
import java.io.*;
import javafx.application.Platform;
/**
 *
 * @author Lloyd Cloer
 */
public class FileCoordinator {
    MuseDB muse;
    public String root_file_path = "";
 //   public static Hashtable<String, File> song_table = new Hashtable<String, File>();
    private Queue<MediaPlayer> import_queue;  // = new LinkedList<MediaPlayer>();
    private Queue<String> path_queue;
    private Timer timer;
    private TimerTask check_imports;
   // private MakeShiftDatabase db_coordinator;
    private boolean isImporting;
    
    
    public FileCoordinator(MuseDB muse){
        this.muse = muse;
        import_queue = new LinkedList<MediaPlayer>();
        path_queue = new LinkedList<String>();
        timer = new Timer();
        check_imports = new TimerTask(){   // Checks to see if media is ready with song metadata, transfers to DB if so.
            public void run(){
                if (import_queue.peek()==null){
                    
       //             isImporting = false;
                    Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    muse.updateDisplay();                              
                                }
                            });
                //    
                    timer.cancel();
                    return;
                }
                // queue not empty
                if (import_queue.peek().getStatus() != MediaPlayer.Status.READY) return;
                // queue head ready
                Hashtable ht = extractMetadata(import_queue.poll());
                ht.put("file path", path_queue.poll()); // add file path to metadata
                System.out.println(ht);
                muse.db.addMusic(ht); // add music to library
            }
        };
    }
    
    public static void moveFile(String source, String target){
        try{
            Files.move(Paths.get(source), 
                    Paths.get(target));
        } catch(Exception ex){            
            ex.printStackTrace();
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    public void importFiles(List<File> new_files){
        //if (import_queue)
    //    isImporting = true;
        for (File f : new_files){
            //System.out.println(f.getAbsolutePath());
            //song_table.put(f.getName(), f);
       
            
            Media media = new Media(f.toURI().toString());
            MediaPlayer media_player = new MediaPlayer(media);
            import_queue.add(media_player);
            path_queue.add(f.getAbsolutePath());
            System.out.println();
        }
        timer.scheduleAtFixedRate(check_imports, 100l, 100l);
        
    }
    
    public static Hashtable extractMetadata(MediaPlayer mp){
        Media media = mp.getMedia();
        ObservableMap om = media.getMetadata();
        Hashtable ht = new Hashtable();
        for (Object s : om.keySet()){
            ht.put(s, om.get(s));
        }
        return ht;
    }
    
    public static void copyFile(File source, String destination){
        File target = new File(destination + source.getName());
        try {
            Files.copy(source.toPath(), target.toPath());
            System.out.println("copied");
        } catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    public void exportPlaylist(String playlist_name, File dest_dir){
        dest_dir = new File(dest_dir.getAbsolutePath()+"\\"+playlist_name);
        if (!dest_dir.exists()){
            dest_dir.mkdir();
        }
        for (int songID : muse.db.getPlaylist(playlist_name)){
            String source_path = (String) muse.db.getSong(songID).get("file path");
            File file = new File(source_path);
            
            String dest = dest_dir.getAbsolutePath()+"\\"+file.getName();
            FileCoordinator.copyFile(file, dest);
        }
            
    }
}
