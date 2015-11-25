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

import net.ae97.notlet.client.frames.listeners.LoginButtonListener;
import net.ae97.notlet.client.frames.listeners.ContinuousButtonListener;
import net.ae97.notlet.client.frames.listeners.ReplayButtonListener;
import net.ae97.notlet.client.frames.listeners.RegisterButtonListener;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class LoginFrame extends JFrame {

    private final JButton loginButton;
    private final JPanel panel;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final JLabel usernameLabel, passwordLabel;
    private final JRadioButton continuous, replay;
    private final ButtonGroup group;
    private final JLabel seed;
    private final JTextField seedField;
    private final JButton registerButton;

    public LoginFrame() {
        panel = new JPanel();
        panel.setLayout(null);

        loginButton = new JButton("Start!");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        usernameLabel = new JLabel("Username");
        passwordLabel = new JLabel("Password");
        continuous = new JRadioButton("continuous");
        replay = new JRadioButton("replay");
        group = new ButtonGroup();
        registerButton = new JButton("Register");

        seed = new JLabel("Seed");
        seedField = new JTextField();

        group.add(continuous);
        group.add(replay);

        setTitle("Notlet Login");
        setSize(300, 200);
        setLocation(500, 280);

        usernameLabel.setBounds(10, 10, 80, 25);
        passwordLabel.setBounds(10, 40, 80, 25);
        usernameField.setBounds(100, 10, 160, 25);
        passwordField.setBounds(100, 40, 160, 25);
        loginButton.setBounds(93, 130, 80, 25);
        continuous.setBounds(10, 70, 120, 25);
        replay.setBounds(192, 70, 80, 25);
        registerButton.setBounds(180, 130, 100, 25);

        seedField.setBounds(100, 100, 160, 25);
        seed.setBounds(10, 100, 80, 25);
        panel.add(seed);
        panel.add(seedField);

        seedField.setVisible(false);
        seed.setVisible(false);

        panel.add(passwordLabel);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(continuous);
        panel.add(replay);
        panel.add(registerButton);

        continuous.setSelected(true);

        ReplayButtonListener listener = new ReplayButtonListener(this);
        replay.addActionListener(listener);

        ContinuousButtonListener clistener = new ContinuousButtonListener(this);
        continuous.addActionListener(clistener);

        LoginButtonListener loginListener = new LoginButtonListener(this);
        loginButton.addActionListener(loginListener);

        RegisterButtonListener registerListener = new RegisterButtonListener(this);
        registerButton.addActionListener(registerListener);

        getContentPane().add(panel);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JLabel getSeed() {
        return seed;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }

    public JRadioButton getContinuous() {
        return continuous;
    }

    public JTextField getSeedField() {
        return seedField;
    }

}
