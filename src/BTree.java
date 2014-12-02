import com.sleepycat.db.DatabaseType;

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
