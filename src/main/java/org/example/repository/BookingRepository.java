package org.example.repository;

import org.example.exception.NoReservationsForDateException;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Repository
public class BookingRepository {

    public static final int minHour = 12;
    public static final int maxHour = 20;
    public static final int minTableSize = 4;
    public static final int maxTableSize = 12;

    private final Map<LocalDateTime, Set<TableDetails>> dateTimeToFreeTables = new HashMap<>();
    private final Map<LocalDateTime, Set<TableAssignment>> dateTimeToTableAssignments = new HashMap<>();

    private static Optional<TableDetails> findSmallestFreeTableBiggerThanTableSize(
            Collection<TableDetails> tables,
            int minTableSize
    ) {
        TableDetails optimalTable = null;
        for (TableDetails table : tables) {
            int nextTableSize = table.tableSize();
            if (nextTableSize < minTableSize) continue;
            if (optimalTable == null || nextTableSize < optimalTable.tableSize()) {
                optimalTable = table;
            }
        }
        return Optional.ofNullable(optimalTable);
    }

    private static Set<TableDetails> makeSetOfTables() {
        Set<TableDetails> tables = new HashSet<>();
        for (int i = 0; i < maxTableSize - 2; i += 2) {
            tables.add(new TableDetails(i, minTableSize + i));
            tables.add(new TableDetails(i + 1, minTableSize + i));
        }
        return tables;
    }

    public Optional<TableDetails> reserve(LocalDateTime localDateTime, String clientName, int tableSize) {
        Set<TableDetails> tables = dateTimeToFreeTables.get(localDateTime);
        if (tables == null) throw new NoReservationsForDateException(localDateTime);
        Optional<TableDetails> optionalTable = findSmallestFreeTableBiggerThanTableSize(tables, tableSize);
        optionalTable.ifPresent(table -> {
            TableAssignment tableAssignment = new TableAssignment(table, clientName);
            dateTimeToTableAssignments.computeIfAbsent(localDateTime, key -> new HashSet<>());
            dateTimeToTableAssignments.get(localDateTime).add(tableAssignment);
            dateTimeToFreeTables.get(localDateTime).remove(table);
        });
        return optionalTable;
    }

    public void prepareTablesFor(LocalDate localDate) {
        for (int hour = minHour; hour <= maxHour; hour += 2) {
            LocalDateTime localDateTime = LocalDateTime.of(localDate, LocalTime.of(hour, 0));
            dateTimeToFreeTables.put(localDateTime, makeSetOfTables());
        }
    }

}
