/*
 * The MIT License
 *
 * Copyright 2015 Joshua.
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
package net.ae97.notlet.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import net.ae97.notlet.network.ErrorPacket;
import net.ae97.notlet.network.LoginPacket;
import net.ae97.notlet.network.Packet;
import net.ae97.notlet.network.SuccessPacket;
import net.ae97.notlet.server.engine.AuthenticationEngine;

public class Client extends Thread {

    private final Socket socket;
    private State state;
    private ObjectOutputStream out;

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        boolean isAlive = true;
        try (Socket connection = socket) {
            try (ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream())) {
                this.out = o;
                try (ObjectInputStream in = new ObjectInputStream(connection.getInputStream())) {
                    while (isAlive) {
                        try {
                            Packet packet = (Packet) in.readObject();
                            switch (packet.getType()) {
                                case Login: {
                                    if (state != State.Login) {
                                        break;
                                    }
                                    LoginPacket login = (LoginPacket) packet;
                                    if (AuthenticationEngine.validate(login.getUser(), login.getPassword())) {
                                        sendPacket(new SuccessPacket());
                                        state = State.Game;
                                    } else {
                                        sendPacket(new ErrorPacket("Invalid username/password"));
                                        isAlive = false;
                                    }
                                }
                                break;
                            }
                        } catch (Exception ex) {
                            //handling packet failed, assuming that we can still run though
                            //however, if any of the sockets are closed, we need to terminate
                            if (socket.isInputShutdown() || socket.isOutputShutdown() || socket.isClosed()) {
                                isAlive = false;
                            }
                        }
                    }
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } finally {
            out = null;
        }
    }

    public synchronized void sendPacket(Packet p) throws IOException {
        out.writeObject(p);
    }

    private enum State {

        Login,
        Game
    }

}
