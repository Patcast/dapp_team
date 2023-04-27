package staff;

import hotel.AddBookingStatus;
import hotel.BookingDetail;
import hotel.BookingInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionClient {

    private static final Logger logger = Logger.getLogger(SessionClient.class.getName());

    /**
     * Start the application and call the client functions
     * @param args null
     */
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry(8080);
            BookingInterface bookingInterface = (BookingInterface) registry.lookup("Hotel");

            logger.log(Level.INFO, "Connected to server, starting client");

            SessionClient client = new SessionClient(bookingInterface);
            client.run();
        } catch (Exception e)
        {
            logger.log(Level.SEVERE, "could not get server.");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    /* class code */

    private final BookingInterface bookingInterface;
    private final Integer sessionKey;

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

    public SessionClient(BookingInterface bookingInterface) throws RemoteException {
        this.bookingInterface = bookingInterface;
        this.sessionKey = bookingInterface.connect();
    }

    public void run() {
        try {
            System.out.println("Welcome! Rooms:");
            System.out.print(bookingInterface.printRooms());
            getInput();
        } catch (RemoteException remoteException) {
            logger.log(Level.SEVERE, "Server error: {0}", remoteException.getMessage());
            System.exit(-1);
        } catch (IOException exception)
        {
            logger.log(Level.SEVERE, "IO error: {0}", exception.getMessage());
            System.exit(-1);
        }
    }

    private void getInput() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.print("> ");

            String input = reader.readLine();

            if (input.equals("exit"))
                break;
            else if (input.startsWith("book"))
                bookRoom(input);
            else if (input.startsWith("confirm"))
                confirm();
            else if (input.startsWith("rooms"))
                System.out.print(bookingInterface.printRooms());
            else if (input.startsWith("cart"))
                System.out.print(bookingInterface.currentCart(sessionKey));
            else
                System.out.println("Unknown command");
        }
    }

    private void bookRoom(String input) throws RemoteException {
        try {
            String[] parameter = input.split(" ");

            if (parameter.length != 4)
                throw new IllegalArgumentException("Invalid number of parameters");

            BookingDetail bookingDetail = new BookingDetail(
                    parameter[1],
                    Integer.valueOf(parameter[2]),
                    LocalDate.parse(parameter[3], formatter));

            AddBookingStatus status = bookingInterface.addBookingDetail(sessionKey, bookingDetail);

            if (status == AddBookingStatus.OK)
                System.out.println("Added booking");
            else if (status == AddBookingStatus.DUPLICATE)
                System.out.println("Booking already exists");
            else if (status == AddBookingStatus.INVALID_ROOM)
                System.out.println("Invalid room");
            else if (status == AddBookingStatus.ERROR)
                System.out.println("Error adding booking");

        } catch (DateTimeException | IllegalArgumentException e) {
            logger.warning(e.getMessage());
        }
    }

    private void confirm() throws RemoteException {
        if (bookingInterface.bookAll(sessionKey))
        {
            System.out.println("Booking completed");
        } else {
            System.out.println("Booking failed");
        }
    }
}
