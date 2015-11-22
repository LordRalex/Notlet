package net.ae97.notlet.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.network.packets.PacketType;
import net.ae97.notlet.network.packets.RegisterPacket;

public class RegisterButtonListener implements ActionListener {

    private final LoginFrame loginFrame;

    public RegisterButtonListener(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username;
        char[] password;

        username = loginFrame.getUsernameField().getText().trim();
        password = loginFrame.getPasswordField().getPassword();
        RegisterPacket registerPacket = new RegisterPacket(username, new String(password));

        ClientToServer clientToSever = new ClientToServer();
        try (Socket socket = clientToSever.makeSocket()) {
            try (ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {
                try (ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream())) {
                    o.writeObject(registerPacket);
                    Packet result = (Packet) in.readObject();
                    if (result.getType() == PacketType.Success) {
                        JOptionPane.showMessageDialog(null, "Registration Successful");
                    } else {
                        JOptionPane.showMessageDialog(null, "Registration Failed");
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LoginButtonListener.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(LoginButtonListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
