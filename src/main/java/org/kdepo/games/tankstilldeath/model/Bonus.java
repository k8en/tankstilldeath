package org.kdepo.games.tankstilldeath.model;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bonus extends Rectangle {

    private final ResourcesController resourcesController;

    private int id;

    private BufferedImage image;

    private final Rectangle hitBox;

    private boolean isActive;

    private long timer;

    public Bonus(double x, double y, int id) {
        this.resourcesController = ResourcesController.getInstance();
        this.hitBox = new Rectangle();
        isActive = false;
        setBonusConfiguration(x, y, id);
    }

    public void setBonusConfiguration(double x, double y, int id) {
        this.x = x;
        this.y = y;
        this.id = id;

        if (Constants.Bonuses.STAR_ID == id) {
            image = resourcesController.getImage("image_bonus_0");

        } else if (Constants.Bonuses.SHIELD_ID == id) {
            image = resourcesController.getImage("image_bonus_1");

        }

        this.width = image.getWidth();
        this.height = image.getHeight();
        hitBox.setX(this.x);
        hitBox.setY(this.y);
        hitBox.setWidth(this.width);
        hitBox.setHeight(this.height);

        timer = System.currentTimeMillis() + 10000;
    }

    public int getId() {
        return id;
    }

    public Rectangle getHitBox() {
        return this.hitBox;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void update() {
        if (System.currentTimeMillis() > timer) {
            isActive = false;
        }
    }

    public void render(Graphics2D g) {
        g.drawImage(
                image,
                (int) x, (int) y,
                null
        );
    }
}
