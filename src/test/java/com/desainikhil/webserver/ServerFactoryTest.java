package com.desainikhil.webserver;

import org.junit.Assert;
import org.junit.Test;

public class ServerFactoryTest {

    @Test
    public void testNullType() {
        try {
            ServerFactory.createServer(null, 8080, 5);
            Assert.fail("Should have thrown exception");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("ServerType cannot be null", e.getMessage());
        }
    }

    @Test
    public void testFixedPoolServerType() {
        Assert.assertTrue(ServerFactory.createServer(ServerType.FixedPoolServer, 8080, 5) instanceof FixedPoolWebServer);
    }
}
