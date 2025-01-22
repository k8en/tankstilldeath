package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.games.tankstilldeath.l18n.Localization;
import org.kdepo.games.tankstilldeath.model.MapData;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;

import java.awt.*;
import java.util.Map;

public class SummaryScreen extends AbstractScreen {

    private final ResourcesController resourcesController;
    private Map<String, Object> parameters;

    /**
     * Delay before switch to the next screen
     */
    private long readyTimer;

    private MapData mapData;

    private Font font39x45o;

    private int txtSummaryTextX;
    private int txtSummaryTextY;
    private int txtSummaryValueX;
    private int txtSummaryLineHeight;

    private int bonusesCollectedCounter;
    private int tanksDestroyedCounter;

    private int txtPressEnterToContinueX;
    private int txtPressEnterToContinueY;

    public SummaryScreen() {
        this.name = Constants.Screens.SUMMARY;
        resourcesController = ResourcesController.getInstance();
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        this.parameters = parameters;

        // Delay before switch to the next screen
        readyTimer = System.currentTimeMillis() + 1000;

        mapData = (MapData) parameters.get(Constants.ScreenParameters.MAP_DATA);

        font39x45o = resourcesController.getFont("font_n39x45o");

        // Calculate position for summary info output
        txtSummaryTextX = 30;
        txtSummaryTextY = 30;
        txtSummaryValueX = 550;
        txtSummaryLineHeight = 45;

        bonusesCollectedCounter = (int) parameters.get(Constants.ScreenParameters.BONUSES_COLLECTED_COUNTER);
        tanksDestroyedCounter = (int) parameters.get(Constants.ScreenParameters.TANKS_DESTROYED_COUNTER);

        // Calculate ready to continue text position
        int txtPressEnterToContinueWidth = font39x45o.getTextWidth(Localization.PRESS_ENTER_TO_CONTINUE);
        int txtPressEnterToContinueHeight = font39x45o.getTextHeight(Localization.PRESS_ENTER_TO_CONTINUE);
        txtPressEnterToContinueX = (Constants.SCREEN_WIDTH - txtPressEnterToContinueWidth) / 2;
        txtPressEnterToContinueY = Constants.SCREEN_HEIGHT - txtPressEnterToContinueHeight * 2;
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        if (keyHandler.isEnterPressed()) {
            if (System.currentTimeMillis() >= readyTimer) {
                boolean isGameOver = (boolean) parameters.get(Constants.ScreenParameters.GAME_OVER);

                String nextMap = mapData.getNextMap();
                parameters.remove(Constants.ScreenParameters.MAP_DATA);
                if (nextMap == null || nextMap.isEmpty() || isGameOver) {
                    screenController.setActiveScreen(Constants.Screens.TITLE, parameters);
                } else {
                    parameters.put(Constants.ScreenParameters.NEXT_MAP, mapData.getNextMap());
                    screenController.setActiveScreen(Constants.Screens.BRIEFING, parameters);
                }
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        font39x45o.render(g, Localization.BONUSES_COLLECTED, txtSummaryTextX, txtSummaryTextY);
        font39x45o.render(g, String.valueOf(bonusesCollectedCounter), txtSummaryValueX, txtSummaryTextY);

        font39x45o.render(g, Localization.TANKS_DESTROYED, txtSummaryTextX, txtSummaryTextY + txtSummaryLineHeight);
        font39x45o.render(g, String.valueOf(tanksDestroyedCounter), txtSummaryValueX, txtSummaryTextY + txtSummaryLineHeight);

        // Notify about ready to continue
        if (System.currentTimeMillis() >= readyTimer) {
            font39x45o.render(g, Localization.PRESS_ENTER_TO_CONTINUE, txtPressEnterToContinueX, txtPressEnterToContinueY);
        }
    }

    @Override
    public void dispose() {

    }
}
