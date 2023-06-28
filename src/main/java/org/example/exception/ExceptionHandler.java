package org.example.exception;

import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.UnhandledExceptionHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExceptionHandler implements UnhandledExceptionHandler {

    @Override
    public boolean handle(MuRequest muRequest, MuResponse muResponse, Throwable throwable) throws Exception {
        String msg = null;
        if (throwable instanceof BookingException) {
            msg = "You got BookingException because of: " + throwable.getMessage();
        } else if (throwable instanceof NoTablesAvailableException) {
            msg = "You're late, all reservations are taken: " + throwable.getMessage();
        } else if (throwable instanceof NoReservationsForDateException) {
            msg = "You cannot make reservation: " + throwable.getMessage();
        } else {
            muResponse.status(500);
        }
        if (msg == null) {
            msg = "Unexpected problem occurred";
            log.debug(msg);
            muResponse.write(msg);
            muResponse.status(500);
            return false;
        } else {
            log.debug(msg);
            muResponse.write(msg);
            muResponse.status(400);
            return true;
        }
    }
}
