package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.geometry.Rectangle;

public class SpawnSpot extends Rectangle {

    private boolean isActive;

    private long timer;

    private int team;

    public SpawnSpot(int x, int y, int team) {
        this.x = x;
        this.y = y;
        this.team = team;

        isActive = false;
        timer = System.currentTimeMillis() + 10000;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getTeam() {
        return team;
    }

    public void update() {
        if (System.currentTimeMillis() > timer) {
            isActive = false;
        }
    }
}
