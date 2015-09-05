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
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import model.TSClientModel;

/**
 * ButtonPanel Class Setting up the buttons in the proper position begins here
 * View is updated every move Button turns white if its a user move and sets the
 * text to "U" Button turns black if its a computer move and sets the text to
 * "S" Will have six empty unassigned buttons at the end of the game
 * 
 * @author Francis Ortega, A635364
 * @author Tony Belanger, A631360
 * @since October 24, 2008
 * 
 */
public class ButtonPanel extends JPanel implements Observer {
	// Class Variable Declarations
	private static final long serialVersionUID = 1L;
	private static final int BORDER_TOP = 4, BORDER_LEFT = 4,
			BORDER_BOTTOM = 4, BORDER_RIGHT = 4;
	private static final int INSET_TOP = 4, INSET_LEFT = 4, INSET_BOTTOM = 4,
			INSET_RIGHT = 4;
	private static final int NUM_OF_GRIDS = 11;
	private static final String name = "O";
	private static int compLastX = 0, compLastY = 0;

	private JButton[][] buttons = null;
	private Point buttonPoint = null;
	private GridBagLayout gbLayout = null;
	private GridBagConstraints gbConstr = null;

	private ActionListener myActionListener;
	private TSClientModel clientModel;

	private ScoresPanel scores = null;

	/**
	 * Class constructor of the buttons panel Sets the buttons in the board Sets
	 * the user and computer score Sets how many moves left the user has
	 * 
	 * @param clientModel
	 *            - an instance of the client model
	 */
	public ButtonPanel(TSClientModel clientModel) {
		super();

		gbLayout = new GridBagLayout();
		gbConstr = new GridBagConstraints();
		gbConstr.insets = new Insets(INSET_TOP, INSET_LEFT, INSET_BOTTOM,
				INSET_RIGHT);
		BorderFactory.createCompoundBorder(BorderFactory
				.createLineBorder(Color.DARK_GRAY), BorderFactory
				.createEmptyBorder(BORDER_TOP, BORDER_LEFT, BORDER_BOTTOM,
						BORDER_RIGHT));

		buttons = new JButton[NUM_OF_GRIDS][NUM_OF_GRIDS];
		scores = new ScoresPanel();
		this.clientModel = clientModel;

		this.setBackground(Color.DARK_GRAY);
		setLayout(gbLayout);
		setButtons();
		this.setVisible(true);
		this.setSize(gbLayout.preferredLayoutSize(this));

	}

	/*
	 * Set buttons border
	 */
	public void initializeButtons() {
		myActionListener = new MyActionListener();
		for (int rows = 0; rows < buttons.length; rows++)
			for (int cols = 0; cols < buttons.length; cols++) {
				buttons[rows][cols] = new JButton();
				buttons[rows][cols].setText(name);
				// add action listeners for each available button
				buttons[rows][cols].addActionListener(myActionListener);
			}
	}

	/**
	 * Set buttons.
	 */
	private void setButtons() {
		initializeButtons();
		setButtonDimension();
		setButtonsInGrid();
		//setWings();
	}

	/**
	 * This method disables the buttons on the form.
	 */
	public void disableButtons() {
		for (int rows = 0; rows < buttons.length; rows++)
			for (int cols = 0; cols < buttons.length; cols++)
				buttons[rows][cols].setEnabled(false);
	}

	/**
	 * This method enables the buttons on the form.
	 */
	public void enableButtons() {
		for (int rows = 0; rows < buttons.length; rows++)
			for (int cols = 0; cols < buttons.length; cols++)
				buttons[rows][cols].setEnabled(true);
	}
	/*
	 * Set Button Dimension
	 */
	public void setButtonDimension() {
		Dimension dim = new Dimension();
		for (int rows = 0; rows < buttons.length; rows++)
			for (int cols = 0; cols < buttons.length; cols++) {
				dim.width = buttons[rows][cols].getPreferredSize().width;
				dim.height = buttons[rows][cols].getPreferredSize().height;
				buttons[rows][cols].setPreferredSize(dim);
			}
	}

