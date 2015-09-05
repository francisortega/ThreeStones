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

package controlmessages;

/**
 * Lists the different types of messages the protocol can use.
 * 
 * @author Tony Belanger, A631360
 * @author Francis Ortega, A635364
 * @version 10.21.2008
 */
public final class MessageTypes {
	public static final int CONTROL_MESSAGE_SIZE = 6; // A message has 6 bytes
	public static final int MAX_NUM_OF_MOVES = 15; // Max number of moves
	public static final int MAX_SCORE = 13; // Maximum possible score
	public static final byte INVALID_MOVE = 5; // Invalid move. No parameters.
	public static final byte ERROR = -1; // Something went wrong
	public static final byte START_NEW_GAME = 0; // No parameters
	public static final byte GAME_OVER = 1; // 1 parameter
	public static final byte TERMINATE_CONNECTION = 2; // No parameters
	public static final byte USER_MOVE = 3; // 2 parameters
	public static final byte UPDATED_DATA_FROM_SERVER = 4;
}
