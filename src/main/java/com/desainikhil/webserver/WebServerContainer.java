package com.desainikhil.webserver;

import com.desainikhil.webserver.IServer;
import com.desainikhil.webserver.ServerFactory;

/**
 * Simple container/bootstrapping logic to start the server
 * 
 * @author Nikhil Desai
 * 
 */
public class WebServerContainer {

    public static void main(String[] args) throws Exception {
        final IServer server = ServerFactory.createServer(ServerType.FixedPoolServer, 8080, 5);
        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                server.stop();
            }
        });
    }
}
