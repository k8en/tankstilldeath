package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.games.tankstilldeath.controllers.SpawnSpotController;
import org.kdepo.games.tankstilldeath.controllers.TankController;
import org.kdepo.games.tankstilldeath.model.Base;
import org.kdepo.games.tankstilldeath.model.Bonus;
import org.kdepo.games.tankstilldeath.model.Bullet;
import org.kdepo.games.tankstilldeath.model.Explosion;
import org.kdepo.games.tankstilldeath.model.MapData;
import org.kdepo.games.tankstilldeath.model.MoveDirection;
import org.kdepo.games.tankstilldeath.model.SpawnSpot;
import org.kdepo.games.tankstilldeath.model.Tank;
import org.kdepo.games.tankstilldeath.model.TankOnDestroyEventType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

public class BattleScreen extends AbstractScreen {

    private final ResourcesController resourcesController;

    private final Random randomizer;

    private final SpawnSpotController spawnSpotController;
    private final TankController tankController;
    private final TileController tileController;

    private Font font13x15o;

    private int playerTanksCounter;
    private Tank playerTank;

    private MapData mapData;

    private int activeTanksLimit;
    private final List<Tank> activeTanksList;
    private final List<Tank> tanksToSpawnList;

    private final List<Bullet> bulletList;
    private final List<Bonus> bonusList;
    private final List<Explosion> explosionList;

    private Base base;

