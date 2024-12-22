package org.kdepo.graphics.k2d.fonts;

import java.util.Objects;

public class SymbolConfiguration {

    /**
     * Symbol key code
     */
    private final int code;

    /**
     * Symbol width
     */
    private final int width;

    /**
     * Symbol height
     */
    private final int height;

    /**
     * Number of column with symbol. Column numbering starts with 0
     */
    private final int x;

    /**
     * Number of row with symbol. Row numbering starts with 0
     */
    private final int y;

    public SymbolConfiguration(int code, int width, int height, int x, int y) {
        this.code = code;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    public int getCode() {
        return this.code;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    @Override
    public String toString() {
        return "SymbolConfiguration{" +
                "code=" + code +
                ", width=" + width +
                ", height=" + height +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SymbolConfiguration that = (SymbolConfiguration) o;
        return code == that.code
                && width == that.width
                && height == that.height
                && x == that.x
                && y == that.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, width, height, x, y);
    }

}
