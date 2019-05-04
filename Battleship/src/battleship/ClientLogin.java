package battleship;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.JPasswordField;

public class ClientLogin {
	// Create Login Screen Frame
		public JFrame loginWindow = new JFrame("Welcome to Battleship");
		private JLabel lbUsername = new JLabel("Username: ");
		private JLabel lbPassword = new JLabel("Password: ");
		public JButton btnLogin = new JButton("Login");
		public JButton btnRegister = new JButton("Register");
		private static JTextField tfUsername = new JTextField(20);
		private static JPasswordField pfPassword = new JPasswordField(20);
		private boolean succeeded;
		
		public ClientLogin() {
			// Create login screen
			JPanel loginScreen = new JPanel(new GridBagLayout());
			GridBagConstraints layout = new GridBagConstraints();
			
			layout.fill = GridBagConstraints.HORIZONTAL;
			
			//loginScreen.setBackground(Color.gray);
			
			// Sets up login screen by grid layout
			// Label user name
			layout.gridx = 0;
			layout.gridy = 0;
			layout.gridwidth = 1;
			loginScreen.add(lbUsername, layout);
			
			// Label Password
			layout.gridx = 0;
			layout.gridy = 1;
			layout.gridwidth = 1;
			loginScreen.add(lbPassword, layout);
			
			// Text Field for user name
			layout.gridx = 1;
			layout.gridy = 0;
			layout.gridwidth = 2;
			loginScreen.add(tfUsername, layout);
			
			// Password Field for password
			pfPassword = new JPasswordField(20);
			layout.gridx = 1;
			layout.gridy = 1;
			layout.gridwidth = 2;
			loginScreen.add(pfPassword, layout);
			loginScreen.setBorder(new LineBorder(Color.gray));
			
			
			
			JPanel btnPanel = new JPanel();
			btnPanel.add(btnLogin);
			btnPanel.add(btnRegister);
			
			loginWindow.getContentPane().add(loginScreen, BorderLayout.CENTER);
	        loginWindow.getContentPane().add(btnPanel, BorderLayout.PAGE_END);
			
		}
		
		public static String getUsername() {
			return tfUsername.getText().trim();
		}
		
		public static String getPassword() {
			return new String(pfPassword.getPassword());
			
		}
		
		public boolean isSucceeded() {
			return succeeded;
		}
		
		public boolean authenticate(String username, String password) {
			return true;
		}
	

}
