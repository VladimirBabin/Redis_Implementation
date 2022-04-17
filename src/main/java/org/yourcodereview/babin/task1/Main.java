package org.yourcodereview.babin.task1;

public class Main {
    public static void main(String[] args) {
        RedisHttpServer httpServer = new RedisHttpServer();
        try {
            httpServer.configureAndStartServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
