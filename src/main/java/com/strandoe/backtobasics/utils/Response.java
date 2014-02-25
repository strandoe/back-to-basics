package com.strandoe.backtobasics.utils;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class Response {

    private static final String locationHeaderName = "Location";
    private static final String internalServerErrorMessage = "Internal server error";

    public static void sendString(HttpExchange t, int httpcode, String s) {
        byte[] r = s.getBytes();
        try {
            t.sendResponseHeaders(httpcode, r.length);
            OutputStream output = t.getResponseBody();
            output.write(r);
            output.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static interface Delegate {
        void process(HttpExchange httpExchange) throws IOException;
    }

    public static void wrapWithExceptionHandling(Delegate delegate, HttpExchange httpExchange) {
        try {
            delegate.process(httpExchange);
        } catch (Exception e) {
            e.printStackTrace();
            sendString(httpExchange, 500, internalServerErrorMessage);
        }
    }

    public static void addLocation(HttpExchange t, String location) {
        List<String> locations = t.getResponseHeaders().get(locationHeaderName);
        locations.add(location);
    }
}
