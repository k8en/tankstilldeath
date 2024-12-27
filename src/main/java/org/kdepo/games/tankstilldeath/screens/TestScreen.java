package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.controllers.BulletController;
import org.kdepo.games.tankstilldeath.controllers.TankController;
import org.kdepo.games.tankstilldeath.model.Bullet;
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
import org.kdepo.graphics.k2d.utils.CollisionsChecker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class TestScreen extends AbstractScreen {

    private final ResourcesController resourcesController;

    private final BulletController bulletController;
    private final TankController tankController;

    private AnimationController animationController;
    private BufferedImage biBackground;
    private Font font13x15o;

    private Tank playerTank;

    public TestScreen() {
        this.name = "test";
        resourcesController = ResourcesController.getInstance();
        bulletController = BulletController.getInstance();
        tankController = TankController.getInstance();
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

        playerTank = new Tank(320, 200, MoveDirection.NORTH);

        tankController.spawn(320, 50, MoveDirection.SOUTH);
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        tankController.update();

        playerTank.resolveControls(keyHandler);
        playerTank.update();

        bulletController.update();
        for (Bullet bullet : bulletController.getBulletList()) {
            if (bullet.isActive()) {
                for (Tank tank : tankController.getTankList()) {
                    if (CollisionsChecker.hasCollision(tank.getHitBox(), bullet.getHitBox())) {
                        bullet.setActive(false);
                    }
                }
            }
        }

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

        tankController.render(g);
        playerTank.render(g);
        bulletController.render(g);
    }

    @Override
    public void dispose() {
        biBackground = null;
        font13x15o = null;
        animationController = null;
        playerTank = null;
    }

}
