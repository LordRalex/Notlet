package net.ae97.notlet.client;

import java.io.IOException;
import java.net.Socket;
import javax.net.SocketFactory;

// Method needs to be called from class.
public class ClientToServer {

    public Socket makeSocket() throws IOException {

        return SocketFactory.getDefault().createSocket("notlet.ae97.net", 9687);
    }
}
