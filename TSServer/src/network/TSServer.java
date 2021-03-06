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

import decode.DecodeMessage;
import encode.EncodeMessage;
import game.TSServerSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JOptionPane;

//import javax.swing.JOptionPane;

import controlmessages.MessageTypes;

/**
 * This class accepts new connections from clients. The communication between
 * the client and the server is done through a protocol called TSProtocol. This
 * class ensures that packets that arrive from the client are compliant with the
 * TSProtocol. It also notices the client of the different events happening
 * during the game. If the client plays an invalid move, TSSession will receive
 * a message from TSServerSession and will forward it to the client through the
 * TSProtocol.
 * 
 * @author Tony Belanger, A631360
 * @author Francis Ortega, A635364
 * @version 10.24.2008
 */
public class TSServer implements Runnable {
	/**
	 * Instance variables.
	 */
	private ServerSocket serverSocket = null; // server socket
	private Socket clientSocket = null; // client socket
	private OutputStream outputToClient; // Output stream to client
	private InputStream inputFromClient; // Input stream from client
	private TSServerSession serverSession; // server session
	private int BUFFER_SIZE = MessageTypes.CONTROL_MESSAGE_SIZE;
	private boolean gameInProgress = false;

	/**
	 * Construction method.
	 * 
	 * @param clntSock
	 *            reference to socket the port at which the server will run.
	 * @param servSock
	 * @throws IOException
	 */

	public TSServer(Socket clntSock, ServerSocket servSock) {
		// create reference to server socket
		this.serverSocket = servSock;
		// create reference to client socket
		this.clientSocket = clntSock;
	}

	/**
	 * Closes current connection.
	 */
	private void closeConnection() {
		displayMessage("\nTerminating connection.");
		try {
			outputToClient.close(); // Close output stream
			inputFromClient.close(); // Close input stream
			clientSocket.close(); // Close client socket
		} // End try
		catch (IOException ioe) {
			ioe.printStackTrace();
		} // End catch
	} // End closeConnection

	/**
	 * Displays a message in the console.
	 * 
	 * @param string
	 */
	private void displayMessage(String string) {
		System.out.println(string);
	}

	/**
	 * This method processes a connection.
	 */
	private void processConnection() {
		int recvMsgSize; // Size of received message
		byte[] byteBuffer = new byte[BUFFER_SIZE]; // Receive buffer
		// Run forever, accepting and servicing connections
		for (;;) {
			try {
				inputFromClient.read(byteBuffer);
				System.out.println("Data received from client: ");
				// get message header
				if (byteBuffer.length == MessageTypes.CONTROL_MESSAGE_SIZE) {
					byte messageHeader = DecodeMessage
							.getMessageHeader(byteBuffer);
					System.out.println("Message header is " + messageHeader);
					switch (messageHeader) {
					case MessageTypes.START_NEW_GAME:
						System.out.println("Message of type START NEW GAME");
						serverSession = new TSServerSession(); // reset game
						gameInProgress = true;
						break;
					case MessageTypes.USER_MOVE:
						System.out.println("Message of type USER MOVE");
						if (gameInProgress) {
							int[] userMove = DecodeMessage
									.decodeUserMove(byteBuffer);
							/**
							 * DEBUG
							 */
							String str = "Line 135; function updateClientData(userMove[0], userMove[1]"
									+ "\n"
									+ "userMove[0]: "
									+ userMove[0]
									+ "\n" + "userMove[1]: " + userMove[1];
							//JOptionPane.showMessageDialog(null, str);
							updateClientData(userMove[0], userMove[1]);
						}
						break;
					case MessageTypes.TERMINATE_CONNECTION:
						System.out
								.println("Message of type TERMINATE CONNECTION");
						closeConnection();
						break;
					default:
						System.out.println("Message was ignored.");
						break;
					}
				} else {
					System.out.println("Message ignored, wrong size.");
				}
			} catch (IOException e) {
				System.out.println("Cannot process connection.");
			}
		}
	}

	/**
	 * This method plays the user's move and returns an array that contains the
	 * most current data about the game being played.
	 * 
	 * @param uRow
	 *            user row
	 * @param uCol
	 *            user column
	 */
	private void updateClientData(int uRow, int uCol) {
		// JOptionPane.showMessageDialog(null, "In updateClientData");
		int[] updatedData = serverSession.performMove(uRow, uCol);
		// JOptionPane.showMessageDialog(null, "updatedData is "
		// + updatedData.length + " and contains: " + updatedData[0]);
		byte[] updatedDataMessage = null;
		if (updatedData[0] == -1) {
			// JOptionPane
			// .showMessageDialog(null,
			// "Message wasn't valid, creating message of type INVALID_MOVE.");
			updatedDataMessage = new byte[] { MessageTypes.INVALID_MOVE };
		} else if (updatedData[4] == 0) {
			// JOptionPane.showMessageDialog(null,
			// "Message of type GAME_OVER will be created");
			updatedDataMessage = EncodeMessage.createNewGameOverMsg(
					updatedData[0], updatedData[1], updatedData[2],
					updatedData[3], updatedData[4]);
			closeConnection();

		} else {
			// JOptionPane
			// .showMessageDialog(null,
			// "Message seems fine, creating message of type UPDATED_DATA.");
			updatedDataMessage = EncodeMessage.createNewUpdatedDataMsg(
					updatedData[0], updatedData[1], updatedData[2],
					updatedData[3], updatedData[4]);
		}
		sendDataToClient(updatedDataMessage);
	}

	/**
	 * This method converts data into an array of bytes and sends it off to the
	 * client.
	 */
	private void sendDataToClient(byte[] byteBuffer) {
		try {
			String str = null;
			for (int i = 0; i < byteBuffer.length; i++)
				str += byteBuffer[i];
			JOptionPane.showMessageDialog(null, "Line 203: " + "\n"
					+ "Buffer contains: " + str + "\n" + "and has "
					+ byteBuffer.length + " bytes.");
			outputToClient.write(byteBuffer);
			displayMessage("Data has been sent to client.");
		} // End try
		catch (IOException ioe) {
			displayMessage("Error writing to socket");
		} // End catch
	} // End method sendData

	/**
	 * Gets streams to send and receive data.
	 */
	private void getStreams() {
		// Set output stream for data
		try {
			outputToClient = clientSocket.getOutputStream();
			outputToClient.flush();
			// Set input stream for data
			inputFromClient = clientSocket.getInputStream();
			displayMessage("Got I/O streams from client!\n");
		} catch (IOException e) {
			System.out.println("Couldn't get I/O streams.");
			e.printStackTrace();
		}
	}

	/**
	 * This method is executed whenever a new Thread gets spun off.
	 */
	public void run() {
		// get i/o streams
		getStreams();
		processConnection();
	}
}
