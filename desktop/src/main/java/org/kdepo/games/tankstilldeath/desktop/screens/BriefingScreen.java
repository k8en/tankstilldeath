package org.kdepo.games.tankstilldeath.desktop.screens;

import org.kdepo.games.tankstilldeath.desktop.Constants;
import org.kdepo.games.tankstilldeath.desktop.l18n.Localization;
import org.kdepo.games.tankstilldeath.desktop.model.MapData;
import org.kdepo.games.tankstilldeath.desktop.utils.MapDataUtils;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.resources.Resource;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;

import java.awt.*;
import java.util.Map;

public class BriefingScreen extends AbstractScreen {

    private final ResourcesController resourcesController;
    private Map<String, Object> parameters;

    /**
     * Delay before switch to the next screen
     */
    private long readyTimer;

    /**
     * Map data for play on battle screen
     */
    private MapData mapData;

    private Font font39x45o;

    private int txtMapNameX;
    private int txtMapNameY;

    private int txtPressEnterToContinueX;
    private int txtPressEnterToContinueY;

    public BriefingScreen() {
        this.name = Constants.Screens.BRIEFING;
        resourcesController = ResourcesController.getInstance();
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        this.parameters = parameters;

        // Check for the next map name
        String nextMap = (String) this.parameters.get(Constants.ScreenParameters.NEXT_MAP);
        if (nextMap == null || nextMap.isEmpty()) {
            throw new RuntimeException("Mandatory parameter not found '" + Constants.ScreenParameters.NEXT_MAP + "'");
        }

        // Load next map data
        Resource mapResource = resourcesController.getResource(nextMap);
        if (mapResource == null) {
            throw new RuntimeException("Map resource not found for resource id '" + nextMap + "'");
        }
        mapData = MapDataUtils.loadMapData(resourcesController.getPath() + mapResource.getPath());

        // Propagate map data to battle screen for play
        parameters.put(Constants.ScreenParameters.MAP_DATA, mapData);

        readyTimer = System.currentTimeMillis() + 1000;

        font39x45o = resourcesController.getFont("font_n39x45o");

        // Calculate map name screen position
        int txtMapNameWidth = font39x45o.getTextWidth(mapData.getMapName());
        int txtMapNameHeight = font39x45o.getTextHeight(mapData.getMapName());
        txtMapNameX = (Constants.SCREEN_WIDTH - txtMapNameWidth) / 2;
        txtMapNameY = (Constants.SCREEN_HEIGHT - txtMapNameHeight) / 2;

        // Calculate ready to play text position
        int txtPressEnterToContinueWidth = font39x45o.getTextWidth(Localization.PRESS_ENTER_TO_CONTINUE);
        int txtPressEnterToContinueHeight = font39x45o.getTextHeight(Localization.PRESS_ENTER_TO_CONTINUE);
        txtPressEnterToContinueX = (Constants.SCREEN_WIDTH - txtPressEnterToContinueWidth) / 2;
        txtPressEnterToContinueY = Constants.SCREEN_HEIGHT - txtPressEnterToContinueHeight * 2;
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        if (keyHandler.isEnterPressed()) {
            if (System.currentTimeMillis() >= readyTimer) {
                parameters.remove(Constants.ScreenParameters.GAME_OVER);
                screenController.setActiveScreen(Constants.Screens.BATTLE, parameters);
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        // Render map name at the center
        font39x45o.render(g, mapData.getMapName(), txtMapNameX, txtMapNameY);

        // Notify about ready to continue
        if (System.currentTimeMillis() >= readyTimer) {
            font39x45o.render(g, Localization.PRESS_ENTER_TO_CONTINUE, txtPressEnterToContinueX, txtPressEnterToContinueY);
        }
    }

    @Override
    public void dispose() {
        mapData = null;
    }
}
