package com.desainikhil.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

public class WorkerTest {
    private Worker worker;
    private Socket socket;
    private OutputStream outputStream;

    @Before
    public void setUp() {
        socket = EasyMock.createMock(Socket.class);
        worker = new Worker(socket);
        outputStream = EasyMock.createMock(OutputStream.class);
    }

    @Test
    public void testRun() throws IOException {
        EasyMock.expect(socket.getOutputStream()).andStubReturn(outputStream);
        outputStream.close();
        outputStream.write(EasyMock.isA((byte [].class)));
        EasyMock.replay(socket, outputStream);
        worker.run();
        EasyMock.verify(socket, outputStream);
    }
    
    @Test
    public void testRun_socket_IOException() throws IOException {
        EasyMock.expect(socket.getOutputStream()).andThrow(new IOException());
        EasyMock.replay(socket, outputStream);
        worker.run(); // no exception (silent failure)
        EasyMock.verify(socket, outputStream);
    }
    
    @Test
    public void testRun_outputStream_IOException() throws IOException {
        EasyMock.expect(socket.getOutputStream()).andStubReturn(outputStream);
        outputStream.write(EasyMock.isA((byte [].class)));        
        EasyMock.expectLastCall().andThrow(new IOException());
        outputStream.close();
        EasyMock.replay(socket, outputStream);
        worker.run();
        EasyMock.verify(socket, outputStream);
    }
}
