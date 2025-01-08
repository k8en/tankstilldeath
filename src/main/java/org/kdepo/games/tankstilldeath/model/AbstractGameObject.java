package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.geometry.Rectangle;

import java.awt.*;

public abstract class AbstractGameObject extends Rectangle {

    protected boolean isActive;

    protected AnimationController animationController;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void update() {
        if (animationController.isAnimationCompleted()) {
            isActive = false;
            return;
        }
        animationController.update();
    }

    public void render(Graphics2D g) {
        g.drawImage(
                animationController.getActiveFrame().getImage(),
                (int) x, (int) y,
                null
        );
    }
}
