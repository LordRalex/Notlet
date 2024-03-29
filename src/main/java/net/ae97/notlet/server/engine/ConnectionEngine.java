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
package net.ae97.notlet.server.engine;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Level;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import net.ae97.notlet.server.ServerCore;

public class ConnectionEngine extends Thread {

    private final String host;
    private final int port;

    public ConnectionEngine(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        ServerCore.getLogger().log(Level.INFO, "Starting server on " + host + ":" + port);
        try (SSLServerSocket server = (SSLServerSocket) SSLServerSocketFactory.getDefault().createServerSocket(port, 5, InetAddress.getByName(host))) {
            server.setEnabledCipherSuites(new String[]{"TLS_DHE_RSA_WITH_AES_128_CBC_SHA256"});
            try {
                while (!server.isClosed()) {
                    Socket socket = server.accept();
                    ServerCore.getLogger().log(Level.INFO, "Client connection [" + socket.getRemoteSocketAddress() + "]");
                    ClientEngine client = new ClientEngine(socket);
                    client.start();
                }

            } catch (IOException ex) {
                ServerCore.getLogger().log(Level.SEVERE, "Error on client creation", ex);
            }
        } catch (Exception ex) {
            ServerCore.getLogger().log(Level.SEVERE, "Error on server creation", ex);
        }
    }

}
