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
	private int songId;
	private int videoId;
	public String import_root;
   static Connection connect() {
        // SQLite connection string
    	
        String url = "jdbc:sqlite:muse.db";
        Connection c = null;
        try {
		    Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection(url);
            System.out.println("I connected");
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
	                       "(ID 	INT PRIMARY KEY  NOT NULL AUTO_INCREMENT   ," +
	                       " ARTIST 	CHAR(50)     ," +
	                       " SONG_NAME         CHAR(50)    , " + 
	                       " ALBUM_NAME           CHAR(50)	, " + 
	                       " SONG_PATH  CHAR(50) 	  , " + 
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
   //Create the main music library table
   public static void makeVideoLibrary(){
  		Connection c = DB.connect();
   	try {
   		Class.forName("org.sqlite.JDBC");
	        Statement stmt = null;

   		System.out.println("Opened database successfully");
       //Create MUSIC TABLE
   		DatabaseMetaData dbm = c.getMetaData();
   	//Verify if table exists
   		ResultSet tables = dbm.getTables(null, null, "VIDEOS", null);
	     if (tables.next()) {
	       // Table exists close connection
	  	   System.out.println("Exists");
	       c.close();
	     }
	     else {
	       // Table does not exist make it with information
	    	System.out.println("Making Table...");
	        stmt = c.createStatement();
	        String sql = "CREATE TABLE VIDEOS" +
	                       "(ID 	INT PRIMARY KEY   NOT NULL AUTO_INCREMENT ," +
	                       " TITLE 	CHAR(50)     ," +
	                       " VIDEO_PATH  CHAR(50))"; 
	        stmt.executeUpdate(sql);
	        stmt.close();
	        c.close();
		      System.out.println("Videos Table created successfully");
	     }
    } 
   	catch ( Exception e ) {
	        System.err.println( e.getClass().getName() + ": " + e.getMessage() );
	        System.exit(0);
   	}
   	
   }
    //Drop a given Table
    public  static void dropTable(String name){
    	 Connection c = DB.connect();
    	 String tableToDelete = name;
		 Statement stmt = null;
    	  try{ 
    		 
    		 String sql = "DROP TABLE '"+tableToDelete+"'";
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
		         String sql = "INSERT INTO MUSIC (ARTIST,SONG_NAME,SONG_PATH,ALBUM_NAME,GENRE) " +
		        		 "VALUES ('"+artist+"', '"+title+"', '"+filePath+"', '"+album+"', '"+genre+"' );"; 
		         stmt.executeUpdate(sql);

		        

		         stmt.close();
		         c.commit();
		         c.close();
		         //Increment id for song
		      } catch ( Exception e ) {
		         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		         System.exit(0);
		      }
		      System.out.println("Records created successfully");
		      
    }

    //Function to add to library 
    public static void addToVideoLibrary(Hashtable hashtable){

    	 //INSERT INTO DB
    	   Connection c = DB.connect();
    	   Hashtable data = new Hashtable();
    	   data = hashtable;
    	   //Get the return value from the file-coordinator function as a hashtable
    	   Object  title, filePath;
    	   title =  data.get("title");
    	   filePath = data.get("file path");
		   try {
		         Class.forName("org.sqlite.JDBC");
		         c = DriverManager.getConnection("jdbc:sqlite:muse.db");
		         c.setAutoCommit(false);
		         System.out.println("Opened database successfully");
		         Statement stmt = null;

		         stmt = c.createStatement();
		         //SQL statement to put the values into the table
		         String sql = "INSERT INTO VIDEOS (ID,TITLE,VIDEO_PATH) " +
		        		 "VALUES ('"+title+"', '"+filePath+"' );"; 
		         stmt.executeUpdate(sql);

		        

		         stmt.close();
		         c.commit();
		         c.close();
		         //Increment id for video
		      } catch ( Exception e ) {
		         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		         System.exit(0);
		      }
		      System.out.println("Records created successfully");
		      
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
    
    public static void createNewPlaylist(String name){
    	String playlistName = name;
    	Connection c = DB.connect();
       	try {
       		Class.forName("org.sqlite.JDBC");
    	        Statement stmt = null;

       		System.out.println("Opened database successfully");
           //Create MUSIC TABLE
       		DatabaseMetaData dbm = c.getMetaData();
       	//Verify if table exists
       		ResultSet tables = dbm.getTables(null, null, playlistName, null);
    	     if (tables.next()) {
    	       // Table exists close connection
    	  	   System.out.println("Exists");
    	       c.close();
    	     }
    	     else {
    	       // Table does not exist make it with information
    	    	System.out.println("Making Table...");
    	        stmt = c.createStatement();
    	        String sql = "CREATE TABLE '"+playlistName+"'" +
	                       "(SONG_ID 	INT PRIMARY KEY   ," +
	                       " TITLE 	CHAR(50)     ," +
	                       " SONG_PATH  CHAR(50))"; 
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
    
    public static void addToPlaylist(String name, int id){
    	String playlistName = name;
    	int songId = id;
    	Connection c = DB.connect();
    	Statement stmt = null;

	     //SQL statement to put the values into the table
	     String sql = "INSERT INTO '"+playlistName+"' (SONG_ID) " +
	    		 "VALUES ('"+songId+"');"; 

	        
       	try {
       		Class.forName("org.sqlite.JDBC");

       		System.out.println("Opened database successfully");
           //Create MUSIC TABLE
       		DatabaseMetaData dbm = c.getMetaData();
       	//Verify if table exists
       		ResultSet tables = dbm.getTables(null, null, playlistName, null);
    	     if (tables.next()) {
    		   stmt = c.createStatement();
    	       // Table exists close connection
    	  	   System.out.println("Exists");
    		   stmt.executeUpdate(sql);
	  	       stmt.close();
	  	       c.commit();
	  	       c.close();
    	     }
    	     //Table does not exist
    	     else {
      	       DB.createNewPlaylist(playlistName);
      	       stmt = c.createStatement();
      	       stmt.executeUpdate(sql);
	  	       stmt.close();
	  	       c.commit();
	  	       c.close();
    	     
    	     }
       	}catch ( Exception e ) {
		         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		         System.exit(0);
		      }
		      System.out.println("Records created successfully");
		      
    	     
    	       // Table does not exist make it with information
    	
    }
    
    public  static void queryMusic(){
    	Connection c = DB.connect();
    	String sql = "SELECT * FROM MUSIC";
    	try{
			c.setAutoCommit(false);
			Statement stmt = null;
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		     
		   
		    if(!rs.next())
		    {
		      // do stuff when no rows prsent.
		    	System.out.println("This Library is empty");
		    }else
		    {
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
		           System.out.println( "Album: " + albumName );           
	
	
		           System.out.println();
		        }
			    rs.close();
			    stmt.close();
			    c.close();
		
		      
		   }
    	}
	    	catch ( Exception e ) {
			      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			      System.exit(0);
			   }
			System.out.println("Operation done successfully");
	    	
    	
    	
    }
    
    public static Hashtable getMusicLibrary(){
    	Hashtable library = new Hashtable();
    	Connection c = DB.connect();
    	String sql = "SELECT * FROM MUSIC";
    	try{
			c.setAutoCommit(false);
			Statement stmt = null;
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    if(!rs.next())
		    {
		      // do stuff when no rows prsent.
		    	System.out.println("This Library is empty");
		    }else
		    {
		       
			       //Query DB for metadata
			       //Return as HASH table
		      while ( rs.next() ) {
		    	   int song_id = rs.getInt("id");
		           String songPath= rs.getString("song_path");
		           String  artist =  rs.getString("artist");
		           String  song = rs.getString("song_name");
		           String  albumName = rs.getString("album_name");
		           String genre = rs.getString("genre");
		           
		           library.put("id", song_id);
		           library.put("artist", artist);
		           library.put("song", song);
		           library.put("album", albumName);
		           library.put("path", songPath);

		        }
			    rs.close();
			    stmt.close();
			    c.close();
		    }
	
	      
	   }
    	catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
    	
		System.out.println("Operation done successfully");
    	return library;
    	
    }
    
    public static Hashtable getVideoLibrary(){
    	Hashtable video_library = new Hashtable();
    	Connection c = DB.connect();
    	String sql = "SELECT * FROM VIDEOS";
    	try{
			c.setAutoCommit(false);
			Statement stmt = null;
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    if(!rs.next())
		    {
		      // do stuff when no rows prsent.
		    	System.out.println("This Library is empty");
		    }else
		    {
		       
			       //Query DB for metadata
			       //Return as HASH table
		      while ( rs.next() ) {
		    	   int video_id = rs.getInt("id");
		           String videoPath= rs.getString("video_path");
		           String  title =  rs.getString("title");

		           
		           video_library.put("id", video_id);
		           video_library.put("path", videoPath);
		           video_library.put("title", title);


		        }
			    rs.close();
			    stmt.close();
			    c.close();
		    }
	
	      
	   }
    	catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
    	
		System.out.println("Operation done successfully");
    	return video_library;
    	
    }

    public static Hashtable getPlaylist(String name){
    	Connection c = DB.connect();
 	  	Hashtable data = new Hashtable();
    	String playlistName = name;
    	String sql = "SELECT * FROM '"+playlistName+"'";
    	try{
			c.setAutoCommit(false);
			Statement stmt = null;
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    if(!rs.next())
		    {
		      // do stuff when no rows prsent.
		    	System.out.println("This playlist is empty");
		    }else
		    {
		       
			       //Query DB for metadata
			       //Return as HASH table
		      while ( rs.next() ) {
		    	   int song_id = rs.getInt("id");
		           String songPath= rs.getString("song_path");
		           String  artist =  rs.getString("artist");
		           String  song = rs.getString("song_name");
		           String  albumName = rs.getString("album_name");
		           String genre = rs.getString("genre");
		           
		           data.put("id", song_id);
		           data.put("artist", artist);
		           data.put("song", song);
		           data.put("album", albumName);

		        }
			    rs.close();
			    stmt.close();
			    c.close();
		    }
	
	      
	   }
    	catch ( Exception e ) {
		      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		      System.exit(0);
		   }
    	
		System.out.println("Operation done successfully");
    	return data;
    	
    }
    
    //Retrieve song
    public Object getSong(int id){
    	Hashtable library = new Hashtable();
    	library = DB.getMusicLibrary();
    	
    	return library.get(id);
    }
    
    //Retrieve song
    public Object getVideo(int id){
    	Hashtable library = new Hashtable();
    	library = DB.getVideoLibrary();
    	
    	return library.get(id);
    }
    
    public static void deleteSong(String name){
    	Connection c = DB.connect();
    	String songName = name;
    }
    
    public void setImportRoot(String path){
        this.import_root = path;
    }
    
    public String getImportRoot(){
        return this.import_root;
    }
 
	public static void main( String args[] ) {
		
		   
		     
	  }

	}	     

