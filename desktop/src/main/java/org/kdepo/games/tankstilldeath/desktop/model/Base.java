package org.kdepo.games.tankstilldeath.desktop.model;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.util.Map;

public class Base extends AbstractHittableGameObject {

    public Base() {
        isActive = false;

        ResourcesController resourcesController = ResourcesController.getInstance();
        Map<String, Animation> animationMap = resourcesController.getAnimations("animation_base_00");
        animationController = new AnimationController(
                animationMap,
                animationMap.get("idle"),
                AnimationPlayDirection.FORWARD,
                AnimationPlayMode.LOOP
        );

        this.x = 608;
        this.y = 896;
        this.width = animationController.getActiveFrame().getImage().getWidth();
        this.height = animationController.getActiveFrame().getImage().getHeight();

        hitBox = new Rectangle(this.x, this.y, width, height);
    }
}
