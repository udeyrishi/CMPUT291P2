import java.io.File;
import java.util.ArrayList;


public class MainClassHelper {

    private Structure table;
    private UIO io;
    private String answersloc;

    private final String UNIX_SEP = "/";
    private final String WINDOWS_SEP = "\\";

    public MainClassHelper(String dbtype, UIO io, String databaseLoc,
            String databaseName, String answerLoc, String answerName) {
        createDirectory(databaseLoc);
        createDirectory(answerLoc);
        this.io = io;
        this.answersloc = getPath(answerLoc, answerName);
        table = getCorrectStructure(dbtype, getPath(databaseLoc, databaseName));
        table.createDatabase();
    }

    private String getPath(String directory, String file) {
        String sep = (System.getProperty("os.name").startsWith("Windows")) ? (WINDOWS_SEP) : (UNIX_SEP);

        if (directory.trim().endsWith(sep))
            return directory.trim() + file.trim();
        else
            return directory.trim() + sep + file.trim();
    }

    // Source: http://stackoverflow.com/questions/3634853/how-to-create-a-directory-in-java
    private void createDirectory(String directoryLoc) {
        File theDir = new File(directoryLoc);

        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("Creating directory: " + directoryLoc);
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            }
            catch(SecurityException se) {
                io.printErrorAndExit(String.format("Unable to create directory %s due to lack of priviliges.", directoryLoc));
            }
            if (result) {
                System.out.println(directoryLoc + " created");
            }
        }
    }


    private Structure getCorrectStructure(String dbtype, String fileloc) {
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

        long start_time = System.nanoTime();
        result = table.retrieveWithKey(search_key);
        long elapsed_time = (System.nanoTime() - start_time) / 1000;

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

        long start_time = System.nanoTime();
        result = table.retrieveWithData(search_data);
        long elapsed_time = (System.nanoTime() - start_time) / 1000;

        String query_info = numResultsTimeToString(result.size(), elapsed_time);
        System.out.println(query_info);
        io.writeToFile(answersloc, query_info + "\n\n" + arrayListKVPairToString(result));
    }

    private void processRetrieveWithRangeOfKeys() {
        ArrayList<KVPair<String,String>> result;
        String first_key = io.getInputString("Enter first key: ");
        String second_key = io.getInputString("Enter second key: ");

        long start_time = System.nanoTime();
        result = table.retrieveWithRangeOfKeys(first_key, second_key);
        long elapsed_time = (System.nanoTime() - start_time) / 1000;

        String query_info = numResultsTimeToString(result.size(), elapsed_time);
        System.out.println(query_info);
        io.writeToFile(answersloc, query_info + "\n\n" + arrayListKVPairToString(result));
    }

    private void printWelcomeMessage() {
        System.out.println("1. Create and populate a database\n"
                        + "2. Retrieve records with a given key\n"
                        + "3. Retrieve records with a given data\n"
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
        return "Number of results: "+num_res+" , Time taken: "+time+" microseconds";
    }
}
