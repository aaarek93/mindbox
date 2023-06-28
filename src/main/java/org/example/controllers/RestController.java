package org.example.controllers;

import io.muserver.Method;
import io.muserver.MuServerBuilder;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.exception.ExceptionHandler;
import org.example.model.Booking;
import org.example.repository.TableDetails;
import org.example.services.BookingService;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
@Slf4j
public class RestController {

    private final BookingService bookingService;

    @PostConstruct
    public void init() {
        bookingService.prepareTables(LocalDate.now(), LocalDate.now().plusDays(10));
        initEndpoints();
    }

    public void initEndpoints() {
        MuServerBuilder.httpServer()
                .addHandler(Method.POST, "/booking", (request, response, pathParams) -> {
                    log.debug("Request is: {}", request);
                    Booking booking = Booking.from(request.query());
                    TableDetails table = bookingService.reserve(booking);
                    String msg = String.format("You got your reservation. Your table is: %s", table);
                    log.debug("Response is: {}", msg);
                    response.write(msg);
                    response.status(200);
                })
                .withHttpPort(5555)
                .withExceptionHandler(new ExceptionHandler())
                .start();
    }

}
