package hotel;

import java.io.Serializable;
import java.time.LocalDate;

public class BookingDetail implements Serializable {

	private String guest;
	private Integer roomNumber;
	private LocalDate date;

	public BookingDetail(String guest, Integer roomNumber, LocalDate date) {
		this.guest = guest;
		this.roomNumber = roomNumber;
		this.date = date;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getGuest() {
		return guest;
	}

	public void setGuest(String guest) {
		this.guest = guest;
	}

	public Integer getRoomNumber() {
		return roomNumber;
	}

	public void setRoomNumber(Integer roomNumber) {
		this.roomNumber = roomNumber;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(getGuest());
		buffer.append(" - ");
		buffer.append(getRoomNumber());
		buffer.append(" - ");
		LocalDate date = getDate();
		buffer.append(date.getDayOfMonth()).append("/");
		buffer.append(date.getMonthValue()).append("/");
		buffer.append(date.getYear()).append("\n");
		
		return buffer.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BookingDetail other)
		{
			return	other.getRoomNumber().equals(this.getRoomNumber()) &&
					other.getDate().equals(this.getDate());
		}
		else return false;
	}

	@Override
	public int hashCode() {
		return roomNumber * date.hashCode();
	}
}
