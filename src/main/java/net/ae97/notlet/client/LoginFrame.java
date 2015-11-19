package net.ae97.notlet.client;

import javax.swing.*;

@SuppressWarnings("serial")
public class LoginFrame extends JFrame {
	
	public LoginFrame() {
		initLogin();	
	}
	
	private void initLogin() {
		// game type -> enum
		// seed
		JButton loginButton = new JButton("Start!");
		JPanel panel = new JPanel();
		JTextField usernameField = new JTextField(15);
		JPasswordField passwordField = new JPasswordField(15);
		JLabel usernameLabel = new JLabel("Username");
		JLabel passwordLabel = new JLabel("Password");
		
		JRadioButton continuous = new JRadioButton("continuous");
		JRadioButton replay = new JRadioButton("replay");
		ButtonGroup group = new ButtonGroup();
		group.add(continuous);
		group.add(replay);
		
		setTitle("Notlet Login");
		setSize(300,175);
		setLocation(500,280);
		
		panel.setLayout(null);
		
		usernameLabel.setBounds(10, 10, 80, 25);
		passwordLabel.setBounds(10, 40, 80, 25);
		usernameField.setBounds(100, 10, 160, 25);
		passwordField.setBounds(100, 40, 160, 25);
		loginButton.setBounds(10, 100, 80, 25);
		continuous.setBounds(10,70,120,25);
		replay.setBounds(192,70,80,25);
		
		panel.add(passwordLabel);
		panel.add(usernameLabel);
		panel.add(usernameField);
		panel.add(passwordField);
		panel.add(loginButton);
		panel.add(continuous);
		panel.add(replay);
		
		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
}
