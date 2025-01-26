package org.kdepo.graphics.k2d;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {

    private GameEngine gameEngine;

    private final KeyHandler keyHandler;
    private final MouseHandler mouseHandler;

    private Thread gameThread;

    public GamePanel(int width, int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(Color.BLACK);

        this.setDoubleBuffered(true);

        keyHandler = new KeyHandler();
        this.addKeyListener(keyHandler);

        mouseHandler = new MouseHandler();
        this.addMouseListener(mouseHandler);

        this.setFocusable(true);
        this.requestFocus();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double frameTime = 1_000_000_000D / 60;
        double nextFrameTime = System.nanoTime() + frameTime;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextFrameTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;

                if (remainingTime > 0) {
                    Thread.sleep((long) remainingTime);
                }

                nextFrameTime = nextFrameTime + frameTime;

            } catch (InterruptedException ignored) {
            }
        }
    }

    public void update() {
        gameEngine.update(keyHandler, mouseHandler);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        gameEngine.render(g2d);

        g2d.dispose();
    }

    public void setGameEngine(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
    }

}
