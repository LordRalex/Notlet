/*
 * The MIT License
 *
 * Copyright 2015 Joshua.
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

import net.ae97.notlet.Direction;
import net.ae97.notlet.Location;
import net.ae97.notlet.server.level.Level;

public class Arrow extends Entity {

    private final Direction direction;
    private final int damage = 10;

    public Arrow(Location loc, Direction direction) {
        super(loc, 1, 0, "arrow", 0.1, 16, .92);
        this.direction = direction;
    }

    @Override
    public void processTick(Level level) {
        Location old = getLocation();
        Location newLocation = old;
        switch (direction) {
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
        setLocation(newLocation);
        if (!level.isPassable(newLocation, new Location(newLocation.getX() + getBlockSize(), newLocation.getY() + getBlockSize()))) {
            level.killEntity(this);
        }
        for (Entity en : level.getEntities()) {
            if (en == this) {
                continue;
            }
            if (en instanceof Monster && en.hasCollidedWith(this)) {
                en.damage(damage);
                level.killEntity(this);
            }
        }
    }

    public Direction getFacingDirection() {
        return direction;
    }

}
