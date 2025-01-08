package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.games.tankstilldeath.controllers.BonusController;
import org.kdepo.games.tankstilldeath.controllers.BulletController;
import org.kdepo.games.tankstilldeath.controllers.ExplosionController;
import org.kdepo.games.tankstilldeath.controllers.SpawnSpotController;
import org.kdepo.games.tankstilldeath.controllers.TankController;
import org.kdepo.games.tankstilldeath.model.Base;
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
import org.kdepo.graphics.k2d.tiles.Tile;
import org.kdepo.graphics.k2d.tiles.TileController;
import org.kdepo.graphics.k2d.utils.CollisionsChecker;

import java.awt.*;
import java.io.File;
import java.util.ListIterator;
import java.util.Map;

public class TestScreen extends AbstractScreen {

    private final ResourcesController resourcesController;

    private final BonusController bonusController;
    private final BulletController bulletController;
    private final ExplosionController explosionController;
    private final SpawnSpotController spawnSpotController;
    private final TankController tankController;
    private final TileController tileController;

    private Font font13x15o;

    private Tank playerTank;

    private MapData mapData;
    private Base base;

    public TestScreen() {
        this.name = "test";
        resourcesController = ResourcesController.getInstance();
        bonusController = BonusController.getInstance();
        bulletController = BulletController.getInstance();
        explosionController = ExplosionController.getInstance();
        spawnSpotController = SpawnSpotController.getInstance();
        tankController = TankController.getInstance();
        tileController = TileController.getInstance();

        Resource tileConfigurationResource = resourcesController.getResource("tile_configuration");
        tileController.loadTilesConfigurations(resourcesController.getPath() + tileConfigurationResource.getPath());

        Resource tankConfigurationResource = resourcesController.getResource("tank_configuration");
        tankController.loadTanksConfigurations(resourcesController.getPath() + tankConfigurationResource.getPath());
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        font13x15o = resourcesController.getFont("font_n13x15o");

        Resource mapResource = resourcesController.getResource("map_test");
        mapData = MapDataUtils.loadMapData(resourcesController.getPath() + mapResource.getPath());
        spawnSpotController.loadSpawnSpotData(mapData.getPathToFolder() + File.separator + mapData.getFileNameSpawnSpots());
        tankController.loadTanksToSpawnData(mapData.getPathToFolder() + File.separator + mapData.getFileNameTanksToSpawn());
        tankController.setActiveTanksLimit(mapData.getActiveTanksLimit());
        tileController.loadLayerData(mapData.getPathToFolder() + File.separator);

        base = new Base();
        base.setActive(true);

        playerTank = tankController.prepareTank(0, Constants.Teams.PLAYER_ID, 500, 600, MoveDirection.NORTH);

        bonusController.spawn(500, 500, Constants.Bonuses.STAR_ID);
        bonusController.spawn(600, 500, Constants.Bonuses.SHIELD_ID);
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        tankController.update();
        if (tankController.canSpawn()) {
            tankController.spawn(Constants.Teams.ENEMY_ID);
        }

        playerTank.resolveControls(keyHandler);
        playerTank.update();

        bonusController.update();
        bulletController.update();
        explosionController.update();
        spawnSpotController.update();

        for (Bullet bullet : bulletController.getBulletList()) {
            if (bullet.isActive()) {
                ListIterator<Tank> it = tankController.getActiveTanksList().listIterator();
                while (it.hasNext()) {
                    Tank tank = it.next();
                    if (CollisionsChecker.hasCollision(tank.getHitBox(), bullet.getHitBox())) {
                        bullet.setActive(false);
                        Point tankCenter = tank.getCenter();
                        explosionController.spawn(tankCenter.getX(), tankCenter.getY(), Constants.Explosions.ANIMATION_MEDIUM);
                        it.remove();
                    }
                }
            }

            if (bullet.isActive()) {
                Point bulletCenter = bullet.getCenter();
                Tile tile = tileController.getTile(TileController.LAYER_MIDDLE, bulletCenter.getX(), bulletCenter.getY());
                if (tile != null && CollisionsChecker.hasCollision(tile.getHitBox(), bulletCenter.getX(), bulletCenter.getY())) {
                    bullet.setActive(false);
                    explosionController.spawn(bulletCenter.getX(), bulletCenter.getY(), Constants.Explosions.ANIMATION_SMALL);

                    if (tile.getId() == 2) {
                        if (MoveDirection.NORTH.equals(bullet.getMoveDirection())) {
                            tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), 3);

                        } else if (MoveDirection.EAST.equals(bullet.getMoveDirection())) {
                            tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), 4);

                        } else if (MoveDirection.SOUTH.equals(bullet.getMoveDirection())) {
                            tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), 5);

                        } else if (MoveDirection.WEST.equals(bullet.getMoveDirection())) {
                            tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), 6);

                        }

                    } else if (tile.getId() == 3 || tile.getId() == 4 || tile.getId() == 5 || tile.getId() == 6) {
                        tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                    }

                }
            }

            if (bullet.isActive() && base.isActive()) {
                if (CollisionsChecker.hasCollision(base.getHitBox(), bullet.getHitBox())) {
                    bullet.setActive(false);
                    base.setActive(false);
                    Point baseCenter = base.getCenter();
                    explosionController.spawn(baseCenter.getX(), baseCenter.getY(), Constants.Explosions.ANIMATION_BIG);
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
        bonusController.render(g);

        if (base.isActive()) {
            base.render(g);
        }

        bulletController.render(g);
        explosionController.render(g);

        tileController.render(g, TileController.LAYER_TOP);

        font13x15o.render(g, "This is a test", 10, 10);
    }

    @Override
    public void dispose() {
        base = null;
        font13x15o = null;
        playerTank = null;
    }
}
