package battleship;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;


/*
 * Another class that I didn't bother to put into another .java
 * Create networking with clients. Ability send or receive commands between server and client
 */

class Player implements Runnable {
	// Attributes for networking
	char playerNum;
	Player opponent;
	Socket socket;
	Scanner input;
	PrintWriter output;
	private String pattern = "(.*)-(.*)";	// Pattern for regex to identify username and password
	private String username;
	private String password;
	
	public Player(Socket socket, char playerNum) {
		this.socket = socket;
		this.playerNum = playerNum;
	}
	
	@Override
	public void run() {
		try {
			setup();	// Initializes sockets and connection with client
			processCommands();	// Processes commands between client and server
		} catch (Exception e) {
			System.out.println("Threading Error: " + e);
		} finally {
			// Ending connection when one player disconnects
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
	
	/**
	 * Initializes sockets and connection with client
	 * @throws IOException
	 */
	private void setup() throws IOException {
		input = new Scanner(socket.getInputStream());
		output = new PrintWriter(socket.getOutputStream(), true);
		output.println("WELCOME " + playerNum);
		
		if (playerNum == '1') {
			Server.currentPlayer = this;
			output.println("MESSAGE Waiting for oppnent to connect");
		}
		else {
			opponent = Server.currentPlayer;
			opponent.opponent = this;
			opponent.output.println("MESSAGE Add your carrier");
		}
		
	}
	
	/**
	 * Processes commands between client and server
	 */
	private void processCommands() {
        while (input.hasNextLine()) {
            String command = input.nextLine();
            System.out.println(command);
            if (command.startsWith("QUIT")) {
                return;
            } 
            else if (command.startsWith("ADD")) {
            	processAddCommand(Integer.parseInt(command.substring(4,5)), Integer.parseInt(command.substring(5,6)), Integer.parseInt(command.substring(6,7)));
            }
            else if (command.startsWith("LOGIN")) {
            	processLoginCommand(command.substring(6));
            }
            else if (command.startsWith("REGISTER")) {
            	processRegisterCommand(command.substring(9));
            }
            else if (command.startsWith("MOVE")) {
                processMoveCommand(Integer.parseInt(command.substring(5,6)), Integer.parseInt(command.substring(6,7)));
            }
        }
    }
	
	/**
	 * Processes move commands and determines if registered as missed, hit, victory, or defeat
	 * @param xloc
	 * @param yloc
	 */
	private void processMoveCommand(int xloc, int yloc) {
        try {
            Server.move(xloc, yloc, this);
            System.out.println("P1 Health: " + Server.p1Life + " P2 Health: " + Server.p2Life);
            output.println("VALID_MOVE");
            System.out.println("Valid move at coordinates (" + xloc + "," + yloc + ")");
            opponent.output.println("OPPONENT_MOVED " + xloc + yloc);
            
            if (Server.shipHit(xloc, yloc, this)) {
            	System.out.println(playerNum + " scored a hit!");
            	output.println("HIT " + xloc + yloc);
            	opponent.output.println("OPPONENT_HIT " + xloc + yloc);
            }
            if (Server.winnerChickenDinner()) {
                output.println("VICTORY");
                opponent.output.println("DEFEAT");
            }
        } catch (IllegalStateException e) {
            output.println("MESSAGE " + e.getMessage());
        }
    }
	
	/**
	 * Processes add commands and gives client a thumbs up if the add is valid
	 * @param xloc
	 * @param yloc
	 * @param shipNum
	 */
	private void processAddCommand(int xloc, int yloc, int shipNum) {
		try {
			Server.add(xloc, yloc, shipNum, this);
			output.println("SHIP_ADDED " + xloc + yloc + shipNum);
			
		} catch (IllegalStateException e) {
			output.println("MESSAGE " + e.getMessage());
		} 
	}
	
	/**
	 * Processes login commands and tells client if the user is authenticated
	 * @param login
	 * 
	 */
	private void processLoginCommand(String login) {
		try {
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(login);
			
			if (m.find()) {
				username = m.group(1);
				password = m.group(2);
			}
			
			if (Server.login(username, password, this)) {
				output.println("AUTH_TRUE");
			}
			else {
				output.println("AUTH_FALSE");
			}
				
			
			//System.out.println(loginInfo);
			
		} catch (IllegalStateException e) {
			output.println("MESSAGE " + e.getMessage());
		}
		
	}
	
	/**
	 * Processes login commands and tells client if the user is authenticated
	 * @param login
	 * 
	 */
	private void processRegisterCommand(String login) {
		try {
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(login);
			
			if (m.find()) {
				username = m.group(1);
				password = m.group(2);
			}
			
			if (Server.register(username, password, this)) {
				output.println("REG_TRUE");
			}
			else {
				output.println("REG_FALSE");
			}
				
			
			//System.out.println(loginInfo);
			
		} catch (IllegalStateException e) {
			output.println("MESSAGE " + e.getMessage());
		}
		
	}
	
}