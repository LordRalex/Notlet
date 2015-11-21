
package net.ae97.notlet.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReplayButtonListener implements ActionListener {
	
	private final LoginFrame loginFrame;
	
	public ReplayButtonListener(LoginFrame loginFrame) {
		this.loginFrame = loginFrame;
	}
	
    public void actionPerformed(ActionEvent e) {
        loginFrame.getSeedField().setVisible(true);
        loginFrame.getSeed().setVisible(true);    
    }

}
