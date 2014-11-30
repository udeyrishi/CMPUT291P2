public class MainClass {
	
	public static void main(String[] args) {
		UIO io = new UIO(System.in);
		while (args.length < 1)
			args = io.getInputString("Please enter the 'db_type_option': ").trim().split(" ");
		MainClassHelper main = new MainClassHelper(args[0], io);
		main.run();
		io.cleanUp();
	}
}

