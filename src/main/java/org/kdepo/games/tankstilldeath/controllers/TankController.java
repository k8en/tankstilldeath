package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.model.MoveDirection;
import org.kdepo.games.tankstilldeath.model.Tank;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TankController {

    private static TankController instance;

    private final List<Tank> tankList;

    public static TankController getInstance() {
        if (instance == null) {
            instance = new TankController();
        }
        return instance;
    }

    private TankController() {
        tankList = new ArrayList<>();
    }

    public List<Tank> getTankList() {
        return tankList;
    }

    public void spawn(double x, double y, MoveDirection moveDirection) {
        tankList.add(new Tank(x, y, moveDirection, 12, 12, 48, 48));
    }

    public void update() {

    }

    public void render(Graphics2D g) {
        for (Tank tank : tankList) {
            tank.render(g);
        }
    }
}
