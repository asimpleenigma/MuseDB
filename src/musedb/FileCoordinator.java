/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musedb;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Hashtable;
import java.util.Set;

import javax.imageio.ImageIO;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException; 


import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import java.io.File;
/**
 *
 * @author Lloyd Cloer
 */
public class FileCoordinator {
    public static Hashtable<String, File> song_table = new Hashtable<String, File>();
    
    public static void moveFile(String source, String target){
        try{
            Files.move(Paths.get(source), 
                    Paths.get(target));
        } catch(Exception ex){            
            ex.printStackTrace();
            System.out.println("Exception: " + ex.getMessage());
        }
    }
    
    public static void importFiles(List<File> new_files){
        for (File f : new_files){
            song_table.put(f.getName(), f);
            System.out.println(f.getName());
        }
        
    }

    static Hashtable extractMetadata(String fileName){
    
        Hashtable<String, String> tempMetaData = new Hashtable<String, String>();
        Mp3File mp3file;
        //Everything happens in try/catch to negate any errors thrown from the file
        try {
          mp3file = new Mp3File(fileName);
          if (mp3file.hasId3v2Tag()) {
            ID3v2 id3v2Tag = mp3file.getId3v2Tag();
            if (id3v2Tag.getTrack() != null){
              tempMetaData.put("Track", (id3v2Tag.getTrack()));
            }
            if (id3v2Tag.getArtist() != null){
              tempMetaData.put("Artist", (id3v2Tag.getArtist()));
            }
            if (id3v2Tag.getTitle() != null){
              tempMetaData.put("Title", (id3v2Tag.getTitle()));
            }
            if (id3v2Tag.getAlbum() != null){
              tempMetaData.put("Album", (id3v2Tag.getAlbum()));
            }
            if (id3v2Tag.getYear() != null){
              tempMetaData.put("Year", (id3v2Tag.getYear()));
            }
            if ((Integer.toString(id3v2Tag.getGenre())) != null){
              tempMetaData.put("Genre", id3v2Tag.getGenre()+" (" + id3v2Tag.getGenreDescription() + ")");
            }
            /**
            byte[] imageData = id3v2Tag.getAlbumImage();
            if (imageData != null && id3v2Tag.getTitle() != null) {
                // Write image to file - can determine appropriate file extension from the mime type
                // Depending on where you put this is where the file gets written to... we need to establish a folder?
                RandomAccessFile file = new RandomAccessFile("C:\\Users\\kpari\\workspace\\museDB\\images\\"+(id3v2Tag.getTitle())+".jpg", "rw");
                file.write(imageData);
                file.close();
            }
            **/
          }
        }
        catch (UnsupportedTagException e) {
          e.printStackTrace();
        }
        catch (InvalidDataException e) {
          e.printStackTrace();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
        return tempMetaData;
      }
    

}
