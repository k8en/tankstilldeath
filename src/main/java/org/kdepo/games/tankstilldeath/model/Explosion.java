package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.util.Map;

public class Explosion extends AbstractGameObject {

    private final ResourcesController resourcesController;

    public Explosion(double x, double y, String animationMapName) {
        resourcesController = ResourcesController.getInstance();
        Map<String, Animation> animationMap = resourcesController.getAnimations(animationMapName);
        animationController = new AnimationController(
                animationMap,
                animationMap.get("explosion"),
                AnimationPlayDirection.FORWARD,
                AnimationPlayMode.ONCE
        );

        this.x = x;
        this.y = y;
        this.width = animationController.getActiveFrame().getImage().getWidth();
        this.height = animationController.getActiveFrame().getImage().getHeight();
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
}
