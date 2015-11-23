package net.ae97.notlet.entity;

import net.ae97.notlet.Location;

import java.io.Serializable;

/**
 * Created by john on 11/22/15.
 */
public abstract class Entity implements Serializable {

    Location loc;
    int hp;
    int value;
    String sprite;

    public Entity(Location loc) {
        this.loc = loc;
    }

    public double distanceSquaredFrom(Location loc) {
        return Math.pow((this.loc.getX() - loc.getX()), 2) + Math.pow((this.loc.getY() - loc.getY()), 2);
    }

    public boolean isCollidedWith(Entity e) {
        return false;
    }

    public void processTick() {

    }

}
