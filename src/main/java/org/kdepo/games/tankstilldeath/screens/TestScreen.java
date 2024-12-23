package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationController;
import org.kdepo.graphics.k2d.animations.AnimationPlayDirection;
import org.kdepo.graphics.k2d.animations.AnimationPlayMode;
import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class TestScreen extends AbstractScreen {

    private final ResourcesController resourcesController;

    private AnimationController animationController;
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

        Map<String, Animation> animationMap = resourcesController.getAnimations("animation_test");
        animationController = new AnimationController(
                animationMap,
                animationMap.get("animation_test"),
                AnimationPlayDirection.FORWARD,
                AnimationPlayMode.LOOP
        );
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        animationController.update();
    }

    @Override
    public void render(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.drawImage(biBackground, 0, 0, null);
        font13x15o.render(g, "This is a test", 10, 10);

        g.drawImage(
                animationController.getActiveFrame().getImage(),
                40, 40,
                null
        );
    }

    @Override
    public void dispose() {
        biBackground = null;
    }

}
