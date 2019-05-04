/*
 * Client.java
 * @author - Jonathan Lee
 * @date - April 4th, 2019
 * 
 */

package battleship;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.StyledDocument;
import javax.swing.JButton;

public class Client {
	
	// Create JFrame and JLabel for client
	public JFrame frame = new JFrame("Battleships");
	private JLabel messageLabel = new JLabel("Add your carrier");
	ClientLogin clientLogin = new ClientLogin();
	JTextPane chatboxField = new JTextPane();
	JTextField chatTextField = new JTextField(20);
	StyledDocument doc = chatboxField.getStyledDocument();
	
	// Create double arrays to keep track of primary and tracking boards
	private static Square[][] primaryboard = new Square[10][10];
	private static Square[][] trackboard = new Square[10][10];
	private static Square currentSquare;
	
	// Attributes for networking; Sending stuff in and out between client and server
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	public int shipNum = 0;	// Identifies the ship type
	
	/**
	 * Creates client object
	 * Also responsible in GUI and mouse
	 * @param serverAddress
	 * @param port
	 * @throws Exception
	 */
	public Client(String serverAddress, int port) throws Exception {
		
		// Initialize networking with the server
		socket = new Socket(serverAddress, port);
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream(), true);
		
		// Creates a client login window
		clientLogin.loginWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientLogin.loginWindow.setSize(300, 110);
		clientLogin.loginWindow.setVisible(true);
		clientLogin.loginWindow.setResizable(false);	// Disabled the ability to resize cause it makes the squares look weird
		
		// Create label for the text display
		messageLabel.setBackground(Color.lightGray);
		frame.getContentPane().add(messageLabel, BorderLayout.CENTER);
		
		// Create JPanel for the primary board
		JPanel boardPanel = new JPanel();
		boardPanel.setBackground(Color.black);
		boardPanel.setLayout(new GridLayout(10, 10, 2, 2));
		
		// Create JPanel for the tracking board
		JPanel trackPanel = new JPanel();
		trackPanel.setBackground(Color.green);
		trackPanel.setLayout(new GridLayout(10, 10, 2, 2));
		
		// Create JPanel for the chatbox
		JPanel chatPanel = new JPanel();
		chatPanel.setBackground(Color.gray);
		chatboxField.setEditable(false);
		chatPanel.setLayout(new GridLayout(2, 1));
		chatPanel.add(chatboxField);
		chatPanel.add(chatTextField);
		
		

		chatTextField.setText("Chat Here");
		
		frame.pack();
		
