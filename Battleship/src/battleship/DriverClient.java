/*
 * DriverClient.java
 * @author - Jonathan Lee
 * @date - April 4th, 2019
 * 
 */

package battleship;

import java.awt.GridLayout;
import javax.swing.JFrame;

// Driver class to initiate the client program
public class DriverClient {
	public static void main(String[] args) throws Exception {
		
		// Default port is 6112 and the IP address can be changed depending on the server address
		Client client = new Client("192.168.1.6", 6112);
		
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(800, 800);
        client.frame.setLayout(new GridLayout(2, 2));	// JFrame GridLayout of 2 columns and 2 rows
        client.frame.setVisible(false);
        client.frame.setResizable(false);	// Disabled the ability to resize cause it makes the squares look weird
        
        client.play();	// Starts the client game
	}

}
