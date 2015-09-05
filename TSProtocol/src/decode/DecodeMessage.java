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

package decode;

import controlmessages.MessageTypes;

/**
 * Decodes a message that is compliant with the TSProtocol. Messages created
 * using EncodeMessage are interpreted using this class.
 * 
 * @author Tony Belanger, A631360
 * @author Francis Ortega, A635364
 * @version 10.21.2008
 */
public class DecodeMessage {
	// get message header
	public static byte getMessageHeader(byte[] byteBuffer) {
		System.out.println(byteBuffer[0]);
		return byteBuffer[0];
	}

	/**
	 * This method decodes a message sent from the server and retrieves the
	 * updated data from it.
	 * 
	 * @param updatedDataMsg
	 * @return
	 */
	public static int[] decodeUpdatedDataMsg(byte[] updatedDataMsg) {
		if (getMessageHeader(updatedDataMsg) == MessageTypes.UPDATED_DATA_FROM_SERVER) {
			int[] decodedData = { (int) updatedDataMsg[1],
					(int) updatedDataMsg[2], (int) updatedDataMsg[3],
					(int) updatedDataMsg[4], (int) updatedDataMsg[5] };
			return decodedData;
		} else {
			return null;
		}
	}
	
	public static int[] decodeGameOverDataMsg(byte[] updatedDataMsg) {
		if (getMessageHeader(updatedDataMsg) == MessageTypes.GAME_OVER) {
			int[] decodedData = { (int) updatedDataMsg[1],
					(int) updatedDataMsg[2], (int) updatedDataMsg[3],
					(int) updatedDataMsg[4], (int) updatedDataMsg[5] };
			return decodedData;
		} else {
			return null;
		}
	}

	/**
	 * This method decodes a message of type USER_MOVE and returns
	 * the coordinates.
	 * @param userMoveMsg message of type USER_MOVE
	 * @return the move's coordinates.
	 */
	public static int[] decodeUserMove(byte[] userMoveMsg) {
		if (getMessageHeader(userMoveMsg) == MessageTypes.USER_MOVE) {
			int[] decodedData = { (int) userMoveMsg[1], (int) userMoveMsg[2] };
			return decodedData;
		} else {
			return null;
		}
	}
}
