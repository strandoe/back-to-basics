package com.strandoe.backtobasics.utils;

import com.google.common.io.CharStreams;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Request {

    public static String bodyAsString(HttpExchange t) {
        InputStream requestBodyStream = t.getRequestBody();
        String requestBody;
        try {
            requestBody = CharStreams.toString(new InputStreamReader(requestBodyStream, "UTF-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return requestBody;
    }

    public enum Type {
        GET, POST;
    }

    public static boolean is(HttpExchange t, Type type) {
        return t.getRequestMethod().equalsIgnoreCase(type.name());
    }
}
