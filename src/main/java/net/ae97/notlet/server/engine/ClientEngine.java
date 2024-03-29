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

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLException;
import net.ae97.notlet.network.packets.ErrorPacket;
import net.ae97.notlet.network.packets.LoginPacket;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.network.packets.RegisterPacket;
import net.ae97.notlet.network.packets.StartGamePacket;
import net.ae97.notlet.network.packets.SuccessPacket;
import net.ae97.notlet.server.ServerCore;

public class ClientEngine extends Thread {

    private final Socket socket;
    private State state = State.Login;
    private ObjectOutputStream out;
    private GameEngine game;
    private String username;

    public ClientEngine(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        boolean isAlive = true;
        try (Socket connection = socket) {
            try (ObjectOutputStream o = new ObjectOutputStream(socket.getOutputStream())) {
                this.out = o;
                try (ObjectInputStream in = new ObjectInputStream(connection.getInputStream())) {
                    while (isAlive && !interrupted()) {
                        try {
                            Packet next = (Packet) in.readObject();
                            switch (next.getType()) {
                                case Login: {
                                    if (state != State.Login) {
                                        break;
                                    }
                                    LoginPacket packet = (LoginPacket) next;
                                    if (AuthenticationEngine.validate(packet.getUser(), packet.getPassword())) {
                                        sendPacket(new SuccessPacket());
                                        username = packet.getUser();
                                        state = State.Pending;
                                    } else {
                                        sendPacket(new ErrorPacket("Invalid username/password"));
                                        isAlive = false;
                                    }
                                }
                                break;
                                case Register: {
                                    if (state != State.Login) {
                                        break;
                                    }
                                    RegisterPacket packet = (RegisterPacket) next;
                                    if (AuthenticationEngine.register(packet.getUser(), packet.getPassword())) {
                                        sendPacket(new SuccessPacket());
                                    } else {
                                        sendPacket(new ErrorPacket("User already exists"));
                                    }
                                    isAlive = false;
                                }
                                break;
                                case StartGame: {
                                    StartGamePacket packet = (StartGamePacket) next;
                                    if (state != State.Pending) {
                                        break;
                                    }
                                    game = new GameEngine(this, packet.getSeed());
                                    game.start();

                                    state = State.Game;
                                }
                                break;
                                default: {
                                    if (state != State.Game || game == null) {
                                        break;
                                    }
                                    game.handleGamePacket(next);
                                }
                            }
                        } catch (EOFException | SocketException | SSLException ex) {
                            isAlive = false;
                        } catch (Exception ex) {
                            ServerCore.getLogger().log(Level.SEVERE, "Error handling client packet", ex);
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
            getLogger().log(Level.SEVERE, "Error on client connection", ex);
        } finally {
            if (game != null) {
                game.stop();
                game = null;
            }
            out = null;
        }
    }

    public synchronized void sendPacket(Packet p) throws IOException {
        out.writeObject(p);
    }

    public Logger getLogger() {
        return ServerCore.getLogger();
    }

    public String getUsername() {
        return username;
    }

    private enum State {

        Pending,
        Login,
        Game
    }

}
