package org.kdepo.games.tankstilldeath.desktop.gui;

import org.kdepo.games.tankstilldeath.desktop.Constants;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.gui.AbstractPopup;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PausePopup extends AbstractPopup {

    private long readyTimer;

    private int popupTitleX;
    private int popupTitleY;
    private final BufferedImage popupTitleImage;

    private final SimpleButton btContinue;
    private final SimpleButton btComplete;

    public PausePopup(int width, int height) {
        super(width, height);
        readyTimer = System.currentTimeMillis();

        ResourcesController resourcesController = ResourcesController.getInstance();
        popupTitleX = 0;
        popupTitleY = 0;
        popupTitleImage = resourcesController.getImage("image_text_pause");

        btContinue = new SimpleButton();
        btContinue.setFocus(true);
        btContinue.setImage(resourcesController.getImage("image_text_continue"));
        btContinue.setImageHighlighted(resourcesController.getImage("image_text_continue_highlighted"));

        btComplete = new SimpleButton();
        btComplete.setImage(resourcesController.getImage("image_text_complete"));
        btComplete.setImageHighlighted(resourcesController.getImage("image_text_complete_highlighted"));
    }

    @Override
    public void setCenter(double cx, double cy) {
        super.setCenter(cx, cy);

        popupTitleX = (int) (x + (width - popupTitleImage.getWidth()) / 2);
        popupTitleY = (int) (y + 30);

        btContinue.setCenter(cx, cy + 50);
        btComplete.setCenter(cx, cy + 100);
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        userAction = Constants.UserActions.NO_ACTION;

        if (!isReady()) {
            return;
        }

        if (keyHandler.isEscapePressed()) {
            userAction = Constants.UserActions.CANCEL;

        } else if (keyHandler.isLeftPressed && !keyHandler.isRightPressed) {
            setFocusOnPreviousControls();
            readyTimer = System.currentTimeMillis() + 100;

        } else if (!keyHandler.isLeftPressed && keyHandler.isRightPressed) {
            setFocusOnNextControls();
            readyTimer = System.currentTimeMillis() + 100;

        } else if (keyHandler.isUpPressed && !keyHandler.isDownPressed) {
            setFocusOnNextControls();
            readyTimer = System.currentTimeMillis() + 100;

        } else if (!keyHandler.isUpPressed && keyHandler.isDownPressed) {
            setFocusOnPreviousControls();
            readyTimer = System.currentTimeMillis() + 100;

        } else if (keyHandler.isEnterPressed()) {
            if (btContinue.hasFocus()) {
                userAction = Constants.UserActions.CANCEL;
            } else if (btComplete.hasFocus()) {
                userAction = Constants.UserActions.CONFIRM;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        g.drawImage(popupTitleImage, popupTitleX, popupTitleY, null);

        btContinue.render(g);
        btComplete.render(g);
    }

    @Override
    public void setFocusOnPreviousControls() {
        if (btContinue.hasFocus()) {
            btContinue.setFocus(false);
            btComplete.setFocus(true);
        } else {
            btContinue.setFocus(true);
            btComplete.setFocus(false);
        }
    }

    @Override
    public void setFocusOnNextControls() {
        if (btContinue.hasFocus()) {
            btContinue.setFocus(false);
            btComplete.setFocus(true);
        } else {
            btContinue.setFocus(true);
            btComplete.setFocus(false);
        }
    }

    public void show() {
        this.isVisible = true;
        readyTimer = System.currentTimeMillis() + 200;
        userAction = Constants.UserActions.NO_ACTION;
    }

    public void hide() {
        this.isVisible = false;
        readyTimer = System.currentTimeMillis() + 200;
        userAction = Constants.UserActions.NO_ACTION;
    }

    public boolean isReady() {
        return System.currentTimeMillis() > readyTimer;
    }
}
