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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.TSClientModel;

/**
 * Three Stones Board Frame Class The frame of the board that contains the
 * button panel Will ask for the valid server's IP address for connection
 * 
 * @author Francis Ortega, A635364
 * @author Tony Belanger, A631360
 * @version October 26, 2008
 */
public class TSBoardFrame extends JFrame {
	/**
	 * UID.
	 */
	private static final long serialVersionUID = 8163676016868162911L;
	private ButtonPanel buttonPanel = null;
	private TSClientModel clientModel = null;
	private ScoresPanel scoresPanel = null;
	private Dimension dim = null;
	private final int serverPort = 50000;
	private MyActionListener myActionListener;

	/**
	 * Class Constructor Makes the first connection from the Client to Server
	 */
	public TSBoardFrame() {
		super("Stone Wars - Game Demo");
		setLookAndFeel();
		setJMenuBar(createMenuBar());
		setLayout(new BorderLayout());
		String serverIP = JOptionPane.showInputDialog("Server IP:");
		clientModel = new TSClientModel(serverIP, serverPort);
		scoresPanel = new ScoresPanel();
		buttonPanel = new ButtonPanel(clientModel);
		buttonPanel.disableButtons();
		clientModel.addObserver(buttonPanel);
		clientModel.addObserver(scoresPanel);

		getContentPane().add(scoresPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		pack();
		centerScreen();
		setVisible(true);

		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
	}

	/**
	 * Creates the menu bar.
	 * 
	 * @return
	 */
	public JMenuBar createMenuBar() {
		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		myActionListener = new MyActionListener();

		// create the menu bar
		menuBar = new JMenuBar();

		// build the file menu
		menu = new JMenu("File");
		menuBar.add(menu);

		// add the bar
		menuItem = new JMenuItem("New Game");
		menu.setMnemonic(KeyEvent.VK_N);
		menu.add(menuItem);
		menuItem.addActionListener(myActionListener);
		// add the bar
		menuItem = new JMenuItem("Exit");
		menu.setMnemonic(KeyEvent.VK_X);
		menu.add(menuItem);
		menuItem.addActionListener(myActionListener);

		return menuBar;
	}

	/**
	 * Method to put the game board at the center of the screen
	 */
	private void centerScreen() {
		dim = getToolkit().getScreenSize();
		Rectangle abounds = getBounds();
		setLocation((dim.width - abounds.width) / 2,
				(dim.height - abounds.height) / 2);
	}

	/**
	 * Action listener for each button pressed sets the background color to
	 * white if user move
	 */
	private class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			// check if new game has been selected from file menu
			if (((JMenuItem) e.getSource()).getText().equals("New Game")) {
				// JOptionPane.showMessageDialog(null, "new Game!");
				buttonPanel.enableButtons();
				clientModel.startNewGame();
			} else if (((JMenuItem) e.getSource()).getText().equals("Exit")) {
				clientModel.exit();
				System.exit(0);
			}
		}
	}

	/**
	 * This method changes the look and feel of the application so that it looks
	 * the same across all platforms.
	 */
	private void setLookAndFeel() {
		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(UIManager
					.getCrossPlatformLookAndFeelClassName());
		} catch (UnsupportedLookAndFeelException e) {
			// handle exception
		} catch (ClassNotFoundException e) {
			// handle exception
		} catch (InstantiationException e) {
			// handle exception
		} catch (IllegalAccessException e) {
			// handle exception
		}
	}

}
