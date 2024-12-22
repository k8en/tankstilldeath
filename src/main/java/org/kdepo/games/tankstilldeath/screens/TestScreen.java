package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class TestScreen extends AbstractScreen {

    private final ResourcesController resourcesController;

    private BufferedImage biBackground;
    private Font font13x15o;

    public TestScreen() {
        this.name = "test";
        resourcesController = ResourcesController.getInstance();
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        biBackground = resourcesController.getImage("image_background");
        font13x15o = resourcesController.getFont("font_n13x15o");
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawImage(biBackground, 0, 0, null);
        font13x15o.render(g, "This is a test", 10, 10);
    }

    @Override
    public void dispose() {
        biBackground = null;
    }

}
