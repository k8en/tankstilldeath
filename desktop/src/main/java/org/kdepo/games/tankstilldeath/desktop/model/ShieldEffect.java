package org.kdepo.games.tankstilldeath.desktop.model;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.util.Map;

public class ShieldEffect extends AbstractGameObject {

    public ShieldEffect() {
        ResourcesController resourcesController = ResourcesController.getInstance();
        Map<String, Animation> animationMap = resourcesController.getAnimations("animation_shield_00");
        animationController = new AnimationController(
                animationMap,
                animationMap.get("shield"),
                AnimationPlayDirection.FORWARD,
                AnimationPlayMode.LOOP
        );

        this.x = 0;
        this.y = 0;
        this.width = animationController.getActiveFrame().getImage().getWidth();
        this.height = animationController.getActiveFrame().getImage().getHeight();
    }

    @Override
    public void update() {
        this.animationController.update();
    }
}
