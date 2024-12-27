package org.kdepo.graphics.k2d.geometry;

import java.util.Objects;

public class Rectangle extends Point {

    protected double width;

    protected double height;

    public Rectangle() {

    }

    public Rectangle(double x, double y, double width, double height) {
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + this.getX() +
                ", y=" + this.getY() +
                ", width=" + this.getWidth() +
                ", height=" + this.getHeight() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Rectangle rectangle = (Rectangle) o;
        return Double.compare(width, rectangle.width) == 0
                && Double.compare(height, rectangle.height) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), width, height);
    }
}