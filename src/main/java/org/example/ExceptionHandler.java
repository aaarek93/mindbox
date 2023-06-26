package org.example;

import io.muserver.MuRequest;
import io.muserver.MuResponse;
import io.muserver.UnhandledExceptionHandler;

public class ExceptionHandler implements UnhandledExceptionHandler {

    @Override
    public boolean handle(MuRequest muRequest, MuResponse muResponse, Throwable throwable) throws Exception {
        if (throwable instanceof BookingException) {
            muResponse.write("You got BookingException because of: " + throwable.getMessage());
            return true;
        } else {
            return false;
        }
    }
}
