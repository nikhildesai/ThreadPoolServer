package com.desainikhil.webserver;

import java.lang.reflect.Field;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FixedPoolWebServerTest extends TestCase {

    private FixedPoolWebServer fixedPoolWebServer;
    private ExecutorService executorService;
    private ServerSocket serverSocket;

    @Before
    public void setUp() throws NoSuchFieldException, SecurityException, IllegalArgumentException,
            IllegalAccessException {
        serverSocket = EasyMock.createMock(ServerSocket.class);
        executorService = EasyMock.createMock(ExecutorService.class);
        fixedPoolWebServer = new FixedPoolWebServer(8080, 5);
        
        // set fields using reflection
        Field executorServiceField = FixedPoolWebServer.class.getDeclaredField("executorService");
        executorServiceField.setAccessible(true);
        executorServiceField.set(fixedPoolWebServer, executorService);

        Field serverSocketField = FixedPoolWebServer.class.getDeclaredField("serverSocket");
        serverSocketField.setAccessible(true);
        serverSocketField.set(fixedPoolWebServer, serverSocket);
    }

    @Test
    public void testRun_notStartedState() {
        EasyMock.replay(serverSocket, executorService);
        fixedPoolWebServer.run();
        EasyMock.verify(serverSocket, executorService);
    }

    @Test
    public void testStart_alreadyStarted() throws Exception {
        Field serverSocketField = FixedPoolWebServer.class.getDeclaredField("serverStatus");
        serverSocketField.setAccessible(true);
        serverSocketField.set(fixedPoolWebServer, FixedPoolWebServer.ServerStatus.STARTED);
        
        try {
            fixedPoolWebServer.start();
            Assert.fail("Should have thrown exception");
        } catch (Exception e) {
            Assert.assertEquals("Server cannot be re-started", e.getMessage());
        }
    }

    @Test
    public void testStart_executorServiceNull() throws Exception {
        Field serverStatusField = FixedPoolWebServer.class.getDeclaredField("serverStatus");
        serverStatusField.setAccessible(true);
        serverStatusField.set(fixedPoolWebServer, FixedPoolWebServer.ServerStatus.NOT_STARTED);

        Field executorServiceField = FixedPoolWebServer.class.getDeclaredField("executorService");
        executorServiceField.setAccessible(true);
        executorServiceField.set(fixedPoolWebServer, null);

        try {
            fixedPoolWebServer.start();
            Assert.fail("Should have thrown exception");
        } catch (Exception e) {
            Assert.assertEquals("Server not initialized correctly", e.getMessage());
        }
    }

}