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
/**
 *
 * @author Lloyd Cloer
 */
public class FileCoordinator {
    public String root_file_path = "";
    public static Hashtable<String, File> song_table = new Hashtable<String, File>();
    private Queue<MediaPlayer> import_queue;  // = new LinkedList<MediaPlayer>();
    private Timer timer;
    private TimerTask check_imports;
    
    
    public FileCoordinator(){
        import_queue = new LinkedList<MediaPlayer>();
        timer = new Timer();
        check_imports = new TimerTask(){
            public void run(){
                if (import_queue.peek()==null){
                    timer.cancel();
                    return;
                }
                // queue not empty
                if (import_queue.peek().getStatus() != MediaPlayer.Status.READY) return;
                // queue head ready
                Hashtable ht = extractMetadata(import_queue.poll());
                System.out.println(ht);

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
        for (File f : new_files){
            song_table.put(f.getName(), f);
            //putMetadata(extractMetadata(f));
        //    System.out.println(f.getName());
            //System.out.println()
          //  System.out.println(extractMetadata(f));
       
            
            Media media = new Media(f.toURI().toString());
            MediaPlayer media_player = new MediaPlayer(media);
            import_queue.add(media_player);
            System.out.println();
        }
        timer.scheduleAtFixedRate(check_imports, 100l, 100l);
        
    }
    /*
    public static ObservableMap extractMetadata(File file){
        Media media = new Media(file.toURI().toString());
        MediaPlayer media_player = new MediaPlayer(media);
        System.out.println(media_player.getStatus());
        return media.getMetadata();
    }*/
    
    public static Hashtable extractMetadata(MediaPlayer mp){
        Media media = mp.getMedia();
        ObservableMap om = media.getMetadata();
        Hashtable ht = new Hashtable();
        for (Object s : om.keySet()){
            ht.put(s, om.get(s));
        }
        ht.put("file path", media.getSource());
        return ht;
    }
    
    public static void copyFile(File source, String destination){
        File target = new File(destination + source.getName());
        try {
            Files.copy(source.toPath(), target.toPath());
        } catch (IOException ex){
            ex.printStackTrace();
            System.out.println("Exception: " + ex.getMessage());
        }
        
    }
 /*   public static void exportSongs(List<File> files, String destination){
        for(File file : files) {
            
        }
    }*/
}