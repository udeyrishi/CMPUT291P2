package structures;

public interface Structure {
	public void createDatabase();
	public void populateDatabase();
	public void destroyDatabase();
	public void retrieveWithKey();
	public void retrieveWithData();
	public void retrieveWithRangeOfKeys();
}