    public BattleScreen() {
        this.name = Constants.Screens.BATTLE;

        randomizer = new Random();

        resourcesController = ResourcesController.getInstance();
        spawnSpotController = SpawnSpotController.getInstance();
        tankController = TankController.getInstance();
        tileController = TileController.getInstance();

        Resource tileConfigurationResource = resourcesController.getResource("tile_configuration");
        tileController.loadTilesConfigurations(resourcesController.getPath() + tileConfigurationResource.getPath());

        Resource tankConfigurationResource = resourcesController.getResource("tank_configuration");
        tankController.loadTanksConfigurations(resourcesController.getPath() + tankConfigurationResource.getPath());

        playerTanksCounter = 3;

        activeTanksList = new ArrayList<>();
        bonusList = new ArrayList<>();
        bulletList = new ArrayList<>();
        explosionList = new ArrayList<>();
        tanksToSpawnList = new ArrayList<>();
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        activeTanksList.clear();
        bonusList.clear();
        bulletList.clear();
        explosionList.clear();
        tanksToSpawnList.clear();

        font13x15o = resourcesController.getFont("font_n13x15o");

        Resource mapResource = resourcesController.getResource("map_test");
        mapData = MapDataUtils.loadMapData(resourcesController.getPath() + mapResource.getPath());
        spawnSpotController.loadSpawnSpotData(mapData.getPathToFolder() + File.separator + mapData.getFileNameSpawnSpots());

        activeTanksLimit = mapData.getActiveTanksLimit();

        tanksToSpawnList.clear();
        List<Tank> tanksToSpawnList = tankController.getTanksToSpawnList(mapData.getPathToFolder() + File.separator + mapData.getFileNameTanksToSpawn());
        this.tanksToSpawnList.addAll(tanksToSpawnList);

        tileController.loadLayerData(mapData.getPathToFolder() + File.separator);

        base = new Base();
        base.setActive(true);

        SpawnSpot playerSpawnSpot = spawnSpotController.getAvailableSpawnSpot(Constants.Teams.PLAYER_ID);
        if (playerSpawnSpot != null) {
            spawnPlayerTank(playerSpawnSpot);
        } else {
            throw new RuntimeException("Player spawn spot is not found for map: " + resourcesController.getPath() + mapResource.getPath());
        }

        // Test bonuses placement
        int bonusX = Constants.SCREEN_WIDTH / 6 - 16;
        int bonusY = Constants.SCREEN_HEIGHT / 2 - 16;
        spawnBonus(bonusX + bonusX * 0, bonusY, Constants.Bonuses.STAR_ID);
        spawnBonus(bonusX + bonusX * 1, bonusY, Constants.Bonuses.SHIELD_ID);
        spawnBonus(bonusX + bonusX * 2, bonusY, Constants.Bonuses.TANK_ID);
        spawnBonus(bonusX + bonusX * 3, bonusY, Constants.Bonuses.GRENADE_ID);
        spawnBonus(bonusX + bonusX * 4, bonusY, Constants.Bonuses.SHOVEL_ID);
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        // Update bullets state
        for (Bullet bullet : bulletList) {
            if (bullet.isActive()) {
                bullet.update();
            }
        }

        // Check if bullet is outside the screen
        for (Bullet bullet : bulletList) {
            if (bullet.isActive()) {
                if (bullet.getX() < 0 || bullet.getX() > Constants.SCREEN_WIDTH || bullet.getY() < 0 || bullet.getY() > Constants.SCREEN_HEIGHT) {
                    bullet.setActive(false);
                }
            }
        }

        // Check if bullet hit a tile
        for (Bullet bullet : bulletList) {
            if (bullet.isActive()) {
                Point bulletCenter = bullet.getCenter();
                Tile tile = tileController.getTile(TileController.LAYER_MIDDLE, bulletCenter.getX(), bulletCenter.getY());
                if (tile != null && CollisionsChecker.hasCollision(tile.getHitBox(), bulletCenter.getX(), bulletCenter.getY())) {
                    bullet.setActive(false);

                    spawnExplosion(bulletCenter.getX(), bulletCenter.getY(), Constants.Explosions.ANIMATION_SMALL);

                    if (tile.getId() == Constants.Tiles.FULL_BRICKS_BLOCK_ID) {
                        if (MoveDirection.NORTH.equals(bullet.getMoveDirection())) {
                            if (Constants.Bullets.STANDARD_ID == bullet.getBulletId()) {
                                tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), Constants.Tiles.BRICKS_AT_THE_NORTH_BLOCK_ID);
                            } else {
                                tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                            }

                        } else if (MoveDirection.EAST.equals(bullet.getMoveDirection())) {
                            if (Constants.Bullets.STANDARD_ID == bullet.getBulletId()) {
                                tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), Constants.Tiles.BRICKS_AT_THE_EAST_BLOCK_ID);
                            } else {
                                tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                            }

                        } else if (MoveDirection.SOUTH.equals(bullet.getMoveDirection())) {
                            if (Constants.Bullets.STANDARD_ID == bullet.getBulletId()) {
                                tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), Constants.Tiles.BRICKS_AT_THE_SOUTH_BLOCK_ID);
                            } else {
                                tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                            }

                        } else if (MoveDirection.WEST.equals(bullet.getMoveDirection())) {
                            if (Constants.Bullets.STANDARD_ID == bullet.getBulletId()) {
                                tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), Constants.Tiles.BRICKS_AT_THE_WEST_BLOCK_ID);
                            } else {
                                tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                            }

                        }

                    } else if (tile.getId() == Constants.Tiles.BRICKS_AT_THE_NORTH_BLOCK_ID
                            || tile.getId() == Constants.Tiles.BRICKS_AT_THE_EAST_BLOCK_ID
                            || tile.getId() == Constants.Tiles.BRICKS_AT_THE_SOUTH_BLOCK_ID
                            || tile.getId() == Constants.Tiles.BRICKS_AT_THE_WEST_BLOCK_ID) {
                        tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());

                    } else if (tile.getId() == Constants.Tiles.CONCRETE_BLOCK_ID) {
                        if (Constants.Bullets.ARMOUR_PIERCING_ID == bullet.getBulletId()) {
                            tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                        }
                    }
                }
            }
        }

        // Check if bullet hit a base
        if (base.isActive()) {
            for (Bullet bullet : bulletList) {
                if (bullet.isActive()) {
                    if (CollisionsChecker.hasCollision(base.getHitBox(), bullet.getHitBox())) {
                        bullet.setActive(false);
                        base.setActive(false);
                        Point baseCenter = base.getCenter();
                        spawnExplosion(baseCenter.getX(), baseCenter.getY(), Constants.Explosions.ANIMATION_BIG);

                        // TODO player win condition
                        //..
                        System.out.println("player lose (base)");
                    }
                }
            }
        }

        // Check if bullet hit an enemy tank
        for (Bullet bullet : bulletList) {
            if (bullet.isActive()) {
                ListIterator<Tank> it = activeTanksList.listIterator();
                while (it.hasNext()) {
                    Tank tank = it.next();
                    if (CollisionsChecker.hasCollision(tank.getHitBox(), bullet.getHitBox())) {
                        bullet.setActive(false);
                        if (tank.isProtectedByShield()) {
                            Point bulletCenter = bullet.getCenter();
                            spawnExplosion(bulletCenter.getX(), bulletCenter.getY(), Constants.Explosions.ANIMATION_SMALL);
                        } else {
                            updateTankArmourOnHit(tank, bullet);
                            boolean isTankDestroyed = updateTankIsDestroyed(tank, bullet);
                            if (isTankDestroyed) {
                                if (tank.getOnDestroyEventType() != null) {
                                    processTankOnDestroyEvent(tank.getOnDestroyEventType());
                                }
                                it.remove();
                            }
                        }
                    }
                }
            }
        }

        // Check if bullet hit a player tank
        if (playerTank.isActive()) {
            for (Bullet bullet : bulletList) {
                if (bullet.isActive()) {
                    if (CollisionsChecker.hasCollision(playerTank.getHitBox(), bullet.getHitBox())) {
                        bullet.setActive(false);
                        if (playerTank.isProtectedByShield()) {
                            Point bulletCenter = bullet.getCenter();
                            spawnExplosion(bulletCenter.getX(), bulletCenter.getY(), Constants.Explosions.ANIMATION_SMALL);
                        } else {
                            updateTankArmourOnHit(playerTank, bullet);
                            boolean isTankDestroyed = updateTankIsDestroyed(playerTank, bullet);
                            if (isTankDestroyed) {
                                playerTank.setActive(false);
                                playerTanksCounter = playerTanksCounter - 1;
                            }
                        }
                    }
                }
            }
        }

        // Process active tanks
        for (Tank tank : activeTanksList) {
            tank.resolveControlsAutomatically(
                    playerTank.getHitBox(),
                    base.getHitBox()
            );

            if (tank.isMoving()) {
                updateTankMovement(tank);
            }

            // Check for enemy tanks collisions with bonus
            for (Bonus bonus : bonusList) {
                if (bonus.isActive()) {
                    if (CollisionsChecker.hasCollision(tank.getHitBox(), bonus.getHitBox())) {
                        bonus.setActive(false);

                        if (Constants.Bonuses.STAR_ID == bonus.getBonusId()) {
                            tank.increaseReloadingSpeed();
                            tank.increaseMoveSpeed();

                        } else if (Constants.Bonuses.SHIELD_ID == bonus.getBonusId()) {
                            tank.setProtectedByShield(Constants.SHIELD_PROTECTION_TIME);

                        } else if (Constants.Bonuses.TANK_ID == bonus.getBonusId()) {
                            Tank additionalTank = tankController.prepareTank(tank.getTankId(), tank.getTeamId(), 0, 0, MoveDirection.NORTH);
                            additionalTank.setMoveSpeed(tank.getMoveSpeed());
                            additionalTank.setBulletTypeId(tank.getBulletTypeId());

                        } else if (Constants.Bonuses.GRENADE_ID == bonus.getBonusId()) {
                            if (playerTank.isActive()) {
                                Point playerTankCenter = playerTank.getCenter();
                                spawnExplosion(playerTankCenter.getX(), playerTankCenter.getY(), Constants.Explosions.ANIMATION_MEDIUM);
                                playerTank.setActive(false);
                                playerTanksCounter = playerTanksCounter - 1;
                            }

                        } else if (Constants.Bonuses.SHOVEL_ID == bonus.getBonusId()) {
                            tileController.setTilesAround(Constants.Tiles.NO_TILE, base.getHitBox());

                        } else {
                            throw new RuntimeException("Cannot resolve effect for bonus id " + bonus.getBonusId());
                        }
                    }
                }
            }

            // Process weaponry
            updateTankWeaponry(tank);

            // Update animations
            tank.update();
        }

        // Process player tank
        if (playerTank.isActive()) {
            playerTank.resolveControls(keyHandler);
        }

        if (playerTank.isActive() && playerTank.isMoving()) {
            updateTankMovement(playerTank);
        }

        // Check for player tank collision with bonus
        if (playerTank.isActive()) {
            for (Bonus bonus : bonusList) {
                if (bonus.isActive()) {
                    if (CollisionsChecker.hasCollision(playerTank.getHitBox(), bonus.getHitBox())) {
                        bonus.setActive(false);

                        if (Constants.Bonuses.STAR_ID == bonus.getBonusId()) {
                            playerTank.increaseReloadingSpeed();
                            playerTank.increaseMoveSpeed();

                        } else if (Constants.Bonuses.SHIELD_ID == bonus.getBonusId()) {
                            playerTank.setProtectedByShield(Constants.SHIELD_PROTECTION_TIME);

                        } else if (Constants.Bonuses.TANK_ID == bonus.getBonusId()) {
                            playerTanksCounter = playerTanksCounter + 1;

                        } else if (Constants.Bonuses.GRENADE_ID == bonus.getBonusId()) {
                            ListIterator<Tank> it = activeTanksList.listIterator();
                            while (it.hasNext()) {
                                Tank tank = it.next();
                                Point tankCenter = tank.getCenter();
                                spawnExplosion(tankCenter.getX(), tankCenter.getY(), Constants.Explosions.ANIMATION_MEDIUM);
                                tank.setActive(false);
                                it.remove();
                            }

                        } else if (Constants.Bonuses.SHOVEL_ID == bonus.getBonusId()) {
                            tileController.setTilesAround(Constants.Tiles.CONCRETE_BLOCK_ID, base.getHitBox());

                        } else {
                            throw new RuntimeException("Cannot resolve effect for bonus id " + bonus.getBonusId());
                        }
                    }
                }
            }
        }

        // Process player tank weaponry
        if (playerTank.isActive()) {
            updateTankWeaponry(playerTank);
        }

        // Update animations
        if (playerTank.isActive()) {
            playerTank.update();
        }

        // Update enemy tanks list on a screen
        if (tanksToSpawnList.isEmpty()) {
            if (activeTanksList.isEmpty()) {
                // TODO player win condition
                //..
                System.out.println("player win");
            }
        } else {
            if (activeTanksList.size() < activeTanksLimit) {
                SpawnSpot spawnSpot = spawnSpotController.getAvailableSpawnSpot(Constants.Teams.ENEMY_ID);
                if (spawnSpot != null) {
                    spawnEnemyTank(spawnSpot);
                }
            }
        }

        // Update player tank presence on a screen
        if (!playerTank.isActive()) {
            if (playerTanksCounter > 0) {
                SpawnSpot playerSpawnSpot = spawnSpotController.getAvailableSpawnSpot(Constants.Teams.PLAYER_ID);
                if (playerSpawnSpot != null) {
                    spawnPlayerTank(playerSpawnSpot);
                }
            } else {
                // TODO player lose condition
                //..
                System.out.println("player lose");
            }
        }

        // Update spawn spots state
        spawnSpotController.update();

        // Update bonuses state
        for (Bonus bonus : bonusList) {
            if (bonus.isActive()) {
                bonus.update();
            }
        }

        // Update explosions state
        for (Explosion explosion : explosionList) {
            if (explosion.isActive()) {
                explosion.update();
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        tileController.render(g, TileController.LAYER_BOTTOM);

        tileController.render(g, TileController.LAYER_MIDDLE);

        for (Tank tank : activeTanksList) {
            if (tank.isActive()) {
                tank.render(g);
            }
        }

        if (playerTank.isActive()) {
            playerTank.render(g);
        }

        for (Bonus bonus : bonusList) {
            if (bonus.isActive()) {
                bonus.render(g);
            }
        }

        if (base.isActive()) {
            base.render(g);
        }

        for (Bullet bullet : bulletList) {
            if (bullet.isActive()) {
                bullet.render(g);
            }
        }

        for (Explosion explosion : explosionList) {
            if (explosion.isActive()) {
                explosion.render(g);
            }
        }

        tileController.render(g, TileController.LAYER_TOP);

        font13x15o.render(g, "This is a test", 10, 10);
    }

    @Override
    public void dispose() {
        base = null;
        font13x15o = null;
        playerTank = null;
    }

    private void updateTankWeaponry(Tank tank) {
        if (tank.isReadyToShot()) {
            Point bulletOffset = tank.getBulletOffset();
            spawnBullet(
                    tank.getX() + bulletOffset.getX(),
                    tank.getY() + bulletOffset.getY(),
                    tank.getMoveDirection(),
                    tank.getTeamId()
            );
            tank.shot();

        } else {
            tank.updateReloading();
        }
    }

    private void updateTankMovement(Tank tank) {
        double nextX = tank.getX() + tank.getMoveSpeed() * tank.getMoveDirection().getX();
        double nextY = tank.getY() + tank.getMoveSpeed() * tank.getMoveDirection().getY();

        tank.getHitBox().setX(nextX + tank.getHitBoxOffsetX());
        tank.getHitBox().setY(nextY + tank.getHitBoxOffsetY());

        if (tileController.hasCollision(tank.getHitBox())) {
            // Rollback hit box position
            tank.getHitBox().setX(tank.getX() + tank.getHitBoxOffsetX());
            tank.getHitBox().setY(tank.getY() + tank.getHitBoxOffsetY());

        } else {
            // Update image position
            tank.setX(nextX);
            tank.setY(nextY);
        }
    }

    private boolean updateTankIsDestroyed(Tank tank, Bullet bullet) {
        boolean isDestroyed = false;

        if (tank.getArmour() == 0) {
            Point tankCenter = tank.getCenter();
            spawnExplosion(tankCenter.getX(), tankCenter.getY(), Constants.Explosions.ANIMATION_MEDIUM);

            isDestroyed = true;

        } else {
            Point bulletCenter = bullet.getCenter();
            spawnExplosion(bulletCenter.getX(), bulletCenter.getY(), Constants.Explosions.ANIMATION_SMALL);

        }

        return isDestroyed;
    }

    public void updateTankArmourOnHit(Tank tank, Bullet bullet) {
        // Update tank armour according to bullet power
        if (bullet.getBulletId() == Constants.Bullets.STANDARD_ID) {
            tank.changeArmour(-1);

        } else if (bullet.getBulletId() == Constants.Bullets.ARMOUR_PIERCING_ID) {
            tank.changeArmour(-3);

        } else {
            throw new RuntimeException("Collision is not implemented for bullet id " + bullet.getBulletId());
        }
    }

    public void spawnBonus(double x, double y, int bonusId) {
        Bonus bonusToSpawn = null;
        for (Bonus bonus : bonusList) {
            if (!bonus.isActive()) {
                bonusToSpawn = bonus;
                break;
            }
        }

        if (bonusToSpawn == null) {
            bonusToSpawn = new Bonus(x, y, bonusId);
            bonusToSpawn.setActive(true);
            bonusList.add(bonusToSpawn);
        } else {
            bonusToSpawn.setX(x);
            bonusToSpawn.setY(y);
            bonusToSpawn.getHitBox().setX(x);
            bonusToSpawn.getHitBox().setY(y);
            bonusToSpawn.setActive(true);
        }
    }

    private void spawnBullet(double x, double y, MoveDirection moveDirection, int teamId) {
        Bullet bulletToSpawn = null;
        for (Bullet bullet : bulletList) {
            if (!bullet.isActive()) {
                bulletToSpawn = bullet;
                break;
            }
        }

        if (bulletToSpawn == null) {
            bulletToSpawn = new Bullet(x, y, moveDirection);
            bulletToSpawn.setTeamId(teamId);
            bulletToSpawn.setActive(true);
            bulletList.add(bulletToSpawn);
        } else {
            bulletToSpawn.setX(x);
            bulletToSpawn.setY(y);
            bulletToSpawn.setMoveDirection(moveDirection);
            bulletToSpawn.setTeamId(teamId);
            bulletToSpawn.setActive(true);
        }
    }

    public void spawnExplosion(double centerX, double centerY, String animationMapName) {
        Explosion explosionToSpawn = null;
        for (Explosion explosion : explosionList) {
            if (!explosion.isActive()) {
                explosionToSpawn = explosion;
                break;
            }
        }

        if (explosionToSpawn == null) {
            explosionToSpawn = new Explosion(0, 0, animationMapName);
            explosionToSpawn.setCenter(centerX, centerY);
            explosionToSpawn.setActive(true);
            explosionList.add(explosionToSpawn);
        } else {
            explosionToSpawn.restartAnimation(animationMapName);
            explosionToSpawn.setCenter(centerX, centerY);
            explosionToSpawn.setActive(true);
        }
    }

    public void spawnEnemyTank(SpawnSpot spawnSpot) {
        spawnSpot.setActive(true);
        spawnSpot.restartTimer();

        Point spawnPointCenter = spawnSpot.getCenter();

        Tank tankToSpawn = tanksToSpawnList.get(0);
        tankToSpawn.setCenter(spawnPointCenter.getX(), spawnPointCenter.getY());
        tankToSpawn.setMoveDirection(spawnSpot.getMoveDirection());
        tankToSpawn.setActive(true);
        activeTanksList.add(tankToSpawn);
        tanksToSpawnList.remove(0);
        System.out.println("Object spawned: " + tankToSpawn);
    }

    public void spawnPlayerTank(SpawnSpot spawnSpot) {
        spawnSpot.setActive(true);
        spawnSpot.restartTimer();

        Point spawnPointCenter = spawnSpot.getCenter();

        playerTank = tankController.prepareTank(0, Constants.Teams.PLAYER_ID, 0, 0, MoveDirection.NORTH);
        playerTank.setCenter(spawnPointCenter.getX(), spawnPointCenter.getY());
        playerTank.setMoveDirection(spawnSpot.getMoveDirection());
        playerTank.setActive(true);

        System.out.println("Object spawned: " + playerTank);
    }

    private void processTankOnDestroyEvent(TankOnDestroyEventType onDestroyEventType) {
        if (TankOnDestroyEventType.SPAWN_BONUS_0.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.STAR_ID);

        } else if (TankOnDestroyEventType.SPAWN_BONUS_1.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.SHIELD_ID);

        } else if (TankOnDestroyEventType.SPAWN_BONUS_2.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.TANK_ID);

        } else if (TankOnDestroyEventType.SPAWN_BONUS_3.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.GRENADE_ID);

        } else if (TankOnDestroyEventType.SPAWN_BONUS_4.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.GRENADE_ID);

        } else {
            throw new RuntimeException("Event processing not implemented for " + onDestroyEventType);
        }
    }
}
