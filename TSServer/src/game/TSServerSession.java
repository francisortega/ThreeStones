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
package game;

//import javax.swing.JOptionPane;

import javax.swing.JOptionPane;

import game.CellState;

/**
 * The core of the TS server. This class handles all the logic and keeps track
 * of the points and the state of the buttons.
 * 
 * @author Francis Ortega, A635364
 * @author Tony Belanger, A631360
 * @version October 26, 2008
 */
public class TSServerSession {

	private static CellState[][] btnState;
	private static final int NUM_ROWS = 11;
	private static final int NUM_COLS = 11;
	private int compRowMove = 0;
	private int compColMove = 0;
	private int userPoints = 0;
	private int compPoints = 0;
	private int numberOfMovesLeft;
	private int[] updatedData;

	public TSServerSession() {
		super();
		// initialize every button to empty state
		btnState = new CellState[NUM_ROWS][NUM_COLS];
		noStateCell();
		System.out.println("Game Commence");
		numberOfMovesLeft = 15;
	}

	/**
	 * PerformMove Method Method gets invoke by the server Server performs its
	 * move
	 * 
	 * @param userRow
	 *            - the row of the user's move
	 * @param userCol
	 *            - the column of the user's move
	 * @return move - concatenated coordinates of server's move including scores
	 */
	public int[] performMove(int userRow, int userCol) {

		// If move is valid then user or computer performs the move
		if (isMoveValid(userRow, userCol, compRowMove, compColMove)) {

			userStateCell(userRow, userCol);

			// call method findBestMove(row,col) for the computer
			findBestMove(userRow, userCol);

			// computer move is valid, therefore initialize the btn to computer
			// state
			compStateCell(compRowMove, compColMove);

			System.out.println("Yes it is valid");

			System.out.println(btnState[userRow][userCol]);

			// concatenate the score
			userPoints += findBestScore(userRow, userCol, CellState.USER_STATE);
			compPoints += findBestScore(compRowMove, compColMove,
					CellState.COMP_STATE);

			System.out.println(userPoints + " OH PLEASE USER");
			System.out.println(compPoints + " COMP OWNS U ALL");

			// concatenate the coordinates for both the user and computer
			// including their scores
			numberOfMovesLeft--;
			updatedData = new int[] { compRowMove, compColMove, userPoints,
					compPoints, numberOfMovesLeft };
			// JOptionPane.showMessageDialog(null, "Move is valid!");
			String str = null;
			if (updatedData.length > 0) {
				for (int i = 0; i < updatedData.length; i++) {
					str += updatedData[i];
				}
				//JOptionPane.showMessageDialog(null, str);
			} else {
				//JOptionPane.showMessageDialog(null,
						//"Move is valid, but buffer is array is empty");
			}
			return updatedData;
		} else {
			// JOptionPane.showMessageDialog(null, "Move wasn't valid man!");
			updatedData = new int[1];
			updatedData[0] = -1;
			//JOptionPane.showMessageDialog(null, "Invalid move");
			return updatedData;
		}

	}

	/**
	 * Method checks whether the move is valid or not Must also check the cell
	 * state Cell state must be empty or within the range for a move to be valid
	 * 
	 * @param row
	 *            - row of the coordinate
	 * @param col
	 *            - column of the coordinate
	 * @return isValid - true if the move is valid, otherwise false.
	 */
	public boolean isMoveValid(int row, int col, int lastRow, int lastCol) {
		// cell state must be empty or within the range for a move to be valid
		System.out.println(row + " " + col);

		// if the specified coordinates is in the same row or column and it is
		// in no state
		// then the move is valid
		if (((row == lastRow || col == lastCol) || (lastRow == 0))
				&& btnState[row][col] == CellState.NO_STATE) {
			return true;
		} else {
			// loop through the row and column and check for a no state button
			// if the loop finds a no state in one of the cell within that row
			// or
			// column then the move is invalid, else return true, meaning there
			// is no
			// other options
			for (int i = 0; i < 11; i++)
				if (btnState[lastRow][i] == CellState.NO_STATE)
					return false;
				else if (btnState[i][lastCol] == CellState.NO_STATE)
					return false;

			return true;
		}

	}// end isMoveValid

	/**
	 * Find Best Move Method Computer will find the best move It will find a
	 * move that can earn a point or two Will try to block user from gaining a
	 * point instead of trying to earn a point *
	 * 
	 * @param row
	 * @param col
	 * @return comp - Coordinates
	 */
	public void findBestMove(int userRow, int userCol) {
		int randRow = 0, randCol = 0;

		// computer will keep generating a random number until its a move
		do {
			randRow = (int) (Math.random() * 7 + 2);
			randCol = (int) (Math.random() * 7 + 2);
		} while (!isMoveValid(randRow, randCol, userRow, userCol));

		// initialize the random coordinates to the last coordinates
		compRowMove = randRow;
		compColMove = randCol;
	}

