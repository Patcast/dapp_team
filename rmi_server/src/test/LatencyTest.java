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

public class LatencyTest {

    private static final Logger logger = Logger.getLogger(LatencyTest.class.getName());
    private static final String SERVER_URL = "yonira.westeurope.cloudapp.azure.com";
    private static final int PORT = 8080;
    private static final String CSV_FILE = "result.csv";
    private static final int[] NO_OF_ROOMS = {1, 2, 4, 8, 16, 32/*, 64, 128, 256, 512*/};   // above 32 crashes
    private static final int NO_OF_TESTS = 100;

    /**
     * Start the application and call the client functions
     * @param args null
     */
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(SERVER_URL, PORT);
            BookingInterface bookingInterface = (BookingInterface) registry.lookup("Hotel");

            logger.log(Level.INFO, "Connected to server, starting client");

            LatencyTest client = new LatencyTest(bookingInterface);
            client.run();
        } catch (Exception e)
        {
            logger.log(Level.SEVERE, e.getMessage());
            e.printStackTrace();
            System.exit(-1);
        }
    }

    private final BookingInterface bookingInterface;

    public LatencyTest(BookingInterface bookingInterface) {
        this.bookingInterface = bookingInterface;
    }

    public void run() throws IOException {
        PrintWriter output = openCSVOutput();
        output.println("number of rooms,time (us)");

        bookingInterface.connect();

        for (int number : NO_OF_ROOMS) {
            System.out.println("Starting test number of rooms " + number);
            bookingInterface.setNumberOfRooms(number);
            System.out.println("Warming up");
            warmUp();
            System.out.println("Starting test");
            test(number, output);
        }

        System.out.println("Test done");
        output.close();
    }

    private PrintWriter openCSVOutput() throws FileNotFoundException {
        File output = new File(CSV_FILE);
        return new PrintWriter(output);
    }

    private void warmUp() throws RemoteException {
        for (int i = 0; i < 10; i++) {
            bookingInterface.getRooms();
            System.out.print("\rWarming up " + i);
        }
        System.out.println();
    }

    private void test(int numberOfRooms, PrintWriter output) throws IOException {
        for (int i = 0; i < NO_OF_TESTS; i++) {
            long start = System.nanoTime();
            bookingInterface.getRooms();
            long time = (System.nanoTime() - start) / 1000;

            output.println(numberOfRooms + "," + time);

            System.out.print("\rStarting test " + i);
        }
        System.out.println();
    }
}
