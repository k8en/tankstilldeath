package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Base extends Rectangle {

    private BufferedImage image;

    private Rectangle hitBox;

    private boolean isActive;

    public Base() {
        ResourcesController resourcesController = ResourcesController.getInstance();
        image = resourcesController.getImage("image_base");

        this.x = 608;
        this.y = 896;
        this.width = image.getWidth();
        this.height = image.getHeight();

        this.hitBox = new Rectangle();
        hitBox.setX(this.x);
        hitBox.setY(this.y);
        hitBox.setWidth(this.width);
        hitBox.setHeight(this.height);

        isActive = false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    public void update() {

    }

    public void render(Graphics2D g) {
        g.drawImage(
                image,
                (int) x, (int) y,
                null
        );
    }
}
