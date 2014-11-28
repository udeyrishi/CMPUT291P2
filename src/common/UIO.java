package common;


import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;

/**
 * 
 * Another UIO class that extends the SimpleUIO class.
 * 
 * The biggest difference between the two is that
 * UIO never throws exceptions. All exceptions are dealt
 * with locally. As such, the methods are less flexible.
 *
 */
public class UIO extends SimpleUIO {

	/**
	 * Constructor.
	 * @param in is an InputStream to read user input
	 * from.
	 */
	public UIO(InputStream in) {
		super(in);
	}
	
	/**
	 * Very similar to getInputInteger in SimpleUIO but 
	 * if the input information cannot be converted to an
	 * Integer, the method will ask you again until an 
	 * Integer is properly read.
	 */
	@Override
	public Integer getInputInteger(String message) {
		while (true) {
			try {
				return super.getInputInteger(message);
			}
			catch (NumberFormatException e) {
				System.err.println("Invalid input. Try again.");
			}
		}
	}
	
	
	/**
	 * Very similar to getInputDate in SimpleUIO but
	 * if the entered information cannot be converted to
	 * a Date, the method will prompt you again until a
	 * Date can be returned.
	 */
	@Override
	public Date getInputDate(String message) {
		while (true) {
			try {
				return super.getInputDate(message);
			}
			catch (IllegalArgumentException e) {
				System.err.println("Invalid input. Try again.");
			}
		}
	}
	
	@Override
	public void writeToFile(String filename, String content) {
		try {
			super.writeToFile(filename, content);
		} catch (FileNotFoundException fe) {
			System.err.println("File location not valid.");
		}
	}

}
