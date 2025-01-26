package org.kdepo.graphics.k2d;

import org.kdepo.graphics.k2d.screens.AbstractScreen;
import org.kdepo.graphics.k2d.screens.ScreenController;

import java.awt.*;
import java.util.Map;

public class GameEngine {

    private int screenWidth;

    private int screenHeight;

    private final ScreenController screenController;

    public GameEngine() {
        screenController = new ScreenController();
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
        screenController.setScreenWidth(screenWidth);
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
        screenController.setScreenHeight(screenHeight);
    }

    public void addScreen(AbstractScreen screen) {
        screenController.addScreen(screen);
    }

    public void setActiveScreen(String screenName, Map<String, Object> parameters) {
        screenController.setActiveScreen(screenName, parameters);
    }

    /**
     * Update application state
     */
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        screenController.update(keyHandler, mouseHandler);
    }

    /**
     * Render application graphics
     */
    public void render(Graphics2D g) {
        screenController.render(g);
    }

}
