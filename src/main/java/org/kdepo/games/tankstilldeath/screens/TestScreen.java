package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.controllers.BulletController;
import org.kdepo.games.tankstilldeath.controllers.ExplosionController;
import org.kdepo.games.tankstilldeath.controllers.TankController;
import org.kdepo.games.tankstilldeath.model.Bullet;
import org.kdepo.games.tankstilldeath.model.MapData;
import org.kdepo.games.tankstilldeath.model.MoveDirection;
import org.kdepo.games.tankstilldeath.model.Tank;
import org.kdepo.games.tankstilldeath.utils.MapDataUtils;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.geometry.Point;
import org.kdepo.graphics.k2d.resources.Resource;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;
import org.kdepo.graphics.k2d.tiles.TileController;
import org.kdepo.graphics.k2d.utils.CollisionsChecker;

import java.awt.*;
import java.io.File;
import java.util.Map;

public class TestScreen extends AbstractScreen {

    private final ResourcesController resourcesController;

    private final BulletController bulletController;
    private final ExplosionController explosionController;
    private final TankController tankController;
    private final TileController tileController;

    private Font font13x15o;

    private Tank playerTank;

    private MapData mapData;

    public TestScreen() {
        this.name = "test";
        resourcesController = ResourcesController.getInstance();
        bulletController = BulletController.getInstance();
        explosionController = ExplosionController.getInstance();
        tankController = TankController.getInstance();
        tileController = TileController.getInstance();
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        font13x15o = resourcesController.getFont("font_n13x15o");

        playerTank = new Tank(320, 200, MoveDirection.NORTH);

        tankController.spawn(320, 50, MoveDirection.SOUTH);

        Resource mapResource = resourcesController.getResource("map_test");
        mapData = MapDataUtils.loadMapData(resourcesController.getPath() + mapResource.getPath());
        tileController.loadLayerData(mapData.getPathToFolder() + File.separator);
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        tankController.update();

        playerTank.resolveControls(keyHandler);
        playerTank.update();

        bulletController.update();
        explosionController.update();

        for (Bullet bullet : bulletController.getBulletList()) {
            if (bullet.isActive()) {
                for (Tank tank : tankController.getTankList()) {
                    if (CollisionsChecker.hasCollision(tank.getHitBox(), bullet.getHitBox())) {
                        bullet.setActive(false);
                        Point tankCenter = tank.getCenter();
                        explosionController.spawn(tankCenter.getX(), tankCenter.getY());
                    }
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        tileController.render(g, TileController.LAYER_BOTTOM);

        tileController.render(g, TileController.LAYER_MIDDLE);

        tankController.render(g);
        playerTank.render(g);
        bulletController.render(g);
        explosionController.render(g);

        tileController.render(g, TileController.LAYER_TOP);

        font13x15o.render(g, "This is a test", 10, 10);
    }

    @Override
    public void dispose() {
        font13x15o = null;
        playerTank = null;
    }

}
