/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation; version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package network;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

//import javax.swing.JOptionPane;

//import javax.swing.JOptionPane;

import controlmessages.MessageTypes;

/**
 * TSClient - Bridge between user interface and server.
 * 
 * @author Francis Ortega, A635364
 * @author Tony Belanger, A631360
 * @version October 26, 2008
 */
public class TSClient {

	/**
	 * Instance variables
	 */
	private OutputStream outputToServer;
	private InputStream inputFromServer;
	private Socket clientSocket;
	private int serverPort;
	private String serverIp;
	private int BUFFER_SIZE = MessageTypes.CONTROL_MESSAGE_SIZE;

	public TSClient(String serverIp, int serverPort) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}

	public void runClient() throws EOFException, IOException {
		connectToServer(); // Create a Socket to make connection
		getStreams(); // Get the input and output streams
	}

	/**
	 * Connects to server.
	 */
	private void connectToServer() throws IOException {
		displayMessage("Attempting connection\n");

		// Create socket to make connection to server.
		clientSocket = new Socket(InetAddress.getByName(serverIp), serverPort);

		// Display connection information
		displayMessage("Connected to: "
				+ clientSocket.getInetAddress().getHostName());
	} // End method connectToServer.

	/**
	 * Gets streams to send and received data.
	 * 
	 * @throws IOException
	 */
	private void getStreams() throws IOException {
		// Set up output streams for data.
		outputToServer = clientSocket.getOutputStream();
		outputToServer.flush(); // Flush output buffer to send header
		// information

		// Set up input stream for objects
		inputFromServer = clientSocket.getInputStream();

		displayMessage("\nGot I/O streams!\n");
	} // End method getStreams

	/**
	 * This method converts data into an array of bytes and sends it off to the
	 * server.
	 */
	public void sendDataToServer(byte[] byteBuffer) {
		try {
			String dataToSend = "";
			for(int i=0;i<byteBuffer.length;i++)
				dataToSend += byteBuffer[i];
		//	JOptionPane.showMessageDialog(null, "TSClient: sending data to server: " + dataToSend);
			outputToServer.write(byteBuffer);
			displayMessage("Data has been sent to server.");
		} // End try
		catch (IOException ioe) {
			displayMessage("Error writing to socket");
		} // End catch
	} // End method sendData

	/**
	 * Get data from server
	 * 
	 * @throws ClassNotFoundException
	 */
	public byte[] getDataFromServer() throws ClassNotFoundException {
		byte[] byteBuffer = new byte[BUFFER_SIZE]; // Receive buffer
		try {
			//JOptionPane.showMessageDialog(null, "TSClient: retrieving data from server");
			inputFromServer.read(byteBuffer);
			//JOptionPane.showMessageDialog(null, size);
			String str = "";
			for(int i=0;i<byteBuffer.length;i++)
				str += byteBuffer[i];
			//JOptionPane.showMessageDialog(null, "TSClient: data from server contains: " + str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Data received from server: ");
		return byteBuffer;
	}// End method sendData

	/**
	 * Displays messages in console.
	 * 
	 * @param string
	 *            the message to display.
	 */
	private void displayMessage(String string) {
		System.out.println(string);
	}

	/**
	 * This method terminates a connection.
	 */
	public void closeConnection() {
		displayMessage("\nTerminating connection.");
		try {
			outputToServer.close(); // Close output stream
			inputFromServer.close(); // Close input stream
			clientSocket.close(); // Close client socket
		} // End try
		catch (IOException ioe) {
			ioe.printStackTrace();
		} // End catch
	}
}
