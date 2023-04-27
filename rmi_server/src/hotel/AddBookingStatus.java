package hotel;

import java.io.Serializable;

public enum AddBookingStatus implements Serializable {
    OK,
    DUPLICATE,
    INVALID_ROOM,
    ERROR
}
