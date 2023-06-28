package org.example.model;

import io.muserver.RequestParameters;
import org.example.exception.BookingException;

import java.time.LocalDate;

public record Booking(String customerName, int tableSize, LocalDate localDate, int hour) {

    public static Booking from(RequestParameters requestParameters) {
        String customerNameParam = requestParameters.get("customerName");
        int tableSize = requestParameters.getInt("tableSize", -1);
        String dateParam = requestParameters.get("date");
        int hour = requestParameters.getInt("hour", -1);
        final LocalDate localDate;
        try {
            localDate = LocalDate.parse(dateParam);
        } catch (Exception e) {
            throw new BookingException("Invalid localDate format " + dateParam);
        }
        return new Booking(customerNameParam, tableSize, localDate, hour);
    }

}
