package net.ae97.notlet.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ContinuousButtonListener implements ActionListener {
	
	private final LoginFrame loginFrame;
	
	public ContinuousButtonListener(LoginFrame loginFrame) {
		this.loginFrame = loginFrame;
	}
	
    public void actionPerformed(ActionEvent e) {
        loginFrame.getSeedField().setVisible(false);
        loginFrame.getSeed().setVisible(false);    
    }

}