	/**
	 * GetScore Method Method is call every after a user or a computer move
	 * Checks the best placement where a computer can get the best score
	 * Calculates the total score for both players
	 * 
	 * @param row
	 *            - row of the coordinate
	 * @param col
	 *            - column of the coordinate
	 * @param player
	 *            - the player who made called the method
	 * @return score - the total score for a given coordinate
	 */
	public int findBestScore(int row, int col, CellState player) {
		int score = 0;

		System.out.println(btnState[row][col] + " == " + player
				+ " for coordinates " + row + " " + col);

		// check each cell that surrounds the selected cell for a possible score
		// top and bottom
		if (btnState[row - 1][col] == player
				&& btnState[row + 1][col] == player) {
			score += 1;
		}

		// left and right
		if (btnState[row][col - 1] == player
				&& btnState[row][col + 1] == player) {
			score += 1;
		}

		// upper left and lower right
		if (btnState[row - 1][col - 1] == player
				&& btnState[row + 1][col + 1] == player) {
			score += 1;
		}

		// upper right and lower left
		if (btnState[row - 1][col + 1] == player
				&& btnState[row + 1][col - 1] == player) {
			score += 1;
		}

		// check for two cells following the selected cell
		// upward
		if (btnState[row - 1][col] == player
				&& btnState[row - 2][col] == player) {
			score += 1;
		}

		// upper right
		if (btnState[row - 1][col + 1] == player
				&& btnState[row - 2][col + 2] == player) {
			score += 1;
		}

		// towards right
		if (btnState[row][col + 1] == player
				&& btnState[row][col + 2] == player) {
			score += 1;
		}

		// lower right
		if (btnState[row + 1][col + 1] == player
				&& btnState[row + 2][col + 2] == player) {
			score += 1;
		}

		// downward
		if (btnState[row + 1][col] == player
				&& btnState[row + 2][col] == player) {
			score += 1;
		}

		// lower left
		if (btnState[row + 1][col - 1] == player
				&& btnState[row + 2][col - 2] == player) {
			score += 1;
		}

		// towards left
		if (btnState[row][col - 1] == player
				&& btnState[row][col - 2] == player) {
			score += 1;
		}

		// upper left
		if (btnState[row - 1][col - 1] == player
				&& btnState[row - 2][col - 2] == player) {
			score += 1;
		}

		return score;
	}

	/**
	 * The following methods are to initialize the state of a button Whether it
	 * is a user move, a computer move, or empty cell
	 */
	public void noStateCell() {
		// initialize the whole board to not unavailable
		for (int countRow = 0; countRow < 11; countRow++) {
			for (int countCol = 0; countCol < 11; countCol++) {
				btnState[countRow][countCol] = CellState.NOT_AVAILABLE;
				System.out.println("btn: " + countRow + "; " + countCol + " "
						+ btnState[countRow][countCol]);
			}
		}

		// initialize every board button to no state for the game
		for (int countRow = 2; countRow < 9; countRow++) {
			for (int countCol = 2; countCol < 9; countCol++) {
				btnState[countRow][countCol] = CellState.NO_STATE;
				System.out.println("btn: " + countRow + "; " + countCol + " "
						+ btnState[countRow][countCol]);
			}
		}

		// for it to be unaccessible, re-assign the four
		// corners of the board to not available
		// upper-left button
		btnState[2][2] = CellState.NOT_AVAILABLE;
		btnState[3][2] = CellState.NOT_AVAILABLE;
		btnState[2][3] = CellState.NOT_AVAILABLE;

		// upper-right button
		btnState[7][2] = CellState.NOT_AVAILABLE;
		btnState[8][2] = CellState.NOT_AVAILABLE;
		btnState[8][3] = CellState.NOT_AVAILABLE;

		// lower-left button
		btnState[2][7] = CellState.NOT_AVAILABLE;
		btnState[2][8] = CellState.NOT_AVAILABLE;
		btnState[3][8] = CellState.NOT_AVAILABLE;

		// lower-right button
		btnState[7][8] = CellState.NOT_AVAILABLE;
		btnState[8][7] = CellState.NOT_AVAILABLE;
		btnState[8][8] = CellState.NOT_AVAILABLE;

		// center button
		btnState[5][5] = CellState.NOT_AVAILABLE;
	}

	/*
	 * Initialize the state of the button to user
	 */
	public void userStateCell(int row, int col) {
		btnState[row][col] = CellState.USER_STATE;
		System.out.println("User has made its move!");
	}

	/*
	 * Initialize the state of the button to computer
	 */
	public void compStateCell(int row, int col) {
		btnState[row][col] = CellState.COMP_STATE;
		System.out.println("Computer has made its move!");
	}
}
