package org.kdepo.games.tankstilldeath.desktop.screens;

import org.kdepo.games.tankstilldeath.desktop.Constants;
import org.kdepo.games.tankstilldeath.desktop.controllers.TankController;
import org.kdepo.games.tankstilldeath.desktop.model.MapData;
import org.kdepo.games.tankstilldeath.desktop.utils.MapDataUtils;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.gui.Label;
import org.kdepo.graphics.k2d.resources.Resource;
import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.screens.AbstractScreen;
import org.kdepo.graphics.k2d.tiles.TileController;

import java.awt.*;
import java.io.File;
import java.util.Map;

public class TitleScreen extends AbstractScreen {

    private final ResourcesController resourcesController;
    private Map<String, Object> parameters;

    /**
     * Delay before switch to the next screen
     */
    private long readyTimer;

    private final TankController tankController;
    private final TileController tileController;

    private MapData mapData;

    private Label[] menuItems;
    private int selectedMenuItemNumber;

    public TitleScreen() {
        this.name = Constants.Screens.TITLE;
        resourcesController = ResourcesController.getInstance();
        tankController = TankController.getInstance();
        tileController = TileController.getInstance();

        Resource tileConfigurationResource = resourcesController.getResource("tile_configuration");
        tileController.loadTilesConfigurations(resourcesController.getPath() + tileConfigurationResource.getPath());

        Resource tankConfigurationResource = resourcesController.getResource("tank_configuration");
        tankController.loadTanksConfigurations(resourcesController.getPath() + tankConfigurationResource.getPath());

        int cx = Constants.SCREEN_WIDTH / 2;
        int cy = Constants.SCREEN_HEIGHT * 2 / 3;
        int dy = 60;

        menuItems = new Label[2];
        selectedMenuItemNumber = 0;

        Label startGame = new Label();
        startGame.setFocus(true);
        startGame.setImage(resourcesController.getImage("image_text_start_game"));
        startGame.setImageHighlighted(resourcesController.getImage("image_text_start_game_highlighted"));
        startGame.setCenter(cx, cy + 0 * dy);
        menuItems[0] = startGame;

        Label exit = new Label();
        exit.setFocus(false);
        exit.setImage(resourcesController.getImage("image_text_exit"));
        exit.setImageHighlighted(resourcesController.getImage("image_text_exit_highlighted"));
        exit.setCenter(cx, cy + 1 * dy);
        menuItems[1] = exit;
    }

    @Override
    public void initialize(Map<String, Object> parameters) {
        this.parameters = parameters;

        Resource mapResource = resourcesController.getResource("map_00");
        mapData = MapDataUtils.loadMapData(resourcesController.getPath() + mapResource.getPath());

        tileController.loadLayersData(mapData.getPathToFolder() + File.separator);

        // Set next map name to load on a brief screen
        String nextMap = mapData.getNextMap();
        if (nextMap == null) {
            parameters.remove(Constants.ScreenParameters.NEXT_MAP);
        } else {
            parameters.put(Constants.ScreenParameters.NEXT_MAP, nextMap);
        }

        readyTimer = System.currentTimeMillis() + 1000;
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        if (keyHandler.isUpPressed() && !keyHandler.isDownPressed()) {
            selectedMenuItemNumber--;
            if (selectedMenuItemNumber < 0) {
                selectedMenuItemNumber = 0;
            }

        } else if (!keyHandler.isUpPressed() && keyHandler.isDownPressed()) {
            selectedMenuItemNumber++;
            if (selectedMenuItemNumber > menuItems.length - 1) {
                selectedMenuItemNumber = menuItems.length - 1;
            }
        } else if (keyHandler.isEnterPressed()) {
            if (System.currentTimeMillis() >= readyTimer) {
                if (selectedMenuItemNumber == 0) {
                    parameters.put(Constants.ScreenParameters.PLAYER_TANKS_COUNTER, Constants.PLAYER_TANKS_COUNTER);
                    screenController.setActiveScreen(Constants.Screens.BRIEFING, parameters);
                } else if (selectedMenuItemNumber == 1) {
                    System.exit(0);
                }
            }
        }

        if (!menuItems[selectedMenuItemNumber].hasFocus()) {
            for (Label label : menuItems) {
                label.setFocus(false);
            }
            menuItems[selectedMenuItemNumber].setFocus(true);
        }
    }

    @Override
    public void render(Graphics2D g) {
        tileController.render(g, TileController.LAYER_BOTTOM);
        tileController.render(g, TileController.LAYER_MIDDLE);
        tileController.render(g, TileController.LAYER_TOP);

        for (Label label : menuItems) {
            label.render(g);
        }
    }

    @Override
    public void dispose() {

    }
}
