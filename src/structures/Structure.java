package structures;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.CursorConfig;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.OperationStatus;

import common.UIO;

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
	
	public void populateDatabase(int size) {
		Random rand = new Random(999);
		DatabaseEntry key, value;
		String s;
		int range;
		
		/* Note that this sequence is very similar to 
		 * the provided sample populateTable.
		 */
		for (int i = 0; i < size; i++) {
			range = 64 + rand.nextInt( 64 );
			s = "";
			for ( int j = 0; j < range; j++ ) 
				s+=(new Character((char)(97+rand.nextInt(26)))).toString();

			key = new DatabaseEntry(s.getBytes());
			key.setSize(s.length()); 

			range = 64 + rand.nextInt( 64 );
			s = "";
			for ( int j = 0; j < range; j++ ) 
				s+=(new Character((char)(97+rand.nextInt(26)))).toString();
			
			value = new DatabaseEntry(s.getBytes());
			value.setSize(s.length()); 
			
			if (rand.nextInt(10000) == 1) {
				System.out.println(new String(key.getData()) + "," + new String(value.getData()));
			}

			try {
				db.putNoOverwrite(null, key, value);
			} catch (DatabaseException e) {
				io.printErrorAndExit("Error inserting key,value into table.\n"+e.toString());
			}
		}
	}
	
	public void closeDatabase() {
		try {
			db.close();
		} catch (DatabaseException e) {
			System.out.println(e.toString());
		}
	}

	public void destroyDatabase() {
		try {
			Database.remove(fileloc, name, config);
		} catch (FileNotFoundException | DatabaseException e) {
			io.printErrorAndExit("Folder does not exist or failed to remove database.\n"+e.toString());
		}
	}

	public KVPair<String,String> retrieveWithKey(String search_key) {
		DatabaseEntry key = new DatabaseEntry(search_key.getBytes());
		DatabaseEntry value = new DatabaseEntry("a".getBytes());
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
		DatabaseEntry value = new DatabaseEntry("a".getBytes());
		try {
			Cursor cursor = db.openCursor(null, new CursorConfig());
			cursor.getSearchKeyRange(key, value, null);
			results.add(new KVPair<String,String>(new String(key.getData()), new String(value.getData())));
			
			while (!cursor.getNext(key, value, null).equals(OperationStatus.NOTFOUND)) {
				if (new String(key.getData()).compareTo(key2) > 0)
					break;
				results.add(new KVPair<String,String>(new String(key.getData()), new String(value.getData())));
			}
			
		} catch (DatabaseException e) {
			io.printErrorAndExit("Problem retrieving from database.\n"+e.toString());
		}
		
		return results;
	}
	

	public ArrayList<KVPair<String,String>> retrieveWithData(String value_search) {
		ArrayList<KVPair<String,String>> results = new ArrayList<KVPair<String,String>>();
		DatabaseEntry key = new DatabaseEntry("a".getBytes());
		DatabaseEntry value = new DatabaseEntry("a".getBytes());
		try {
			Cursor cursor = db.openCursor(null, new CursorConfig());
			while (!cursor.getNext(key, value, null).equals(OperationStatus.NOTFOUND)) {
				if (new String(value.getData()).equals(value_search))
					results.add(new KVPair<String,String>(new String(key.getData()), new String(value.getData())));
			}
			
		} catch (DatabaseException e) {
			io.printErrorAndExit("Problem retrieving from database.\n"+e.toString());
		}
		
		return results;
	}
}
