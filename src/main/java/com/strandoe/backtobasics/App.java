package com.strandoe.backtobasics;

import com.asual.lesscss.LessEngine;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.strandoe.backtobasics.utils.Request;
import com.strandoe.backtobasics.utils.Response;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

public class App {

    private static final Gson gson = new Gson();

    public static void main(String[] args) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(8089), 8089);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HttpContext context = server.createContext("/", new MyHandler());

        context.getFilters().add(new ParameterFilter());
        server.createContext("/css/", new CssHandler());
        server.createContext("/api/", new ApiHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
    }

    private static class ApiHandler implements HttpHandler, Response.Delegate {

        @Override
        public void handle(HttpExchange t) throws IOException {
            Response.wrapWithExceptionHandling(this, t);
        }

        @Override
        public void process(HttpExchange t) throws IOException {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Request.is(t, Request.Type.POST)) {
                java.lang.reflect.Type stringMapType = new TypeToken<Map<String, Object>>() {}.getType();
                Map map = gson.fromJson(Request.bodyAsString(t), stringMapType);
                Map<String, Object> responseMap = new HashMap<String, Object>();
                responseMap.put("svar", "Hei du også");
                Response.sendString(t, 201, gson.toJson(responseMap, stringMapType));
            }
            else {
                Response.sendString(t, 405, "");
            }
        }
    }

    public static class MyHandler implements HttpHandler {

        private Map<String, Object> map = Entry.map(
                Entry.e("errors", Entry.map()),
                Entry.e("text", "åååååaaøøøø")
        );

        @Override
        public void handle(HttpExchange t) throws IOException {
            if (Request.is(t, Request.Type.POST)) {
                Map<String, Object> params = (Map<String, Object>) t.getAttribute("parameters");
                Map<String, List<String>> errors = MyFormValidator.validate(params);
                map.put("text", params.get("text"));
                if (errors.size() == 0) {
                    map.put("errors", null);
                    Response.addLocation(t, "/");
                    Response.sendString(t, 302, "");
                } else {
                    map.put("errors", errors);
                    Response.sendString(t, 200, Templates.form.apply(map));
                }
            } else {
                Response.sendString(t, 200, Templates.form.apply(map));
            }
        }
    }

    public static class CssHandler implements HttpHandler {

        private LessEngine lessEngine = new LessEngine();

        @Override
        public void handle(HttpExchange t) throws IOException {
            URI requestURI = t.getRequestURI();
            String path = requestURI.getPath();
            int i = path.lastIndexOf("/");
            String filename = path.substring(i + 1);
            String compiled;
            try {
                compiled = lessEngine.compile(new File(getClass().getResource(filename).toURI()), true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            Response.sendString(t, 200, compiled);
        }
    }

    private static class MyFormValidator {
        public static Map<String, List<String>> validate(Map<String, Object> input) {
            Map<String, List<String>> errors = new HashMap<String, List<String>>();
            if (((String)input.get("text")).length() < 4) {
                errors.put("text", Arrays.asList("too-short"));
            }
            return errors;
        }
    }
}
