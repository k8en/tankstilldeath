package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.model.Bonus;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.utils.CollisionsChecker;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BonusController {

    private static BonusController instance;

    private final List<Bonus> bonusList;

    public static BonusController getInstance() {
        if (instance == null) {
            instance = new BonusController();
        }
        return instance;
    }

    private BonusController() {
        bonusList = new ArrayList<>();
    }

    public void spawn(double x, double y, int id) {
        Bonus bonusToSpawn = null;
        for (Bonus bonus : bonusList) {
            if (!bonus.isActive()) {
                bonusToSpawn = bonus;
                break;
            }
        }

        if (bonusToSpawn == null) {
            bonusToSpawn = new Bonus(x, y, id);
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

    public Bonus getBonusAtCollision(Rectangle rect) {
        Bonus bonusAtCollision = null;
        for (Bonus bonus : bonusList) {
            if (bonus.isActive()) {
                if (CollisionsChecker.hasCollision(bonus.getHitBox(), rect)) {
                    bonusAtCollision = bonus;
                    break;
                }
            }
        }

        return bonusAtCollision;
    }

    public void update() {
        for (Bonus bonus : bonusList) {
            if (bonus.isActive()) {
                bonus.update();
            }
        }
    }

    public void render(Graphics2D g) {
        for (Bonus bonus : bonusList) {
            if (bonus.isActive()) {
                bonus.render(g);
            }
        }
    }
}
