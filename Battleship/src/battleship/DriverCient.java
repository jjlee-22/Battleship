package battleship;

import javax.swing.JFrame;

public class DriverCient {
	public static void main(String[] args) throws Exception {
		Client client = new Client("192.168.1.11", 6112);
		client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setSize(320, 320);
        client.frame.setVisible(true);
        client.frame.setResizable(false);
        client.play();
	}

}
