import java.util.ArrayList;
import com.sleepycat.db.Cursor;
import com.sleepycat.db.CursorConfig;
import com.sleepycat.db.DatabaseEntry;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;
import com.sleepycat.db.OperationStatus;

public class Hash extends Structure {

	public Hash(String fileloc, UIO io) {
		super(fileloc, io);
		name = "Hash";
	}

	@Override
	public void createDatabase() {
		createBerkeleyDatabase(DatabaseType.HASH);
	}
	
	@Override
	public ArrayList<KVPair<String,String>> retrieveWithRangeOfKeys(String key1, String key2) {
		ArrayList<KVPair<String,String>> results = new ArrayList<KVPair<String,String>>();
		DatabaseEntry key = new DatabaseEntry("".getBytes());
		DatabaseEntry value = new DatabaseEntry("".getBytes());
		try {
			Cursor cursor = db.openCursor(null, new CursorConfig());
			while (!cursor.getNext(key, value, null).equals(OperationStatus.NOTFOUND)) {
				if ((DBEntryToString(key).compareTo(key1) >= 0) && 
					(DBEntryToString(key).compareTo(key2) < 0))
					results.add(new KVPair<String,String>(DBEntryToString(key), DBEntryToString(value)));
			}
			
			cursor.close();
			
		} catch (DatabaseException e) {
			io.printErrorAndExit("Problem retrieving from database.\n"+e.toString());
		}
		
		return results;
	}

}
