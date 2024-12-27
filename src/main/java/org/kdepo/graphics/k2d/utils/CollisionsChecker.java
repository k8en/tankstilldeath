package org.kdepo.graphics.k2d.utils;

import org.kdepo.graphics.k2d.geometry.Rectangle;

public class CollisionsChecker {

    public static boolean hasCollision(Rectangle boundingBox, double x, double y) {
        // Bottom left point
        double x1 = boundingBox.getX();
        double y1 = boundingBox.getY();

        // Top right point
        double x2 = x1 + boundingBox.getWidth();
        double y2 = y1 + boundingBox.getHeight();

        return x >= x1 && x <= x2 & y >= y1 && y <= y2;
    }

    public static boolean hasCollision(Rectangle rect1, Rectangle rect2) {
        double rect1X1 = rect1.getX();
        double rect1X2 = rect1.getX() + rect1.getWidth();
        double rect1Y1 = rect1.getY();
        double rect1Y2 = rect1.getY() + rect1.getHeight();

        double rect2X1 = rect2.getX();
        double rect2X2 = rect2.getX() + rect2.getWidth();
        double rect2Y1 = rect2.getY();
        double rect2Y2 = rect2.getY() + rect2.getHeight();

        boolean xCollides = rect1X2 >= rect2X1 && rect1X1 <= rect2X2;
        boolean yCollides = rect1Y2 >= rect2Y1 && rect1Y1 <= rect2Y2;

        return xCollides && yCollides;
    }

}
