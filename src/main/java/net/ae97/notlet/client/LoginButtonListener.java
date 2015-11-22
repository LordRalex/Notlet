package net.ae97.notlet.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.swing.JOptionPane;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.network.packets.PacketType;
import net.ae97.notlet.network.packets.LoginPacket;
import net.ae97.notlet.network.packets.StartGamePacket;



public class LoginButtonListener implements ActionListener {
	
	private final LoginFrame loginFrame;
	
	public LoginButtonListener(LoginFrame loginFrame) {
		this.loginFrame = loginFrame;
	}
	
       @Override
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
        
        ClientToServer clientToSever = new ClientToServer();
        SSLSocket socket = clientToSever.makeSocket();
        LoginPacket loginPacket = new LoginPacket(username, new String(password));
	StartGamePacket startGamePacket = new StartGamePacket(seed);
        
         try (ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream())) {
            o.writeObject(loginPacket);
                try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                    Packet result = (Packet)in.readObject();
                    if ( result.getType() == PacketType.Success ) {
                        o.writeObject(startGamePacket);
                    } else {
                        JOptionPane.showMessageDialog(null,"Login Failed");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LoginButtonListener.class.getName()).log(Level.SEVERE, null, ex);
                }        
        }   catch (IOException ex) {
                Logger.getLogger(LoginButtonListener.class.getName()).log(Level.SEVERE, null, ex);
            }
       
    }

}
