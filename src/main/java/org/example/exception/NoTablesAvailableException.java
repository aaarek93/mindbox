package org.example.exception;

import java.time.LocalDateTime;

public class NoTablesAvailableException extends RuntimeException {
    public NoTablesAvailableException(LocalDateTime localDateTime, int tableSize) {
        super(String.format("No free tables with size %s found for %s", tableSize, localDateTime));
    }
}
