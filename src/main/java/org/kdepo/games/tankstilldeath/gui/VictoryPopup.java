package org.kdepo.games.tankstilldeath.gui;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.gui.AbstractPopup;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import java.awt.*;
import java.awt.image.BufferedImage;

public class VictoryPopup extends AbstractPopup {

    private int popupTitleX;
    private int popupTitleY;
    private final BufferedImage popupTitleImage;

    private final SimpleButton btContinue;

    public VictoryPopup(int width, int height) {
        super(width, height);

        ResourcesController resourcesController = ResourcesController.getInstance();
        popupTitleX = 0;
        popupTitleY = 0;
        popupTitleImage = resourcesController.getImage("image_text_victory");

        btContinue = new SimpleButton();
        btContinue.setFocus(true);
        btContinue.setImage(resourcesController.getImage("image_text_continue"));
        btContinue.setImageHighlighted(resourcesController.getImage("image_text_continue_highlighted"));
    }

    @Override
    public void setCenter(double cx, double cy) {
        super.setCenter(cx, cy);

        popupTitleX = (int) (x + (width - popupTitleImage.getWidth()) / 2);
        popupTitleY = (int) (y + 30);

        btContinue.setCenter(cx, cy + 50);
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        if (keyHandler.isEnterPressed()) {
            userAction = Constants.UserActions.CONFIRM;
        }
    }

    @Override
    public void render(Graphics2D g) {
        super.render(g);
        g.drawImage(popupTitleImage, popupTitleX, popupTitleY, null);

        btContinue.render(g);
    }

    @Override
    public void setFocusOnPreviousControls() {

    }

    @Override
    public void setFocusOnNextControls() {

    }

    public void show() {
        this.isVisible = true;
        userAction = Constants.UserActions.NO_ACTION;
    }

    public void hide() {
        this.isVisible = false;
        userAction = Constants.UserActions.NO_ACTION;
    }
}
