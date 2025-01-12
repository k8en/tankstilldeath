package org.kdepo.games.tankstilldeath.model;

import java.util.Objects;

public class SpawnSpot extends AbstractGameObject {

    private long timer;

    private int teamId;

    private final MoveDirection moveDirection;

    public SpawnSpot(int x, int y, int teamId, MoveDirection moveDirection) {
        isActive = false;
        this.x = x;
        this.y = y;
        this.teamId = teamId;
        this.moveDirection = moveDirection;
    }

    public int getTeamId() {
        return teamId;
    }

    public MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void restartTimer() {
        timer = System.currentTimeMillis() + 10000;
    }

    @Override
    public void update() {
        if (System.currentTimeMillis() > timer) {
            isActive = false;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SpawnSpot spawnSpot = (SpawnSpot) o;
        return timer == spawnSpot.timer
                && teamId == spawnSpot.teamId
                && moveDirection == spawnSpot.moveDirection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), timer, teamId, moveDirection);
    }

    @Override
    public String toString() {
        return "SpawnSpot{" +
                "timer=" + timer +
                ", teamId=" + teamId +
                ", moveDirection=" + moveDirection +
                '}';
    }
}
