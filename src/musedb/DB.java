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



	static Connection connect() {
		// SQLite connection string

		String url = "jdbc:sqlite:muse.db";
		Connection c = null;
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection(url);
			System.out.println("I connected");
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.getMessage());
			System.exit(0);
		}
		return c;
	}

	public static void makeMusicLibrary() {
		Connection c = DB.connect();
		try {
			Class.forName("org.sqlite.JDBC");
			Statement stmt = null;

			System.out.println("Opened database successfully");
			// Create MUSIC TABLE
			DatabaseMetaData dbm = c.getMetaData();
			// Verify if table exists
			ResultSet tables = dbm.getTables(null, null, "MUSIC", null);
			if (tables.next()) {
				// Table exists close connection
				System.out.println("Exists");
				c.close();
			} else {
				// Table does not exist make it with information
				System.out.println("Making Table...");
				stmt = c.createStatement();
				String sql = "CREATE TABLE MUSIC" + "(ID    INTEGER PRIMARY KEY AUTOINCREMENT,"
						+ " ARTIST 	CHAR(50)     ," + " SONG_NAME         CHAR(50)    , "
						+ " ALBUM_NAME           CHAR(50)	, " + " SONG_PATH  CHAR(50) 	  , "
						+ " GENRE        CHAR(50)," + " TRACK CHAR(50))";
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
				System.out.println("Table created successfully");
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	public static void makeVideoLibrary() {
		Connection c = DB.connect();
		try {
			Class.forName("org.sqlite.JDBC");
			Statement stmt = null;

			System.out.println("Opened database successfully");
			// Create MUSIC TABLE
			DatabaseMetaData dbm = c.getMetaData();
			// Verify if table exists
			ResultSet tables = dbm.getTables(null, null, "VIDEOS", null);
			if (tables.next()) {
				// Table exists close connection
				System.out.println("Exists");
				c.close();
			} else {
				// Table does not exist make it with information
				System.out.println("Making Table...");
				stmt = c.createStatement();
				String sql = "CREATE TABLE VIDEOS" + "(ID 	INTEGER PRIMARY KEY AUTOINCREMENT ,"
						+ " TITLE 	CHAR(50)     ," + " VIDEO_PATH  CHAR(50))";
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
				System.out.println("Videos Table created successfully");
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	public static void dropTable(String name) {
		Connection c = DB.connect();
		String tableToDelete = name;
		Statement stmt = null;
		try {

			String sql = "DROP TABLE '" + tableToDelete + "'";
			stmt = c.createStatement();
			stmt.executeUpdate(sql);
			System.out.println("Table  deleted in given database...");
		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					c.close();
			} catch (SQLException se) {
			} // do nothing
			try {
				if (c != null)
					c.close();
			} catch (SQLException se) {
				se.printStackTrace();
			} // end finally try
		} // end try
		System.out.println("Goodbye!");

	}

	public List<Integer> getMusiclibrary() {
		List<Integer> lib = new ArrayList<Integer>();
		Connection c = DB.connect();
		String sql = "SELECT * FROM MUSIC";
		try {
			c.setAutoCommit(false);
			Statement stmt = null;
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// Query DB for metadata
			// Return as HASH table
			int i = 0;
			while (rs.next()) {
				lib.add(i);
				int song_id = rs.getInt("id");
				String songPath = rs.getString("song_path");
				String artist = rs.getString("artist");
				String song = rs.getString("song_name");
				String albumName = rs.getString("album_name");
				String genre = rs.getString("genre");

				System.out.println("Path: " + songPath);
				System.out.println("Artist: " + artist);
				System.out.println("Song: " + song);
				System.out.println("Album: " + albumName);
				System.out.println("ID: " + song_id);

				i++;
			}
			rs.close();
			stmt.close();
			c.close();

		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

		System.out.println("Operation done successfully");

		return lib;
	}

	public void addMusic(Hashtable ht) {
		// ADD to music library hashtable

		// INSERT INTO DB
		Connection c = DB.connect();
		Hashtable data = new Hashtable();
		data = ht;
		// Get the return value from the file-coordinator function as a
		// hashtable
		Object artist, album, title, genre, filePath;
		artist = data.get("artist");
		album = data.get("album");
		title = data.get("title");
		genre = data.get("genre");
		filePath = data.get("file path");
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:muse.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			Statement stmt = null;

			stmt = c.createStatement();
			// SQL statement to put the values into the table
			String sql = "INSERT INTO MUSIC (ARTIST,SONG_NAME,SONG_PATH,ALBUM_NAME,GENRE) " + "VALUES ('" + artist
					+ "', '" + title + "', '" + filePath + "', '" + album + "', '" + genre + "' );";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
			// Increment id for song
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");
	}

	public void addVideo(Hashtable ht) {

		// Add video to hashtable
		Hashtable data = new Hashtable();
		Connection c = DB.connect();
		// Get the return value from the file-coordinator function as a
		// hashtable
		Object title, filePath;
		title = data.get("title");
		filePath = data.get("file path");
		try {
			Class.forName("org.sqlite.JDBC");
			c = DriverManager.getConnection("jdbc:sqlite:muse.db");
			c.setAutoCommit(false);
			System.out.println("Opened database successfully");
			Statement stmt = null;

			stmt = c.createStatement();
			// SQL statement to put the values into the table
			String sql = "INSERT INTO VIDEOS (ID,TITLE,VIDEO_PATH) " + "VALUES ('" + title + "', '" + filePath + "' );";
			stmt.executeUpdate(sql);

			stmt.close();
			c.commit();
			c.close();
			// Increment id for video
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");

	}

	// Get song from hashtable
	public Object getSong(int SongID) {
 	   	Hashtable data = new Hashtable();
		Connection c = DB.connect();
		String sql = "SELECT * FROM MUSIC WHERE ID = '"+SongID+"'";
		try{
			c.setAutoCommit(false);
			Statement stmt = null;
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		     
		   
		    if(!rs.next())
		    {
		      // do stuff when no rows prsent.
		    	System.out.println("This Song ID does not exist");
		    } //Query DB for metadata
		       //Return as HASH table
		    while ( rs.next() ) {
		    		int id = rs.getInt("id");
		    	   data.put(id, SongID);
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
	    	catch ( Exception e ) {
			      System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			      System.exit(0);
			   }
			System.out.println("Operation done successfully");
		
		return data.get("id");
	}

	// Get video from hashtable
	public Object getVideo(int VideoID) {
 	   	Hashtable data = new Hashtable();
		Connection c = DB.connect();
		String sql = "SELECT * FROM VIDEOS WHERE ID = '"+VideoID+"'";
		try{
			c.setAutoCommit(false);
			Statement stmt = null;
		    stmt = c.createStatement();
		    ResultSet rs = stmt.executeQuery(sql);
		     
		   
		    if(!rs.next())
		    {
		      // do stuff when no rows prsent.
		    	System.out.println("This ID does not exist");
		    } //Query DB for metadata
		       //Return as HASH table
		    while ( rs.next() ) {
	    		   int id = rs.getInt("id");
		    	   data.put(id, VideoID);
		           String video_path= rs.getString("video_path");
		           String  title =  rs.getString("title");
	
		           
		           System.out.println( "Path:  " + video_path );
		           System.out.println( "Title:  " + title );
		           System.out.println( "ID: " + id );
         
	
	
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
		
		return data.get("id");
	}

	// Create Playlist
	public static void createPlaylist(String name) {

		Connection c = DB.connect();
		try {
			Class.forName("org.sqlite.JDBC");
			Statement stmt = null;

			System.out.println("Opened database successfully");
			// Create MUSIC TABLE
			DatabaseMetaData dbm = c.getMetaData();
			// Verify if table exists
			ResultSet tables = dbm.getTables(null, null, name, null);
			if (tables.next()) {
				// Table exists close connection
				System.out.println("Exists");
				c.close();
			} else {
				// Table does not exist make it with information
				System.out.println("Making Table...");
				stmt = c.createStatement();
				String sql = "CREATE TABLE '" + name + "'" + "(SONG_ID 	INT PRIMARY KEY   ," + " TITLE 	CHAR(50)     ,"
						+ " SONG_PATH  CHAR(50))";
				stmt.executeUpdate(sql);
				stmt.close();
				c.close();
				System.out.println("Table created successfully");
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}

	}

	// Get Playlist
	public List<Integer> getPlaylist(String playlist_name) {
		Connection c = DB.connect();
		Hashtable data = new Hashtable();
		List<Integer> playlist = new ArrayList<Integer>();
		String name = playlist_name;
		String sql = "SELECT * FROM '" + name + "'";
		try {
			c.setAutoCommit(false);
			Statement stmt = null;
			stmt = c.createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (!rs.next()) {
				// do stuff when no rows prsent.
				System.out.println("This playlist is empty");
			}
				// Query DB for metadata

				while (rs.next()) {
					int id = rs.getInt("id");
					playlist.add(id);
					
					String songPath = rs.getString("song_path");
					String artist = rs.getString("artist");
					String song = rs.getString("song_name");
					String albumName = rs.getString("album_name");
					String genre = rs.getString("genre");


					data.put("artist", artist);
					data.put("song", song);
					data.put("album", albumName);

				}
				rs.close();
				stmt.close();
				c.close();
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Operation done successfully");
		System.out.println(playlist);
		return playlist;
	}

	
	public static void addToPlaylist(String name, int id) {
		String playlistName = name;
		int songId = id;
		Connection c = DB.connect();
		Statement stmt = null;

		// SQL statement to put the values into the table
		String sql = "INSERT INTO '" + playlistName + "' (SONG_ID) " + "VALUES ('" + songId + "');";

		try {
			Class.forName("org.sqlite.JDBC");

			System.out.println("Opened database successfully");
			// Create MUSIC TABLE
			DatabaseMetaData dbm = c.getMetaData();
			// Verify if table exists
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
			// Table does not exist
			else {
				DB.createPlaylist(playlistName);
				stmt = c.createStatement();
				stmt.executeUpdate(sql);
				stmt.close();
				c.commit();
				c.close();

			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Records created successfully");

		// Table does not exist make it with information

	}

	// Delete a song
	public static void deleteSong(int SongId) {
		Connection c = DB.connect();
		int id = SongId;
		String sql = "DELETE FROM MUSIC WHERE id = '" + SongId + "'";
		try {
			c.setAutoCommit(false);
			Statement stmt = null;
			stmt = c.createStatement();
			int songsDeleted = stmt.executeUpdate(sql);
			if (songsDeleted == 0) {
				System.out.println("song Id not found in Database.");
				stmt.close();
				c.close();
			} else {
				System.out.println("success");

				stmt.close();
				c.close();
			}
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		System.out.println("Song removed successfully");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DB db = new DB();
		db.getPlaylist("MUSIC");
		db.getSong(2);

	}

}