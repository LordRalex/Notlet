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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import javax.net.ssl.SSLServerSocketFactory;
import net.ae97.notlet.server.Client;
import net.ae97.notlet.server.CoreServer;

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
        CoreServer.getLogger().log(Level.INFO, "Starting server on {0}:{1}", new String[]{host, Integer.toString(port)});
        try (ServerSocket server = SSLServerSocketFactory.getDefault().createServerSocket(port, 5, InetAddress.getByName(host))) {
            Socket socket = server.accept();
            CoreServer.getLogger().log(Level.INFO, "Client connection [{0}]", socket.getRemoteSocketAddress());
            Client client = new Client(socket);
            client.start();
        } catch (IOException ex) {
            CoreServer.getLogger().log(Level.SEVERE, "Error on server creation", ex);
        }
    }

}
