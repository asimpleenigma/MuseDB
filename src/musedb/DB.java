package musedb;
import java.io.IOException;
import java.sql.*;
import java.util.Hashtable;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

public class DB {
   private Connection connect() {
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
    //Drop a given Table
    public void dropTable(){
    	 Connection c = this.connect();
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
    
    public void addToMusicLibrary(){
    	 //INSERT INTO DB
    	   Connection c = this.connect();
		   try {
		         Class.forName("org.sqlite.JDBC");
		         c = DriverManager.getConnection("jdbc:sqlite:muse.db");
		         c.setAutoCommit(false);
		         System.out.println("Opened database successfully");
		         Statement stmt = null;

		         stmt = c.createStatement();
		         String sql = "INSERT INTO MUSIC (ARTIST,SONG_NAME,ALBUM_NAME,SONG_PATH,SONG_LENGTH) " +
		                        "VALUES ('ARTIST', 'SONG_NAME', 'ALBUM_NAME', 'SONG_PATH', 12 );"; 
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
    
    public void addToPlaylist(){
    	
    }
    
    public void addToFavorites(){
    	Connection c = this.connect();
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
   
    //Create the main music library table
    public void makeMusicLibrary(){
   		Connection c = this.connect();
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
	                       "(ARTIST 	TEXT     NOT NULL," +
	                       " SONG_NAME         TEXT    NOT NULL, " + 
	                       " ALBUM_NAME           TEXT     NOT NULL, " + 
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
   //By default there will already be a favorites table made 
    public void makeFavoritesTable(){
   		Connection c = this.connect();
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
	                       "(ARTIST 	TEXT     NOT NULL," +
	                       " SONG_NAME         TEXT    NOT NULL, " + 
	                       " ALBUM_NAME           TEXT     NOT NULL, " + 
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
    
    public  Object queryMusic(){
    	Connection c = this.connect();
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
	           int songLength = rs.getInt("song_length");
	           int songPlays = rs.getInt("number_of_plays");
	           
	           System.out.println( "Path:  " + songPath );
	           System.out.println( "Artist:  " + artist );
	           System.out.println( "Song: " + song );
	           System.out.println( "Album:  = " + albumName );
	           System.out.println( "Length: = " + songLength );
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
    	
    	return true;
    }


	public static void main( String args[] ) {
		Hashtable<String, String> tempMetaData = new Hashtable<String, String>();
        Mp3File mp3file;
       String fileName = ("C:\\Users\\hayes_000\\Music\\iTunes\\11.Grey.mp3");
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
        System.out.println(tempMetaData);
        
		   String artist = tempMetaData.get("Artist");
		   String track = tempMetaData.get("Track");
		   String album = tempMetaData.get("Album");
		   String song = tempMetaData.get("Title");
		   String genre = tempMetaData.get("Genre");
		   String year = tempMetaData.get("Year");
	       
		   Connection c = null;
	       Statement stmt = null;
	   	String sql = "SELECT * FROM MUSIC";
    	try{
	         c = DriverManager.getConnection("jdbc:sqlite:muse.db");
			c.setAutoCommit(false);
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		       
		       //Query DB for metadata
		       //Return as HASH table
	      while ( rs.next() ) {
	           String songPath= rs.getString("song_path");
	           String  artist1 =  rs.getString("artist");
	           String  song1 = rs.getString("song_name");
	           String  albumName = rs.getString("album_name");

	           
	           System.out.println( "Path:  " + songPath );
	           System.out.println( "Artist:  " + artist1 );
	           System.out.println( "Song: " + song1 );
	           System.out.println( "Album:  = " + albumName );

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
	       /*
		   try {
		         Class.forName("org.sqlite.JDBC");
		         c = DriverManager.getConnection("jdbc:sqlite:muse.db");
		         c.setAutoCommit(false);
		         System.out.println("Opened database successfully");
		   

		         stmt = c.createStatement();
		         String sql1 = "INSERT INTO MUSIC (ARTIST,SONG_NAME,ALBUM_NAME,SONG_PATH,GENRE, TRACK) " +
		                        "VALUES ('"+artist+"', '"+song+"', '"+album+"', '"+fileName+"', '"+genre+"', '"+track+"' );"; 
		         stmt.executeUpdate(sql1);

		        

		         stmt.close();
		         c.commit();
		         c.close();
		      } catch ( Exception e ) {
		         System.err.println( e.getClass().getName() + ": " + e.getMessage() );
		         System.exit(0);
		      }
		      System.out.println("Records created successfully");*/
		     
		   
		   
		   
		     
	  }
		     
}
