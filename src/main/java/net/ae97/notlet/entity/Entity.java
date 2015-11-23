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

}
