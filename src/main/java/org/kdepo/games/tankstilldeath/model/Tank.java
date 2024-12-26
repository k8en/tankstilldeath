package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.util.Map;

public class Tank extends Rectangle {

    private MoveDirection moveDirection;
    private boolean isMoving;
    private double movementSpeed;
    private Rectangle hitBox;

    private final AnimationController animationController;

    public Tank(double x, double y, MoveDirection moveDirection) {
        this.x = x;
        this.y = y;
        this.moveDirection = moveDirection;
        isMoving = false;
        movementSpeed = 2.5d;

        ResourcesController resourcesController = ResourcesController.getInstance();
        Map<String, Animation> animationMap = resourcesController.getAnimations("animation_tank_00");

        Animation activeAnimation = null;
        if (MoveDirection.NORTH.equals(moveDirection)) {
            activeAnimation = animationMap.get("idle_north");
        } else if (MoveDirection.EAST.equals(moveDirection)) {
            activeAnimation = animationMap.get("idle_east");
        } else if (MoveDirection.SOUTH.equals(moveDirection)) {
            activeAnimation = animationMap.get("idle_south");
        } else if (MoveDirection.WEST.equals(moveDirection)) {
            activeAnimation = animationMap.get("idle_west");
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
    }

    public void resolveControls(KeyHandler keyHandler) {
        if (keyHandler.isUpPressed() && !keyHandler.isRightPressed() && !keyHandler.isDownPressed() && !keyHandler.isLeftPressed()) {
            isMoving = true;
            if (!MoveDirection.NORTH.equals(moveDirection)) {
                moveDirection = MoveDirection.NORTH;
                animationController.switchToAnimation("move_north");
            }

        } else if (!keyHandler.isUpPressed() && keyHandler.isRightPressed() && !keyHandler.isDownPressed() && !keyHandler.isLeftPressed()) {
            isMoving = true;
            if (!MoveDirection.EAST.equals(moveDirection)) {
                moveDirection = MoveDirection.EAST;
                animationController.switchToAnimation("move_east");
            }

        } else if (!keyHandler.isUpPressed() && !keyHandler.isRightPressed() && keyHandler.isDownPressed() && !keyHandler.isLeftPressed()) {
            isMoving = true;
            if (!MoveDirection.SOUTH.equals(moveDirection)) {
                moveDirection = MoveDirection.SOUTH;
                animationController.switchToAnimation("move_south");
            }

        } else if (!keyHandler.isUpPressed() && !keyHandler.isRightPressed() && !keyHandler.isDownPressed() && keyHandler.isLeftPressed()) {
            isMoving = true;
            if (!MoveDirection.WEST.equals(moveDirection)) {
                moveDirection = MoveDirection.WEST;
                animationController.switchToAnimation("move_west");
            }

        } else if (!keyHandler.isUpPressed() && !keyHandler.isRightPressed() && !keyHandler.isDownPressed() && !keyHandler.isLeftPressed()) {
            isMoving = false;
            if (MoveDirection.NORTH.equals(moveDirection)) {
                animationController.switchToAnimation("idle_north");
            } else if (MoveDirection.EAST.equals(moveDirection)) {
                animationController.switchToAnimation("idle_east");
            } else if (MoveDirection.SOUTH.equals(moveDirection)) {
                animationController.switchToAnimation("idle_south");
            } else if (MoveDirection.WEST.equals(moveDirection)) {
                animationController.switchToAnimation("idle_west");
            }
        }

    }

    public void update() {
        if (isMoving) {
            x = x + movementSpeed * moveDirection.getX();
            y = y + movementSpeed * moveDirection.getY();
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
