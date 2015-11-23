package net.ae97.notlet.entity;

import net.ae97.notlet.Location;

/**
 * Created by john on 11/22/15.
 */
public class HealthPotion extends Item {
    public HealthPotion (Location loc) {

        super(loc);
        hp = 1;
        hpresto = 50;
        value = 10;
        sprite = "hp";

    }

}
