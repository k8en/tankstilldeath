package org.kdepo.games.tankstilldeath.model;

public class VirtualKeyHandler {

    public boolean isSpacePressed;

    public boolean isUpPressed;

    public boolean isRightPressed;

    public boolean isDownPressed;

    public boolean isLeftPressed;

    public boolean isSpacePressed() {
        return isSpacePressed;
    }

    public void setSpacePressed(boolean spacePressed) {
        isSpacePressed = spacePressed;
    }

    public boolean isUpPressed() {
        return isUpPressed;
    }

    public void setUpPressed(boolean upPressed) {
        isUpPressed = upPressed;
    }

    public boolean isRightPressed() {
        return isRightPressed;
    }

    public void setRightPressed(boolean rightPressed) {
        isRightPressed = rightPressed;
    }

    public boolean isDownPressed() {
        return isDownPressed;
    }

    public void setDownPressed(boolean downPressed) {
        isDownPressed = downPressed;
    }

    public boolean isLeftPressed() {
        return isLeftPressed;
    }

    public void setLeftPressed(boolean leftPressed) {
        isLeftPressed = leftPressed;
    }
}
