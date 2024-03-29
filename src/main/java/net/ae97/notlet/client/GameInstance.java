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

import java.awt.Font;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import net.ae97.notlet.Direction;
import net.ae97.notlet.Location;
import net.ae97.notlet.client.network.ServerConnection;
import net.ae97.notlet.entity.Arrow;
import net.ae97.notlet.entity.Entity;
import net.ae97.notlet.entity.Player;
import net.ae97.notlet.network.packets.AttackRequestPacket;
import net.ae97.notlet.network.packets.MoveRequestPacket;
import net.ae97.notlet.network.packets.Packet;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class GameInstance {

    private static ServerConnection connection;
    private static boolean[][] levelMap;
    private static final List<Entity> entities = new LinkedList<>();
    private static final Map<String, Texture> textureMapping = new HashMap<>();
    private static final int width = 608, height = 650;
    private static boolean isLoaded = false;
    private static boolean acceptInput = false;
    private static TrueTypeFont font;

    public static void createTextures() throws LWJGLException {
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create();
        Display.setVSyncEnabled(true);

        GL11.glEnable(GL11.GL_TEXTURE_2D);

        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        GL11.glViewport(0, 0, width, height);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        Font awtFont = new Font("Times New Roman", Font.BOLD, 18);
        font = new TrueTypeFont(awtFont, true);

        try {
            textureMapping.put("SMALLLET", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("SMALLLET.png")));
            textureMapping.put("healthbar", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("healthbar.png")));
            textureMapping.put("score", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("score.png")));
            textureMapping.put("arrow", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("arrow.png")));
            textureMapping.put("dirt", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("dirt.png")));
            textureMapping.put("rangerD", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerD.png")));
            textureMapping.put("rangerL", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerL.png")));
            textureMapping.put("rangerR", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerR.png")));
            textureMapping.put("rangerU", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("rangerU.png")));
            textureMapping.put("skele", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("skele.png")));
            textureMapping.put("slime", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("slime.png")));
            textureMapping.put("hp", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("hp.png")));
            textureMapping.put("pb", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("pb.png")));
            textureMapping.put("wall", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("wall.png")));
            textureMapping.put("exit", TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("exit.png")));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void render(ServerConnection conn) {
        connection = conn;
        Color.white.bind();
        Display.sync(60);

        while (!Display.isCloseRequested()) {
            GameInstance.renderFrame();
            Display.update();
        }

        connection.close();
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static void unload() {
        isLoaded = false;
        acceptInput = false;
    }

    public static void setAcceptUserInput(boolean accept) {
        acceptInput = accept;
    }

    public static void init(boolean[][] map, List<Entity> entityList, ServerConnection conn) {
        init(map, entityList);
        connection = conn;
        isLoaded = true;
        acceptInput = true;
    }

    public static void init(boolean[][] map, List<Entity> entityList) {
        levelMap = map;
        synchronized (entities) {
            entities.clear();
            entities.addAll(entityList);
        }
        isLoaded = true;
        acceptInput = true;
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
        if (!isLoaded()) {
            return;
        }
        synchronized (entities) {
            Player player = getPlayer();
            renderBackground();
            renderHUD(player);
            entities.forEach((en) -> renderEntity(en));
        }

        pollInput();
    }

    private static void renderHUD(Player player) {

        textureMapping.get("SMALLLET").bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(200, 608);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(400, 608);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(400, 648);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(200, 648);
        GL11.glEnd();

        font.drawString(65, 610, Integer.toString(player.getHp()), Color.white);
        textureMapping.get("healthbar").bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(20, 610);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(60, 610);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(60, 630);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(20, 630);
        GL11.glEnd();
    }

    private static void renderEntity(Entity entity) {
        int SpriteScaleFactor = 1;
        double x = entity.getLocation().getX();
        double y = entity.getLocation().getY();
        Texture texture = textureMapping.get(entity.getSprite());
        texture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2d(x * 32, y * 32);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2d((x * 32) + texture.getTextureWidth() * SpriteScaleFactor, y * 32);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2d((x * 32) + texture.getTextureWidth() * SpriteScaleFactor, (y * 32) + texture.getTextureHeight() * SpriteScaleFactor);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2d(x * 32, (y * 32) + texture.getTextureHeight() * SpriteScaleFactor);
        if (entity instanceof Arrow) {
            Arrow arrow = (Arrow) entity;
            switch (arrow.getFacingDirection()) {
                case DOWN:
                    GL11.glRotated(90, 0, 0, 0);
                case LEFT:
                    GL11.glRotated(90, 0, 0, 0);
                case UP:
                    GL11.glRotated(90, 0, 0, 0);
            }
        }
        GL11.glEnd();
    }

    private static void renderBackground() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        if (GameInstance.getMap() == null) {
            return;
        }
        boolean[][] map = GameInstance.getMap();
        textureMapping.get("dirt").bind();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j]) {
                    renderFG(i * 32, j * 32);
                }
            }
        }

        textureMapping.get("wall").bind();
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (!map[i][j]) {
                    renderFG(i * 32, j * 32);
                }
            }
        }

        textureMapping.get("exit").bind();
        renderFG((map.length - 1) * 32, (map.length - 1) * 32);
    }

    private static void pollInput() {
        if (!acceptInput) {
            return;
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            sendPacket(new MoveRequestPacket(Direction.LEFT));
            getPlayer().setFacingDirection(Direction.LEFT);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            sendPacket(new MoveRequestPacket(Direction.RIGHT));
            getPlayer().setFacingDirection(Direction.RIGHT);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            sendPacket(new MoveRequestPacket(Direction.UP));
            getPlayer().setFacingDirection(Direction.UP);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            sendPacket(new MoveRequestPacket(Direction.DOWN));
            getPlayer().setFacingDirection(Direction.DOWN);
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            sendPacket(new AttackRequestPacket(Direction.LEFT));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            sendPacket(new AttackRequestPacket(Direction.RIGHT));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            sendPacket(new AttackRequestPacket(Direction.UP));
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            sendPacket(new AttackRequestPacket(Direction.DOWN));
        }

    }

    public static void renderFG(int x, int y) {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2f(x + 32, y);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2f(x + 32, y + 32);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2f(x, y + 32);
        GL11.glEnd();
    }

    private static void sendPacket(Packet packet) {
        try {
            connection.sendPacket(packet);
        } catch (IOException ex) {
            ClientCore.getLogger().log(java.util.logging.Level.SEVERE, "Error sending packet", ex);
        }
    }

}
