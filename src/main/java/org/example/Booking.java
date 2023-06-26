package org.example;

import java.time.LocalDateTime;

public record Booking (String customerName, int tableSize, LocalDateTime localDateTime){

    public static Booking from(String customerName, int tableSize, String dateTime) {
        if (customerName == null || customerName.isBlank()) throw new BookingException("Invalid customer Name: " + customerName);
        if (tableSize < 0 || tableSize > 8) throw new BookingException("Invalid table size: " + tableSize);
        final LocalDateTime localDateTime;
        try {
            localDateTime = LocalDateTime.parse(dateTime);
        } catch (Exception e) {
            throw new BookingException("Invalid localDateTime format " + dateTime);
        }
        return new Booking(customerName, tableSize, localDateTime);
    }
}
