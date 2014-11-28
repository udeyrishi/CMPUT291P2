package cmput291p2;

import java.util.ArrayList;

import common.UIO;

import structures.BTree;
import structures.Hash;
import structures.IndexFile;
import structures.KVPair;
import structures.Structure;

public class MainClass {
	
	public static void main(String[] args) {
		MainClass main = new MainClass(args[0]);
		main.run();
	}
	
	public MainClass(String dbtype) {
		io = new UIO(System.in);
		
		table = getCorrectStructure(dbtype);
		table.createDatabase();
	}
	
	private final String fileloc = "/tmp/shahzeb/database";
	private Structure table;
	private UIO io;
	
	private Structure getCorrectStructure(String dbtype) {
		if (dbtype.equals("btree")) return new BTree(fileloc, io);
		else if (dbtype.equals("hash")) return new Hash(fileloc, io);
		else if (dbtype.equals("indexfile")) return new IndexFile(fileloc, io);
		
		io.printErrorAndExit("Structure type not recognized.");
		return null;
	}

	public void run() {
		printWelcomeMessage();
		Boolean quit_flag = false;
		while (!quit_flag) {
			quit_flag = processOptions(io.getInputInteger("BDB: "));
		}
	}
	
	private Boolean processOptions(int option) {
		switch (option) {
			case 1:
				table.populateDatabase(100000);
				return false;
			case 2:
				System.out.println(table.retrieveWithKey(
											io.getInputString("Enter key: ")).toString());
				return false;
			case 3:
				printArrayListKVPair(table.retrieveWithData(
											io.getInputString("Enter data: ")));
				return false;
			case 4:
				printArrayListKVPair(table.retrieveWithRangeOfKeys(
											io.getInputString("Enter first key: "), 
											io.getInputString("Enter second key: ")));
				return false;
			case 5:
				table.destroyDatabase();
				return false;
			case 6:
				io.cleanUp();
				return true;
			default:
				System.out.println("Option not recognized.");
				return false;
		}
	}

	private void printWelcomeMessage() {
		System.out.println("1 Create and populate a database\n"
						+ "2 Retrieve records with a given key\n"
						+ "3 Retrieve records with a given data\n"
						+ "4. Retrieve records with a given range of key values\n"
						+ "5. Destroy the database\n"
						+ "6. Quit");
	}
	
	private <K,V> void printArrayListKVPair(ArrayList<KVPair<K,V>> list) {
		System.out.println("-----");
		for (KVPair<K,V> pair : list) {
			System.out.println(pair.toString());
		}
	}

}

