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
import java.net.Socket;
import java.security.GeneralSecurityException;
import net.ae97.notlet.network.ErrorPacket;
import net.ae97.notlet.network.LoginPacket;
import net.ae97.notlet.network.Packet;
import net.ae97.notlet.network.SuccessPacket;
import net.ae97.notlet.network.stream.EncryptedPacketInputStream;
import net.ae97.notlet.network.stream.EncryptedPacketOutputStream;
import net.ae97.notlet.server.engine.AuthenticationEngine;

public class Client extends Thread {

    private final Socket socket;
    private State state;
    private EncryptedPacketOutputStream out;

    public Client(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        boolean isAlive = true;
        try (Socket connection = socket) {
            try (EncryptedPacketOutputStream o = new EncryptedPacketOutputStream(socket.getOutputStream(), "replace-me")) {
                this.out = o;
                try (EncryptedPacketInputStream in = new EncryptedPacketInputStream(connection.getInputStream(), "replace-me")) {
                    while (isAlive) {
                        Packet packet = in.readPacket();
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
                    }
                }
            }
        } catch (IOException | GeneralSecurityException ex) {
        } finally {
            out = null;
        }
    }

    public void sendPacket(Packet p) throws IOException {
        out.sendPacket(p);
    }

    private enum State {

        Login,
        Game
    }

}
