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

import java.util.Random;

/**
 * @author John
 */
public class Level {

    private final int seed;
    private boolean[][] map = new boolean[0][0];

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
    }

    private void fill(Random rng) {
        int i = 0;
        int j = 0;

        while (i < map.length && j < map.length) {
            if (i > map.length && map[i + 1][j]) {
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
            while (i < map.length && j >= 0) {
                if (rng.nextBoolean()) {
                    map[i][j] = true;
                    i++;
                } else {
                    map[i][j] = true;
                    j--;
                }
            }
        } else {
            while (i >= 0 && j < map.length) {
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

    public boolean[][] getMap() {
        return map;
    }

    public int getSeed() {
        return seed;
    }

    public int getSize() {
        return map.length;
    }

}
