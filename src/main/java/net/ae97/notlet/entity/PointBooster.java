package net.ae97.notlet.entity;

import net.ae97.notlet.Location;

/**
 * Created by john on 11/22/15.
 */
public class PointBooster extends Item {

    public PointBooster (Location loc) {

        super(loc);
        hp = 1;
        hpresto = 0;
        value = 1000;
        sprite = "pb";

    }

}
