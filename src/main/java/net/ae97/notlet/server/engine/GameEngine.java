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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import net.ae97.notlet.GlobalIdentification;
import net.ae97.notlet.Location;
import net.ae97.notlet.entity.Player;
import net.ae97.notlet.logging.LoggerFactory;
import net.ae97.notlet.network.packets.EndGamePacket;
import net.ae97.notlet.network.packets.EndLevelPacket;
import net.ae97.notlet.network.packets.EntityLocationUpdatePacket;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.network.packets.StartLevelPacket;
import net.ae97.notlet.server.level.Level;

public class GameEngine implements Runnable {

    //Indicates which game number is the newest
    private static final GlobalIdentification engineCounter = new GlobalIdentification();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Logger logger;
    private final int threadId;
    private Level level;
    private final boolean isSingleLevel;
    private final ClientEngine client;
    private Player player;
    private Location endPoint;

    public GameEngine(ClientEngine client, String seed) {
        this.client = client;
        threadId = engineCounter.next();
        logger = LoggerFactory.create("Engine-" + threadId);
        if (seed == null || seed.isEmpty()) {
            isSingleLevel = false;
            level = new Level();
        } else {
            isSingleLevel = true;
            level = new Level(seed.hashCode());
        }
        player = new Player(new Location(0, 0));
        level.spawnEntity(player);
    }

    /**
     * Start the game engine
     */
    public void start() throws IOException {
        level.generate();
        endPoint = new Location(level.getSize(), level.getSize());
        client.sendPacket(new StartLevelPacket(level.getMap(), level.getEntities()));
        executor.scheduleAtFixedRate(this, 0, 25, TimeUnit.MILLISECONDS);
        logger.info("Started");
    }

    /**
     * Executes a single 'tick'
     */
    @Override
    public void run() {
        try {
            level.getEntities().stream().forEach((entity) -> {
                Location previous = entity.getLocation();
                entity.processTick(level);
                if (!previous.equals(entity.getLocation())) {
                    sendPacket(new EntityLocationUpdatePacket(entity.getId(), entity.getLocation()));
                }
            });
            level.getEntities().stream().filter((en) -> (en.getHp() <= 0)).forEach((en) -> {
                if (!(en instanceof Player)) {
                    return;
                }
                player.addScore(en.getValue());
            });
            if (player.getHp() <= 0) {
                sendPacket(new EndGamePacket(player.getScore()));
                stop();

                return;
            }
            level.processTick();
            if (player.isAt(endPoint)) {
                sendPacket(new EndLevelPacket());
                if (isSingleLevel) {
                    sendPacket(new EndGamePacket(player.getScore()));
                    stop();
                } else {
                    level = new Level();
                    level.generate();
                    endPoint = new Location(level.getSize(), level.getSize());
                    player.setLocation(new Location(0, 0));
                    level.spawnEntity(player);
                    sendPacket(new StartLevelPacket(level.getMap(), level.getEntities()));
                }
            }
        } catch (Exception ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error on handling tick", ex);
        }
    }

    /**
     * Stops future ticks from occurring
     */
    public void stop() {
        logger.info("Stopping");
        synchronized (client) {
            client.interrupt();
        }
        executor.shutdown();
    }

    public void handleGamePacket(Packet p) {
        player.addToQueue(p);
    }

    private void sendPacket(Packet p) {
        try {
            client.sendPacket(p);
        } catch (IOException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Error on sending packet", ex);
        }
    }
}
