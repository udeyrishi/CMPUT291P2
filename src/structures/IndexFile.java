package structures;

import java.util.ArrayList;

import common.UIO;

public class IndexFile extends Structure {


	public IndexFile(String fileloc, UIO io) {
		super(fileloc, io);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createDatabase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeDatabase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void populateDatabase(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyDatabase() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public KVPair<String, String> retrieveWithKey(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<KVPair<String, String>> retrieveWithRangeOfKeys(
			String key1, String key2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<KVPair<String, String>> retrieveWithData(String data) {
		// TODO Auto-generated method stub
		return null;
	}

}
