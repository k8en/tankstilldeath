package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.util.Map;

public class Bullet extends AbstractHittableGameObject {

    private int bulletId;

    private int teamId;

    private MoveDirection moveDirection;

    private double moveSpeed;

    public Bullet(double x, double y, MoveDirection moveDirection) {
        this.x = x;
        this.y = y;
        this.moveDirection = moveDirection;
        moveSpeed = 7.5d;

        ResourcesController resourcesController = ResourcesController.getInstance();
        Map<String, Animation> animationMap = resourcesController.getAnimations("animation_bullet_00");

        Animation activeAnimation = null;
        if (MoveDirection.NORTH.equals(moveDirection)) {
            activeAnimation = animationMap.get("move_north");
        } else if (MoveDirection.EAST.equals(moveDirection)) {
            activeAnimation = animationMap.get("move_east");
        } else if (MoveDirection.SOUTH.equals(moveDirection)) {
            activeAnimation = animationMap.get("move_south");
        } else if (MoveDirection.WEST.equals(moveDirection)) {
            activeAnimation = animationMap.get("move_west");
        }

        if (activeAnimation == null) {
            throw new RuntimeException("Start animation not resolved!");
        }

        animationController = new AnimationController(
                animationMap,
                activeAnimation,
                AnimationPlayDirection.FORWARD,
                AnimationPlayMode.LOOP
        );

        this.width = animationController.getActiveFrame().getImage().getWidth();
        this.height = animationController.getActiveFrame().getImage().getHeight();

        hitBox = new Rectangle(this.x, this.y, width, height);
    }

    @Override
    public void update() {
        x = x + moveSpeed * moveDirection.getX();
        y = y + moveSpeed * moveDirection.getY();
        hitBox.setX(x);
        hitBox.setY(y);

        animationController.update();
    }

    public int getBulletId() {
        return bulletId;
    }

    public void setBulletId(int bulletId) {
        this.bulletId = bulletId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }
}
