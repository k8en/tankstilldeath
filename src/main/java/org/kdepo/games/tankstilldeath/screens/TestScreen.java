package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.controllers.BulletController;
import org.kdepo.games.tankstilldeath.model.MoveDirection;
import org.kdepo.games.tankstilldeath.model.Tank;
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

    private final BulletController bulletController;

    private AnimationController animationController;
    private BufferedImage biBackground;
    private Font font13x15o;

    private Tank tank;

    public TestScreen() {
        this.name = "test";
        resourcesController = ResourcesController.getInstance();
        bulletController = BulletController.getInstance();
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

        tank = new Tank(320, 200, MoveDirection.NORTH);
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        tank.resolveControls(keyHandler);
        tank.update();

        bulletController.update();

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

        tank.render(g);
        bulletController.render(g);
    }

    @Override
    public void dispose() {
        biBackground = null;
        font13x15o = null;
        animationController = null;
        tank = null;
    }

}
