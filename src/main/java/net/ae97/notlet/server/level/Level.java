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
package net.ae97.notlet.server.level;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import net.ae97.notlet.Location;
import net.ae97.notlet.entity.Entity;
import net.ae97.notlet.entity.HealthPotion;
import net.ae97.notlet.entity.Player;
import net.ae97.notlet.entity.PointBooster;
import net.ae97.notlet.entity.Skeleton;
import net.ae97.notlet.entity.Slime;

/**
 * @author John
 */
public class Level {

    private final int seed;
    private boolean[][] map = new boolean[0][0];
    private final List<Entity> entities = new LinkedList<>();
    private final Stack<Entity> toKill = new Stack<>();

    public Level() {
        this(new Random().nextInt());
    }

    public Level(int seed) {
        this.seed = seed;
    }

    /**
     * Generates a boolean 2D array representing levels for the game engine to render
     * True means walkable space
     * False means a wall
     *
     * Currently creates spindally spiderry passages.
     */
    public void generate() {
        Random rng = new Random(seed);
        int size = 100 + (rng.nextInt(50));
        map = new boolean[size][size];

        int i = 0;
        int j = 0;

        map[i][j] = true;

        while (i < size && j < size) {
            if (rng.nextBoolean()) {
                map[i][j] = true;
                i++;
            } else {
                map[i][j] = true;
                j++;
            }
        }

        if (i > j) {
            i--;
            while (j < size) {
                map[i][j] = true;
                j++;
            }
        } else if (i < j) {
            j--;
            while (i < size) {
                map[i][j] = true;
                i++;
            }
        }
        fill(rng);
        populate(rng);
    }

    public boolean[][] getMap() {
        return map;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public boolean isPassable(Location loc) {
        if (loc.getX() < 0 || loc.getY() < 0) {
            return false;
        }
        return map[(int) loc.getX()][(int) loc.getY()];
    }

    public int getSeed() {
        return seed;
    }

    public int getSize() {
        return map.length;
    }

    public void killEntity(Entity entity) {
        synchronized (toKill) {
            toKill.add(entity);
        }
    }

    public void spawnEntity(Entity entity) {
        entities.add(entity);
    }

    public void processTick() {
        synchronized (toKill) {
            entities.removeAll(toKill);
            toKill.clear();
        }
    }

    public Player getPlayer() {
        return (Player) entities.stream().filter((e) -> (e instanceof Player)).findFirst().get();
    }

    private void fill(Random rng) {
        int i = 0;
        int j = 0;

        while (i < map.length && j < map.length) {
            if (i + 1 < map.length) {
                i++;
            } else {
                j++;
            }
            if (rng.nextBoolean()) {
                branch(rng, i, j);
            }
        }
    }

    private void branch(Random rng, int i, int j) {
        if (rng.nextBoolean()) {
            while (i < map.length && i >= 0 && j < map.length && j >= 0) {
                if (rng.nextBoolean()) {
                    map[i][j] = true;
                    i++;
                } else {
                    map[i][j] = true;
                    j--;
                }
            }
        } else {
            while (j < map.length && j >= 0 && i < map.length && i >= 0) {
                if (rng.nextBoolean()) {
                    map[i][j] = true;
                    i--;
                } else {
                    map[i][j] = true;
                    j++;
                }
            }
        }
    }

    private void populate(Random rng) {
        for (int i = 10; i < map.length; i++) {
            for (int j = 10; j < map.length; j++) {
                if (map[i][j]) {
                    if (rng.nextInt(4) == 1) {
                        if (rng.nextInt(4) == 1) {
                            entities.add(new Skeleton(new Location(i, j)));
                        } else {
                            entities.add(new Slime(new Location(i, j)));
                        }
                    } else {
                        switch (rng.nextInt(37)) {
                            case 1:
                                entities.add(new PointBooster(new Location(i, j)));
                                break;
                            case 2:
                                entities.add(new HealthPotion(new Location(i, j)));
                                break;
                        }
                    }
                }
            }
        }
    }

}
