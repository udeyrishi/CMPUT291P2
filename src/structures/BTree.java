package structures;

import com.sleepycat.db.DatabaseType;

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
	
}
