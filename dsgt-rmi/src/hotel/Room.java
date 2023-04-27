package hotel;

import staff.SessionClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Room {

	private static final Logger logger = Logger.getLogger(SessionClient.class.getName());

	private Integer roomNumber;
	private List<BookingDetail> bookings;

	public Room(Integer roomNumber) {
		this.roomNumber = roomNumber;
		bookings = new ArrayList<>();
	}

	public synchronized Integer getRoomNumber() {
		return roomNumber;
	}

	public synchronized void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	public synchronized List<BookingDetail> getBookings() {
		return bookings;
	}

	public synchronized void setBookings(List<BookingDetail> bookings) {
		this.bookings = bookings;
	}

	public synchronized void addBooking(BookingDetail bookingDetail) {
		logger.log(Level.INFO, "Adding booking to room {0}", roomNumber);

		bookings.add(bookingDetail);
		String message = String.format(
				Locale.ENGLISH,"Booking for %s added to room %d",
				bookingDetail.getGuest(), roomNumber);
		logger.log(Level.INFO, message);
	}

	public synchronized boolean available(LocalDate date) {
		for (BookingDetail bookingDetail : getBookings()) {
			if (bookingDetail.getDate().equals(date))
				return false;
		}
		return true;
	}

	@Override
	public synchronized String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("Number = ").append(roomNumber).append("\n");

		if (!bookings.isEmpty()) {
			buffer.append("\tBookings:\n");

			for (BookingDetail booking : bookings) {
				buffer.append("\t");
				buffer.append(booking.toString());
			}
		}

		return buffer.toString();
	}
}