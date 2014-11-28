package cmput291p2;

import java.util.ArrayList;
import java.util.Calendar;

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
	
	private final String fileloc = "/tmp/shahzeb1/database";
	private final String answers = "/tmp/shahzeb1/answers";
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
				processRetrieveWithKey();
				return false;
			case 3:
				processRetrieveWithData();
				return false;
			case 4:
				processRetrieveWithRangeOfKeys();
				return false;
			case 5: case 6:
				table.destroyDatabase();
				return true;
			default:
				System.out.println("Option not recognized.");
				printWelcomeMessage();
				return false;
		}
	}

	private void processRetrieveWithKey() {
		KVPair<String,String> result;
		long start_time = Calendar.getInstance().getTimeInMillis();
		result = table.retrieveWithKey(io.getInputString("Enter key: "));
		long elapsed_time = Calendar.getInstance().getTimeInMillis() - start_time;
		io.writeToFile(answers, result.toString());
		printNumResultsAndTime(1, elapsed_time);
	}

	private void processRetrieveWithData() {
		ArrayList<KVPair<String,String>> result;
		long start_time = Calendar.getInstance().getTimeInMillis();
		result = table.retrieveWithData(io.getInputString("Enter data: "));
		long elapsed_time = Calendar.getInstance().getTimeInMillis() - start_time;
		io.writeToFile(answers, arrayListKVPairToString(result));
		printNumResultsAndTime(result.size(), elapsed_time);
	}

	private void processRetrieveWithRangeOfKeys() {
		ArrayList<KVPair<String,String>> result;
		long start_time = Calendar.getInstance().getTimeInMillis();
		result = table.retrieveWithRangeOfKeys(
				io.getInputString("Enter first key: "), 
				io.getInputString("Enter second key: "));
		long elapsed_time = Calendar.getInstance().getTimeInMillis() - start_time;
		io.writeToFile(answers, arrayListKVPairToString(result));
		printNumResultsAndTime(result.size(), elapsed_time);
	}

	private void printWelcomeMessage() {
		System.out.println("1 Create and populate a database\n"
						+ "2 Retrieve records with a given key\n"
						+ "3 Retrieve records with a given data\n"
						+ "4. Retrieve records with a given range of key values\n"
						+ "5. Destroy the database\n"
						+ "6. Quit");
	}
	
	private <K,V> String arrayListKVPairToString(ArrayList<KVPair<K,V>> list) {
		String res = "";
		for (KVPair<K,V> pair : list) {
			res += pair.toString() + "\n\n";
		}
		return res;
	}
	
	private void printNumResultsAndTime(int num_res, long time) {
		System.out.println("Number of results: "+num_res+" , Time taken: "+time+" ms");
	}

}

