import java.util.ArrayList;
import java.util.Calendar;


public class MainClassHelper {

	private final String fileloc = "/home/udeyrishi/Desktop/database";
	//private final String fileloc = "/tmp/shahzeb1_db/database";
	private final String answersloc = "./answers";
	private Structure table;
	private UIO io;
	
	public MainClassHelper(String dbtype, UIO io) {
		this.io = io;
		table = getCorrectStructure(dbtype);
		table.createDatabase();
	}
	
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
		String search_key = io.getInputString("Enter key: ");
		
		long start_time = Calendar.getInstance().getTimeInMillis();
		result = table.retrieveWithKey(search_key);
		long elapsed_time = Calendar.getInstance().getTimeInMillis() - start_time;
		
		if (result.getValue().isEmpty()) {
			System.out.println("No such key in database.");
		} else {
			String query_info = numResultsTimeToString(1, elapsed_time);
			System.out.println(query_info);
			io.writeToFile(answersloc, query_info + "\n\n" + result.toString());
		}
	}

	private void processRetrieveWithData() {
		ArrayList<KVPair<String,String>> result;
		String search_data = io.getInputString("Enter data: ");
		
		long start_time = Calendar.getInstance().getTimeInMillis();
		result = table.retrieveWithData(search_data);
		long elapsed_time = Calendar.getInstance().getTimeInMillis() - start_time;
		
		String query_info = numResultsTimeToString(result.size(), elapsed_time);
		System.out.println(query_info);
		io.writeToFile(answersloc, query_info + "\n\n" + arrayListKVPairToString(result));
	}

	private void processRetrieveWithRangeOfKeys() {
		ArrayList<KVPair<String,String>> result;
		String first_key = io.getInputString("Enter first key: ");
		String second_key = io.getInputString("Enter second key: ");
		
		long start_time = Calendar.getInstance().getTimeInMillis();
		result = table.retrieveWithRangeOfKeys(first_key, second_key);
		long elapsed_time = Calendar.getInstance().getTimeInMillis() - start_time;
		
		String query_info = numResultsTimeToString(result.size(), elapsed_time);
		System.out.println(query_info);
		io.writeToFile(answersloc, query_info + "\n\n" + arrayListKVPairToString(result));
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
		StringBuilder res = new StringBuilder();
		for (KVPair<K,V> pair : list) {
			res.append(pair.toString() + "\n\n");
		}
		return res.toString();
	}
	
	private String numResultsTimeToString(int num_res, long time) {
		return "Number of results: "+num_res+" , Time taken: "+time+" ms";
	}
}
