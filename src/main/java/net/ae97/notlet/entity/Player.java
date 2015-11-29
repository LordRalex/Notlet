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
package net.ae97.notlet.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import net.ae97.notlet.Direction;
import net.ae97.notlet.Location;
import net.ae97.notlet.network.packets.AttackRequestPacket;
import net.ae97.notlet.network.packets.EntitySpawnPacket;
import net.ae97.notlet.network.packets.MoveRequestPacket;
import net.ae97.notlet.network.packets.Packet;
import net.ae97.notlet.server.engine.GameEngine;
import net.ae97.notlet.server.level.Level;

/**
 * Created by john on 11/22/15.
 */
public class Player extends Entity {

    private final Queue<Packet> requests = new LinkedList<>();
    private int score;
    private int attackCooldown;
    private Direction direction = Direction.DOWN;
    private transient final GameEngine engine;

    public Player(Location loc, GameEngine engine) {
        super(loc, 100, 200, "rangerD", 0.06, 32, .93);
        this.engine = engine;
    }

    @Override
    public void processTick(Level level) {
        synchronized (requests) {
            List<Packet> uniqueRequests = selectFirstUniquePackets(requests);
            for (Packet packet : uniqueRequests) {
                switch (packet.getType()) {
                    case MoveRequest: {
                        MoveRequestPacket request = (MoveRequestPacket) packet;
                        Location old = getLocation();
                        Location newLocation = old;
                        switch (request.getDirection()) {
                            case LEFT:
                                newLocation = new Location(old.getX() - getMovementSpeed(), old.getY());
                                break;
                            case RIGHT:
                                newLocation = new Location(old.getX() + getMovementSpeed(), old.getY());
                                break;
                            case UP:
                                newLocation = new Location(old.getX(), old.getY() - getMovementSpeed());
                                break;
                            case DOWN:
                                newLocation = new Location(old.getX(), old.getY() + getMovementSpeed());
                                break;
                        }
                        if (level.isPassable(newLocation, new Location(newLocation.getX() + getBlockSize(), newLocation.getY() + getBlockSize()))) {
                            setLocation(newLocation);
                        }
                    }
                    break;
                    case AttackRequest: {
                        if (attackCooldown != 0) {
                            break;
                        }
                        AttackRequestPacket request = (AttackRequestPacket) packet;
                        Location old = getLocation();
                        Location spawnLocation = old;
                        switch (request.getDirection()) {
                            case LEFT:
                                spawnLocation = new Location(old.getX() - .1, old.getY());
                                break;
                            case RIGHT:
                                spawnLocation = new Location(old.getX() + getBlockSize() + .1, old.getY());
                                break;
                            case UP:
                                spawnLocation = new Location(old.getX(), old.getY() - .1);
                                break;
                            case DOWN:
                                spawnLocation = new Location(old.getX(), old.getY() + getBlockSize() + .1);
                                break;
                        }
                        if (level.isPassable(spawnLocation, new Location(spawnLocation.getX() + getBlockSize(), spawnLocation.getY() + getBlockSize()))) {
                            Arrow arrow = new Arrow(spawnLocation, request.getDirection());
                            level.spawnEntity(arrow);
                            engine.sendPacket(new EntitySpawnPacket(arrow));
                            attackCooldown = 10;
                        }
                    }
                    break;
                }
            }
            requests.clear();
        }
        if (attackCooldown > 0) {
            attackCooldown--;
        }
    }

    public void addToQueue(Packet p) {
        synchronized (requests) {
            requests.add(p);
        }
    }

    public void addScore(int value) {
        score += value;
    }

    public int getScore() {
        return score;
    }

    public Direction getFacingDirection() {
        return direction;
    }

    public void setFacingDirection(Direction direction) {
        this.direction = direction;
    }

    @Override
    public String getSprite() {
        switch (direction) {
            case UP:
                return "rangerU";
            case DOWN:
                return "rangerD";
            case LEFT:
                return "rangerL";
            case RIGHT:
                return "rangerR";
            default:
                return "rangerD";
        }
    }

    private List<Packet> selectFirstUniquePackets(Queue<Packet> packetRequests) {
        List<Packet> packets = new LinkedList<>();
        boolean unique;
        for (Packet p : packetRequests) {
            unique = true;
            for (Packet t : packets) {
                if (p.isEqual(t)) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                packets.add(p);
            }
        }

        return packets;
    }
}
