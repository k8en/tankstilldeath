package org.kdepo.games.tankstilldeath.model;

import org.kdepo.games.tankstilldeath.controllers.BulletController;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.tiles.TileController;

import java.awt.*;
import java.util.Map;

public class Tank extends Rectangle {

    private TileController tileController;

    private MoveDirection moveDirection;
    private boolean isMoving;
    private double movementSpeed;

    private Rectangle hitBox;
    private int hitBoxOffsetX;
    private int hitBoxOffsetY;

    boolean isReadyToShot;
    private double reloadingProgress;
    private double reloadingSpeed;

    private final AnimationController animationController;

    public Tank(double x, double y, MoveDirection moveDirection, int hitBoxOffsetX, int hitBoxOffsetY, int hitBoxWidth, int hitBoxHeight) {
        tileController = TileController.getInstance();

        this.x = x;
        this.y = y;
        this.moveDirection = moveDirection;
        isMoving = false;
        movementSpeed = 2.5d;

        isReadyToShot = true;
        reloadingProgress = 100;
        reloadingSpeed = 2.5d;

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

        this.hitBoxOffsetX = hitBoxOffsetX;
        this.hitBoxOffsetY = hitBoxOffsetY;
        hitBox = new Rectangle(
                this.x + this.hitBoxOffsetX,
                this.y + this.hitBoxOffsetY,
                hitBoxWidth,
                hitBoxHeight
        );
    }

    public Rectangle getHitBox() {
        return hitBox;
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

        if (keyHandler.isSpacePressed()) {
            if (isReadyToShot && reloadingProgress == 100) {
                int bulletOffsetX = 0;
                int bulletOffsetY = 0;
                if (MoveDirection.NORTH.equals(moveDirection)) {
                    bulletOffsetX = 33;
                    bulletOffsetY = -2;
                } else if (MoveDirection.EAST.equals(moveDirection)) {
                    bulletOffsetX = 68;
                    bulletOffsetY = 33;
                } else if (MoveDirection.SOUTH.equals(moveDirection)) {
                    bulletOffsetX = 33;
                    bulletOffsetY = 68;
                } else if (MoveDirection.WEST.equals(moveDirection)) {
                    bulletOffsetX = -2;
                    bulletOffsetY = 33;
                }

                BulletController.getInstance().spawn(x + bulletOffsetX, y + bulletOffsetY, moveDirection);
                isReadyToShot = false;
                reloadingProgress = 0;
            }
        }

    }

    public void update() {
        if (isMoving) {
            double nextX = x + movementSpeed * moveDirection.getX();
            double nextY = y + movementSpeed * moveDirection.getY();

            hitBox.setX(nextX + hitBoxOffsetX);
            hitBox.setY(nextY + hitBoxOffsetY);

            if (tileController.hasCollision(hitBox)) {
                hitBox.setX(x + hitBoxOffsetX);
                hitBox.setY(y + hitBoxOffsetY);
            } else {
                x = nextX;
                y = nextY;
            }
        }
        animationController.update();

        if (reloadingProgress < 100) {
            reloadingProgress = reloadingProgress + reloadingSpeed;
            if (reloadingProgress >= 100) {
                isReadyToShot = true;
                reloadingProgress = 100;
            }
        }
    }

    public void render(Graphics2D g) {
        g.drawImage(
                animationController.getActiveFrame().getImage(),
                (int) x, (int) y,
                null
        );

        //g.setColor(Color.MAGENTA);
        //g.drawRect((int) hitBox.getX(), (int) hitBox.getY(), (int) hitBox.getWidth(), (int) hitBox.getHeight());
    }

}
