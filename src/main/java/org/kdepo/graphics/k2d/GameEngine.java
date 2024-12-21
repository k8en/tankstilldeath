package org.kdepo.graphics.k2d;

import java.awt.*;

public class GameEngine {

    private int screenWidth;

    private int screenHeight;

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }

    /**
     * Update application state
     */
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
    }

    /**
     * Render application graphics
     */
    public void render(Graphics2D g) {
    }

}
