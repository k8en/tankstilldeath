package org.kdepo.games.tankstilldeath.desktop.screens;

import org.kdepo.games.tankstilldeath.desktop.Constants;
import org.kdepo.games.tankstilldeath.desktop.controllers.SpawnSpotController;
import org.kdepo.games.tankstilldeath.desktop.controllers.TankController;
import org.kdepo.games.tankstilldeath.desktop.gui.DefeatPopup;
import org.kdepo.games.tankstilldeath.desktop.gui.PausePopup;
import org.kdepo.games.tankstilldeath.desktop.gui.VictoryPopup;
import org.kdepo.games.tankstilldeath.desktop.model.*;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.geometry.Point;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;
import org.kdepo.graphics.k2d.tiles.Tile;
import org.kdepo.graphics.k2d.tiles.TileController;
import org.kdepo.graphics.k2d.utils.CollisionsChecker;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;

public class BattleScreen extends AbstractScreen {

    private final ResourcesController resourcesController;
    private Map<String, Object> parameters;

    private final Random randomizer;

    private int state;

    private DefeatPopup defeatPopup;
    private PausePopup pausePopup;
    private VictoryPopup victoryPopup;

    private final SpawnSpotController spawnSpotController;
    private final TankController tankController;
    private final TileController tileController;

    private int playerTanksCounter;
    private Tank playerTank;

    private int bonusesCollectedCounter;
    private int tanksDestroyedCounter;

    private int activeTanksLimit;
    private final List<Tank> activeTanksList;
    private final List<Tank> tanksToSpawnList;

    private final List<Bullet> bulletList;
    private final List<Bonus> bonusList;
    private final List<Explosion> explosionList;

    private Base base;

