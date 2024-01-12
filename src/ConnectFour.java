import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JTextField;
import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;

/*
 * Sabrina Wang
 * January 24th, 2022
 * A two-player game of connect four
 * Research sources: 
 * https://docs.oracle.com/javase/tutorial/uiswing/components/dialog.html 
 * https://docs.oracle.com/javase/tutorial/uiswing/components/frame.html 
 * https://www.javatpoint.com/java-joptionpane 
 * https://www.fgbradleys.com/rules/Connect%20Four.pdf
 * https://www.youtube.com/watch?v=uaQ1bBoK7HU
 * https://www.freecodecamp.org/news/java-string-to-int-how-to-convert-a-string-to-an-integer/
 */

public class ConnectFour {
	static int[][] grid;
	static ArrayList<Integer> y1Coordinate = new ArrayList<Integer>();
	static ArrayList<Integer> x1Coordinate = new ArrayList<Integer>();
	static ArrayList<Integer> y2Coordinate = new ArrayList<Integer>();
	static ArrayList<Integer> x2Coordinate = new ArrayList<Integer>();
	static Board b;
	static JFrame f;
	static String player1, player2;
	static int p1Score = 0;
	static int p2Score = 0;
	static boolean continueGame = true;
	static final int BOARD_ROWS = 6;
	static final int BOARD_COLUMNS = 7;

	/**
	 * starts a new game and resets the move numbers and arrays
	 */
	public static void playGame() {
		int moveNum = 0;
		// stores the number of pegs in each column
		int[] pegsInColumn = new int[BOARD_COLUMNS];
		// stores whether a slot is occupied by player 1, player 2, or empty
		grid = new int[BOARD_ROWS][BOARD_COLUMNS];
		int player = 1;

		// while the board is not full, players may continue placing pegs
		while (moveNum < BOARD_ROWS * BOARD_COLUMNS) {
			if (moveNum % 2 == 0) {
				b.displayMessage(player1 + "'s turn");
			} else {
				b.displayMessage(player2 + "'s turn");
			}

			Coordinate loc;
			loc = b.getClick();
			int col = loc.getCol();
			int row = 5 - pegsInColumn[col];

			// prompts the player again if the column is full
			while (pegsInColumn[col] == BOARD_ROWS) {
				b.displayMessage("Column full, pick another one");
				loc = b.getClick();
				col = loc.getCol();
				row = 5 - pegsInColumn[col];
			}

			moveNum++;
			pegsInColumn[col] += 1;
			grid[row][col] = player;

			// determines whether player 1 or player 2 goes
			if (moveNum % 2 != 0) {
				// player 1's move and checks if the move is a winning move
				placePeg("red", row, col);
				if (checkWin(row, col)) {
					b.displayMessage(player1 + " wins!");
					p1Score += 1;
					break;

				}
				player = 2;
			} else {
				// player 2's move and checks if the move is a winning move
				placePeg("yellow", row, col);
				if (checkWin(row, col)) {
					b.displayMessage(player2 + " wins!");
					p2Score += 1;
					break;
				}
				player = 1;

				if (moveNum == BOARD_ROWS * BOARD_COLUMNS) {
					b.displayMessage("Draw!");
				}
			}
		}
	}

