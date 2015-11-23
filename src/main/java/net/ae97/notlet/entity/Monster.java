package net.ae97.notlet.entity;

import net.ae97.notlet.Location;

/**
 * Created by john on 11/22/15.
 */
public abstract class Monster extends Entity {
    boolean is_aggro = false;

    public Monster(Location loc) {
        super(loc);
    }

}

