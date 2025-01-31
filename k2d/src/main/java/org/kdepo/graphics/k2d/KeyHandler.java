package org.kdepo.graphics.k2d;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    private boolean isEscapePressed;

    private boolean isEnterPressed;

    private boolean isSpacePressed;

    private boolean isUpPressed;

    private boolean isRightPressed;

    private boolean isDownPressed;

    private boolean isLeftPressed;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            isEscapePressed = true;
        }
        if (code == KeyEvent.VK_ENTER) {
            isEnterPressed = true;
        }
        if (code == KeyEvent.VK_SPACE) {
            isSpacePressed = true;
        }
        if (code == KeyEvent.VK_UP) {
            isUpPressed = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            isRightPressed = true;
        }
        if (code == KeyEvent.VK_DOWN) {
            isDownPressed = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            isLeftPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_ESCAPE) {
            isEscapePressed = false;
        }
        if (code == KeyEvent.VK_ENTER) {
            isEnterPressed = false;
        }
        if (code == KeyEvent.VK_SPACE) {
            isSpacePressed = false;
        }
        if (code == KeyEvent.VK_UP) {
            isUpPressed = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            isRightPressed = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            isDownPressed = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            isLeftPressed = false;
        }
    }

    public boolean isEscapePressed() {
        return isEscapePressed;
    }

    public boolean isEnterPressed() {
        return isEnterPressed;
    }

    public boolean isSpacePressed() {
        return isSpacePressed;
    }

    public boolean isUpPressed() {
        return isUpPressed;
    }

    public boolean isRightPressed() {
        return isRightPressed;
    }

    public boolean isDownPressed() {
        return isDownPressed;
    }

    public boolean isLeftPressed() {
        return isLeftPressed;
    }
}