package org.kdepo.games.tankstilldeath.desktop.model;

import org.kdepo.graphics.k2d.geometry.Rectangle;

public abstract class AbstractHittableGameObject extends AbstractGameObject {

    protected Rectangle hitBox;

    public Rectangle getHitBox() {
        return hitBox;
    }
}
