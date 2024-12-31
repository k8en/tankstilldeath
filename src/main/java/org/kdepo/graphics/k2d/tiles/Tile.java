package org.kdepo.graphics.k2d.tiles;

import org.kdepo.graphics.k2d.geometry.Rectangle;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class Tile extends Rectangle {

    private final BufferedImage image;

    private Rectangle hitBox;

    public Tile(double x, double y, BufferedImage image, Rectangle hitBox) {
        this.x = x;
        this.y = y;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.image = image;
        this.hitBox = new Rectangle(this.x + hitBox.getX(), this.y + hitBox.getY(), hitBox.getWidth(), hitBox.getHeight());
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
