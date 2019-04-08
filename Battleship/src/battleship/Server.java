package battleship;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Server {
	
	// Grid board
	private Player[][] board = new Player[10][10];
	
	Player currentPlayer;
	
	public synchronized void move(int xloc, int yloc, Player player) {
        if (player != currentPlayer) {
        	System.out.println("Not Player " + player.playerNum + "'s turn");
            throw new IllegalStateException("Not your turn");
        } 
        else if (player.opponent == null) {
        	System.out.println("Player " + player.playerNum + " doesn't have an opponent yet");
            throw new IllegalStateException("You don't have an opponent yet");
        } 
        else if (board[xloc][yloc] != null) {
        	System.out.println("Cell already occupied");
            throw new IllegalStateException("Cell already occupied");
        }
        board[xloc][yloc] = currentPlayer;
        currentPlayer = currentPlayer.opponent;
    }
	
	class Player implements Runnable {
		char playerNum;
		Player opponent;
		Socket socket;
		Scanner input;
		PrintWriter output;
		
		public Player(Socket socket, char playerNum) {
			this.socket = socket;
			this.playerNum = playerNum;
		}
		
		@Override
		public void run() {
			try {
				setup();
				processCommands();
			} catch (Exception e) {
				System.out.println("Threading error: " + e);
			} finally {
				if (opponent != null && opponent.output != null) {
					System.out.println("Player " + opponent.playerNum + " left the game");
					opponent.output.println("OTHER_PLAYER_LEFT");
				}
				try {
					socket.close();
				} catch (IOException e) {
					
				}
			}
		}
		
		private void setup() throws IOException {
			input = new Scanner(socket.getInputStream());
			output = new PrintWriter(socket.getOutputStream(), true);
			output.println("WELCOME " + playerNum);
			
			if (playerNum == '1') {
				currentPlayer = this;
				output.println("MESSAGE Waiting for oppnent to connect");
			}
			else {
				opponent = currentPlayer;
				opponent.opponent = this;
				opponent.output.println("MESSAGE Your move");
			}
			
		}
		
		private void processCommands() {
            while (input.hasNextLine()) {
                String command = input.nextLine();
                if (command.startsWith("QUIT")) {
                    return;
                } 
                else if (command.startsWith("MOVE")) {
                	//System.out.println(Integer.parseInt(command.substring(5,6)));
                	//System.out.println(Integer.parseInt(command.substring(6,7)));
                    processMoveCommand(Integer.parseInt(command.substring(5,6)), Integer.parseInt(command.substring(6,7)));
                }
            }
        }
		
		private void processMoveCommand(int xloc, int yloc) {
            try {
                move(xloc, yloc, this);
                output.println("VALID_MOVE");
                System.out.println("Valid move at coordinates (" + xloc + "," + yloc + ")");
                opponent.output.println("OPPONENT_MOVED " + xloc + yloc);
//                if (hasWinner()) {
//                    output.println("VICTORY");
//                    opponent.output.println("DEFEAT");
//                } else if (boardFilledUp()) {
//                    output.println("TIE");
//                    opponent.output.println("TIE");
//                }
            } catch (IllegalStateException e) {
                output.println("MESSAGE " + e.getMessage());
            }
        }
		
	}
}