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
package net.ae97.notlet.client.frames;

import net.ae97.notlet.client.Testing;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import net.ae97.notlet.client.ClientCore;
import net.ae97.notlet.client.network.ServerConnection;
import net.ae97.notlet.network.packets.StartGamePacket;

public class LoadScreen extends JFrame {

    private final ServerConnection connection;
    private final String seed;

    public LoadScreen(ServerConnection connection, String seed) {
        super();
        this.connection = connection;
        this.seed = seed;
        setTitle("Notlet");
        setSize(500, 150);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-this.getSize().width/2, dim.height/2-this.getSize().height/2); 
        setLayout(new BorderLayout());
        setContentPane(new JLabel(new ImageIcon(getClass().getResource("/BIGLET.png"))));
        setLayout(new FlowLayout());
        repaint();
        
    }

    public void start() {
        new Thread(() -> {
            try {
                synchronized (this) {
                    wait(3000);
                }
                StartGamePacket startGamePacket = new StartGamePacket(seed);
                connection.sendPacket(startGamePacket);
                dispose();
                Testing.display();
            } catch (Exception ex) {
                ClientCore.getLogger().log(Level.SEVERE, "Error on starting game", ex);
                JOptionPane.showMessageDialog(null, "Error on starting: " + ex.getMessage());
            }
        }).start();
    }

}
