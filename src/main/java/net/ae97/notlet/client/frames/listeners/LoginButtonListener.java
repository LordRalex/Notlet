/*
 * The MIT License
 *
 * Copyright 2015 AE97
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package net.ae97.notlet.client.frames.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import javax.swing.JOptionPane;
import net.ae97.notlet.client.ClientCore;
import net.ae97.notlet.client.frames.LoadScreen;
import net.ae97.notlet.client.frames.LoginFrame;
import net.ae97.notlet.client.network.ServerConnection;
import net.ae97.notlet.network.packets.LoginPacket;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.network.packets.PacketType;

public class LoginButtonListener implements ActionListener {

    private final LoginFrame loginFrame;

    public LoginButtonListener(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String username = loginFrame.getUsernameField().getText().trim();
        String password = new String(loginFrame.getPasswordField().getPassword());
        String seed = loginFrame.getSeedField().getText().trim();

        // If the continuous button is selected, seed should be null
        if (loginFrame.getContinuous().isSelected() == true) {
            seed = null;

            // Otherwise you are doing replay, and which cannot be an empty string
        } else if (loginFrame.getContinuous().isSelected() == false && seed.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please enter a seed");
        }

        try {
            ServerConnection socket = ServerConnection.open();
            socket.sendPacket(new LoginPacket(username, password));
            Packet result = socket.readPacket();
            if (result.getType() == PacketType.Success) {
                LoadScreen loadScreen = new LoadScreen(socket, seed);
                loadScreen.start();
                loginFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(null, "Login Failed");
            }
        } catch (IOException ex) {
            ClientCore.getLogger().log(Level.SEVERE, "Error talking to server", ex);
            JOptionPane.showMessageDialog(null, "An error occurred while trying to talk to server: " + ex.getMessage());
        }
    }

}
