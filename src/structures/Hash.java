package structures;

import com.sleepycat.db.DatabaseType;

import common.UIO;

public class Hash extends Structure {

	public Hash(String fileloc, UIO io) {
		super(fileloc, io);
		name = "Hash";
	}

	@Override
	public void createDatabase() {
		createBerkeleyDatabase(DatabaseType.HASH);
	}

}
