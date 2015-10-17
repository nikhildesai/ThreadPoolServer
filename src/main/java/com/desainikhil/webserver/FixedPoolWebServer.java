package com.desainikhil.webserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Socket server implementation that uses a fixed-size thread pool
 * 
 * @author Nikhil Desai
 * 
 */
public class FixedPoolWebServer implements Runnable, IServer {
    private int port = 3000; // default port
    private ServerSocket serverSocket;
    private ExecutorService executorService;
    private ServerStatus serverStatus = ServerStatus.NOT_STARTED;

    public FixedPoolWebServer(int port, int poolSize) {
        this.port = port;
        this.executorService = Executors.newFixedThreadPool(poolSize);
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
        }

        while (serverStatus == ServerStatus.STARTED) {
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                if (serverStatus != ServerStatus.STARTED) {
                    return;
                } else {
                    e.printStackTrace();
                }
            }
            executorService.execute(new Worker(clientSocket));
        }
    }

    public synchronized void stop() {
        serverStatus = ServerStatus.STOPPED;
        executorService.shutdown();
    }

    @Override
    public synchronized void start() throws Exception {
        if (serverStatus != ServerStatus.NOT_STARTED) {
            throw new Exception("Server cannot be re-started");
        }

        if (port == 0 || executorService == null) {
            throw new Exception("Server not initialized correctly");
        }

        new Thread(this).start();
        serverStatus = ServerStatus.STARTED;
        System.out.println("Server started on port: " + port);
    }

    public enum ServerStatus {
        NOT_STARTED, STARTED, STOPPED
    }
}
