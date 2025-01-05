package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.model.MoveDirection;
import org.kdepo.games.tankstilldeath.model.SpawnSpot;
import org.kdepo.games.tankstilldeath.model.Tank;
import org.kdepo.games.tankstilldeath.model.TankConfiguration;
import org.kdepo.graphics.k2d.geometry.Point;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TankController {

    private static TankController instance;

    private final Map<Integer, TankConfiguration> tankConfigurationMap;

    private final List<Tank> tankList;
    private int activeTanksCurrent;
    private int activeTanksMax;

    private final SpawnSpotController spawnSpotController;

    public static TankController getInstance() {
        if (instance == null) {
            instance = new TankController();
        }
        return instance;
    }

    private TankController() {
        tankConfigurationMap = new HashMap<>();
        tankList = new ArrayList<>();
        activeTanksCurrent = 0;

        spawnSpotController = SpawnSpotController.getInstance();
    }

    public void loadTanksConfigurations(String pathToFile) {
        System.out.println("Loading tanks configurations from " + pathToFile);

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            System.out.println("Cannot load tanks configurations because path to file is not provided");
            return;
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Cannot load tanks configurations because path to file is not exists or directory: " + pathToFile);
            return;
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Cannot load tanks configurations from file: " + pathToFile);
            e.printStackTrace();
            return;
        }

        tankConfigurationMap.clear();

        // Load tanks configurations
        Document xmlDocument = null;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            System.out.println("Cannot load tanks configurations from file: " + pathToFile);
            e.printStackTrace();
            return;
        }

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node tanksNode = list.item(i);
            if ("tanks".equals(tanksNode.getNodeName())) {

                NodeList tanksNodesList = tanksNode.getChildNodes();
                for (int j = 0; j < tanksNodesList.getLength(); j++) {

                    Node tankNode = tanksNodesList.item(j);
                    if ("tank".equals(tankNode.getNodeName())) {

                        Element tankElement = (Element) tankNode;

                        int id = resolveIntAttribute(tankElement, "id");
                        String animationsName = resolveStringAttribute(tankElement, "animations");
                        double movementSpeed = resolveDoubleAttribute(tankElement, "movement_speed");
                        double reloadingSpeed = resolveDoubleAttribute(tankElement, "reloading_speed");
                        int hitBoxOffsetX = resolveIntAttribute(tankElement, "hit_box_offset_x");
                        int hitBoxOffsetY = resolveIntAttribute(tankElement, "hit_box_offset_y");
                        int hitBoxWidth = resolveIntAttribute(tankElement, "hit_box_width");
                        int hitBoxHeight = resolveIntAttribute(tankElement, "hit_box_height");
                        int bulletTypeId = resolveIntAttribute(tankElement, "bullet_type_id");
                        int bulletOffsetNorthX = resolveIntAttribute(tankElement, "bullet_offset_north_x");
                        int bulletOffsetNorthY = resolveIntAttribute(tankElement, "bullet_offset_north_y");
                        int bulletOffsetEastX = resolveIntAttribute(tankElement, "bullet_offset_east_x");
                        int bulletOffsetEastY = resolveIntAttribute(tankElement, "bullet_offset_east_y");
                        int bulletOffsetSouthX = resolveIntAttribute(tankElement, "bullet_offset_south_x");
                        int bulletOffsetSouthY = resolveIntAttribute(tankElement, "bullet_offset_south_y");
                        int bulletOffsetWestX = resolveIntAttribute(tankElement, "bullet_offset_west_x");
                        int bulletOffsetWestY = resolveIntAttribute(tankElement, "bullet_offset_west_y");
                        int armorTypeId = resolveIntAttribute(tankElement, "armor_type_id");

                        TankConfiguration tankConfiguration = new TankConfiguration(
                                id,
                                animationsName,
                                movementSpeed,
                                reloadingSpeed,
                                hitBoxOffsetX,
                                hitBoxOffsetY,
                                hitBoxWidth,
                                hitBoxHeight,
                                bulletTypeId,
                                bulletOffsetNorthX,
                                bulletOffsetNorthY,
                                bulletOffsetEastX,
                                bulletOffsetEastY,
                                bulletOffsetSouthX,
                                bulletOffsetSouthY,
                                bulletOffsetWestX,
                                bulletOffsetWestY,
                                armorTypeId
                        );

                        tankConfigurationMap.put(tankConfiguration.getTankId(), tankConfiguration);

                        System.out.println("Tank configuration loaded " + tankConfiguration);
                    }
                }
            }
        }

        System.out.println("Loaded tanks configurations: " + tankConfigurationMap.size());
    }

    public void loadTankData(String pathToFile) {
        System.out.println("Loading tanks data from " + pathToFile);

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            System.out.println("Cannot load tanks data because path to file is not provided");
            return;
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Cannot load tanks data because path to file is not exists or directory: " + pathToFile);
            return;
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Cannot load tanks data from file: " + pathToFile);
            e.printStackTrace();
            return;
        }

        // Load tanks data
        Document xmlDocument = null;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            System.out.println("Cannot load tanks data from file: " + pathToFile);
            e.printStackTrace();
            return;
        }

        tankList.clear();
        activeTanksCurrent = 0;

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node childNode = list.item(i);
            if ("tanks".equals(childNode.getNodeName())) {

                NodeList tanksNodesList = childNode.getChildNodes();
                for (int j = 0; j < tanksNodesList.getLength(); j++) {

                    Node tankNode = tanksNodesList.item(j);
                    if ("tank".equals(tankNode.getNodeName())) {

                        Element tankElement = (Element) tankNode;

                        int id = resolveIntAttribute(tankElement, "id");
                        int team = resolveIntAttribute(tankElement, "team");

                        Tank tank = prepareTank(id, team, 0, 0, MoveDirection.NORTH);
                        tankList.add(tank);
                    }
                }
            }
        }
    }

    public List<Tank> getTankList() {
        return tankList;
    }

    public void spawn(int team) {
        //tankList.add(new Tank(x, y, moveDirection, 12, 12, 48, 48));
        Tank tankToSpawn = null;
        for (Tank tank : tankList) {
            if (!tank.isActive()) {
                tankToSpawn = tank;
                break;
            }
        }

        if (tankToSpawn == null) {
            return;
        }

        SpawnSpot spawnSpot = spawnSpotController.getAvailableSpawnSpot(team);
        Point spawnPointCenter = spawnSpot.getCenter();

        tankToSpawn.setCenter(spawnPointCenter.getX(), spawnPointCenter.getY());
        tankToSpawn.setActive(true);

        spawnSpot.setActive(true);
        // TODO set timer
    }

    public Tank prepareTank(int id, int team, double x, double y, MoveDirection moveDirection) {
        TankConfiguration tankConfiguration = tankConfigurationMap.get(id);

        return new Tank(
                tankConfiguration.getAnimationsCollectionName(),
                x, y,
                team,
                moveDirection,
                tankConfiguration.getMovementSpeed(),
                tankConfiguration.getBulletTypeId(),
                tankConfiguration.getReloadingSpeed(),
                tankConfiguration.getArmorTypeId(),
                tankConfiguration.getHitBoxOffsetX(), tankConfiguration.getHitBoxOffsetY(), tankConfiguration.getHitBoxWidth(), tankConfiguration.getHitBoxHeight(),
                tankConfiguration.getBulletOffsetXNorth(), tankConfiguration.getBulletOffsetYNorth(),
                tankConfiguration.getBulletOffsetXEast(), tankConfiguration.getBulletOffsetYEast(),
                tankConfiguration.getBulletOffsetXSouth(), tankConfiguration.getBulletOffsetYSouth(),
                tankConfiguration.getBulletOffsetXWest(), tankConfiguration.getBulletOffsetYWest()
        );
    }

    private static String resolveStringAttribute(Element element, String attributeName) {
        String value = element.getAttribute(attributeName);
        if (value.isEmpty()) {
            System.out.println("Tank '" + attributeName + "' not found for " + element);
        }
        return value;
    }

    private int resolveIntAttribute(Element element, String attributeName) {
        String valueStr = element.getAttribute(attributeName);
        if (valueStr.isEmpty()) {
            System.out.println("Tank '" + attributeName + "' not found for " + element);
        }
        int value = -1;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            System.out.println("Tank '" + attributeName + "' not resolved for " + valueStr);
        }
        return value;
    }

    private double resolveDoubleAttribute(Element element, String attributeName) {
        String valueStr = element.getAttribute(attributeName);
        if (valueStr.isEmpty()) {
            System.out.println("Tank '" + attributeName + "' not found for " + element);
        }
        double value = -1;
        try {
            value = Double.parseDouble(valueStr);
        } catch (NumberFormatException e) {
            System.out.println("Tank '" + attributeName + "' not resolved for " + valueStr);
        }
        return value;
    }

    public void update() {
        for (Tank tank : tankList) {
            if (tank.isActive()) {
                tank.update();
            }
        }
    }

    public void render(Graphics2D g) {
        for (Tank tank : tankList) {
            if (tank.isActive()) {
                tank.render(g);
            }
        }
    }
}
