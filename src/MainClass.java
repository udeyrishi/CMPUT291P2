public class MainClass {

    private final static String DATABASE_LOC = "/tmp/udey_db";
    private final static String DATABASE_NAME = "database";
    private final static String ANSWERS_LOC = ".";
    private final static String ANSWERS_NAME = "answers";

    public static void main(String[] args) {
        UIO io = new UIO(System.in);
        while (args.length < 1)
            args = io.getInputString("Please enter the 'db_type_option': ").trim().split(" ");
        MainClassHelper main = new MainClassHelper(args[0], io, DATABASE_LOC, DATABASE_NAME,
                ANSWERS_LOC, ANSWERS_NAME);
        main.run();
        io.cleanUp();
    }
}

