package encode;

//import javax.swing.JOptionPane;

import controlmessages.MessageTypes;

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

/**
 * Creates a message that is compliant with the TSProtocol. Messages created
 * using EncodeMessage can be interpreted using DecodeMessage.
 * 
 * @author Tony Belanger, A631360
 * @author Francis Ortega, A635364
 * @version 10.21.2008
 */
public class EncodeMessage {
	/**
	 * The server uses this method to create a message that containts the
	 * updated data to send to the client.
	 * 
	 * @param cRow
	 *            computer row
	 * @param cCol
	 *            computer column
	 * @param uScore
	 *            user's score
	 * @param cScore
	 *            computer's score
	 * @param numOfMovesLeft
	 *            number of moves left for the user
	 * @return the updated data + header
	 */
	public static byte[] createNewUpdatedDataMsg(int cRow, int cCol,
			int uScore, int cScore, int numOfMovesLeft) {
		// JOptionPane.showMessageDialog(null, "In EncodeMessage...");
		/**
		 * validate data before creating message
		 */

		// check if row & col are not greater than 7 or lower than 0
		if (cRow < 0 || cCol < 0)
			return null;
		else if (cRow > 7 || cCol > 7)
			return null;
		else if (uScore < 0 || uScore > MessageTypes.MAX_SCORE)
			return null;
		else if (cScore < 0 || cScore > MessageTypes.MAX_SCORE)
			return null;
		else if (numOfMovesLeft > 15)
			return null;
		else if (numOfMovesLeft < 0)
			return null;
		else {
			// Data has been validated
			byte[] message = { MessageTypes.UPDATED_DATA_FROM_SERVER,
					(byte) cRow, (byte) cCol, (byte) uScore, (byte) cScore,
					(byte) numOfMovesLeft };

			return message;
		}
	}

	/**
	 * This method creates a mesage of type User Move.
	 * 
	 * @param uRow
	 * @param uCol
	 * @return a message of type USER_MOVE.
	 */
	public static byte[] createNewUserMoveMsg(int uRow, int uCol) {
		byte[] message = { MessageTypes.USER_MOVE, (byte) uRow, (byte) uCol };
		return message;
	}

	/**
	 * This method creates a message of type START_NEW_GAME.
	 * 
	 * @return a message of type START_NEW_GAME.
	 */
	public static byte[] createNewStartNewGameMsg() {
		byte[] message = { MessageTypes.START_NEW_GAME };
		return message;
	}

	/**
	 * This method creates a new message of type TERMINATE_CONNECTION.
	 * 
	 * @return a message of type TERMINATE_CONNECTION.
	 */
	public static byte[] createNewTerminateConnectionMsg() {
		byte[] message = { MessageTypes.TERMINATE_CONNECTION };
		return message;
	}

	/**
	 * This method creates a new message of type GAME_OVER.
	 * 
	 * @return
	 */
	public static byte[] createNewGameOverMsg(int cRow, int cCol, int uScore,
			int cScore, int numOfMovesLeft) {
		// Data has been validated
		byte[] message = { MessageTypes.GAME_OVER, (byte) cRow, (byte) cCol,
				(byte) uScore, (byte) cScore, (byte) numOfMovesLeft };
		return message;
	}
}
