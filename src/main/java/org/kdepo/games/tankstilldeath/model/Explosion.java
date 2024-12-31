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

    private final ResourcesController resourcesController;

    private boolean isActive;

    private final AnimationController animationController;

    public Explosion(double x, double y, String animationMapName) {
        this.x = x;
        this.y = y;

        resourcesController = ResourcesController.getInstance();
        Map<String, Animation> animationMap = resourcesController.getAnimations(animationMapName);

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

    public void restartAnimation(String animationMapName) {
        Map<String, Animation> animationMap = resourcesController.getAnimations(animationMapName);
        Animation activeAnimation = animationMap.get("explosion");
        animationController.setAnimationsMap(animationMap);
        animationController.setActiveAnimation(activeAnimation);
        animationController.restartActiveAnimation();

        width = activeAnimation.getAnimationFrames().get(0).getImage().getWidth();
        height = activeAnimation.getAnimationFrames().get(0).getImage().getHeight();
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
