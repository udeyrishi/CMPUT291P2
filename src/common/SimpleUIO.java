package common;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * 
 * SimpleUIO is a class designed to abstract away common
 * user IO operations such as getting input integers, strings, 
 * etc.
 * 
 * SimpleUIO throws exceptions and should be used if you 
 * plan on catching exceptions yourself.
 *
 */
public class SimpleUIO {
	private Scanner input;
	
	/**
	 * Constructor.
	 * @param in is an InputStream to read user
	 * input from.
	 */
	public SimpleUIO(InputStream in) {
		this.input = new Scanner(in);
	}
	
	/**
	 * Must be called when program closes to close 
	 * Scanner.
	 */
	public void cleanUp() {
		if (input != null)
			input.close();
	}
	
	/**
	 * Displays message and asks user for input.
	 * Entered information is returned as a String.
	 *  
	 * @param message
	 * @return
	 */
	public String getInputString(String message) {
		System.out.print(message);
		String output = input.nextLine();
		return output.trim();
	}
	
	/**
	 * Displays message and asks user for input.
	 * Tries to return entered information as an Integer.
	 * May throw NumberFormatException if the entered 
	 * information cannot be converted to an Integer.
	 * 
	 * @param message
	 * @return
	 * @throws NumberFormatException
	 */
	public Integer getInputInteger(String message) throws NumberFormatException {
		return Integer.parseInt(getInputString(message));
	}
	
	/**
	 * Displays message and asks user for input.
	 * Tries to return a Date based on the user's 
	 * entered information. If the information is not
	 * valid, it will throw IllegalArgumentException.
	 * 
	 * @param message
	 * @return
	 * @throws IllegalArgumentException
	 */
	public Date getInputDate(String message) throws IllegalArgumentException {
		return getDate(getInputString(message));
	}
	
	private Date getDate(String date_string) throws IllegalArgumentException {
		String[] split_date = date_string.split("\\-");
		Calendar date = Calendar.getInstance();
		date.clear();
		try {
			date.set(Calendar.YEAR, Integer.valueOf(split_date[0]));
			date.set(Calendar.MONTH, Integer.valueOf(split_date[1])-1);
			date.set(Calendar.DATE, Integer.valueOf(split_date[2]));
			return date.getTime();
		} catch (NumberFormatException nfe) {
			throw new IllegalArgumentException("Please enter dates as yyyy-mm-dd");
		} catch (ArrayIndexOutOfBoundsException idxe) {
			throw new IllegalArgumentException("Please enter dates as yyyy-mm-dd");
		}
	}
	
	/**
	 * Simple function to convert Date into an SQL query 
	 * friendly format. 
	 * 
	 * E.g. Date --> 28-JAN-2004
	 * 
	 * @param date
	 * @return
	 */
	public String getTestDateInSQLDateStringForm(Date date) {
		String rv = (new SimpleDateFormat("dd-MMM-yyyy")).format(date);
	    return String.format("to_date('%s','DD-MON-YYYY')", rv); 
	}
	
	public void printErrorAndExit(String s) {
		System.err.println(s);
		System.exit(0);
	}
	
	public void writeToFile(String filename, String content) throws FileNotFoundException {
		PrintWriter file = new PrintWriter(filename);
		file.println(content);
		file.close();
	}
	
}
