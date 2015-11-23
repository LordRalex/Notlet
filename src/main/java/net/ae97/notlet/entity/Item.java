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
public abstract class Item extends Entity {

    public Item(Location loc, int hp, int value, String sprite) {
        super(loc, hp, value, sprite);
    }

    public abstract void onPickup(Entity entity);

    @Override
    public void processTick(Level level) {
        for (Entity entity : level.getEntities()) {
            if (entity == this || !(entity instanceof Player)) {
                continue;
            }
            if (entity.hasCollidedWith(this)) {
                onPickup(entity);
                level.killEntity(this);
            }
        }
    }

}
