package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.util.Map;

public class Explosion extends Rectangle {

    private boolean isActive;

    private final AnimationController animationController;

    public Explosion(double x, double y) {
        this.x = x;
        this.y = y;

        ResourcesController resourcesController = ResourcesController.getInstance();
        Map<String, Animation> animationMap = resourcesController.getAnimations("animation_explosion_01");

        animationController = new AnimationController(
                animationMap,
                animationMap.get("explosion"),
                AnimationPlayDirection.FORWARD,
                AnimationPlayMode.ONCE
        );

        this.width = animationController.getActiveFrame().getImage().getWidth();
        this.height = animationController.getActiveFrame().getImage().getHeight();
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void restartAnimation() {
        animationController.restartActiveAnimation();
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