	/*
	 * Initialize the score panel as well as the moves left
	 */
	public void setLabelsForScore() {
		gbConstr.weightx = 1.0;
		gbConstr.weighty = 1.0;

		gbConstr.gridwidth = 2;
		gbConstr.gridheight = 1;
		gbConstr.gridx = 0;
		gbConstr.gridy = 0;
		// add(userScore, gbConstr);

		gbConstr.gridwidth = 2;
		gbConstr.gridheight = 1;

		gbConstr.gridwidth = 2;
		gbConstr.gridheight = 1;

	}

	/*
	 * Set buttons in the grid
	 */
	public void setButtonsInGrid() {
		gbConstr.weightx = 1.0;
		gbConstr.weighty = 1.0;

		// add buttons to the panel
		// assign their coordinates
		for (int rows = 0; rows < buttons.length; rows++)
			for (int cols = 0; cols < buttons.length; cols++) {
				gbConstr.gridx = cols;
				gbConstr.gridy = rows;
				buttonPoint = new Point(cols, rows);
				buttons[rows][cols].setBackground(Color.LIGHT_GRAY);
				buttons[rows][cols].setLocation(buttonPoint);
				add(buttons[rows][cols], gbConstr);
			}

		// disable unwanted buttons
		for (int rows = 0; rows < buttons.length; rows++)
			for (int cols = 0; cols < buttons.length; cols++) {
				if (cols < 2 || cols > 8) {
					buttons[rows][cols].setText("");
					buttons[rows][cols].setEnabled(false);
					buttons[rows][cols].setBorderPainted(false);
					buttons[rows][cols].setBackground(null);
				}

				if (rows < 2 || rows > 8) {
					buttons[rows][cols].setText("");
					buttons[rows][cols].setEnabled(false);
					buttons[rows][cols].setBorderPainted(false);
					buttons[rows][cols].setBackground(null);
				}
			}

		// upper-left position disabling the buttons
		// removing the border, disabling the background color
		buttons[2][2].setText("");
		buttons[2][2].setEnabled(false);
		buttons[2][2].setBorderPainted(false);
		buttons[2][2].setBackground(null);

		buttons[3][2].setText("");
		buttons[3][2].setEnabled(false);
		buttons[3][2].setBorderPainted(false);
		buttons[3][2].setBackground(null);

		buttons[2][3].setText("");
		buttons[2][3].setEnabled(false);
		buttons[2][3].setBorderPainted(false);
		buttons[2][3].setBackground(null);

		// upper-right corner
		buttons[7][2].setText("");
		buttons[7][2].setEnabled(false);
		buttons[7][2].setBorderPainted(false);
		buttons[7][2].setBackground(null);

		buttons[8][2].setText("");
		buttons[8][2].setEnabled(false);
		buttons[8][2].setBorderPainted(false);
		buttons[8][2].setBackground(null);

		buttons[8][3].setText("");
		buttons[8][3].setEnabled(false);
		buttons[8][3].setBorderPainted(false);
		buttons[8][3].setBackground(null);

		// lower-left corner
		buttons[2][7].setText("");
		buttons[2][7].setEnabled(false);
		buttons[2][7].setBorderPainted(false);
		buttons[2][7].setBackground(null);

		buttons[2][8].setText("");
		buttons[2][8].setEnabled(false);
		buttons[2][8].setBorderPainted(false);
		buttons[2][8].setBackground(null);

		buttons[3][8].setText("");
		buttons[3][8].setEnabled(false);
		buttons[3][8].setBorderPainted(false);
		buttons[3][8].setBackground(null);

		// lower-right corner
		buttons[7][8].setText("");
		buttons[7][8].setEnabled(false);
		buttons[7][8].setBorderPainted(false);
		buttons[7][8].setBackground(null);

		buttons[8][7].setText("");
		buttons[8][7].setEnabled(false);
		buttons[8][7].setBorderPainted(false);
		buttons[8][7].setBackground(null);

		buttons[8][8].setText("");
		buttons[8][8].setEnabled(false);
		buttons[8][8].setBorderPainted(false);
		buttons[8][8].setBackground(null);

		// disable the middle button
		buttons[5][5].setText("X");
		buttons[5][5].setEnabled(false);
		buttons[5][5].setBorderPainted(false);
		buttons[5][5].setBackground(null);
	}

