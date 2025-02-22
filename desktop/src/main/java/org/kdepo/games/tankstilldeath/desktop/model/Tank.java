package org.kdepo.games.tankstilldeath.desktop.model;

import org.kdepo.games.tankstilldeath.desktop.Constants;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.geometry.Point;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.tiles.Tile;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Tank extends AbstractHittableGameObject {

    private final int tankTypeId;
    private final int teamId;

    private MoveDirection moveDirection;
    private boolean isMoving;
    private double moveSpeed;

    private int hitBoxOffsetX;
    private int hitBoxOffsetY;

    boolean isReadyToShot;
    private double reloadingProgress;
    private double reloadingSpeed;

    private int armourAmount;

    private boolean isProtectedByShield;
    private long shieldTimer;
    private ShieldEffect shieldEffect;

    private int bulletTypeId;
    private int bulletOffsetNorthX;
    private int bulletOffsetNorthY;
    private int bulletOffsetEastX;
    private int bulletOffsetEastY;
    private int bulletOffsetSouthX;
    private int bulletOffsetSouthY;
    private int bulletOffsetWestX;
    private int bulletOffsetWestY;

    private OnTankDestroyEventType onDestroyEventType;

    private final VirtualKeyHandler keyHandler;
    private Bot bot;

    public Tank(int tankTypeId,
                Map<String, Animation> animationMap,
                double centerX,
                double centerY,
                int teamId,
                MoveDirection moveDirection,
                double moveSpeed,
                int bulletTypeId,
                double reloadingSpeed,
                int armourAmount,
                int hitBoxOffsetX,
                int hitBoxOffsetY,
                int hitBoxWidth,
                int hitBoxHeight,
                int bulletOffsetNorthX,
                int bulletOffsetNorthY,
                int bulletOffsetEastX,
                int bulletOffsetEastY,
                int bulletOffsetSouthX,
                int bulletOffsetSouthY,
                int bulletOffsetWestX,
                int bulletOffsetWestY) {
        // Setup generic parameters
        uuid = UUID.randomUUID();
        this.tankTypeId = tankTypeId;
        this.teamId = teamId;
        this.moveSpeed = moveSpeed;
        isMoving = false;
        this.armourAmount = armourAmount;
        isActive = false;
        this.moveDirection = moveDirection;

        isProtectedByShield = false;
        shieldEffect = new ShieldEffect();

        // Setup animations
        Animation activeAnimation = null;
        if (MoveDirection.NORTH.equals(this.moveDirection)) {
            activeAnimation = animationMap.get("idle_north");
        } else if (MoveDirection.EAST.equals(this.moveDirection)) {
            activeAnimation = animationMap.get("idle_east");
        } else if (MoveDirection.SOUTH.equals(this.moveDirection)) {
            activeAnimation = animationMap.get("idle_south");
        } else if (MoveDirection.WEST.equals(this.moveDirection)) {
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

        // Setup geometry parameters
        this.width = animationController.getActiveFrame().getImage().getWidth();
        this.height = animationController.getActiveFrame().getImage().getHeight();
        this.x = centerX - width / 2;
        this.y = centerY - height / 2;

        this.hitBoxOffsetX = hitBoxOffsetX;
        this.hitBoxOffsetY = hitBoxOffsetY;
        hitBox = new Rectangle(
                this.x + this.hitBoxOffsetX,
                this.y + this.hitBoxOffsetY,
                hitBoxWidth,
                hitBoxHeight
        );

        // Setup weapon parameters
        this.bulletTypeId = bulletTypeId;
        isReadyToShot = true;
        reloadingProgress = 100;
        this.reloadingSpeed = reloadingSpeed;
        this.bulletOffsetNorthX = bulletOffsetNorthX;
        this.bulletOffsetNorthY = bulletOffsetNorthY;
        this.bulletOffsetEastX = bulletOffsetEastX;
        this.bulletOffsetEastY = bulletOffsetEastY;
        this.bulletOffsetSouthX = bulletOffsetSouthX;
        this.bulletOffsetSouthY = bulletOffsetSouthY;
        this.bulletOffsetWestX = bulletOffsetWestX;
        this.bulletOffsetWestY = bulletOffsetWestY;

        keyHandler = new VirtualKeyHandler();
        if (Constants.Teams.ENEMY_ID == teamId) {
            bot = new Bot();
            bot.setMovementArea(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
        }
    }

    @Override
    public void setCenter(double cx, double cy) {
        super.setCenter(cx, cy);
        hitBox.setX(this.x + this.hitBoxOffsetX);
        hitBox.setY(this.y + this.hitBoxOffsetY);
    }

    @Override
    public void update() {
        this.animationController.update();

        if (isProtectedByShield) {
            if (System.currentTimeMillis() >= shieldTimer) {
                isProtectedByShield = false;
                System.out.println("Shield disabled");
            } else {
                shieldEffect.setCenter(this.getCenter());
                shieldEffect.update();
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        if (isProtectedByShield) {
            shieldEffect.render(g);
        }
    }

    public int getTankTypeId() {
        return tankTypeId;
    }

    public int getTeamId() {
        return teamId;
    }

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(MoveDirection moveDirection) {
        this.moveDirection = moveDirection;

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

    public boolean isMoving() {
        return isMoving;
    }

    public double getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(double moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public void increaseMoveSpeed() {
        moveSpeed = moveSpeed + Constants.TANK_MOVE_SPEED_INCREMENT;
        if (moveSpeed > Constants.TANK_MOVE_SPEED_LIMIT) {
            moveSpeed = Constants.TANK_MOVE_SPEED_LIMIT;
        }
    }

    public int getHitBoxOffsetX() {
        return hitBoxOffsetX;
    }

    public int getHitBoxOffsetY() {
        return hitBoxOffsetY;
    }

    public boolean isReadyToShot() {
        return isReadyToShot && reloadingProgress == 100;
    }

    public void shot() {
        isReadyToShot = false;
        reloadingProgress = 0;
    }

    public void updateReloading() {
        if (reloadingProgress < 100) {
            reloadingProgress = reloadingProgress + reloadingSpeed;
            if (reloadingProgress >= 100) {
                isReadyToShot = true;
                reloadingProgress = 100;
            }
        }
    }

    public void increaseReloadingSpeed() {
        reloadingSpeed = reloadingSpeed + Constants.TANK_RELOADING_SPEED_INCREMENT;
        if (reloadingSpeed > Constants.TANK_RELOADING_SPEED_LIMIT) {
            reloadingSpeed = Constants.TANK_RELOADING_SPEED_LIMIT;
            bulletTypeId = Constants.Bullets.ARMOUR_PIERCING_ID;
        }
    }

    public int getArmourAmount() {
        return armourAmount;
    }

    public boolean isProtectedByShield() {
        return isProtectedByShield;
    }

    public void setProtectedByShield(long millis) {
        isProtectedByShield = true;
        shieldTimer = System.currentTimeMillis() + millis;
    }

    public void changeArmourAmount(int value) {
        armourAmount = armourAmount + value;
        if (armourAmount < 0) {
            armourAmount = 0;
        } else if (armourAmount > 3) {
            armourAmount = 3;
        }
    }

    public int getBulletTypeId() {
        return bulletTypeId;
    }

    public void setBulletTypeId(int bulletTypeId) {
        this.bulletTypeId = bulletTypeId;
    }

    public Point getBulletOffset() {
        int bulletOffsetX = 0;
        int bulletOffsetY = 0;
        if (MoveDirection.NORTH.equals(moveDirection)) {
            bulletOffsetX = bulletOffsetNorthX;
            bulletOffsetY = bulletOffsetNorthY;
        } else if (MoveDirection.EAST.equals(moveDirection)) {
            bulletOffsetX = bulletOffsetEastX;
            bulletOffsetY = bulletOffsetEastY;
        } else if (MoveDirection.SOUTH.equals(moveDirection)) {
            bulletOffsetX = bulletOffsetSouthX;
            bulletOffsetY = bulletOffsetSouthY;
        } else if (MoveDirection.WEST.equals(moveDirection)) {
            bulletOffsetX = bulletOffsetWestX;
            bulletOffsetY = bulletOffsetWestY;
        }

        return new Point(bulletOffsetX, bulletOffsetY);
    }

    /**
     * To process human inputs
     */
    public void resolveControls(KeyHandler keyHandler) {
        this.keyHandler.setUpPressed(keyHandler.isUpPressed());
        this.keyHandler.setRightPressed(keyHandler.isRightPressed());
        this.keyHandler.setDownPressed(keyHandler.isDownPressed());
        this.keyHandler.setLeftPressed(keyHandler.isLeftPressed());
        this.keyHandler.setSpacePressed(keyHandler.isSpacePressed());

        applyVirtualKeys(this.keyHandler);
    }

    /**
     * To process AI inputs
     */
    public void resolveControlsAutomatically(Rectangle player, Rectangle base, Tank tank, List<Tank> activeTanksList, List<Bullet> bulletList, Tile[][] layerDataBottom, Tile[][] layerDataMiddle, Tile[][] layerDataTop) {
        if (bot != null) {
            bot.pressAnyKeys(this.keyHandler, player, base, tank, activeTanksList, bulletList, layerDataBottom, layerDataMiddle, layerDataTop);
            applyVirtualKeys(this.keyHandler);
        }
    }

    private void applyVirtualKeys(VirtualKeyHandler keyHandler) {
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
            isReadyToShot = true;
        } else {
            isReadyToShot = false;
        }
    }

    public OnTankDestroyEventType getOnDestroyEventType() {
        return onDestroyEventType;
    }

    public void setOnDestroyEventType(OnTankDestroyEventType onDestroyEventType) {
        this.onDestroyEventType = onDestroyEventType;
    }

    @Override
    public String toString() {
        return "Tank{" +
                "teamId=" + teamId +
                ", x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                ", hitBox.x=" + hitBox.getX() +
                ", hitBox.y=" + hitBox.getY() +
                ", moveDirection=" + moveDirection +
                ", isMoving=" + isMoving +
                ", movementSpeed=" + moveSpeed +
                ", bulletTypeId=" + bulletTypeId +
                ", armourAmount=" + armourAmount +
                ", isReadyToShot=" + isReadyToShot +
                ", reloadingProgress=" + reloadingProgress +
                ", reloadingSpeed=" + reloadingSpeed +
                ", hitBoxOffsetX=" + hitBoxOffsetX +
                ", hitBoxOffsetY=" + hitBoxOffsetY +
                ", bulletOffsetNorthX=" + bulletOffsetNorthX +
                ", bulletOffsetNorthY=" + bulletOffsetNorthY +
                ", bulletOffsetEastX=" + bulletOffsetEastX +
                ", bulletOffsetEastY=" + bulletOffsetEastY +
                ", bulletOffsetSouthX=" + bulletOffsetSouthX +
                ", bulletOffsetSouthY=" + bulletOffsetSouthY +
                ", bulletOffsetWestX=" + bulletOffsetWestX +
                ", bulletOffsetWestY=" + bulletOffsetWestY +
                '}';
    }
}
