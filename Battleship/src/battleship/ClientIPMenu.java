package battleship;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class ClientIPMenu {
	// Create IP Screen Frame
			public JFrame loginIP = new JFrame("Connect to server");
			private JLabel lbIP = new JLabel("IP Address: ");
			public JButton btnIP = new JButton("Connect");
			private static JTextField tfIP = new JTextField(20);
			
			public ClientIPMenu() {
				// Create IP menu screen
				JPanel loginScreen = new JPanel(new GridBagLayout());
				GridBagConstraints layout = new GridBagConstraints();
				
				layout.fill = GridBagConstraints.HORIZONTAL;
				
				// Sets up IP screen by grid layout
				// Label IP address
				layout.gridx = 0;
				layout.gridy = 0;
				layout.gridwidth = 1;
				loginScreen.add(lbIP, layout);
				
				// Text Field for IP input
				layout.gridx = 1;
				layout.gridy = 0;
				layout.gridwidth = 2;
				loginScreen.add(tfIP, layout);	
				
				loginScreen.setBorder(new LineBorder(Color.gray));
				
				JPanel btnPanel = new JPanel();
				btnPanel.add(btnIP);
				
				loginIP.getContentPane().add(loginScreen, BorderLayout.CENTER);
				loginIP.getContentPane().add(btnPanel, BorderLayout.PAGE_END);
			}
			
			public static String getIPaddress() {
				
				return tfIP.getText().trim();
			}
}
