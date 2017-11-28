/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package musedb;

import java.util.*;
/**
 *
 * @author Lloyd Cloer
 */
public class Organizer {  // Decides what to display
    MuseDB muse;
    String listing_name;
    List<Integer> listing;
    
    
    public Organizer(MuseDB muse){
        this.muse = muse;
        listing_name = "Music";
        setListing("Music");
        
    }
    
    public void setListing(String listing_name){
        this.listing_name = listing_name;
        if (listing_name == "Music"){
            listing = muse.db.getMusicLibrary();
        } else {
            listing = muse.db.getPlaylist(listing_name);
        }
          //  throw new Error("Invalid Medium");
        //}
    }
}
