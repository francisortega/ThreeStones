package network;

import java.net.*; // for Socket, ServerSocket, and InetAddress
import java.io.*; // for IOException and Input/OutputStream

/**
 * This is an alternative version of the Server class. It has the ability to
 * spawn off multiple threads.
 * 
 * @author Tony Belanger, A631360
 * @author Francis Ortega, A635364
 * @version 11.20.2008
 */
public class TSServerAlternative {
	private final static int PORT_NUMBER = 50000;

	public static void main(String[] args) throws IOException {

		// Create a server socket to accept client connection requests
		ServerSocket servSock = new ServerSocket(PORT_NUMBER);

		// Run forever, accepting and spawning threads to service each
		// connection
		for (;;) {
			try {
				Socket clntSock = servSock.accept(); // Block waiting for
				// connection
				TSServer serverInstance = new TSServer(clntSock, servSock);
				Thread thread = new Thread(serverInstance);
				thread.start();
				System.out.println("Created and started Thread = "
						+ thread.getName());
			} catch (IOException e) {
				System.out.println("Exception = " + e.getMessage());
			}
		}
		/* NOT REACHED */
	}
}
