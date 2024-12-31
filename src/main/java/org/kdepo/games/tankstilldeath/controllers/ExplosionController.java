package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.model.Explosion;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ExplosionController {

    private static ExplosionController instance;

    private final List<Explosion> explosionList;

    public static ExplosionController getInstance() {
        if (instance == null) {
            instance = new ExplosionController();
        }
        return instance;
    }

    private ExplosionController() {
        explosionList = new ArrayList<>();
    }

    public void spawn(double centerX, double centerY, String animationMapName) {
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

    public void update() {
        for (Explosion explosion : explosionList) {
            if (explosion.isActive()) {
                explosion.update();
            }
        }
    }

    public void render(Graphics2D g) {
        for (Explosion explosion : explosionList) {
            if (explosion.isActive()) {
                explosion.render(g);
            }
        }
    }
}
