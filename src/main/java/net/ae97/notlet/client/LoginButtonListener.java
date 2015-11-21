package net.ae97.notlet.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

public class LoginButtonListener implements ActionListener {
	
	private final LoginFrame loginFrame;
	
	public LoginButtonListener(LoginFrame loginFrame) {
		this.loginFrame = loginFrame;
	}
	
    public void actionPerformed(ActionEvent e) {
    	String username;
    	char[] password;
    	String seed;
    	username = loginFrame.getUsernameField().getText().trim();
    	password = loginFrame.getPasswordField().getPassword();
		seed = loginFrame.getSeedField().getText().trim();
		
		// If the continuous button is selected, seed should be null
		if ( loginFrame.getContinuous().isSelected() == true ) {
			seed = null;
		// Otherwise you are doing replay, and which cannot be an empty string
		} else if ( loginFrame.getContinuous().isSelected() == false && seed.trim().isEmpty()) {
			JOptionPane.showMessageDialog(null,"Please enter a seed");
		}
		
		// TODO:
		// Send data to server
		// Receive data
			// if credentials checkout, move to game frame
			// if credentials fail, give error and re-prompt
	
		// Testing
		System.out.println(username);
		System.out.println(password);
		System.out.println(seed);
    	
    }

}
