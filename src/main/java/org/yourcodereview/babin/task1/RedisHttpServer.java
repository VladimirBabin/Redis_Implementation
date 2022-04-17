package org.yourcodereview.babin.task1;

import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class RedisHttpServer {
    ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(10);
    public final int PORT_NUMBER = 8888;
    public final String START_CONTEXT = "/redis";


    public void configureAndStartServer() throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress("localhost", PORT_NUMBER), 0);
        server.createContext(START_CONTEXT, new RedisHttpHandler());
        server.setExecutor(threadPoolExecutor);
        System.out.println("Server started at port 8888");
        server.start();
    }
}
