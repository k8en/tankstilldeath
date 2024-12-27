package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.util.Map;

public class Bullet extends Rectangle {

    private MoveDirection moveDirection;
    private double movementSpeed;
    private Rectangle hitBox;

    private boolean isActive;

    private final AnimationController animationController;

    public Bullet(double x, double y, MoveDirection moveDirection) {
        this.x = x;
        this.y = y;
        this.moveDirection = moveDirection;
        movementSpeed = 7.5d;

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

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void update() {
        x = x + movementSpeed * moveDirection.getX();
        y = y + movementSpeed * moveDirection.getY();
        hitBox.setX(x);
        hitBox.setY(y);

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
