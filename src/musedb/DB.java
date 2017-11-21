package musedb;
import java.io.IOException;
import java.sql.*;
import java.util.Hashtable;
import javafx.application.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.embed.swing.JFXPanel;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class DB {
   private static Connection connect() {
        // SQLite connection string
    	
        String url = "jdbc:sqlite:muse.db";
        Connection c = null;
        try {
		    Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
        } 
        catch (SQLException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            System.exit(0);
        }
        return c;
    }
   //Create the main music library table
   public static void makeMusicLibrary(){
  		Connection c = DB.connect();
   	try {
   		Class.forName("org.sqlite.JDBC");
	        Statement stmt = null;

   		System.out.println("Opened database successfully");
       //Create MUSIC TABLE
   		DatabaseMetaData dbm = c.getMetaData();
   	//Verify if table exists
   		ResultSet tables = dbm.getTables(null, null, "MUSIC", null);
	     if (tables.next()) {
	       // Table exists close connection
	  	   System.out.println("Exists");
	       c.close();
	     }
	     else {
	       // Table does not exist make it with information
	    	System.out.println("Making Table...");
	        stmt = c.createStatement();
	        String sql = "CREATE TABLE MUSIC" +
	                       "(ARTIST 	CHAR(50)     ," +
	                       " SONG_NAME         CHAR(50)    , " + 
	                       " ALBUM_NAME           CHAR(50)	, " + 
	                       " SONG_PATH  CHAR(50) 	PRIMARY KEY   , " + 
	                       " GENRE        CHAR(50),"+
	                       " TRACK CHAR(50))"; 
	        stmt.executeUpdate(sql);
	        stmt.close();
	        c.close();
		      System.out.println("Table created successfully");
	     }
    } 
   	catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
   	}
   	
   }
    //Drop a given Table
    public  static void dropTable(){
    	 Connection c = DB.connect();
		 Statement stmt = null;
    	  try{ 
    		 
    		 String sql = "DROP TABLE MUSIC ";
    	     stmt  = c.createStatement();
			 stmt.executeUpdate(sql);
			 System.out.println("Table  deleted in given database...");
			}
    	  catch(SQLException se){
			//Handle errors for JDBC
			se.printStackTrace();
			}
    	  catch(Exception e){
			//Handle errors for Class.forName
			e.printStackTrace();
			}
    	  finally{
			//finally block used to close resources
			try{
			   if(stmt!=null)
			      c.close();
			}
			catch(SQLException se){
			}// do nothing
			try{
			   if(c!=null)
			      c.close();
			}
			catch(SQLException se){
			   se.printStackTrace();
			}//end finally try
			}//end try
			System.out.println("Goodbye!");


    }
    //Function to add to library 
    public static void addToMusicLibrary(Hashtable hashtable){
    	 //INSERT INTO DB
    	   Connection c = DB.connect();
    	   Hashtable data = new Hashtable();
    	   data = hashtable;
    	   //Get the return value from the file-coordinator function as a hashtable
    	   data = FileCoordinator.extractMetadata(null);
    	   Object artist, album, title, genre, filePath;
    	   artist = data.get("artist");
    	   album =  data.get("album");
    	   title =  data.get("title");
    	   genre = data.get("genre");
    	   filePath = data.get("file path");
		   try {
		         Class.forName("org.sqlite.JDBC");
		         c = DriverManager.getConnection("jdbc:sqlite:muse.db");
		         c.setAutoCommit(false);
		         System.out.println("Opened database successfully");
		         Statement stmt = null;

		         stmt = c.createStatement();
		         //SQL statement to put the values into the table
		         String sql = "INSERT INTO MUSIC (ARTIST,SONG_NAME,SONG_PATH,GENRE) " +
		        		 "VALUES ('"+artist+"', '"+title+"', '"+filePath+"', '"+genre+"' );"; 
		         stmt.executeUpdate(sql);

		        

		         stmt.close();
		         c.commit();
		         c.close();
		      } catch ( Exception e ) {
		         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		         System.exit(0);
		      }
		      System.out.println("Records created successfully");
		      
    }
    
    public static void addToPlaylist(){
    	
    }
    
    public static void addToFavorites(){
    	Connection c = DB.connect();
    	try {
	         Class.forName("org.sqlite.JDBC");
	         c = DriverManager.getConnection("jdbc:sqlite:muse.db");
	         c.setAutoCommit(false);
	         System.out.println("Opened database successfully");
	         Statement stmt = null;

	         stmt = c.createStatement();
	         String sql = "INSERT INTO FAVORITES (ARTIST,SONG_NAME,ALBUM_NAME,SONG_PATH,SONG_LENGTH) " +
	                        "VALUES ('ARTIST', 'SONG_NAME', 'ALBUM_NAME', 'SONG_PATH', 12 );"; 
	         stmt.executeUpdate(sql);

	        

	         stmt.close();
	         c.commit();
	         c.close();
	      } catch ( Exception e ) {
	         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	         System.exit(0);
	      }
	      System.out.println("Favorites Playlist updated");
	      
    }
   
   
   //By default there will already be a favorites table made 
    public static void makeFavoritesTable(){
   		Connection c = DB.connect();
    	try {
    		Class.forName("org.sqlite.JDBC");
	        Statement stmt = null;

    		System.out.println("Opened database successfully");
        //Create FAVORITES TABLE
    		DatabaseMetaData dbm = c.getMetaData();
    	//Verify if table exists
    		ResultSet tables = dbm.getTables(null, null, "FAVORITES", null);
	     if (tables.next()) {
	       // Table exists close connection
	  	   System.out.println("Exists");
	       c.close();
	     }
	     else {
	       // Table does not exist make it with information
	    	System.out.println("Making Table...");
	        stmt = c.createStatement();
	        String sql = "CREATE TABLE FAVORITES" +
	                       "(ARTIST 	CHAR(50)     ," +
	                       " SONG_NAME         CHAR(50)   , " + 
	                       " ALBUM_NAME           CHAR(50)    , " + 
	                       " SONG_PATH  CHAR(50) 	PRIMARY KEY   , " + 
	                       " GENRE        CHAR(50),"+
	                       " TRACK CHAR(50))"; 
	        stmt.executeUpdate(sql);
	        stmt.close();
	        c.close();
		      System.out.println("Table created successfully");
	     }
     } 
    	catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
    	}
    }
    
    public void createNewPlaylist(){
    	
    }
    
    public  static void queryMusic(){
    	Connection c = DB.connect();
    	String sql = "SELECT * FROM MUSIC";
    	try{
			c.setAutoCommit(false);
			Statement stmt = null;
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		       
		       //Query DB for metadata
		       //Return as HASH table
	      while ( rs.next() ) {
	           String songPath= rs.getString("song_path");
	           String  artist =  rs.getString("artist");
	           String  song = rs.getString("song_name");
	           String  albumName = rs.getString("album_name");
	           String genre = rs.getString("genre");

	           
	           System.out.println( "Path:  " + songPath );
	           System.out.println( "Artist:  " + artist );
	           System.out.println( "Song: " + song );


	           System.out.println();
	        }
		    rs.close();
		    stmt.close();
		    c.close();
	
	      
	   }
    	catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
		System.out.println("Operation done successfully");
    	
    	
    }
    
 
	public static void main( String args[] ) {

         
		
		   
		     
	  }

	}	     

