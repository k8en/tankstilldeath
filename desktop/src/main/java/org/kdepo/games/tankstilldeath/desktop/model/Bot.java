package org.kdepo.games.tankstilldeath.desktop.model;

import org.kdepo.graphics.k2d.geometry.Rectangle;

public class Bot {

    private long timeToChangeMoveDirection;

    public Bot() {
        timeToChangeMoveDirection = System.currentTimeMillis() + 3000;
    }

    public void pressAnyKeys(VirtualKeyHandler keyHandler, Rectangle bot, Rectangle player, Rectangle base) {
        int dx = (int) (bot.getX() - player.getX());
        int dy = (int) (bot.getY() - player.getY());

        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > timeToChangeMoveDirection) {
            timeToChangeMoveDirection = currentTimeMillis + 3000;
            if (Math.abs(dx) > Math.abs(dy)) {
                if (dx > 0) {
                    keyHandler.setUpPressed(false);
                    keyHandler.setRightPressed(false);
                    keyHandler.setDownPressed(false);
                    keyHandler.setLeftPressed(true);
                } else {
                    keyHandler.setUpPressed(false);
                    keyHandler.setRightPressed(true);
                    keyHandler.setDownPressed(false);
                    keyHandler.setLeftPressed(false);
                }
            } else {
                if (dy > 0) {
                    keyHandler.setUpPressed(true);
                    keyHandler.setRightPressed(false);
                    keyHandler.setDownPressed(false);
                    keyHandler.setLeftPressed(false);
                } else {
                    keyHandler.setUpPressed(false);
                    keyHandler.setRightPressed(false);
                    keyHandler.setDownPressed(true);
                    keyHandler.setLeftPressed(false);
                }
            }
        }

        keyHandler.setSpacePressed(true);
    }
}
