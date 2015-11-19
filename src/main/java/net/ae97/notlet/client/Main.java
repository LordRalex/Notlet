package net.ae97.notlet.client;

import java.awt.EventQueue;

public class Main {

	public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            
            @Override
            public void run() {
                LoginFrame login = new LoginFrame();
                login.setVisible(true);
            }
        });

	}

}
