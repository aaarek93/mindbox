package org.example.exception;

import java.time.LocalDateTime;

public class NoReservationsForDateException extends RuntimeException {
    public NoReservationsForDateException(LocalDateTime localDateTime) {
        super(String.format("We don't make reservation for given date: %s", localDateTime));
    }
}