	/**
	 * animates the dropping of a peg by moving it down one row at a time and
	 * displays it at the end
	 * 
	 * @param colour
	 * @param r
	 * @param c
	 */
	public static void placePeg(String colour, int r, int c) {
		//
		for (int a = 0; a < r; a++) {
			b.putPeg(colour, a, c);
			try {
				Thread.sleep(35);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			b.removePeg(a, c);
		}
		b.putPeg(colour, r, c);
	}

	/**
	 * checks for a horizontal, vertical, or diagonal win and stores the
	 * coordinates in an array
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public static boolean checkWin(int row, int col) {
		boolean win = false;
		// checks the row of the last placed peg for a horizontal win
		for (int c = 0; c < 4; c++) {
			if (grid[row][c] != 0 && grid[row][c] == grid[row][c + 1] && grid[row][c + 2] == grid[row][c + 3]
					&& grid[row][c] == grid[row][c + 3]) {
				win = true;
				b.drawLine(row, c, row, c + 3);
				y1Coordinate.add(row);
				x1Coordinate.add(c);
				y2Coordinate.add(row);
				x2Coordinate.add(c + 3);
				break;
			}
		}
		// checks the column of the last placed peg for a vertical win
		for (int r = 5; r > 2; r--) {
			if (grid[r][col] != 0 && grid[r][col] == grid[r - 1][col] && grid[r - 2][col] == grid[r - 3][col]
					&& grid[r][col] == grid[r - 3][col]) {
				win = true;
				b.drawLine(r, col, r - 3, col);
				y1Coordinate.add(r);
				x1Coordinate.add(col);
				y2Coordinate.add(r - 3);
				x2Coordinate.add(col);
				break;
			}
		}
		// checks for a diagonal win from --> /
		for (int r = 5; r > 2; r--) {
			for (int c = 6; c > 2; c--) {
				if (grid[r][c] != 0 && grid[r][c] == grid[r - 1][c - 1] && grid[r - 1][c - 1] == grid[r - 2][c - 2]
						&& grid[r - 2][c - 2] == grid[r - 3][c - 3]) {
					win = true;
					b.drawLine(r, c, r - 3, c - 3);
					y1Coordinate.add(r);
					x1Coordinate.add(c);
					y2Coordinate.add(r - 3);
					x2Coordinate.add(c - 3);
					break;
				}
			}
		}

		// checks for a diagonal win --> \
		for (int r = 5; r > 2; r--) {
			for (int c = 0; c < 4; c++) {
				if (grid[r][c] != 0 && grid[r][c] == grid[r - 1][c + 1] && grid[r - 1][c + 1] == grid[r - 2][c + 2]
						&& grid[r - 2][c + 2] == grid[r - 3][c + 3]) {
					win = true;
					b.drawLine(r, c, r - 3, c + 3);
					y1Coordinate.add(r);
					x1Coordinate.add(c);
					y2Coordinate.add(r - 3);
					x2Coordinate.add(c + 3);
					break;
				}
			}
		}
		return win;
	}

	/**
	 * resets the board by removing all pegs and lines
	 */
	public static void resetGame() {
		for (int r = 0; r < BOARD_ROWS; r++) {
			for (int c = 0; c < BOARD_COLUMNS; c++) {
				b.removePeg(r, c);
			}
		}
		for (int i = 0; i < y1Coordinate.size(); i++) {
			b.removeLine(y1Coordinate.get(i), x1Coordinate.get(i), y2Coordinate.get(i), x2Coordinate.get(i));
		}
	}

	public static void main(String[] args) {
		// displays instructions and startup messages
		f = new JFrame();
		File textFile = new File("scores.txt");
		String saveData[] = new String[4];
		// defaults player names to Player 1 and Player 2 in case the user
		// chooses not to input a name
		player1 = "Player 1";
		player2 = "Player 2";

		JOptionPane.showMessageDialog(f,
				"Player 1 and Player 2 will take turns dropping pegs onto the board.\nThe peg will automatically drop to the bottom and stack."
						+ "\n\nThe goal of the game is to be the first to connect four of your pegs\nin a row, either horizontally, vertically "
						+ "or diagonally before the board\nfills up. Once a player has reached four in a row, the game will be\nover. If neither"
						+ "player reaches four in a row by the time the board\nfills up, the match will result in a draw.\n\nClick on any "
						+ "column to begin. Have fun!",
				"Instructions", JOptionPane.INFORMATION_MESSAGE);

		Object[] options = { "New Game", "Load Save" };
		int n = JOptionPane.showOptionDialog(f, "Would you like to create a new game or load the last saved game?",
				"Game Setup", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[1]);

		if (n == 1) {
			// if player has selected "Load Game", searches for the file and
			// sets the variables to the data in the text file
			if (textFile.exists()) {
				Scanner input = null;

				try {
					input = new Scanner(textFile);
				} catch (FileNotFoundException ex) {
				}
				while (input.hasNextLine()) {
					for (int i = 0; i < 4; i++) {
						saveData[i] = input.nextLine();
					}
				}
				player1 = saveData[0];
				p1Score = Integer.parseInt(saveData[1]);
				player2 = saveData[2];
				p2Score = Integer.parseInt(saveData[3]);
			} else {
				JOptionPane.showMessageDialog(f, "File not found, starting new game.", "Error",
						JOptionPane.WARNING_MESSAGE);
			}

		} else {
			// prompts the players for their names if they chose new game
			JTextField field1 = new JTextField();
			JTextField field2 = new JTextField();

			Object[] fields = { "Player 1", field1, "Player 2", field2 };
			int o = JOptionPane.showConfirmDialog(null, fields, "Enter Player Names", JOptionPane.OK_CANCEL_OPTION);
			if (o == 0) {
				player1 = field1.getText();
				player2 = field2.getText();
			}
		}
		b = new Board(BOARD_ROWS, BOARD_COLUMNS); // creates the board

		// asks the player if they want to play again once the game ends
		do {
			playGame();
			Object[] options2 = { "Play Again", "Save and Exit" };
			int c = JOptionPane.showOptionDialog(f,
					player1 + " wins: " + p1Score + "\n" + player2 + " wins: " + p2Score, "Game Over",
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options2, options2[1]);
			// if no previous file exists, creates a new one and saves the data
			if (c == 1) {
				PrintWriter output = null;
				if (textFile.exists()) {
				} else {
					try {
						textFile.createNewFile();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(f, "File could not be created.", "Error",
								JOptionPane.WARNING_MESSAGE);
					}
				}
				try {
					output = new PrintWriter(textFile);
				} catch (FileNotFoundException e) {
					System.exit(1);
				}
				output.println(player1);
				output.println(p1Score);
				output.println(player2);
				output.print(p2Score);
				output.close();
				continueGame = false;
			} else {
				resetGame();
			}
		} while (continueGame);

	}

}
