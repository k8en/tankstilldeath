package org.kdepo.games.tankstilldeath;

import org.kdepo.games.tankstilldeath.screens.BattleScreen;
import org.kdepo.graphics.k2d.GameEngine;
import org.kdepo.graphics.k2d.GamePanel;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Launcher {

    public static void main(String[] args) {
        // Calculate root folder
        String rootFolder = null;
        try {
            rootFolder = new File(Launcher.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent();
            rootFolder = rootFolder + File.separator;
        } catch (URISyntaxException e) {
            System.out.println("Error in root folder calculation!");
            e.printStackTrace();
            return;
        }

        // Load resources definition
        ResourcesController resourcesController = ResourcesController.getInstance();
        resourcesController.setPath(rootFolder);
        resourcesController.loadDefinitions("resources.xml");

        GameEngine gameEngine = new GameEngine();
        gameEngine.setScreenWidth(Constants.SCREEN_WIDTH);   // 80 blocks
        gameEngine.setScreenHeight(Constants.SCREEN_HEIGHT); // 60 blocks

        BattleScreen battleScreen = new BattleScreen();

        gameEngine.addScreen(battleScreen);

        Map<String, Object> parameters = new HashMap<>();
        gameEngine.setActiveScreen(battleScreen.getName(), parameters);

        GamePanel gamePanel = new GamePanel(gameEngine.getScreenWidth(), gameEngine.getScreenHeight());
        gamePanel.setGameEngine(gameEngine);

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Game");
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }

}