	/*
	 * Displays the wings of the board
	 */
	/*public void setWings() {
		int cols = 0;
		while (cols < 3) {
			if (cols == 0) {
				for (int rows = 1; rows < 10; rows++) {
					buttons[rows][cols].setBackground(Color.WHITE);
				}
			}
			if (cols == 1) {
				buttons[0][cols].setBackground(Color.WHITE);
				buttons[1][cols].setBackground(Color.WHITE);
				buttons[9][cols].setBackground(Color.WHITE);
				buttons[10][cols].setBackground(Color.WHITE);
			}

			if (cols == 2) {
				buttons[0][cols].setBackground(Color.WHITE);
				buttons[10][cols].setBackground(Color.WHITE);
			}

			cols++;
		}

		int cols2 = 8;
		while (cols2 > 7 && cols2 < 11) {
			if (cols2 == 10) {
				for (int rows = 1; rows < 10; rows++) {
					buttons[rows][cols2].setBackground(Color.BLACK);
				}
			}
			if (cols2 == 9) {
				buttons[0][cols2].setBackground(Color.BLACK);
				buttons[1][cols2].setBackground(Color.BLACK);
				buttons[9][cols2].setBackground(Color.BLACK);
				buttons[10][cols2].setBackground(Color.BLACK);
			}

			if (cols2 == 8) {
				buttons[0][cols2].setBackground(Color.BLACK);
				buttons[10][cols2].setBackground(Color.BLACK);
			}

			cols2++;
		}
	}*/

	/**
	 * Action listener for each button pressed sets the background color to
	 * white if user move
	 */
	private class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int userRow = 0, userCol = 0;

			// searches for the button pressed
			for (int x = 2; x < 9; x++) {
				for (int y = 2; y < 9; y++) {
					if (buttons[x][y].equals((JButton) e.getSource())) {
						userRow = x;
						userCol = y;
						// send button coordinates for encoding
						clientModel.setReceivedCoordinates(userRow, userCol);
						break;
					}
				} // end inner for
			} // end outer for
		}
	}

	/**
	 * Method update Updates the view when computer makes a move Sets the text
	 * to "S" for server instead of "C" for computer as not to be confused with
	 * "Client"
	 * 
	 * @param obs
	 * @param oo
	 *            - the object that is sent by the client which contains
	 *            computer's move
	 */
	public void update(Observable obs, Object oo) {
		int cX, cY, uX, uY;
		int compX = 0, compY = 0, userX = 0, userY = 0;

		if (obs instanceof TSClientModel) {
			int data[] = (int[]) oo;
			scores.update(null, oo);

			// substring
			uX = data[5];
			uY = data[6];
			cX = data[0];
			cY = data[1];

			// parsing for user move and score
			userX = uX;
			userY = uY;

			// set the border to original color
			buttons[compLastX][compLastY].setBorder(BorderFactory
					.createLineBorder(null));

			// perform move for user
			buttons[userX][userY].setBackground(Color.WHITE);
			buttons[userX][userY].setForeground(Color.BLACK);
			buttons[userX][userY].setText("U");
			buttons[userX][userY].setEnabled(false);

			// parsing for computer move and score
			compX = cX;
			compY = cY;

			// perform move for computer
			buttons[compX][compY].setBackground(Color.BLACK);
			buttons[compX][compY].setForeground(Color.WHITE);
			buttons[compX][compY].setText("S");
			buttons[compX][compY].setEnabled(false);

			// highlight the last move made by the computer
			buttons[compX][compY].setBorder(BorderFactory.createLineBorder(
					Color.YELLOW, 5));

			compLastX = compX;
			compLastY = compY;
		}
	}
}
