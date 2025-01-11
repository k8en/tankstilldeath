package org.kdepo.graphics.k2d.tiles;

import org.kdepo.graphics.k2d.geometry.Point;
import org.kdepo.graphics.k2d.geometry.Rectangle;
import org.kdepo.graphics.k2d.utils.CollisionsChecker;
import org.kdepo.graphics.k2d.utils.FilesUtils;
import org.kdepo.graphics.k2d.utils.TilesUtils;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileController {

    public static int LAYER_BOTTOM = 0;

    public static int LAYER_MIDDLE = 1;

    public static int LAYER_TOP = 2;

    private Tile[][] layerData0;
    private Tile[][] layerData1;
    private Tile[][] layerData2;

    private Map<Integer, TileConfiguration> tileConfigurationMap;

    private static TileController instance;

    public static TileController getInstance() {
        if (instance == null) {
            instance = new TileController();
        }
        return instance;
    }

    private TileController() {
        tileConfigurationMap = new HashMap<>();
    }

    public void loadTilesConfigurations(String pathToFile) {
        tileConfigurationMap = TilesUtils.loadConfigurations(pathToFile);
        System.out.println("Loaded tiles configurations: " + tileConfigurationMap.size());
    }

    public void loadLayerData(String pathToFile) {
        System.out.println("Loading layer data from " + pathToFile);

        layerData0 = getLayerData(pathToFile + File.separator + "0.layer");
        layerData1 = getLayerData(pathToFile + File.separator + "1.layer");
        layerData2 = getLayerData(pathToFile + File.separator + "2.layer");
    }

    private Tile[][] getLayerData(String pathToFile) {
        List<String> rawLines = FilesUtils.readLines(pathToFile);

        int y = rawLines.size();

        List<int[]> rowsList = new ArrayList<>();
        for (String line : rawLines) {
            String[] idsAsStr = line.split(",", -1);

            int[] ids = new int[idsAsStr.length];
            for (int i = 0; i < idsAsStr.length; i++) {
                ids[i] = Integer.parseInt(idsAsStr[i]);
            }

            rowsList.add(ids);
        }

        int[][] layerIdsData = new int[y][rowsList.get(0).length];
        for (int j = 0; j < y; j++) {
            System.arraycopy(rowsList.get(j), 0, layerIdsData[j], 0, rowsList.get(j).length);
        }

        Tile[][] layerData = new Tile[layerIdsData.length][layerIdsData[0].length];
        for (int j = 0; j < y; j++) {
            for (int i = 0; i < layerIdsData[j].length; i++) {
                TileConfiguration tileConfiguration = tileConfigurationMap.get(layerIdsData[j][i]);
                if (tileConfiguration != null) {
                    layerData[j][i] = new Tile(
                            tileConfiguration.getId(),
                            i,
                            j,
                            i * 16,
                            j * 16,
                            tileConfiguration.getImage(),
                            tileConfiguration.getHitBox());
                }
            }
        }

        return layerData;
    }

    public Tile getTile(int layer, double x, double y) {
        int tileX = (int) (x / 16);
        int tileY = (int) (y / 16);

        if (tileX < 0 || tileX >= 80 || tileY < 0 || tileY >= 60) {
            return null;
        }

        Tile tile = null;
        if (TileController.LAYER_BOTTOM == layer) {
            tile = layerData0[tileY][tileX];
        } else if (TileController.LAYER_MIDDLE == layer) {
            tile = layerData1[tileY][tileX];
        } else if (TileController.LAYER_TOP == layer) {
            tile = layerData2[tileY][tileX];
        }

        return tile;
    }

    public void setTile(int layer, int tileX, int tileY, int tileId) {
        TileConfiguration tileConfiguration = tileConfigurationMap.get(tileId);
        Tile tile = new Tile(
                tileId,
                tileX,
                tileY,
                tileX * 16,
                tileY * 16,
                tileConfiguration.getImage(),
                tileConfiguration.getHitBox()
        );

        if (TileController.LAYER_BOTTOM == layer) {
            layerData0[tileY][tileX] = tile;
        } else if (TileController.LAYER_MIDDLE == layer) {
            layerData1[tileY][tileX] = tile;
        } else if (TileController.LAYER_TOP == layer) {
            layerData2[tileY][tileX] = tile;
        }
    }

    public void removeTile(int layer, int tileX, int tileY) {
        if (TileController.LAYER_BOTTOM == layer) {
            layerData0[tileY][tileX] = null;
        } else if (TileController.LAYER_MIDDLE == layer) {
            layerData1[tileY][tileX] = null;
        } else if (TileController.LAYER_TOP == layer) {
            layerData2[tileY][tileX] = null;
        }
    }

    public boolean hasCollision(Point point) {
        int tileX = (int) (point.getX() / 16);
        int tileY = (int) (point.getY() / 16);

        if (tileX < 0 || tileX >= 80 || tileY < 0 || tileY >= 60) {
            return false;
        }

        Tile tile = layerData1[tileY][tileX];
        if (tile != null) {
            return CollisionsChecker.hasCollision(tile.getHitBox(), point.getX(), point.getY());
        }
        return false;
    }

    public boolean hasCollision(Rectangle rectangle) {
        int tileXTopLeft = (int) (rectangle.getX() / 16);
        if (tileXTopLeft < 0) {
            tileXTopLeft = 0;
        }

        int tileYTopLeft = (int) (rectangle.getY() / 16);
        if (tileYTopLeft < 0) {
            tileYTopLeft = 0;
        }

        int tileXBottomRight = (int) ((rectangle.getX() + rectangle.getWidth()) / 16);
        if (tileXBottomRight > 79) {
            tileXBottomRight = 79;
        }

        int tileYBottomRight = (int) ((rectangle.getY() + rectangle.getHeight()) / 16);
        if (tileYBottomRight > 59) {
            tileYBottomRight = 59;
        }

        for (int y = tileYTopLeft; y <= tileYBottomRight; y++) {
            for (int x = tileXTopLeft; x <= tileXBottomRight; x++) {
                Tile tile = layerData1[y][x];
                if (tile != null) {
                    if (CollisionsChecker.hasCollision(rectangle, tile.getHitBox())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void update() {

    }

    public void render(Graphics2D g, int layer) {
        if (LAYER_BOTTOM == layer) {
            for (Tile[] tiles : layerData0) {
                for (Tile tile : tiles) {
                    if (tile != null) {
                        g.drawImage(tile.getImage(), (int) tile.getX(), (int) tile.getY(), null);
                    }
                }
            }

        } else if (LAYER_MIDDLE == layer) {
            for (Tile[] tiles : layerData1) {
                for (Tile tile : tiles) {
                    if (tile != null) {
                        g.drawImage(tile.getImage(), (int) tile.getX(), (int) tile.getY(), null);

                        //g.setColor(Color.MAGENTA);
                        //g.drawRect((int) tile.getHitBox().getX(), (int) tile.getHitBox().getY(), (int) tile.getHitBox().getWidth(), (int) tile.getHitBox().getHeight());
                    }
                }
            }

        } else if (LAYER_TOP == layer) {
            for (Tile[] tiles : layerData2) {
                for (Tile tile : tiles) {
                    if (tile != null) {
                        g.drawImage(tile.getImage(), (int) tile.getX(), (int) tile.getY(), null);
                    }
                }
            }

        } else {
            throw new RuntimeException("Unknown layer " + layer);
        }
    }
}
