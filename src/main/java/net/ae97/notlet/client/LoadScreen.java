package net.ae97.notlet.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class LoadScreen extends JFrame {
	
	public LoadScreen() {
		setTitle("Notlet");
		setSize(500,150);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUndecorated(true);
		setVisible(true);
		setLayout(new BorderLayout());
	    setContentPane(new JLabel(new ImageIcon("/home/x/Downloads/BIGLET.png")));
	    setLayout(new FlowLayout());
	    repaint();
	    
        new Thread(()->{try {
						synchronized(this){wait(3000);}
						dispose();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}}).start();
	}
	
	
}
