package structures;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.CursorConfig;
import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.OperationStatus;
import com.sleepycat.db.Transaction;

import common.UIO;

public class IndexFile extends Structure {

	public IndexFile(String fileloc, UIO io) {
		super(fileloc, io);
		name = "IndexFile";
	}
	
	private Database revdb;

	@Override
	public void createDatabase() {
		createBerkeleyDatabase(DatabaseType.BTREE);
		config = new DatabaseConfig();
		config.setType(DatabaseType.BTREE);
		config.setAllowCreate(true);
		config.setSortedDuplicates(true);
		try {
			revdb = new Database(fileloc+"rev", name, config);
		} catch (FileNotFoundException e) {
			io.printErrorAndExit("Folder does not exist. Failed to create database.\n"+e.toString());
		} catch (DatabaseException e) {
			io.printErrorAndExit("Failed to create database.\n"+e.toString());
		}
	}
	
	@Override
	protected OperationStatus insert(Transaction txn, DatabaseEntry key, DatabaseEntry value) {
		try {
			revdb.put(null, value, key);
			return db.putNoOverwrite(null, key, value);
		} catch (DatabaseException e) {
			io.printErrorAndExit("Error inserting key,value into table.\n"+e.toString());
		}
		return null;
	}
	
	@Override
	public ArrayList<KVPair<String,String>> retrieveWithData(String value_search) {
		ArrayList<KVPair<String,String>> results = new ArrayList<KVPair<String,String>>();
		
		DatabaseEntry key = new DatabaseEntry("".getBytes());
		DatabaseEntry value = new DatabaseEntry(value_search.getBytes());
		
		try {
			Cursor cursor = revdb.openCursor(null, new CursorConfig());
			if (cursor.getSearchKey(value, key, null).equals(OperationStatus.NOTFOUND))
				return results;
			
			results.add(new KVPair<String,String>(DBEntryToString(key), value_search));
			
			while (!cursor.getNext(value, key, null).equals(OperationStatus.NOTFOUND)) {
				if (DBEntryToString(value).compareTo(value_search) != 0)
					break;
				results.add(new KVPair<String,String>(DBEntryToString(key), value_search));
			}
			
			cursor.close();
			
		} catch (DatabaseException e) {
			io.printErrorAndExit("Problem retrieving from database.\n"+e.toString());
		}
		
		return results;
	}
	
	@Override
	public void closeDatabase() {
		super.closeDatabase();
		try {
			revdb.close();
		} catch (DatabaseException e) {
			io.printErrorAndExit("Failed to close database.\n"+e.toString());
		} catch (NullPointerException ne) {
		}
	}

	@Override
	public void destroyDatabase() {
		closeDatabase();
		super.destroyDatabase();
		try {
			Database.remove(fileloc+"rev", null, null);
		} catch (FileNotFoundException e) {
			io.printErrorAndExit("Folder does not exist. Failed to remove database.\n"+e.toString());
		} catch (DatabaseException e) {
			io.printErrorAndExit("Failed to remove database.\n"+e.toString());
		}
	}

}
