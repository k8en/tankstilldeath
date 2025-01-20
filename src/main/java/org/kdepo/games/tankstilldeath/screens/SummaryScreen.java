package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;

import java.awt.*;
import java.util.Map;

public class SummaryScreen extends AbstractScreen {

    private final ResourcesController resourcesController;

    private Font font13x15o;

    public SummaryScreen() {
        this.name = Constants.Screens.SUMMARY;
        resourcesController = ResourcesController.getInstance();
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        font13x15o = resourcesController.getFont("font_n13x15o");
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {

    }

    @Override
    public void render(Graphics2D g) {
        // Console output on top of the all drawings
        font13x15o.render(g, "summary screen", 10, 10);
    }

    @Override
    public void dispose() {

    }
}
