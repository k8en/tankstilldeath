package org.kdepo.graphics.k2d.gui;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.geometry.Rectangle;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class AbstractButton extends Rectangle {

    protected BufferedImage image;
    protected BufferedImage imageHighlighted;
    protected int buttonId;

    protected boolean hasFocus;

    public abstract void update(KeyHandler keyHandler, MouseHandler mouseHandler);

    public void render(Graphics2D g) {
        if (hasFocus) {
            g.drawImage(imageHighlighted, (int) x, (int) y, null);
        } else {
            g.drawImage(image, (int) x, (int) y, null);
        }
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        width = image.getWidth();
        height = image.getHeight();
    }

    public void setImageHighlighted(BufferedImage imageHighlighted) {
        this.imageHighlighted = imageHighlighted;
    }

    public int getButtonId() {
        return buttonId;
    }

    public boolean hasFocus() {
        return hasFocus;
    }

    public void setFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}
