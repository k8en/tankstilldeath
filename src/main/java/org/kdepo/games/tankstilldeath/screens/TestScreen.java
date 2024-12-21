package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.screens.AbstractScreen;

import java.awt.*;
import java.util.Map;

public class TestScreen extends AbstractScreen {

    public TestScreen() {
        this.name = "test";
    }

    @Override
    public void initialize(Map<String, Object> parameters) {

    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {

    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawLine(0,0, screenController.getScreenWidth(), screenController.getScreenHeight());
    }

    @Override
    public void dispose() {

    }

}
