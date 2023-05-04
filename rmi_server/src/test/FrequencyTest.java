package test;

import hotel.BookingInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FrequencyTest {

    private static final Logger logger = Logger.getLogger(LengthTest.class.getName());
    private static final String SERVER_URL = "yonira.westeurope.cloudapp.azure.com";
    private static final int PORT = 8080;
    private static final int NO_OF_TESTS = 10000;

    /**
     * Start the application and call the client functions
     * @param args null
     */
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_URL, PORT);
            BookingInterface bookingInterface = (BookingInterface) registry.lookup("Hotel");

            logger.log(Level.INFO, "Connected to server, starting client");

            FrequencyTest client = new FrequencyTest(bookingInterface);
            client.run(args);
        } catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private final BookingInterface bookingInterface;

    public FrequencyTest(BookingInterface bookingInterface) {
        this.bookingInterface = bookingInterface;
    }

    public void run(String[] args) throws IOException {
        PrintWriter output = openCSVOutput(Integer.parseInt(args[0]));
        output.println("time (us)");

        bookingInterface.connect();

        System.out.println("Warming up");
        warmUp();
        System.out.println("Starting test");
        test( output);

        System.out.println("Test done");
        output.close();
    }

    private PrintWriter openCSVOutput(int frequency) throws FileNotFoundException {
        File output = new File("Frequency_" + frequency + ".csv");
        return new PrintWriter(output);
    }

    private void warmUp() throws RemoteException {
        for (int i = 0; i < 10; i++) {
            bookingInterface.getRooms();
            System.out.print("\rWarming up " + i);
        }
        System.out.println();
    }

    private void test(PrintWriter output) throws IOException {
        for (int i = 0; i < NO_OF_TESTS; i++) {
            long start = System.nanoTime();
            bookingInterface.getRooms();
            long time = (System.nanoTime() - start) / 1000;

            output.println(time);

            System.out.print("\rStarting test " + i);
        }
        System.out.println();
    }
}
