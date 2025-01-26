package org.kdepo.games.tankstilldeath.desktop.model;

import java.util.Objects;

public class TankConfiguration {

    /**
     * Tank type identification
     */
    private final int tankId;

    /**
     * Images and animations used by tank
     */
    private final String animationsCollectionName;

    /**
     * Base movement speed
     */
    private final double movementSpeed;

    /**
     * Base weapon reloading speed
     */
    private final double reloadingSpeed;

    /**
     * Hit box offset parameter
     */
    private final int hitBoxOffsetX;

    /**
     * Hit box offset parameter
     */
    private final int hitBoxOffsetY;

    /**
     * Hit box offset parameter
     */
    private final int hitBoxWidth;

    /**
     * Hit box offset parameter
     */
    private final int hitBoxHeight;

    /**
     * Bullet type id
     */
    private final int bulletTypeId;

    /**
     * Bullet offset start position when move direction is north
     */
    private final int bulletOffsetXNorth;

    /**
     * Bullet offset start position when move direction is north
     */
    private final int bulletOffsetYNorth;

    /**
     * Bullet offset start position when move direction is east
     */
    private final int bulletOffsetXEast;

    /**
     * Bullet offset start position when move direction is east
     */
    private final int bulletOffsetYEast;

    /**
     * Bullet offset start position when move direction is south
     */
    private final int bulletOffsetXSouth;

    /**
     * Bullet offset start position when move direction is south
     */
    private final int bulletOffsetYSouth;

    /**
     * Bullet offset start position when move direction is west
     */
    private final int bulletOffsetXWest;

    /**
     * Bullet offset start position when move direction is west
     */
    private final int bulletOffsetYWest;

    /**
     * Armor type id
     */
    private final int armorTypeId;

    public TankConfiguration(int tankId, String animationsCollectionName, double movementSpeed, double reloadingSpeed, int hitBoxOffsetX, int hitBoxOffsetY, int hitBoxWidth, int hitBoxHeight, int bulletTypeId, int bulletOffsetXNorth, int bulletOffsetYNorth, int bulletOffsetXEast, int bulletOffsetYEast, int bulletOffsetXSouth, int bulletOffsetYSouth, int bulletOffsetXWest, int bulletOffsetYWest, int armorTypeId) {
        this.tankId = tankId;
        this.animationsCollectionName = animationsCollectionName;
        this.movementSpeed = movementSpeed;
        this.reloadingSpeed = reloadingSpeed;
        this.hitBoxOffsetX = hitBoxOffsetX;
        this.hitBoxOffsetY = hitBoxOffsetY;
        this.hitBoxWidth = hitBoxWidth;
        this.hitBoxHeight = hitBoxHeight;
        this.bulletTypeId = bulletTypeId;
        this.bulletOffsetXNorth = bulletOffsetXNorth;
        this.bulletOffsetYNorth = bulletOffsetYNorth;
        this.bulletOffsetXEast = bulletOffsetXEast;
        this.bulletOffsetYEast = bulletOffsetYEast;
        this.bulletOffsetXSouth = bulletOffsetXSouth;
        this.bulletOffsetYSouth = bulletOffsetYSouth;
        this.bulletOffsetXWest = bulletOffsetXWest;
        this.bulletOffsetYWest = bulletOffsetYWest;
        this.armorTypeId = armorTypeId;
    }

    public int getTankId() {
        return tankId;
    }

    public String getAnimationsCollectionName() {
        return animationsCollectionName;
    }

    public double getMovementSpeed() {
        return movementSpeed;
    }

    public double getReloadingSpeed() {
        return reloadingSpeed;
    }

    public int getHitBoxOffsetX() {
        return hitBoxOffsetX;
    }

    public int getHitBoxOffsetY() {
        return hitBoxOffsetY;
    }

    public int getHitBoxWidth() {
        return hitBoxWidth;
    }

    public int getHitBoxHeight() {
        return hitBoxHeight;
    }

    public int getBulletTypeId() {
        return bulletTypeId;
    }

    public int getBulletOffsetXNorth() {
        return bulletOffsetXNorth;
    }

    public int getBulletOffsetYNorth() {
        return bulletOffsetYNorth;
    }

    public int getBulletOffsetXEast() {
        return bulletOffsetXEast;
    }

    public int getBulletOffsetYEast() {
        return bulletOffsetYEast;
    }

    public int getBulletOffsetXSouth() {
        return bulletOffsetXSouth;
    }

    public int getBulletOffsetYSouth() {
        return bulletOffsetYSouth;
    }

    public int getBulletOffsetXWest() {
        return bulletOffsetXWest;
    }

    public int getBulletOffsetYWest() {
        return bulletOffsetYWest;
    }

    public int getArmorTypeId() {
        return armorTypeId;
    }

    @Override
    public String toString() {
        return "TankConfiguration{" +
                "tankId=" + tankId +
                ", animationsCollectionName='" + animationsCollectionName + '\'' +
                ", movementSpeed=" + movementSpeed +
                ", reloadingSpeed=" + reloadingSpeed +
                ", hitBoxOffsetX=" + hitBoxOffsetX +
                ", hitBoxOffsetY=" + hitBoxOffsetY +
                ", hitBoxWidth=" + hitBoxWidth +
                ", hitBoxHeight=" + hitBoxHeight +
                ", bulletTypeId=" + bulletTypeId +
                ", bulletOffsetXNorth=" + bulletOffsetXNorth +
                ", bulletOffsetYNorth=" + bulletOffsetYNorth +
                ", bulletOffsetXEast=" + bulletOffsetXEast +
                ", bulletOffsetYEast=" + bulletOffsetYEast +
                ", bulletOffsetXSouth=" + bulletOffsetXSouth +
                ", bulletOffsetYSouth=" + bulletOffsetYSouth +
                ", bulletOffsetXWest=" + bulletOffsetXWest +
                ", bulletOffsetYWest=" + bulletOffsetYWest +
                ", armorTypeId=" + armorTypeId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TankConfiguration that = (TankConfiguration) o;
        return tankId == that.tankId
                && Double.compare(movementSpeed, that.movementSpeed) == 0
                && Double.compare(reloadingSpeed, that.reloadingSpeed) == 0
                && hitBoxOffsetX == that.hitBoxOffsetX
                && hitBoxOffsetY == that.hitBoxOffsetY
                && hitBoxWidth == that.hitBoxWidth
                && hitBoxHeight == that.hitBoxHeight
                && bulletTypeId == that.bulletTypeId
                && bulletOffsetXNorth == that.bulletOffsetXNorth
                && bulletOffsetYNorth == that.bulletOffsetYNorth
                && bulletOffsetXEast == that.bulletOffsetXEast
                && bulletOffsetYEast == that.bulletOffsetYEast
                && bulletOffsetXSouth == that.bulletOffsetXSouth
                && bulletOffsetYSouth == that.bulletOffsetYSouth
                && bulletOffsetXWest == that.bulletOffsetXWest
                && bulletOffsetYWest == that.bulletOffsetYWest
                && armorTypeId == that.armorTypeId
                && Objects.equals(animationsCollectionName, that.animationsCollectionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tankId, animationsCollectionName, movementSpeed, reloadingSpeed, hitBoxOffsetX, hitBoxOffsetY, hitBoxWidth, hitBoxHeight, bulletTypeId, bulletOffsetXNorth, bulletOffsetYNorth, bulletOffsetXEast, bulletOffsetYEast, bulletOffsetXSouth, bulletOffsetYSouth, bulletOffsetXWest, bulletOffsetYWest, armorTypeId);
    }
}
