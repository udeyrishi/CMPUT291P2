package structures;

import java.io.FileNotFoundException;

import com.sleepycat.db.Database;
import com.sleepycat.db.DatabaseConfig;
import com.sleepycat.db.DatabaseException;
import com.sleepycat.db.DatabaseType;

import common.UIO;

public class BTree extends Structure {

	public BTree(String fileloc, UIO io) {
		super(fileloc, io);
		name = "BTree";
	}
	
	@Override
	public void createDatabase() {
		config = new DatabaseConfig();
		config.setType(DatabaseType.BTREE);
		config.setAllowCreate(true);
		try {
			db = new Database(fileloc, name, config);
		} catch (FileNotFoundException | DatabaseException e) {
			io.printErrorAndExit("Folder does not exist or failed to create database.\n"+e.toString());
		}
	}
}
