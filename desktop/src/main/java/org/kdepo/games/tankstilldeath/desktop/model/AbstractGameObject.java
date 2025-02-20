package org.kdepo.games.tankstilldeath.desktop.model;

import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.geometry.Rectangle;

import java.awt.*;
import java.util.UUID;

public abstract class AbstractGameObject extends Rectangle {

    protected UUID uuid;

    protected boolean isActive;

    protected AnimationController animationController;

    public UUID getUuid() {
        return uuid;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    public void setAnimationController(AnimationController animationController) {
        this.animationController = animationController;
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
