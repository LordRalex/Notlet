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
import javax.swing.JOptionPane;
import net.ae97.notlet.client.GameInstance;
import net.ae97.notlet.network.packets.EndGamePacket;
import net.ae97.notlet.network.packets.EntityDamagePacket;
import net.ae97.notlet.network.packets.EntityDeathPacket;
import net.ae97.notlet.network.packets.EntityLocationUpdatePacket;
import net.ae97.notlet.network.packets.EntitySpawnPacket;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.network.packets.StartLevelPacket;

public class GameProcessor extends Thread {

    private final ServerConnection connection;

    public GameProcessor(ServerConnection connection) {
        super();
        this.connection = connection;
    }

    @Override
    public void run() {
        try {
            while (!connection.isClosed() && !this.isInterrupted()) {
                Packet next = connection.readPacket();
                switch (next.getType()) {
                    case EndGame: {
                        interrupt();
                        GameInstance.unload();
                        JOptionPane.showMessageDialog(null, "Score: " + ((EndGamePacket) next).getFinalScore());
                        System.exit(0);
                    }
                    break;
                    case EndLevel: {
                        GameInstance.unload();
                        break;
                    }
                    case StartLevel: {
                        StartLevelPacket packet = (StartLevelPacket) next;
                        GameInstance.init(packet.getMap(), packet.getEntities(), connection);
                    }
                    break;
                    case EntityLocationUpdate: {
                        EntityLocationUpdatePacket packet = (EntityLocationUpdatePacket) next;
                        GameInstance.updateEntityLocation(packet.getEntityId(), packet.getLocation());
                    }
                    break;
                    case EntitySpawn: {
                        EntitySpawnPacket packet = (EntitySpawnPacket) next;
                        GameInstance.spawnEnemy(packet.getEntity());
                    }
                    break;
                    case EntityDeath: {
                        EntityDeathPacket packet = (EntityDeathPacket) next;
                        GameInstance.removeEntity(packet.getEntityId());
                    }
                    break;
                    case EntityDamage: {
                        EntityDamagePacket packet = (EntityDamagePacket) next;
                        GameInstance.updateEntityHealth(packet.getEntityId(), packet.getDamage());
                    }
                    break;
                }
            }
        } catch (IOException ex) {

        } finally {
            connection.close();
        }
    }

    public void close() {
        connection.close();
        synchronized (this) {
            this.interrupt();
        }
    }

}
