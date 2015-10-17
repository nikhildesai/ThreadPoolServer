package com.desainikhil.webserver;

import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class FixedPoolServerIntegrationTest {

    private IServer server;

    @Test
    public void testThreadPoolCount_upperLimit() throws Exception {
        server = ServerFactory.createServer(ServerType.FixedPoolServer, 8080, 5); // pool size 5
        server.start();

        // 10 requests
        for (int i = 0; i < 10; i++) {
            URL url = new URL("http://localhost:8080");
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            urlConnection.getContent();
        }

        // verify the active thread count is just 5 even if a greater number of requests have been made
        int activeThreadsInPool = getActiveThreadCount();
        Assert.assertEquals(5, activeThreadsInPool);
    }

    private int getActiveThreadCount() {
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        int activeThreadsInPool = 0;
        for (Thread t : threadSet) {
            if (t.getName().contains("pool")) {
                activeThreadsInPool++;
            }
        }
        return activeThreadsInPool;
    }

    @Test
    public void testThreadPoolCount_noActiveThreads() throws Exception {
        server = ServerFactory.createServer(ServerType.FixedPoolServer, 8001, 5);
        server.start();
        int activeThreadsInPool = getActiveThreadCount();
        Assert.assertEquals(0, activeThreadsInPool);
    }

    @Test
    public void testInvalidServerType() throws Exception {
        try {
            server = ServerFactory.createServer(null, 8004, 5); // null ServerType
            Assert.fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("ServerType cannot be null", e.getMessage());
        }
    }

    @Test
    public void testStoppingMoreThanOnce() throws Exception {
        server = ServerFactory.createServer(ServerType.FixedPoolServer, 8083, 3);
        server.start();
        server.stop();
        server.stop(); // should not throw exception
    }

    @Test
    public void testStartingAfterStopping() throws Exception {
        server = ServerFactory.createServer(ServerType.FixedPoolServer, 8084, 10);
        server.start();
        server.stop();

        try {
            server.start();
            Assert.fail("Should have thrown exception");
        } catch (Exception e) {
            Assert.assertEquals("Server cannot be re-started", e.getMessage());
        }
    }

    @Test
    public void testStartingTwice() throws Exception {
        server = ServerFactory.createServer(ServerType.FixedPoolServer, 8085, 5);
        server.start();
        try {
            server.start();
            Assert.fail("Should have thrown exception");
        } catch (Exception e) {
            Assert.assertEquals("Server cannot be re-started", e.getMessage());
        }
    }
}
