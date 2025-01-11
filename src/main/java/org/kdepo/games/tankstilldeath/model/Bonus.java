package org.kdepo.games.tankstilldeath.model;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.util.Map;

public class Bonus extends AbstractHittableGameObject {

    private final ResourcesController resourcesController;

    private int bonusId;

    private long timer;

    public Bonus(double x, double y, int bonusId) {
        isActive = false;
        this.resourcesController = ResourcesController.getInstance();
        this.hitBox = new Rectangle();
        setBonusConfiguration(x, y, bonusId);
    }

    public void setBonusConfiguration(double x, double y, int bonusId) {
        this.x = x;
        this.y = y;
        this.bonusId = bonusId;

        Map<String, Animation> animationMap = null;
        if (Constants.Bonuses.STAR_ID == bonusId) {
            animationMap = resourcesController.getAnimations("animation_bonus_00");

        } else if (Constants.Bonuses.SHIELD_ID == bonusId) {
            animationMap = resourcesController.getAnimations("animation_bonus_01");

        } else if (Constants.Bonuses.TANK_ID == bonusId) {
            animationMap = resourcesController.getAnimations("animation_bonus_02");

        } else if (Constants.Bonuses.GRENADE_ID == bonusId) {
            animationMap = resourcesController.getAnimations("animation_bonus_03");

        } else if (Constants.Bonuses.SHOVEL_ID == bonusId) {
            animationMap = resourcesController.getAnimations("animation_bonus_04");

        }

        if (animationMap == null) {
            throw new RuntimeException("Animations not resolved for bonusId " + bonusId);
        }

        animationController = new AnimationController(
                animationMap,
                animationMap.get("idle"),
                AnimationPlayDirection.FORWARD,
                AnimationPlayMode.LOOP
        );

        this.width = animationController.getActiveFrame().getImage().getWidth();
        this.height = animationController.getActiveFrame().getImage().getHeight();
        hitBox.setX(this.x);
        hitBox.setY(this.y);
        hitBox.setWidth(this.width);
        hitBox.setHeight(this.height);

        timer = System.currentTimeMillis() + 100000;
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() > timer) {
            isActive = false;
        }
    }

    public int getBonusId() {
        return bonusId;
    }
}
