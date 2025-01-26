package org.kdepo.graphics.k2d.tiles;

import org.kdepo.graphics.k2d.geometry.Rectangle;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class Tile extends Rectangle {

    private final int id;

    private final int tileX;

    private final int tileY;

    private final BufferedImage image;

    private Rectangle hitBox;

    public Tile(int id, int tileX, int tileY, double x, double y, BufferedImage image, Rectangle hitBox) {
        this.id = id;
        this.tileX = tileX;
        this.tileY = tileY;
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.image = image;
        this.hitBox = new Rectangle(this.x + hitBox.getX(), this.y + hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
    }

    public int getId() {
        return id;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Tile tile = (Tile) o;
        return Objects.equals(image, tile.image)
                && Objects.equals(hitBox, tile.hitBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), image, hitBox);
    }
}
