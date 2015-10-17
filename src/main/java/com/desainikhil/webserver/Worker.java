package com.desainikhil.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * Simple worker that returns 200 OK. In theory, this could be any logic e.g. an
 * HTTP request to get some data from a different service etc.
 * 
 * @author Nikhil Desai
 * 
 */
public class Worker implements Runnable {

    private Socket clientSocket;

    public Worker(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        OutputStream outputStream = null;
        try {
            outputStream = clientSocket.getOutputStream();
            String content = "Success message\n\n";

            StringBuilder outputStringBuilder = new StringBuilder();
            outputStringBuilder.append("HTTP/1.1 200 OK\n");
            outputStringBuilder.append("Content-Type: text/html\n");
            outputStringBuilder.append("Date: ").append(new Date()).append("\n");
            outputStringBuilder.append("Connection: keep-alive\n");
            outputStringBuilder.append("Server: Nikhil's awesome server\n");
            outputStringBuilder.append("Content-Length: ").append(content.length()).append("\n\n");
            outputStringBuilder.append(content);

            outputStream.write(outputStringBuilder.toString().getBytes());
        } catch (IOException e) {
          // log error  
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
