package org.kdepo.graphics.k2d.tiles;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class Tile {

    private final int x;

    private final int y;

    private final BufferedImage image;

    public Tile(int x, int y, BufferedImage image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Tile tile = (Tile) o;
        return x == tile.x
                && y == tile.y
                && Objects.equals(image, tile.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, image);
    }
}
