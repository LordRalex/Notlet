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
import net.ae97.notlet.GlobalIdentification;
import net.ae97.notlet.Location;
import net.ae97.notlet.server.level.Level;

/**
 * Created by john on 11/22/15.
 */
public abstract class Entity implements Serializable {

    private static final GlobalIdentification tracker = new GlobalIdentification();
    private final int entityId;
    private Location location;
    private int hp;
    private int value;
    private final String sprite;
    private double movementSpeed = 0;

    public Entity(Location loc, int hp, int value, String sprite, double movementSpeed) {
        this.entityId = tracker.next();
        this.location = loc;
        this.hp = hp;
        this.value = value;
        this.sprite = sprite;
        this.movementSpeed = movementSpeed;
    }

    public abstract void processTick(Level level);

    public int getId() {
        return entityId;
    }

    public void damage(int damage) {
        hp -= damage;
        if (hp < 0) {
            hp = 0;
        }
    }

    public Location getLocation() {
        return location;
    }

    public int getHp() {
        return hp;
    }

    public int getValue() {
        return value;
    }

    public String getSprite() {
        return sprite;
    }

    public final double distanceSquaredFrom(Location target) {
        return Math.pow(location.getX() - target.getX(), 2) + Math.pow(location.getY() - target.getY(), 2);
    }

    public boolean hasCollidedWith(Entity en) {
        return false;
    }

    public boolean isAt(Location loc) {
        return false;
    }

    public boolean canSee(Location location) {
        return false;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setMovementSpeed(double movementSpeed) {
        this.movementSpeed = movementSpeed;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
