package battleship;

import java.awt.GridLayout;
import javax.swing.JFrame;

public class DriverClient {
	public static void main(String[] args) throws Exception {
		Client client = new Client("192.168.1.6", 6112);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(800, 800);
        client.frame.setLayout(new GridLayout(2, 2));
        client.frame.setVisible(true);
        client.frame.setResizable(false);
        client.play();
	}

}
