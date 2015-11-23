package net.ae97.notlet.entity;

import net.ae97.notlet.Location;

/**
 * Created by john on 11/22/15.
 */
public class Player extends Entity {
    public Player (Location loc) {

        super(loc);
        hp = 100;
        value = 200;
        sprite = "rangerD";

    }
}
