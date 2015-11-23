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

import java.io.Serializable;
import net.ae97.notlet.Location;
import net.ae97.notlet.server.level.Level;

/**
 * Created by john on 11/22/15.
 */
public abstract class Entity implements Serializable {

    protected Location loc;
    protected int hp;
    protected int value;
    protected String sprite;

    public Entity(Location loc, int hp, int value, String sprite) {
        this.loc = loc;
        this.hp = hp;
        this.value = value;
        this.sprite = sprite;
    }

    public final double distanceSquaredFrom(Location target) {
        return Math.pow(loc.getX() - target.getX(), 2) + Math.pow(loc.getY() - target.getY(), 2);
    }

    public boolean hasCollidedWith(Entity en) {
        return false;
    }

    public boolean isAt(Location loc) {
        return false;
    }

    public abstract void processTick(Level level);

    public void damage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    public final Location getLocation() {
        return loc;
    }

    public final int getHp() {
        return hp;
    }

    public final int getValue() {
        return value;
    }

    public final String getSprite() {
        return sprite;
    }
}
