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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JLabel; //import javax.swing.JOptionPane;
//import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Scores Panel Class Scores are viewed for user and computer Game and Exit
 * buttons are placed Game button has the same action as "New Game"
 * 
 * @author Francis Ortega, A635364
 * @author Tony Belanger, A631360
 * @version October 26, 2008
 */
public class ScoresPanel extends JPanel implements Observer {
	// Class Variable Declarations
	private static final long serialVersionUID = 1L;
	private GridBagConstraints textFieldConstraints = null;
	private JTextField userScoreField = null;
	private JTextField compScoreField = null;
	private JLabel movesLeft = null;

	/**
	 * Class Constructor for the Scores Panel
	 */
	public ScoresPanel() {
		super();

		textFieldConstraints = new GridBagConstraints();

		setLayout(new BorderLayout());
		setVisible(true);
		setUserFields();
		setServerFields();
		setMovesLeft();
	}

	/**
	 * This method resets the scores panel.
	 */
	public void resetAllFields() {
		setUserFields();
		setServerFields();
		setMovesLeft();
	}

	/*
	 * Set the user display field
	 */
	public void setUserFields() {
		textFieldConstraints.weightx = 1.0;
		textFieldConstraints.weighty = 1.0;
		userScoreField = new JTextField(5);
		userScoreField.setText("0");
		userScoreField.setEditable(false);
		userScoreField.setEnabled(true);
		userScoreField.setBackground(Color.WHITE);
		userScoreField.setForeground(Color.BLACK);
		userScoreField.setHorizontalAlignment(JTextField.LEFT);
		userScoreField.setText("0");
		add(userScoreField, BorderLayout.WEST);
	}

	/*
	 * Set the moves left
	 */
	public void setMovesLeft() {
		textFieldConstraints.weightx = 1.0;
		textFieldConstraints.weighty = 1.0;
		movesLeft = new JLabel("Moves Left: 15");
		movesLeft.setFont(new Font("Dialog", Font.BOLD, 12));
		movesLeft.setHorizontalTextPosition(JLabel.CENTER);
		add(movesLeft);
	}

	/*
	 * Set server side score field
	 */
	public void setServerFields() {
		textFieldConstraints.weightx = 1.0;
		textFieldConstraints.weighty = 1.0;
		compScoreField = new JTextField(5);
		compScoreField.setText("0");
		compScoreField.setEditable(false);
		compScoreField.setBackground(Color.BLACK);
		compScoreField.setForeground(Color.WHITE);
		compScoreField.setHorizontalAlignment(JTextField.RIGHT);
		add(compScoreField, BorderLayout.EAST);
	}

	/**
	 * Update Method Scores are displayed for every scores made
	 * 
	 * @param Observable
	 *            - TSCLientModel
	 * @param Object
	 *            - Contains the moves and scores
	 */
	public void update(Observable u, Object oo) {
		int[] data;

		// cast the object that was sent
		data = (int[]) oo;
		String str = "";
		for (int i = 0; i < data.length; i++)
			str += data[i];

		//JOptionPane.showMessageDialog(null, "ScoresPanel: data contains" +
		//str);

		userScoreField.setText(String.valueOf(data[2]));
		compScoreField.setText(String.valueOf(data[3]));

		movesLeft.setText("Moves Left: " + data[4]);

		int uScr = data[2];
		int cScr = data[3];
		String winner = null;

		if (data[4] == 0) {

			if (uScr > cScr) {
				winner = "User Wins";
			} else if (cScr > uScr) {
				winner = "Computer Wins";
			} else {
				winner = "DRAW";
			}
			//JOptionPane.showMessageDialog(null, "Game Over \n" + winner);
		}
	}
}
