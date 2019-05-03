package battleship;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagLayout;


import javax.swing.JLabel;
import javax.swing.JPanel;

class Square extends JPanel {
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