    public BattleScreen() {
        this.name = Constants.Screens.BATTLE;

        randomizer = new Random(System.currentTimeMillis());

        resourcesController = ResourcesController.getInstance();
        spawnSpotController = SpawnSpotController.getInstance();
        tankController = TankController.getInstance();
        tileController = TileController.getInstance();

        activeTanksList = new ArrayList<>();
        bonusList = new ArrayList<>();
        bulletList = new ArrayList<>();
        explosionList = new ArrayList<>();
        tanksToSpawnList = new ArrayList<>();
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        state = Constants.BattleScreenStates.PLAY;
        this.parameters = parameters;

        defeatPopup = new DefeatPopup(400, 300);
        defeatPopup.setCenter((double) Constants.SCREEN_WIDTH / 2, (double) Constants.SCREEN_HEIGHT / 2);
        defeatPopup.setVisible(false);

        pausePopup = new PausePopup(400, 300);
        pausePopup.setCenter((double) Constants.SCREEN_WIDTH / 2, (double) Constants.SCREEN_HEIGHT / 2);
        pausePopup.setVisible(false);

        victoryPopup = new VictoryPopup(400, 300);
        victoryPopup.setCenter((double) Constants.SCREEN_WIDTH / 2, (double) Constants.SCREEN_HEIGHT / 2);
        victoryPopup.setVisible(false);

        activeTanksList.clear();
        bonusList.clear();
        bulletList.clear();
        explosionList.clear();
        tanksToSpawnList.clear();

        MapData mapData = (MapData) parameters.get(Constants.ScreenParameters.MAP_DATA);
        spawnSpotController.loadSpawnSpotData(mapData.getPathToFolder() + File.separator + mapData.getFileNameSpawnSpots());

        activeTanksLimit = mapData.getActiveTanksLimit();
        bonusesCollectedCounter = 0;
        playerTanksCounter = (int) parameters.get(Constants.ScreenParameters.PLAYER_TANKS_COUNTER);
        tanksDestroyedCounter = 0;

        tanksToSpawnList.clear();
        List<Tank> tanksToSpawnList = tankController.getTanksToSpawnList(mapData.getPathToFolder() + File.separator + mapData.getFileNameTanksToSpawn());
        this.tanksToSpawnList.addAll(tanksToSpawnList);

        tileController.loadLayersData(mapData.getPathToFolder() + File.separator);

        base = new Base();
        base.setActive(true);

        SpawnSpot playerSpawnSpot = spawnSpotController.getAvailableSpawnSpot(Constants.Teams.PLAYER_ID);
        if (playerSpawnSpot != null) {
            spawnPlayerTank(playerSpawnSpot);
        } else {
            throw new RuntimeException("Player spawn spot is not found for map: " + resourcesController.getPath() + mapData.getFileNameSpawnSpots());
        }
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        if (Constants.BattleScreenStates.PLAY == state) {
            if (keyHandler.isEscapePressed() && pausePopup.isReady()) {
                state = Constants.BattleScreenStates.PAUSE;
                System.out.println("Change to PAUSE state");
                pausePopup.show();
            }

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
                                if (Constants.Bullets.STANDARD_ID == bullet.getBulletTypeId()) {
                                    tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), Constants.Tiles.BRICKS_AT_THE_NORTH_BLOCK_ID);
                                } else {
                                    tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                                }

                            } else if (MoveDirection.EAST.equals(bullet.getMoveDirection())) {
                                if (Constants.Bullets.STANDARD_ID == bullet.getBulletTypeId()) {
                                    tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), Constants.Tiles.BRICKS_AT_THE_EAST_BLOCK_ID);
                                } else {
                                    tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                                }

                            } else if (MoveDirection.SOUTH.equals(bullet.getMoveDirection())) {
                                if (Constants.Bullets.STANDARD_ID == bullet.getBulletTypeId()) {
                                    tileController.setTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY(), Constants.Tiles.BRICKS_AT_THE_SOUTH_BLOCK_ID);
                                } else {
                                    tileController.removeTile(TileController.LAYER_MIDDLE, tile.getTileX(), tile.getTileY());
                                }

                            } else if (MoveDirection.WEST.equals(bullet.getMoveDirection())) {
                                if (Constants.Bullets.STANDARD_ID == bullet.getBulletTypeId()) {
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
                            if (Constants.Bullets.ARMOUR_PIERCING_ID == bullet.getBulletTypeId()) {
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

                            state = Constants.BattleScreenStates.LOSE;
                            System.out.println("Change to LOSE state (no base)");

                            defeatPopup.show();
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

                                    if (bullet.getTeamId() == Constants.Teams.PLAYER_ID) {
                                        tanksDestroyedCounter++;
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
                        base.getHitBox(),
                        tank,
                        activeTanksList,
                        bulletList,
                        tileController.getLayerData(TileController.LAYER_BOTTOM),
                        tileController.getLayerData(TileController.LAYER_MIDDLE),
                        tileController.getLayerData(TileController.LAYER_TOP)
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
                                Tank additionalTank = tankController.prepareTank(tank.getTankTypeId(), tank.getTeamId(), 0, 0, MoveDirection.NORTH);
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

            // Check for player tank has collision with bonus
            if (playerTank.isActive()) {
                for (Bonus bonus : bonusList) {
                    if (bonus.isActive()) {
                        if (CollisionsChecker.hasCollision(playerTank.getHitBox(), bonus.getHitBox())) {
                            bonus.setActive(false);
                            bonusesCollectedCounter++;

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
                    state = Constants.BattleScreenStates.WIN;
                    System.out.println("Change to WIN state");
                    victoryPopup.show();
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
                    state = Constants.BattleScreenStates.LOSE;
                    System.out.println("Change to LOSE state (no tanks)");
                    defeatPopup.show();
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

        } else if (Constants.BattleScreenStates.PAUSE == state) {
            pausePopup.update(keyHandler, mouseHandler);
            if (pausePopup.isReady()) {
                if (Constants.UserActions.CANCEL == pausePopup.getUserAction()) {
                    state = Constants.BattleScreenStates.PLAY;
                    System.out.println("Change to PLAY state");
                    pausePopup.hide();
                } else if (Constants.UserActions.CONFIRM == pausePopup.getUserAction()) {
                    pausePopup.hide();
                    screenController.setActiveScreen(Constants.Screens.TITLE, parameters);
                }
            }

        } else if (Constants.BattleScreenStates.WIN == state) {
            victoryPopup.update(keyHandler, mouseHandler);
            if (Constants.UserActions.CONFIRM == victoryPopup.getUserAction()) {
                victoryPopup.hide();
                parameters.put(Constants.ScreenParameters.GAME_OVER, false);
                parameters.put(Constants.ScreenParameters.PLAYER_TANKS_COUNTER, playerTanksCounter);
                parameters.put(Constants.ScreenParameters.BONUSES_COLLECTED_COUNTER, bonusesCollectedCounter);
                parameters.put(Constants.ScreenParameters.TANKS_DESTROYED_COUNTER, tanksDestroyedCounter);
                screenController.setActiveScreen(Constants.Screens.SUMMARY, parameters);
            }

            // Update explosions state
            for (Explosion explosion : explosionList) {
                if (explosion.isActive()) {
                    explosion.update();
                }
            }

        } else if (Constants.BattleScreenStates.LOSE == state) {
            defeatPopup.update(keyHandler, mouseHandler);
            if (Constants.UserActions.CONFIRM == defeatPopup.getUserAction()) {
                defeatPopup.hide();
                parameters.put(Constants.ScreenParameters.GAME_OVER, true);
                parameters.put(Constants.ScreenParameters.PLAYER_TANKS_COUNTER, playerTanksCounter);
                parameters.put(Constants.ScreenParameters.BONUSES_COLLECTED_COUNTER, bonusesCollectedCounter);
                parameters.put(Constants.ScreenParameters.TANKS_DESTROYED_COUNTER, tanksDestroyedCounter);
                screenController.setActiveScreen(Constants.Screens.SUMMARY, parameters);
            }

            // Update explosions state
            for (Explosion explosion : explosionList) {
                if (explosion.isActive()) {
                    explosion.update();
                }
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

        // GUI elements to render
        if (Constants.BattleScreenStates.PAUSE == state) {
            pausePopup.render(g);
        } else if (Constants.BattleScreenStates.WIN == state) {
            victoryPopup.render(g);
        } else if (Constants.BattleScreenStates.LOSE == state) {
            defeatPopup.render(g);
        }
    }

    @Override
    public void dispose() {
        base = null;
        playerTank = null;
    }

    private void updateTankWeaponry(Tank tank) {
        if (tank.isReadyToShot()) {
            Point bulletOffset = tank.getBulletOffset();
            spawnBullet(
                    tank.getX() + bulletOffset.getX(),
                    tank.getY() + bulletOffset.getY(),
                    tank.getMoveDirection(),
                    tank.getTeamId(),
                    tank.getBulletTypeId()
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

        if (tank.getArmourAmount() == 0) {
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
        if (bullet.getBulletTypeId() == Constants.Bullets.STANDARD_ID) {
            tank.changeArmourAmount(-1);

        } else if (bullet.getBulletTypeId() == Constants.Bullets.ARMOUR_PIERCING_ID) {
            tank.changeArmourAmount(-3);

        } else {
            throw new RuntimeException("Collision is not implemented for bullet id " + bullet.getBulletTypeId());
        }
    }

    public void spawnBonus(double x, double y, int bonusId) {
        Bonus bonusToSpawn = null;
        for (Bonus bonus : bonusList) {
            if (!bonus.isActive() && bonus.getBonusId() == bonusId) {
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

    private void spawnBullet(double x, double y, MoveDirection moveDirection, int teamId, int bulletTypeId) {
        Bullet bulletToSpawn = null;
        for (Bullet bullet : bulletList) {
            if (!bullet.isActive()) {
                bulletToSpawn = bullet;
                break;
            }
        }

        if (bulletToSpawn == null) {
            bulletToSpawn = new Bullet(x, y, moveDirection);
            bulletList.add(bulletToSpawn);
        } else {
            bulletToSpawn.setX(x);
            bulletToSpawn.setY(y);
            bulletToSpawn.setMoveDirection(moveDirection);
        }

        bulletToSpawn.setTeamId(teamId);
        bulletToSpawn.setBulletTypeId(bulletTypeId);
        bulletToSpawn.setActive(true);
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

    private void processTankOnDestroyEvent(OnTankDestroyEventType onDestroyEventType) {
        if (OnTankDestroyEventType.SPAWN_BONUS_0.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.STAR_ID);

        } else if (OnTankDestroyEventType.SPAWN_BONUS_1.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.SHIELD_ID);

        } else if (OnTankDestroyEventType.SPAWN_BONUS_2.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.TANK_ID);

        } else if (OnTankDestroyEventType.SPAWN_BONUS_3.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.GRENADE_ID);

        } else if (OnTankDestroyEventType.SPAWN_BONUS_4.equals(onDestroyEventType)) {
            int x = randomizer.nextInt(Constants.SCREEN_WIDTH);
            int y = randomizer.nextInt(Constants.SCREEN_HEIGHT);
            spawnBonus(x, y, Constants.Bonuses.SHOVEL_ID);

        } else {
            throw new RuntimeException("Event processing not implemented for " + onDestroyEventType);
        }
    }
}
