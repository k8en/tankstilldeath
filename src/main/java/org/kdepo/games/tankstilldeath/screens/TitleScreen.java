package org.kdepo.games.tankstilldeath.screens;

import org.kdepo.games.tankstilldeath.Constants;
import org.kdepo.games.tankstilldeath.model.MapData;
import org.kdepo.games.tankstilldeath.utils.MapDataUtils;
import org.kdepo.graphics.k2d.KeyHandler;
import org.kdepo.graphics.k2d.MouseHandler;
import org.kdepo.graphics.k2d.fonts.Font;
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

    private final TileController tileController;

    private MapData mapData;

    private Label[] menuItems;
    private int selectedMenuItemNumber;

    private Font font13x15o;

    public TitleScreen() {
        this.name = Constants.Screens.TITLE;
        resourcesController = ResourcesController.getInstance();
        tileController = TileController.getInstance();

        Resource tileConfigurationResource = resourcesController.getResource("tile_configuration");
        tileController.loadTilesConfigurations(resourcesController.getPath() + tileConfigurationResource.getPath());

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

        tileController.loadLayerData(mapData.getPathToFolder() + File.separator);


        font13x15o = resourcesController.getFont("font_n13x15o");
    }

    @Override
    public void update(KeyHandler keyHandler, MouseHandler mouseHandler) {
        if (keyHandler.isUpPressed && !keyHandler.isDownPressed) {
            selectedMenuItemNumber--;
            if (selectedMenuItemNumber < 0) {
                selectedMenuItemNumber = 0;
            }

        } else if (!keyHandler.isUpPressed && keyHandler.isDownPressed) {
            selectedMenuItemNumber++;
            if (selectedMenuItemNumber > menuItems.length - 1) {
                selectedMenuItemNumber = menuItems.length - 1;
            }
        } else if (keyHandler.isEnterPressed()) {
            if (selectedMenuItemNumber == 0) {
                screenController.setActiveScreen(Constants.Screens.BRIEFING, parameters);

            } else if (selectedMenuItemNumber == 1) {
                System.exit(0);
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

        // Console output on top of the all drawings
        font13x15o.render(g, "title screen", 10, 10);
    }

    @Override
    public void dispose() {
        font13x15o = null;
    }
}
