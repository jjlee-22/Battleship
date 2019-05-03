/*
 * Server.java
 * @author - Jonathan Lee
 * @date - April 4th, 2019
 * 
 */

package battleship;

class Server {
	
	// Double arrays to for the server to keep track of shots and ships for each board
	private static Player[][] p1board = new Player[10][10];
	private static Player[][] p2board = new Player[10][10];
	
	// Currently selected player and player 1/2 ship health
	static Player currentPlayer;
	public static int p1Life = 1700;
	public static int p2Life = 1700;
	
	/**
	 * Condition for when either player 1 or player 2 health drop to 0 or below 0
	 * @return boolean
	 */
	public static boolean winnerChickenDinner() {
		return (p1Life <= 0 || p2Life <= 0);
	}
	
	/**
	 * Condition for when the selected coordinates contain a ship or not
	 * If yes, register as hit. Else, no hit.
	 * @param xloc
	 * @param yloc
	 * @param player
	 * @return boolean
	 */
	public static boolean shipHit(int xloc, int yloc, Player player) {
		boolean hit = false;
		
		// Searches the entire grid to see if it hit or not
		if (player.playerNum == '2') {
			for (int i = 0; i < p1board.length; i++) {
				for (int j = 0; j < p1board.length; j++) {
					if (p1board[xloc][yloc] != null) {
						hit = true;
						p1Life--;
					} else { hit = false; }
				}
			}
		}
		else if (player.playerNum == '1') {
			for (int i = 0; i < p2board.length; i++) {
				for (int j = 0; j < p2board.length; j++) {
					if (p2board[xloc][yloc] != null) {
						hit = true;
						p2Life--;
					} else { hit = false; }
				}
			}
		}
		return(hit);
	}
	
	/**
	 * Checks if the current player's turn or not
	 * If not, throws an exception message that get send to the violator
	 * @param xloc
	 * @param yloc
	 * @param player
	 */
	public synchronized static void move(int xloc, int yloc, Player player) {
        if (player != currentPlayer) {
        	System.out.println("Not Player " + player.playerNum + "'s turn");
            throw new IllegalStateException("Not your turn");
        } 
        currentPlayer = currentPlayer.opponent; // Player take turns
    }
	
	/**
	 * Add method that waits for each client to send the added ship's location and type
	 * Absolute abysmal coding here. Could be written much, much better.
	 * Not even going to document this due to really bad coding practice
	 * @param xloc
	 * @param yloc
	 * @param shipNum
	 * @param player
	 */
	public synchronized static void add(int xloc, int yloc, int shipNum, Player player) {
		if (player.opponent == null) {
        	System.out.println("Player " + player.playerNum + " doesn't have an opponent yet");
            throw new IllegalStateException("You don't have an opponent yet");
        } 
		else if (player.playerNum == '1') {
				if (shipNum == 0) {
					for(int i = 0; i <= 4; i++) {
						if ((p1board[xloc][yloc+i] != null) || (yloc > 5))
							throw new IllegalStateException("Ship already exist in here or out of bounds");
						p1board[xloc][yloc+ i] = currentPlayer;
						
					}
				}
				else if (shipNum == 1) {
					for(int i = 0; i <= 3; i++) {
						if (p1board[xloc][yloc+i] != null || (yloc > 6))
							throw new IllegalStateException("Ship already exist in here or out of bounds");
						p1board[xloc][yloc+ i] = currentPlayer;
					}
				}
				else if (shipNum == 2) {
					for(int i = 0; i <= 2; i++) {
						if (p1board[xloc][yloc+i] != null || (yloc > 7))
							throw new IllegalStateException("Ship already exist in here or out of bounds");
						p1board[xloc][yloc+ i] = currentPlayer;
					}
				}
				else if (shipNum == 3) {
					for(int i = 0; i <= 2; i++) {
						if (p1board[xloc][yloc+i] != null || (yloc > 7))
							throw new IllegalStateException("Ship already exist in here or out of bounds");
						p1board[xloc][yloc+ i] = currentPlayer;
					}
				}
				else if (shipNum == 4) {
					for(int i = 0; i <= 1; i++) {
						if (p1board[xloc][yloc+i] != null || (yloc > 8))
							throw new IllegalStateException("Ship already exist in here or out of bounds");
						p1board[xloc][yloc+ i] = currentPlayer;
					}
					player.output.println("MESSAGE Waiting for other player to finish placement");
				}
		}
		else if (player.playerNum == '2') {
			if (shipNum == 0) {
				for(int i = 0; i <= 4; i++) {
					if (p2board[xloc][yloc+i] != null || (yloc > 5))
						throw new IllegalStateException("Ship already exist in here or out of bounds");
					p2board[xloc][yloc+ i] = currentPlayer;
					
				}
			}
			else if (shipNum == 1) {
				for(int i = 0; i <= 3; i++) {
					if (p2board[xloc][yloc+i] != null|| (yloc > 6))
						throw new IllegalStateException("Ship already exist in here or out of bounds");
					p2board[xloc][yloc+ i] = currentPlayer;
				}
			}
			else if (shipNum == 2) {
				for(int i = 0; i <= 2; i++) {
					if (p2board[xloc][yloc+i] != null || (yloc > 7))
						throw new IllegalStateException("Ship already exist in here or out of bounds");
					p2board[xloc][yloc+ i] = currentPlayer;
				}
			}
			else if (shipNum == 3) {
				for(int i = 0; i <= 2; i++) {
					if (p2board[xloc][yloc+i] != null || (yloc > 7))
						throw new IllegalStateException("Ship already exist in here or out of bounds");
					p2board[xloc][yloc+ i] = currentPlayer;
				}
			}
			else if (shipNum == 4) {
				for(int i = 0; i <= 1; i++) {
					if (p2board[xloc][yloc+i] != null || (yloc > 8))
						throw new IllegalStateException("Ship already exist in here or out of bounds");
					p2board[xloc][yloc+ i] = currentPlayer;
				}
				player.output.println("MESSAGE Waiting for other player to finish placement");
				player.opponent.output.println("MESSAGE Opponent finished placement, attack when ready!");
			}
		}
        
    }
}