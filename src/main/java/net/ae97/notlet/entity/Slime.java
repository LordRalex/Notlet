package net.ae97.notlet.entity;

import net.ae97.notlet.Location;

/**
 * Created by john on 11/22/15.
 */
public class Slime extends Monster {

    public Slime (Location loc) {

        super(loc);
        hp = 20;
        value = 10;
        sprite = "slime";

    }

}
