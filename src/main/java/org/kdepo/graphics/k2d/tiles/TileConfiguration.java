package org.kdepo.graphics.k2d.tiles;

import org.kdepo.graphics.k2d.geometry.Rectangle;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class TileConfiguration {

    private final int id;

    private final BufferedImage image;

    private final Rectangle hitBox;

    public TileConfiguration(int id, BufferedImage image, int x, int y, int width, int height) {
        this.id = id;
        this.image = image;
        this.hitBox = new Rectangle(x, y, width, height);
    }

    public int getId() {
        return id;
    }

    public BufferedImage getImage() {
        return image;
    }

    public Rectangle getHitBox() {
        return hitBox;
    }

    @Override
    public String toString() {
        return "TileConfiguration{" +
                "id=" + id +
                ", hitBox=" + hitBox +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TileConfiguration that = (TileConfiguration) o;
        return id == that.id
                && Objects.equals(image, that.image)
                && Objects.equals(hitBox, that.hitBox);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image, hitBox);
    }
}
