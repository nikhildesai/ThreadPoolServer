package com.desainikhil.webserver;

/**
 * Factory to create server implementations
 * 
 * @author Nikhil Desai
 * 
 */
public class ServerFactory {

	public static IServer createServer(ServerType type, int port, int poolSize) {
		if (type == null) {
			throw new IllegalArgumentException("ServerType cannot be null");
		} else if (type.equals(ServerType.FixedPoolServer)) {
			return new FixedPoolWebServer(port, poolSize);
		} // other implementations can be added for different ServerTypes
		return null; 
	}

}
