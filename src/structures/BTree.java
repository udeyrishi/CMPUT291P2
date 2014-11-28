package structures;

import java.util.ArrayList;

import com.sleepycat.db.Cursor;
import com.sleepycat.db.CursorConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.OperationStatus;

import common.UIO;

public class BTree extends Structure {

	public BTree(String fileloc, UIO io) {
		super(fileloc, io);
		name = "BTree";
	}
	
	@Override
	public void createDatabase() {
		createBerkeleyDatabase(DatabaseType.BTREE);
	}
	
	@Override
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
}
