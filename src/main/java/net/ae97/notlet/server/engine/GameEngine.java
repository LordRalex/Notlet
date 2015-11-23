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
import net.ae97.notlet.Location;
import net.ae97.notlet.entity.Player;
import net.ae97.notlet.logging.LoggerFactory;
import net.ae97.notlet.network.packets.EndGamePacket;
import net.ae97.notlet.network.packets.EndLevelPacket;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.network.packets.StartLevelPacket;
import net.ae97.notlet.server.Client;
import net.ae97.notlet.server.level.Level;

public class GameEngine implements Runnable {

    //Indicates which game number is the newest
    private static final EngineCounter engineCounter = new EngineCounter();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final Logger logger;
    private final int threadId;
    private Level level;
    private final boolean isSingleLevel;
    private final Client client;
    private Player player;
    private Location endPoint;
    private int tickCount = 0;

    public GameEngine(Client client, String seed) {
        this.client = client;
        threadId = engineCounter.increment();
        logger = LoggerFactory.create("Engine-" + threadId);
        if (seed == null || seed.isEmpty()) {
            isSingleLevel = false;
            level = new Level();
        } else {
            isSingleLevel = true;
            level = new Level(seed.hashCode());
        }
    }

    /**
     * Start the game engine
     */
    public void start() throws IOException {
        level.generate();
        endPoint = new Location(level.getSize(), level.getSize());
        client.sendPacket(new StartLevelPacket(level.getMap(), level.getEntities()));
        executor.scheduleAtFixedRate(this, 0, 25, TimeUnit.MILLISECONDS);
    }

    /**
     * Executes a single 'tick'
     */
    @Override
    public void run() {
        tickCount++;
        level.getEntities().stream().forEach((entity) -> {
            entity.processTick(level);
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
                return;
            } else {
                level = new Level();
                level.generate();
                endPoint = new Location(level.getSize(), level.getSize());
                sendPacket(new StartLevelPacket(level.getMap(), level.getEntities()));
            }
        }
    }

    /**
     * Stops future ticks from occurring
     */
    public void stop() {
        logger.info("Stopping");
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

    private final static class EngineCounter {

        private int counter;

        public synchronized int increment() {
            counter++;
            return counter;
        }

    }

}
