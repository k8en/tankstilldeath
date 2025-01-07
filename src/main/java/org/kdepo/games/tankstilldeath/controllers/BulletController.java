package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.games.tankstilldeath.model.Bullet;
import org.kdepo.games.tankstilldeath.model.MoveDirection;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BulletController {

    private static BulletController instance;

    private final List<Bullet> bulletList;

    public static BulletController getInstance() {
        if (instance == null) {
            instance = new BulletController();
        }
        return instance;
    }

    private BulletController() {
        bulletList = new ArrayList<>();
    }

    public List<Bullet> getBulletList() {
        return bulletList;
    }

    public void spawn(double x, double y, MoveDirection moveDirection, int teamId) {
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

    public void update() {
        for (Bullet bullet : bulletList) {
            if (bullet.isActive()) {
                if (bullet.getX() < 0 || bullet.getX() > Constants.SCREEN_WIDTH || bullet.getY() < 0 || bullet.getY() > Constants.SCREEN_HEIGHT) {
                    bullet.setActive(false);
                    continue;
                }
                bullet.update();
            }
        }
    }

    public void render(Graphics2D g) {
        for (Bullet bullet : bulletList) {
            if (bullet.isActive()) {
                bullet.render(g);
            }
        }
    }
}
