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
package net.ae97.notlet.client.network;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.net.SocketFactory;
import net.ae97.notlet.network.packets.Packet;

public class ServerConnection implements AutoCloseable {

    private static SocketFactory socketFactory = SocketFactory.getDefault();
    private final Socket socket;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private ServerConnection() throws IOException {
        socket = socketFactory.createSocket("notlet.ae97.net", 9687);
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
    }

    public static ServerConnection open() throws IOException {
        ServerConnection conn = new ServerConnection();
        return conn;
    }

    public void sendPacket(Packet packet) throws IOException {
        out.writeObject(packet);
    }

    public Packet readPacket() throws IOException {
        try {
            return (Packet) in.readObject();
        } catch (ClassNotFoundException ex) {
            throw new IOException(ex);
        }
    }

    public boolean isClosed() {
        return socket.isClosed();
    }

    @Override
    public void close() {
        try {
            in.close();
        } catch (IOException ex) {
        }
        try {
            out.close();
        } catch (IOException ex) {
        }
        try {
            socket.close();
        } catch (IOException ex) {
        }
    }

}
