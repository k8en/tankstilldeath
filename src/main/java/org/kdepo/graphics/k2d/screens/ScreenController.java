package org.kdepo.graphics.k2d.screens;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ScreenController {

    /**
     * Current active screen to update and render
     */
    private AbstractScreen activeScreen;

    /**
     * All available screens map
     */
    private final Map<String, AbstractScreen> screenMap;

    /**
     * Screen width in pixels
     */
    private int screenWidth;

    /**
     * Screen height in pixels
     */
    private int screenHeight;

    public ScreenController() {
        screenMap = new HashMap<>();
    }

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

    public void addScreen(AbstractScreen screen) {
        // Screen name is mandatory parameter
        if (screen.getName() == null) {
            throw new RuntimeException("Screen name not provided");
        }

        // Screen name should be unique
        if (screenMap.get(screen.getName()) != null) {
            throw new RuntimeException("Screen already exist for name " + screen.getName());
        }

        // Set back reference
        screen.screenController = this;

        screenMap.put(screen.getName(), screen);
    }

    /**
     * Set current active screen for update and render
     *
     * @param screenName name of screen to set as active
     * @param parameters parameters shared between screens
     */
    public void setActiveScreen(String screenName, Map<String, Object> parameters) {
        // Dispose resources for the current active screen
        if (activeScreen != null) {
            activeScreen.dispose();
        }

        // Search for the new active screen
        AbstractScreen screen = screenMap.get(screenName);
        if (screen == null) {
            throw new RuntimeException("Screen not found: " + screenName);
        }

        screen.initialize(parameters);

        // Set screen as active
        activeScreen = screen;
    }

    public void render(Graphics2D g) {
        this.activeScreen.render(g);
    }

    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        this.activeScreen.update(keyHandler, mouseHandler);
    }

}