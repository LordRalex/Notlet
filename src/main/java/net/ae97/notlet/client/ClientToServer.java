package net.ae97.notlet.client;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

// Method needs to be called from class.
public class ClientToServer {
    
    private SSLSocket sslSocket;
    
    public SSLSocket makeSocket() {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try {
            sslSocket = (SSLSocket) sslSocketFactory.createSocket("notlet.ae97.net", 9687);
        } catch (IOException ex) {
            Logger.getLogger(ClientToServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return(sslSocket);
    }
}
