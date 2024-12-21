package org.kdepo.games.tankstilldeath;

import org.kdepo.graphics.k2d.GameEngine;
import org.kdepo.graphics.k2d.GamePanel;

import javax.swing.*;

public class Launcher {

    public static void main(String[] args) {
        GameEngine gameEngine = new GameEngine();
        gameEngine.setScreenWidth(800);
        gameEngine.setScreenHeight(600);

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
