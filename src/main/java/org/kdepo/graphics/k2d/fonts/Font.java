package org.kdepo.graphics.k2d.fonts;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class Font {

    /**
     * Font name
     * Can be used to search font by ResourcesController. So should be unique
     */
    private final String name;

    /**
     * Image with all symbols drawings
     */
    private final BufferedImage symbolsImage;

    /**
     * Width of cell with symbol on image with characters
     */
    private final int cellWidth;

    /**
     * Height of cell with symbol on image with characters
     */
    private final int cellHeight;

    private final HashMap<Integer, SymbolConfiguration> symbolsConfigurationsMap;

    private final HashMap<Integer, BufferedImage> symbolsImagesMap;

    /**
     * Space between two characters
     */
    private int space;

    private String textToRender;
    private BufferedImage renderedText;

    public Font(String name, int cellWidth, int cellHeight, HashMap<Integer, SymbolConfiguration> symbolsConfigurationsMap, BufferedImage symbolsImage) {
        this.name = name;
        this.cellWidth = cellWidth;
        this.cellHeight = cellHeight;
        this.symbolsImage = symbolsImage;
        this.symbolsConfigurationsMap = symbolsConfigurationsMap;

        this.space = 1; // default

        textToRender = "";
        renderedText = null;

        this.symbolsImagesMap = new HashMap<>();

        for (Map.Entry<Integer, SymbolConfiguration> entry : symbolsConfigurationsMap.entrySet()) {
            symbolsImagesMap.put(
                    entry.getKey(),
                    symbolsImage.getSubimage(
                            entry.getValue().getX() * this.cellWidth,
                            entry.getValue().getY() * this.cellHeight,
                            entry.getValue().getWidth(),
                            entry.getValue().getHeight()
                    )
            );
        }
    }

    public String getName() {
        return this.name;
    }

    /**
     * Set space between symbols
     *
     * @param space space in pixels
     */
    public void setSpace(int space) {
        this.space = space;
    }

    public void setText(String textToRender) {
        if (this.textToRender.equals(textToRender)) {
            return;
        }

        if (textToRender == null || textToRender.isEmpty()) {
            return;
        }

        this.textToRender = textToRender;

        // Convert text to image
        int imageWidth = this.textToRender.length() * (this.cellWidth + this.space);
        int imageHeight = this.cellHeight;
        this.renderedText = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics g = renderedText.getGraphics();
        int currentPos = 0;
        for (int i = 0; i < this.textToRender.length(); i++) {

            int code = this.textToRender.charAt(i);
            SymbolConfiguration symbolConfiguration = getSymbolConfiguration(code);

            g.drawImage(
                    symbolsImagesMap.get(symbolConfiguration.getCode()),
                    currentPos, 0,
                    null
            );

            currentPos = currentPos + symbolConfiguration.getWidth() + space;
        }
    }

    public String getTextToRender() {
        return this.textToRender;
    }

    /**
     * Draw string with current font settings
     *
     * @param g    graphic context
     * @param text text to draw
     */
    public void render(Graphics2D g, String text, int x, int y) {
        if (text != null && !text.isEmpty()) {
            int currentPos = x;
            for (int i = 0; i < text.length(); i++) {

                int code = text.charAt(i);
                SymbolConfiguration symbolConfiguration = getSymbolConfiguration(code);

                g.drawImage(
                        symbolsImagesMap.get(symbolConfiguration.getCode()),
                        currentPos, y,
                        null
                );

                currentPos = currentPos + symbolConfiguration.getWidth() + space;
            }
        }
    }

    public void render(Graphics2D g, String text, int x, int y, TextAlignment textAlignment) {
        int offsetX = 0;

        if (TextAlignment.RIGHT == textAlignment) {
            offsetX = -getTextWidth(text);
        }

        render(g, text, x + offsetX, y);
    }

    public void render(Graphics2D g, int x, int y) {
        if (textToRender == null || textToRender.isEmpty()) {
            return;
        }
        g.drawImage(renderedText, x, y, null);
    }

    /**
     * Calculate text width in pixels
     *
     * @param text text for calculation
     * @return text width in pixels
     */
    public int getTextWidth(String text) {
        int width = 0;
        if (text != null && !text.isEmpty()) {
            for (int i = 0; i < text.length(); i++) {

                int code = (int) text.charAt(i);
                SymbolConfiguration symbolConfiguration = getSymbolConfiguration(code);

                width = width + symbolConfiguration.getWidth() + space;
            }
            width = width - space;
        }
        return width;
    }

    public int getTextHeight(String text) {
        return this.cellHeight;
    }

    /**
     * Return symbol configuration by symbol code. If symbol not found then stub symbol will be return
     */
    private SymbolConfiguration getSymbolConfiguration(int code) {
        SymbolConfiguration config = symbolsConfigurationsMap.get(code);
        if (config == null) {
            config = symbolsConfigurationsMap.get(0);
        }

        return config;
    }

}
