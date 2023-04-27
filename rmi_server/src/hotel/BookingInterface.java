package hotel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface BookingInterface extends Remote {
     int connect() throws RemoteException;
     String printRooms() throws RemoteException;
     String currentCart(Integer key) throws RemoteException;
     AddBookingStatus addBookingDetail(Integer key, BookingDetail bookingDetail) throws RemoteException;
     boolean bookAll(Integer key) throws RemoteException;;
}
