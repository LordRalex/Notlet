package net.ae97.notlet.client;

import java.io.IOException;
import java.net.Socket;
import javax.net.SocketFactory;

// Method needs to be called from class.
public class ClientToServer {

    public Socket makeSocket() throws IOException {

        return SocketFactory.getDefault().createSocket("192.168.19.10", 9687);
    }
}
