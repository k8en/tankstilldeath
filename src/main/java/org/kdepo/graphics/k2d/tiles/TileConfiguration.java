package org.kdepo.graphics.k2d.tiles;

import java.awt.image.BufferedImage;
import java.util.Objects;

public class TileConfiguration {

    private final int id;
    private final BufferedImage image;

    public TileConfiguration(int id, BufferedImage image) {
        this.id = id;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public BufferedImage getImage() {
        return image;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        TileConfiguration that = (TileConfiguration) o;
        return id == that.id
                && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, image);
    }
}
