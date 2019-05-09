/*
 * DriverClient.java
 * @author - Jonathan Lee
 * @date - April 4th, 2019
 * 
 */

package battleship;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// Driver class to initiate the client program
public class DriverClient {
	private static Boolean ip = false;
	private static String address;
	
	public static void main(String[] args) throws Exception {
		
		// Prompt for IP address before connection
		ClientIPMenu clientIP = new ClientIPMenu();
		
		clientIP.loginIP.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		clientIP.loginIP.setSize(300, 100);
		clientIP.loginIP.setVisible(true);
		clientIP.loginIP.setResizable(false);

		while(!ip) {
			// Adds listener to client IP button
			clientIP.btnIP.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						address = clientIP.getIPaddress();
						ip = true;
						clientIP.loginIP.setVisible(false);
					} catch (Exception ex) {
						System.out.println(ex);
					}
			     }
			});
		}
		
		try {
			// Default port is 6112 and the IP address can be changed depending on the server address
			Client client = new Client(address, 6112);
			
			client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        client.frame.setSize(800, 800);
	        client.frame.setLayout(new GridLayout(2, 2));	// JFrame GridLayout of 2 columns and 2 rows
	        client.frame.setVisible(false);
	        client.frame.setResizable(false);	// Disabled the ability to resize cause it makes the squares look weird
	        
	        client.play();	// Starts the client game
		} catch  (Exception ex){
			System.out.println(ex);
			JOptionPane.showMessageDialog(clientIP.loginIP, "Cannot find server or invalid IP address");
		}
		
	}

}
