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
package net.ae97.notlet.client;

import java.util.LinkedList;
import java.util.List;
import net.ae97.notlet.Location;
import net.ae97.notlet.entity.Entity;
import net.ae97.notlet.entity.Player;

public class GameInstance {

    private static boolean[][] levelMap;
    private static final List<Entity> entities = new LinkedList<>();

    public static void init(boolean[][] map, List<Entity> entityList) {
        levelMap = map;
        entities.addAll(entityList);
    }

    public static boolean[][] getMap() {
        return levelMap;
    }

    public static List<Entity> getEntities() {
        return entities;
    }

    public static Player getPlayer() {
        return (Player) entities.stream().filter((e) -> e instanceof Player).findFirst().get();
    }

    public static void updateEntityHealth(int entityId, int damage) {
        synchronized (entities) {
            Entity entity = entities.stream().filter((en) -> en.getId() == entityId).findFirst().get();
            entity.damage(damage);
        }
    }

    public static void updateEntityLocation(int entityId, Location location) {
        synchronized (entities) {
            Entity entity = entities.stream().filter((en) -> en.getId() == entityId).findFirst().get();
            entity.setLocation(location);
        }
    }

    public static void removeEntity(int entityId) {
        synchronized (entities) {
            entities.removeIf((en) -> en.getId() == entityId);
        }
    }

    public static void spawnEnemy(Entity newEntity) {
        synchronized (entities) {
            entities.add(newEntity);
        }
    }

    /**
     * This renders a single frame for the game
     */
    public static void renderFrame() {
        synchronized (entities) {
            Player player = getPlayer();
            renderBackground(player.getLocation());
            entities.forEach((en) -> renderEntity(en, player.getLocation()));
        }
    }

    private static void renderEntity(Entity entity, Location reference) {

    }

    private static void renderBackground(Location reference) {

    }

}
