package org.kdepo.graphics.k2d.gui;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractPopup extends Rectangle {

    protected boolean isVisible;
    protected BufferedImage biBackground;

    protected int userAction;

    public AbstractPopup(int width, int height) {
        ResourcesController resourcesController = ResourcesController.getInstance();

        BufferedImage biPopupTopLeft = resourcesController.getImage("image_popup_top_left");
        BufferedImage biPopupTop = resourcesController.getImage("image_popup_top");
        BufferedImage biPopupTopRight = resourcesController.getImage("image_popup_top_right");
        BufferedImage biPopupRight = resourcesController.getImage("image_popup_right");
        BufferedImage biPopupBottomRight = resourcesController.getImage("image_popup_bottom_right");
        BufferedImage biPopupBottom = resourcesController.getImage("image_popup_bottom");
        BufferedImage biPopupBottomLeft = resourcesController.getImage("image_popup_bottom_left");
        BufferedImage biPopupLeft = resourcesController.getImage("image_popup_left");
        BufferedImage biPopupCenter = resourcesController.getImage("image_popup_center");

        this.x = 0;
        this.y = 0;
        this.width = width;
        this.height = height;

        biBackground = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = biBackground.createGraphics();

        int x0 = 0;
        int x1 = x0 + biPopupTopLeft.getWidth();
        int x2 = width - biPopupTopRight.getWidth();
        int x3 = width;

        int y0 = 0;
        int y1 = y0 + biPopupTopLeft.getHeight();
        int y2 = height - biPopupBottomRight.getHeight();
        int y3 = height;

        // Top left part
        g2d.drawImage(
                biPopupTopLeft,
                x0, y0, x1, y1,
                0, 0, biPopupTopLeft.getWidth(), biPopupTopLeft.getHeight(),
                null
        );

        // Top part
        g2d.drawImage(
                biPopupTop,
                x1, y0, x2, y1,
                0, 0, biPopupTop.getWidth(), biPopupTop.getHeight(),
                null
        );

        // Top right part
        g2d.drawImage(
                biPopupTopRight,
                x2, y0, x3, y1,
                0, 0, biPopupTopRight.getWidth(), biPopupTopRight.getHeight(),
                null
        );

        // Left part
        g2d.drawImage(
                biPopupLeft,
                x0, y1, x1, y2,
                0, 0, biPopupLeft.getWidth(), biPopupLeft.getHeight(),
                null
        );

        // Center part
        g2d.drawImage(
                biPopupCenter,
                x1, y1, x2, y2,
                0, 0, biPopupCenter.getWidth(), biPopupCenter.getHeight(),
                null
        );

        // Right part
        g2d.drawImage(
                biPopupRight,
                x2, y1, x3, y2,
                0, 0, biPopupRight.getWidth(), biPopupRight.getHeight(),
                null
        );

        // Bottom left part
        g2d.drawImage(
                biPopupBottomLeft,
                x0, y2, x1, y3,
                0, 0, biPopupBottomLeft.getWidth(), biPopupBottomLeft.getHeight(),
                null
        );

        // Bottom part
        g2d.drawImage(
                biPopupBottom,
                x1, y2, x2, y3,
                0, 0, biPopupBottom.getWidth(), biPopupBottom.getHeight(),
                null
        );

        // Bottom right part
        g2d.drawImage(
                biPopupBottomRight,
                x2, y2, x3, y3,
                0, 0, biPopupBottomRight.getWidth(), biPopupBottomRight.getHeight(),
                null
        );

        isVisible = false;
    }

    public abstract void update(KeyHandler keyHandler, MouseHandler mouseHandler);

    public void render(Graphics2D g) {
        if (isVisible) {
            g.drawImage(biBackground, (int) x, (int) y, null);
        }
    }

    public abstract void setFocusOnPreviousControls();

    public abstract void setFocusOnNextControls();

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public int getUserAction() {
        return userAction;
    }
}
