package battleship;

import java.awt.Font;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Scanner;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Client {
	
	public JFrame frame = new JFrame("Battleships");
	private JLabel messageLabel = new JLabel("Add your carrier");
	
	private Square[][] primaryboard = new Square[10][10];
	private Square[][] trackboard = new Square[10][10];
	private Square currentSquare;
	
	private Socket socket;
	private Scanner in;
	private PrintWriter out;
	public int shipNum = 0;
	
	public Client(String serverAddress, int port) throws Exception {
		
		socket = new Socket(serverAddress, port);
		in = new Scanner(socket.getInputStream());
		out = new PrintWriter(socket.getOutputStream(), true);
		
		messageLabel.setBackground(Color.lightGray);
		frame.getContentPane().add(messageLabel, BorderLayout.CENTER);
		
		JPanel boardPanel = new JPanel();
		boardPanel.setBackground(Color.black);
		boardPanel.setLayout(new GridLayout(10, 10, 2, 2));
		
		JPanel trackPanel = new JPanel();
		trackPanel.setBackground(Color.green);
		trackPanel.setLayout(new GridLayout(10, 10, 2, 2));
		
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
		                    out.println("MOVE " + k + l);
						}
	                }
				});
				trackboard[i][j].addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent e) {
	                    currentSquare = trackboard[k][l];
	                    if (shipNum <= 4) {
	                    	out.println("ADD " + k + l + shipNum);
	                    	
	                    }
	                }
				});
				boardPanel.add(primaryboard[i][j]);
				trackPanel.add(trackboard[i][j]);
			}
			frame.getContentPane().add(trackPanel, BorderLayout.CENTER);
			frame.getContentPane().add(boardPanel, BorderLayout.CENTER);
		}
		
	}
	
	public void play() throws Exception {
		try {
			String response = in.nextLine();
			char playerNum = response.charAt(8);
			char opponentNum = playerNum == '1' ? '2' : '1';
			frame.setTitle("Battleship: Player " + playerNum);
			
			while (in.hasNextLine()) {
				response = in.nextLine();
				
				if (response.startsWith("VALID_MOVE")) {
					messageLabel.setText("Missed! Waiting for opponent...");
					currentSquare.setText(playerNum);
					currentSquare.repaint();
				}
				else if (response.startsWith("SHIP_ADDED")) {
					int xloc = Integer.parseInt(response.substring(11,12));
					int yloc = Integer.parseInt(response.substring(12,13));
					int shipNum = Integer.parseInt(response.substring(13,14));
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
					//currentSquare.setShip();
					//currentSquare.repaint();
				}
				else if (response.startsWith("OPPONENT_MOVED")) {
					int xloc = Integer.parseInt(response.substring(15,16));
					int yloc = Integer.parseInt(response.substring(16,17));
					trackboard[xloc][yloc].setText(opponentNum);
					trackboard[xloc][yloc].repaint();
					messageLabel.setText("Opponent attacked, your turn...");
				}
				else if (response.startsWith("HIT")) {
					messageLabel.setText("You scored a hit! Waiting for opponent...");
					currentSquare.hitShip();
					currentSquare.repaint();
				}
				else if (response.startsWith("OPPONENT_HIT")) {
					messageLabel.setText("One of your ships been hit! Your turn to attack...");
				}
				else if (response.startsWith("VICTORY")) {
					JOptionPane.showMessageDialog(frame, "Winner, Winner Chicken Dinner!");
                    break;
				}
				else if (response.startsWith("DEFEAT")) {
					JOptionPane.showMessageDialog(frame, "You got owned");
                    break;
				}
				else if (response.startsWith("MESSAGE")) {
					messageLabel.setText(response.substring(8));
				}
				else if (response.startsWith("OTHER_PLAYER_LEFT")) {
					JOptionPane.showMessageDialog(frame, "Other player rage quitted");
					break;
				}
			}
			out.println("QUIT");
			
		} catch (Exception e) {
			System.out.println("Cannot start play(): "+ e);
		}
	}
	
	static class Square extends JPanel {
		JLabel label = new JLabel();
		
		public Square() {
			setBackground(Color.white);
			setLayout(new GridBagLayout());
			label.setFont(new Font("Arial", Font.BOLD, 40));
			add(label);
		}
		
		public void setText(char text) {
			label.setForeground(text == '1' ? Color.BLUE : Color.RED);
			label.setText('X' + "");
		}
		
		public void setShip() {
			setBackground(Color.gray);
		}
		
		public void hitShip() {
			setBackground(Color.orange);
		}
	}

}
