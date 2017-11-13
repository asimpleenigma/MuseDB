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
}
