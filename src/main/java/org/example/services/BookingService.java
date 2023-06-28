package org.example.services;

import lombok.RequiredArgsConstructor;
import org.example.exception.BookingException;
import org.example.exception.NoTablesAvailableException;
import org.example.model.Booking;
import org.example.repository.BookingRepository;
import org.example.repository.TableDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

import static org.example.repository.BookingRepository.*;

@Component
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;

    public TableDetails reserve(Booking booking) {
        final String customerName = booking.customerName();
        if (customerName == null || customerName.isBlank()) {
            throw new BookingException("Invalid customer Name: " + customerName);
        }
        final int tableSize = booking.tableSize();
        if (tableSize < minTableSize || tableSize > maxTableSize) {
            throw new BookingException("Invalid table size: " + tableSize);
        }
        final int hour = booking.hour();
        if (hour < minHour || hour > maxHour || hour % 2 != 0) {
            throw new BookingException("Invalid reservation time: " + hour);
        }
        final LocalDate localDate = booking.localDate();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(hour, 0));
        Optional<TableDetails> tableAssignment = bookingRepository.reserve(localDateTime, customerName, tableSize);
        if (tableAssignment.isPresent()) {
            return tableAssignment.get();
        } else {
            throw new NoTablesAvailableException(localDateTime, tableSize);
        }
    }

    /*
     * Method should be invoked to prepare tables for given range of days.
     * After that reservation may be done for these days.
     * */
    public void prepareTables(LocalDate minDate, LocalDate maxDate) {
        for (LocalDate date = minDate; date.isBefore(maxDate); date = date.plusDays(1)) {
            bookingRepository.prepareTablesFor(date);
        }
    }
}
