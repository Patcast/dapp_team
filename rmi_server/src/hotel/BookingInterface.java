package hotel;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BookingInterface extends Remote {
     int connect() throws RemoteException;
     List<Room> getRooms() throws RemoteException;
     String printRooms() throws RemoteException;
     String currentCart(Integer key) throws RemoteException;
     AddBookingStatus addBookingDetail(Integer key, BookingDetail bookingDetail) throws RemoteException;
     boolean bookAll(Integer key) throws RemoteException;
     void setNumberOfRooms(int noOfRooms) throws RemoteException;
}
