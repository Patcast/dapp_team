package hotel;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BookingServer {

    private static final Logger logger = Logger.getLogger(BookingServer.class.getName());

    public static void main(String[] args) {
        BookingInterface server = new BookingManager();
        Registry registry = null;

        try {
            registry = LocateRegistry.createRegistry(8080);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Could not get RMI registry");
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            BookingInterface stub = (BookingInterface) UnicastRemoteObject.exportObject(server, 8081);
            registry.rebind("Hotel", stub);
            logger.log(Level.INFO, "Stub registered");
        } catch (RemoteException exception) {
            logger.log(Level.SEVERE, "Could not get the stub");
            exception.printStackTrace();
            System.exit(-1);
        }
    }
}
