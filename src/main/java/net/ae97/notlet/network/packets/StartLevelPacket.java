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
package net.ae97.notlet.network.packets;

import java.util.List;
import net.ae97.notlet.entity.Entity;

public class StartLevelPacket implements Packet {

    private final boolean[][] levelMap;
    private final List<Entity> entities;

    public StartLevelPacket(boolean[][] level, List<Entity> entities) {
        this.levelMap = level;
        this.entities = entities;
    }

    @Override
    public PacketType getType() {
        return PacketType.StartLevel;
    }

    public boolean[][] getMap() {
        return levelMap;
    }

    public List<Entity> getEntities() {
        return entities;
    }
}
