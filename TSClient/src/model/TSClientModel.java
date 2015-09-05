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
package model;

import java.io.EOFException;
import java.io.IOException;
import java.util.Observable;

//import javax.swing.JOptionPane;

import controlmessages.MessageTypes;

import decode.DecodeMessage;
import encode.EncodeMessage;

import network.TSClient;

/**
 * Three Stones Client Model Class Serves as the encoder for every move that is
 * done Sends move to the server for further processing Accepts a move from the
 * server and notify the observers for sudden change.
 * 
 * @author Francis Ortega, A635364
 * @author Tony Belanger, A631360
 * @version October 26, 2008
 */

public class TSClientModel extends Observable {
	// Class variable declarations
	private TSClient tsClient = null;

	/**
	 * Class Constructor for the Client Model
	 * 
	 * @param serverPort
	 */
	public TSClientModel(String serverIP, int serverPort) {
		super();
		// create a new instance of TSClient
		tsClient = new TSClient(serverIP, serverPort);
		try {
			tsClient.runClient();
		} catch (EOFException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * This method sends the coordinates received by the user to the server and
	 * updates the board when it received the server's move.
	 * 
	 * @param userRow
	 * @param userCol
	 */
	public void setReceivedCoordinates(int uRow, int uCol) {
		// JOptionPane.showMessageDialog(null,
		// "Model: has received coordinates: " + uRow + " " + uCol);
		byte[] coordinates = EncodeMessage.createNewUserMoveMsg(uRow, uCol);
		// JOptionPane.showMessageDialog(null,
		// "Model: coordinates have been encoded: " + coordinates[1] + " " +
		// coordinates[2]);
		tsClient.sendDataToServer(coordinates);
		int[] updatedData = null;
		int[] dataForDisplay = null;
		try {
			byte[] byteBuffer = tsClient.getDataFromServer();
			if (byteBuffer.length > 0) {
				if (byteBuffer[0] == MessageTypes.GAME_OVER) {
					//JOptionPane.showMessageDialog(null,
					//		"Model received message of type game over.");
					updatedData = DecodeMessage
							.decodeGameOverDataMsg(byteBuffer);
					//JOptionPane.showMessageDialog(null, "Game Over");
					dataForDisplay = new int[] { updatedData[0],
							updatedData[1], updatedData[2], updatedData[3],
							updatedData[4], uRow, uCol };
					setChanged();
					notifyObservers(dataForDisplay);
				} else if (byteBuffer[0] == MessageTypes.UPDATED_DATA_FROM_SERVER) {
					//JOptionPane.showMessageDialog(null,
					//		"Model received message of type UPDATED_DATA.");
					updatedData = DecodeMessage
							.decodeUpdatedDataMsg(byteBuffer);
					dataForDisplay = new int[] { updatedData[0],
							updatedData[1], updatedData[2], updatedData[3],
							updatedData[4], uRow, uCol };
					setChanged();
					notifyObservers(dataForDisplay);
				} else if (byteBuffer[0] == MessageTypes.INVALID_MOVE) {
				//	JOptionPane
				//			.showMessageDialog(null, "Your move is invalid.");
				} else {
					System.out.println("Message was not recognized. Ignored.");
				}
			}
		} catch (ClassNotFoundException e) {
			//JOptionPane.showMessageDialog(null, "Something went wrong!");
			e.printStackTrace();
		}
	}

	/**
	 * This method sends a request to the server for a new game.
	 */
	public void startNewGame() {
		// create new message of type START_NEW_GAME
		byte[] startNewGameMsg = EncodeMessage.createNewStartNewGameMsg();
		tsClient.sendDataToServer(startNewGameMsg);
	}

	/**
	 * This method closes the connection.
	 */
	public void exit() {
		// create new message to type TERMINATE_CONNECTION
		byte[] terminateConnectionMsg = EncodeMessage
				.createNewTerminateConnectionMsg();
		tsClient.sendDataToServer(terminateConnectionMsg);
		tsClient.closeConnection();
	}
}
