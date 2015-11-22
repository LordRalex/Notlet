package net.ae97.notlet.server.level;

/**
 * john
 * Level
 *
 * Generates a boolean 2D array representing levels for the game engine to render
 * True means walkable space
 * False means a wall
 *
 * Currently creates spindally spiderry passages.
 */

import java.util.Random;

public class Level {

    private boolean[][] map;
    private int seed;
    private int size = 0;

    public Level() {

        this(new Random().nextInt());

    }

    public Level(int seed) {

        this.seed = seed;

    }

    public void generate(){

        Random RandBool = new Random(seed);
        size = 100 + (RandBool.nextInt(50));
        map = new boolean[size][size];

        int i = 0;
        int j = 0;

        map[i][j] = true;

        while (i < size && j < size) {
            if (RandBool.nextBoolean()) {
                map[i][j] = true;
                i++;
            }
            else {
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
        }
        else if (i < j) {
            j--;
            while (i < size) {
                map[i][j] = true;
                i++;
            }
        }
        fill(RandBool);
    }

    public void fill(Random RandBool) {

        int i = 0;
        int j = 0;

        while (i < size && j < size) {
            if (i > size && map[i+1][j]) {
                i++;
            }
            else {
                j++;
            }
            if (RandBool.nextBoolean()) {
                branch(RandBool, i, j);
            }
        }
    }

    public void branch(Random RandBool, int i, int j) {

        if (RandBool.nextBoolean()) {
            while (i < size && j >= 0) {
                if (RandBool.nextBoolean()) {
                    map[i][j] = true;
                    i++;
                }
                else {
                    map[i][j] = true;
                    j--;
                }
            }
        }
        else {
            while (i >= 0 && j < size) {
                if (RandBool.nextBoolean()) {
                    map[i][j] = true;
                    i--;
                }
                else {
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
        return size;
    }

}
