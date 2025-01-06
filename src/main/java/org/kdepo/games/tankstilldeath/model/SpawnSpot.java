package org.kdepo.games.tankstilldeath.model;

import org.kdepo.graphics.k2d.geometry.Rectangle;

public class SpawnSpot extends Rectangle {

    private boolean isActive;

    private long timer;

    private int team;

    private final MoveDirection moveDirection;

    public SpawnSpot(int x, int y, int team, MoveDirection moveDirection) {
        this.x = x;
        this.y = y;
        this.team = team;
        this.moveDirection = moveDirection;

        isActive = false;
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

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void restartTimer() {
        timer = System.currentTimeMillis() + 10000;
    }

    public void update() {
        if (System.currentTimeMillis() > timer) {
            isActive = false;
        }
    }

    @Override
    public String toString() {
        return "SpawnSpot{" +
                "isActive=" + isActive +
                ", timer=" + timer +
                ", team=" + team +
                ", moveDirection=" + moveDirection +
                '}';
    }
}
