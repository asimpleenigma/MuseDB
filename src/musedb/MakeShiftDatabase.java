/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musedb;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 *
 * @author Lloyd Cloer
 */
public class MakeShiftDatabase {
    List<Hashtable> music_library;
    List<Hashtable> video_library;
    List<Hashtable> image_library;
    Hashtable<String, PlayList> play_lists;
    String import_root;
    
    MakeShiftDatabase(){
        import_root = "";
        music_library = new ArrayList<Hashtable>();
        video_library = new ArrayList<Hashtable>(); 
        image_library = new ArrayList<Hashtable>(); 
        play_lists = new Hashtable<String, PlayList>();
    }
    
    public List<Integer> getMusicLibrary(){
        List<Integer> lib = new ArrayList<Integer>();
        for (int i = 0; i < music_library.size(); i++){
            lib.add(i);
        }
        return lib;
    }
    
    public void setImportRoot(String path){
        this.import_root = path;
    }
    
    public String getImportRoot(){
        return this.import_root;
    }
    
    public void addMusic(Hashtable ht){
        music_library.add(ht);
    }
    
    
    public void addImage(Hashtable ht){
        image_library.add(ht);
    }
    
    public void addVideo(Hashtable ht){
        video_library.add(ht);
    }
    
    public void createPlaylist(String name){
        play_lists.put(name, new PlayList(name));
    }
    
    public void addSongToPlaylist(String play_list_name, int songID){
        play_lists.get(play_list_name).addSong(songID);
    }
    
    public List<Integer> getPlaylist(String playlist_name){
        return play_lists.get(playlist_name).song_list;
    }
    
    public Hashtable getSong(int SongID){
        return music_library.get(SongID);
    }
    
    public Hashtable getVideo(int VideoID){
        return video_library.get(VideoID);
    }
    
    private class PlayList{
        String name;
        List<Integer> song_list;
        
        public PlayList(String name){
           this.name = name;
           song_list = new ArrayList<Integer>();
        }
        
        private void addSong(Integer songID){
            song_list.add(songID);
        }
    
    }
}
