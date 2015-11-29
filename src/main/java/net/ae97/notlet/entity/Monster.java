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

import net.ae97.notlet.Location;
import net.ae97.notlet.server.level.Level;

/**
 * Created by john on 11/22/15.
 */
public abstract class Monster extends Entity {

    private boolean isAggro = true;
    private Location targetLocation;
    private final int damage;

    public Monster(Location loc, int hp, int value, String sprite, double movementSpeed, int damage) {
        super(loc, hp, value, sprite, movementSpeed, 32, .93);
        this.damage = damage;
    }

    @Override
    public void processTick(Level level) {
        Location old = getLocation();
        targetLocation = level.getPlayer().getLocation();

        double distanceX = old.getX() - targetLocation.getX();
        double distanceY = old.getY() - targetLocation.getY();

        Location newLocation;

        if (Math.random() * 2 > 1) {
            if (distanceX > 0) {
                newLocation = new Location(old.getX() - getMovementSpeed(), old.getY());
                //move right
            } else {
                newLocation = new Location(old.getX() + getMovementSpeed(), old.getY());
                //move left
            }
        } else {
            if (distanceY > 0) {
                newLocation = new Location(old.getX(), old.getY() - getMovementSpeed());
                //move down
            } else {
                newLocation = new Location(old.getX(), old.getY() + getMovementSpeed());
            }
        }

        if (level.isPassable(newLocation, new Location(newLocation.getX() + getBlockSize(), newLocation.getY() + getBlockSize()))) {
            setLocation(newLocation);
        }

        level.getEntities().stream().filter((en) -> (en instanceof Player && en.hasCollidedWith(this))).forEach((en) -> {
            en.damage(damage);
        });
    }

    public Location getTargetLocation() {
        return targetLocation;
    }

    public void setTargetLocation(Location location) {
        targetLocation = location;
    }

    public void setAggro(boolean newVal) {
        isAggro = newVal;
    }

    public boolean isAggro() {
        return isAggro;
    }

}
