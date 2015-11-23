package net.ae97.notlet.entity;

import net.ae97.notlet.Location;

/**
 * Created by john on 11/22/15.
 */
public class Skeleton extends Monster {

    public Skeleton (Location loc) {

        super(loc);
        hp = 50;
        value = 100;
        sprite = "skele";

    }

}
