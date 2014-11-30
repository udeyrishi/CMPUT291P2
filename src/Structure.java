import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import com.sleepycat.db.Cursor;
import com.sleepycat.db.CursorConfig;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.OperationStatus;
import com.sleepycat.db.Transaction;

public abstract class Structure {
	
	public Structure(String fileloc, UIO io) {
		this.io = io;
		this.fileloc = fileloc;
	}
	
	UIO io;
	String fileloc;
	Database db;
	DatabaseConfig config;
	String name;
	
	public abstract void createDatabase();
	
	protected void createBerkeleyDatabase(DatabaseType type) {
		config = new DatabaseConfig();
		config.setType(type);
		config.setAllowCreate(true);
		try {
			db = new Database(fileloc, name, config);
		} catch (FileNotFoundException e) {
			io.printErrorAndExit("Folder does not exist. Failed to create database.\n"+e.toString());
		} catch (DatabaseException e) {
			io.printErrorAndExit("Failed to create database.\n"+e.toString());
		}
	}
	
	public void populateDatabase(int size) {
		Random rand = new Random(999);
		for (int i = 0; i < size; ++i)
			insert(null, getRandomDBEntry(rand), getRandomDBEntry(rand));
	}
	
	private DatabaseEntry getRandomDBEntry(Random rand) {
		int range = 64 + rand.nextInt( 64 );
		String s = "";
		for ( int j = 0; j < range; j++ ) 
			s+=(new Character((char)(97+rand.nextInt(26)))).toString();

		DatabaseEntry rv = new DatabaseEntry(s.getBytes());
		rv.setSize(s.length()); 
		return rv;
	}
	
	protected OperationStatus insert(Transaction txn, DatabaseEntry key, DatabaseEntry value) {
		try {
			return db.putNoOverwrite(null, key, value);
		} catch (DatabaseException e) {
			io.printErrorAndExit("Error inserting key,value into table.\n"+e.toString());
		}
		return null;
	}
	
	public void closeDatabase() {
		try {
			db.close();
		} catch (DatabaseException e) {
			io.printErrorAndExit("Failed to close database.\n"+e.toString());
		} catch (NullPointerException ne) {
		}
	}

	public void destroyDatabase() {
		closeDatabase();
		try {
			Database.remove(fileloc, null, null);
		} catch (FileNotFoundException e) {
			io.printErrorAndExit("Folder does not exist. Failed to remove database.\n"+e.toString());
		} catch (DatabaseException e) {
			io.printErrorAndExit("Failed to remove database.\n"+e.toString());
		}
	}

	public KVPair<String,String> retrieveWithKey(String search_key) {
		DatabaseEntry key = new DatabaseEntry(search_key.getBytes());
		DatabaseEntry value = new DatabaseEntry("".getBytes());
		try {
			db.get(null, key, value, null);
		} catch (DatabaseException e) {
			io.printErrorAndExit("Problem retrieving from database.\n"+e.toString());
		}
		
		return new KVPair<String,String>(new String(key.getData()), new String(value.getData()));
	}
	
	public ArrayList<KVPair<String,String>> retrieveWithRangeOfKeys(String key1, String key2) {
		ArrayList<KVPair<String,String>> results = new ArrayList<KVPair<String,String>>();
		DatabaseEntry key = new DatabaseEntry(key1.getBytes());
		DatabaseEntry value = new DatabaseEntry("".getBytes());
		try {
			Cursor cursor = db.openCursor(null, new CursorConfig());
			cursor.getSearchKeyRange(key, value, null);
			results.add(new KVPair<String,String>(DBEntryToString(key), DBEntryToString(value)));
			
			while (!cursor.getNext(key, value, null).equals(OperationStatus.NOTFOUND)) {
				if (DBEntryToString(key).compareTo(key2) > 0)
					break;
				results.add(new KVPair<String,String>(DBEntryToString(key), DBEntryToString(value)));
			}
			
			cursor.close();
			
		} catch (DatabaseException e) {
			io.printErrorAndExit("Problem retrieving from database.\n"+e.toString());
		}
		
		return results;
	}
	

	public ArrayList<KVPair<String,String>> retrieveWithData(String value_search) {
		ArrayList<KVPair<String,String>> results = new ArrayList<KVPair<String,String>>();
		DatabaseEntry key = new DatabaseEntry("".getBytes());
		DatabaseEntry value = new DatabaseEntry("".getBytes());
		try {
			Cursor cursor = db.openCursor(null, new CursorConfig());
			while (!cursor.getNext(key, value, null).equals(OperationStatus.NOTFOUND)) {
				if (DBEntryToString(value).equals(value_search))
					results.add(new KVPair<String,String>(DBEntryToString(key), DBEntryToString(value)));
			}
			
			cursor.close();
			
		} catch (DatabaseException e) {
			io.printErrorAndExit("Problem retrieving from database.\n"+e.toString());
		}
		
		return results;
	}
	
	/*
	 * 	There is an insanely annoying bug hidden here. When using a cursor to
	 * 	go through the database, the keys would always be perfect. But the values
	 * 	would often randomly get padded with information from other values so that
	 * 	all the values were the same length.
	 * 	
	 *  This only happened if you used the actual getData() method of DatabaseEntry.
	 *  Funnily enough, the getSize() method reports the right size but getData() 
	 *  gives too much data back. The work around is to use only the first x bytes
	 *  of the array returned by getData().
	 *  
	 *  Credit to 
	 *  http://stackoverflow.com/questions/1100371/grabbing-a-segment-of-an-array-in-java
	 *  for mentioning Arrays.copyOfRange.
	 */
	String DBEntryToString(DatabaseEntry entry) {
		return new String(Arrays.copyOfRange(entry.getData(), 0, entry.getSize()));
	}
}
