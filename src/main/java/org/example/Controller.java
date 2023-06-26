package org.example;

import io.muserver.Method;
import io.muserver.MuServer;
import io.muserver.MuServerBuilder;

public class Controller {

    public static void initEndpoints() {
        MuServer server = MuServerBuilder.httpServer()
                .addHandler(Method.POST, "/booking", (request, response, pathParams) -> {
                    String customerName = request.query().get("customerName");
                    int tableSize = request.query().getInt("tableSize", -1);
                    String dateTimeParam = request.query().get("dateTime");
                    Booking booking = Booking.from(customerName, tableSize, dateTimeParam);
                    System.out.println(booking);
                    response.write(booking.toString());
                })
                .withHttpPort(5555)
                .start();
    }

}
