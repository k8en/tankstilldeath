package org.kdepo.graphics.k2d.screens;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;

import java.awt.*;
import java.util.Map;

public abstract class AbstractScreen {

    /**
     * Screen name. Should be unique within the screen controller
     */
    protected String name;

    protected ScreenController screenController;

    public String getName() {
        return name;
    }

    public ScreenController getScreenController() {
        return screenController;
    }

    public void setScreenController(ScreenController screenController) {
        this.screenController = screenController;
    }

    /**
     * Initialize and prepare resources for use
     *
     * @param parameters init parameters
     */
    public abstract void initialize(Map<String, Object> parameters);

    public abstract void update(KeyHandler keyHandler, MouseHandler mouseHandler);

    public abstract void render(Graphics2D g);

    /**
     * Dispose used resources
     */
    public abstract void dispose();

}