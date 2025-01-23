package org.kdepo.games.tankstilldeath;

import org.kdepo.games.tankstilldeath.screens.BattleScreen;
import org.kdepo.games.tankstilldeath.screens.BriefingScreen;
import org.kdepo.games.tankstilldeath.screens.SummaryScreen;
import org.kdepo.games.tankstilldeath.screens.TitleScreen;
import org.kdepo.graphics.k2d.GameEngine;
import org.kdepo.graphics.k2d.GamePanel;
import org.kdepo.graphics.k2d.resources.ResourcesController;

import javax.swing.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

// "mvn clean compile assembly:single" to get executable jar
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

        TitleScreen titleScreen = new TitleScreen();
        BriefingScreen briefingScreen = new BriefingScreen();
        BattleScreen battleScreen = new BattleScreen();
        SummaryScreen summaryScreen = new SummaryScreen();

        gameEngine.addScreen(titleScreen);
        gameEngine.addScreen(briefingScreen);
        gameEngine.addScreen(battleScreen);
        gameEngine.addScreen(summaryScreen);

        // Parameters map to share between screens
        Map<String, Object> parameters = new HashMap<>();
        gameEngine.setActiveScreen(titleScreen.getName(), parameters);

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