		chatTextField.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	try {
					//doc.insertString(0, chatTextField.getText()+"\n", null);
					doc.insertString(doc.getLength(), "\nYou: "+chatTextField.getText(), null);
					out.println("CHAT "+chatTextField.getText());
					chatTextField.setText("");
				} catch (Exception i) {
					System.out.println(i);
				}
		    }
		});
		
		// Adds listener to client login button
		clientLogin.btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				out.println("LOGIN "+clientLogin.getUsername()+"-"+clientLogin.getPassword());
            }
		});
		
		// Adds listener to client register button
				clientLogin.btnRegister.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						out.println("REGISTER "+clientLogin.getUsername()+"-"+clientLogin.getPassword());
		            }
				});
		
		// Create Square objects in a grid with each individual square having its own clickable object
		// Basically a setup for the UI
		for (int i = 0; i < primaryboard.length; i++) {
			for (int j = 0; j < primaryboard.length; j++) {
				final int k = i;
				final int l = j;
				primaryboard[i][j] = new Square();
				trackboard[i][j] = new Square();
				primaryboard[i][j].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
						if (shipNum <= 4) {
							messageLabel.setText("Finish adding your ships");
						} else {
							currentSquare = primaryboard[k][l];
		                    out.println("MOVE " + k + l);	// Mouse press indicates an attack has been made
						}
	                }
				});
				trackboard[i][j].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
	                    currentSquare = trackboard[k][l];
	                    if (shipNum <= 4) {
	                    	out.println("ADD " + k + l + shipNum); // Mouse press indicates an add has been made
	                    	
	                    }
	                }
				});
				boardPanel.add(primaryboard[i][j]);
				trackPanel.add(trackboard[i][j]);
			}
			frame.getContentPane().add(trackPanel, BorderLayout.CENTER);
			frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
			frame.getContentPane().add(chatPanel, BorderLayout.CENTER);
		}
		
	}
	
	/**
	 * Initializes client processes. Client will begin a stream of communication with the server 
	 * @throws Exception
	 */
	public void play() throws Exception {
		try {
			String response = in.nextLine();	// Read from socket
			char playerNum = response.charAt(8);
			char opponentNum = playerNum == '1' ? '2' : '1';	// Determines who's player 1 or 2 (it's first come, first serve)
			frame.setTitle("Battleship: Player " + playerNum);
			boolean playerAuth = false;
			
			while (in.hasNextLine()) {
				response = in.nextLine();
				
				if (playerAuth == true) {
					// If server responds with a valid move string (without a hit), client repaints that square
					if (response.startsWith("VALID_MOVE")) {
						messageLabel.setText("Missed! Waiting for opponent...");
						currentSquare.setText(playerNum);
						currentSquare.repaint();
					}
					// Server confirms that selected ship can be added during the add ship phase
					else if (response.startsWith("SHIP_ADDED")) {
						int xloc = Integer.parseInt(response.substring(11,12));
						int yloc = Integer.parseInt(response.substring(12,13));
						int shipNum = Integer.parseInt(response.substring(13,14));
						
						/*
						 *  This could be written better, but it works for now
						 *  Adds the ship during the add phase
						 *  shipNum = 0 is the carrier (5 blocks)
						 *	shipNum = 1 is the battleship (4 blocks)
						 *  shipNum = 2 is the cruiser (3 blocks)
						 *  shipNum = 3 is the submarine (3 blocks)
						 *  shipNum = 4 is the destroyer (2 blocks)
						 */
						if (shipNum == 0) {
							for (int i = 0; i <= 4; i++) {
								trackboard[xloc][yloc+i].setShip();
								trackboard[xloc][yloc+i].repaint();
							}
							messageLabel.setText("Add your battleship");
						}
						else if (shipNum == 1) {
							for (int i = 0; i <= 3; i++) {
								trackboard[xloc][yloc+i].setShip();
								trackboard[xloc][yloc+i].repaint();
							}
							messageLabel.setText("Add your cruiser");
						}
						else if (shipNum == 2) {
							for (int i = 0; i <= 2; i++){
								trackboard[xloc][yloc+i].setShip();
								trackboard[xloc][yloc+i].repaint();
							}
							messageLabel.setText("Add your submarine");
						}
						else if (shipNum == 3) {
							for (int i = 0; i <= 2; i++){
								trackboard[xloc][yloc+i].setShip();
								trackboard[xloc][yloc+i].repaint();
							}
							messageLabel.setText("Add your destroyer");
						}
						else if (shipNum == 4) {
							for (int i = 0; i <= 1; i++){
								trackboard[xloc][yloc+i].setShip();
								trackboard[xloc][yloc+i].repaint();
							}
						}
						this.shipNum++;
					}
					// If client receives "opponent moved" string from server, client repaints the coordinates to the tracking board
					else if (response.startsWith("OPPONENT_MOVED")) {
						int xloc = Integer.parseInt(response.substring(15,16));
						int yloc = Integer.parseInt(response.substring(16,17));
						trackboard[xloc][yloc].setText(opponentNum);
						trackboard[xloc][yloc].repaint();
						messageLabel.setText("Opponent attacked, your turn...");
					}
					//  If client receives "hit" string from server, client repaints the coordinates to the primary board
					else if (response.startsWith("HIT")) {
						messageLabel.setText("You scored a hit! Waiting for opponent...");
						currentSquare.hitShip();
						currentSquare.repaint();
					}
					//  If client receives "opponent_hit" string from server, client repaints the coordinates to the tracking board
					else if (response.startsWith("OPPONENT_HIT")) {
						messageLabel.setText("One of your ships been hit! Your turn to attack...");
					}
					//  If client receives "victory" string from server, client celebrates by popping a dialog
					else if (response.startsWith("VICTORY")) {
						JOptionPane.showMessageDialog(frame, "Winner, Winner Chicken Dinner!");
	                    break;
					}
					//  If client receives "defeat" string from server, client mocks you
					else if (response.startsWith("DEFEAT")) {
						JOptionPane.showMessageDialog(frame, "You got owned");
	                    break;
					}
					// If client receives "message" string from server, client will display text after the preceding "MESSAGE" string using a label
					else if (response.startsWith("MESSAGE")) {
						messageLabel.setText(response.substring(8));
					}
					// If client receives "other_player_left" string from server, client will notify that the other player left and will close the socket
					else if (response.startsWith("OTHER_PLAYER_LEFT")) {
						JOptionPane.showMessageDialog(frame, "Other player rage quitted");
						break;
					}
					else if (response.startsWith("CHAT")) {
						doc.insertString(doc.getLength(), "\nOpponent: "+response.substring(5), null);
					}
				}
				else if(playerAuth == false) {
					if (response.startsWith("AUTH_TRUE")) {
						playerAuth = true;
						frame.setVisible(true);
						clientLogin.loginWindow.dispose();
					}
					else if (response.startsWith("AUTH_FALSE")) {
						JOptionPane.showMessageDialog(frame, "No existing username or wrong password");
					}
					else if (response.startsWith("REG_TRUE")) {
						JOptionPane.showMessageDialog(frame, "Registered! Login again.");
					}
					else if (response.startsWith("REG_FALSE")) {
						JOptionPane.showMessageDialog(frame, "Username already exists!");
					}
				}
			}
			out.println("QUIT"); // Send out "quit" string to let server know that client is quitting
			
		} catch (Exception e) {
			System.out.println("Cannot start play(): "+ e);
		}
	}
	


}
