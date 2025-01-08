package org.kdepo.games.tankstilldeath.model;

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
    public String toString() {
        return "SpawnSpot{" +
                "timer=" + timer +
                ", teamId=" + teamId +
                ", moveDirection=" + moveDirection +
                '}';
    }
}
