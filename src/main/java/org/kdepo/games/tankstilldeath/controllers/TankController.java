package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.model.MoveDirection;
import org.kdepo.games.tankstilldeath.model.Tank;
import org.kdepo.games.tankstilldeath.model.TankConfiguration;
import org.kdepo.graphics.k2d.utils.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TankController {

    private static TankController instance;

    private final Map<Integer, TankConfiguration> tankConfigurationMap;

    public static TankController getInstance() {
        if (instance == null) {
            instance = new TankController();
        }
        return instance;
    }

    private TankController() {
        tankConfigurationMap = new HashMap<>();
    }

    public void loadTanksConfigurations(String pathToFile) {
        System.out.println("Loading tanks configurations from " + pathToFile);

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            throw new RuntimeException("Cannot load tanks configurations because path to file is not provided");
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            throw new RuntimeException("Cannot load tanks configurations because path to file is not exists or directory: " + pathToFile);
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Cannot load tanks configurations from file: " + pathToFile, e);
        }

        tankConfigurationMap.clear();

        // Load tanks configurations
        Document xmlDocument;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            throw new RuntimeException("Cannot load tanks configurations from file: " + pathToFile, e);
        }

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node tanksConfigurationsNode = list.item(i);
            if ("tanks_configurations".equals(tanksConfigurationsNode.getNodeName())) {

                NodeList tanksConfigurationsNodesList = tanksConfigurationsNode.getChildNodes();
                for (int j = 0; j < tanksConfigurationsNodesList.getLength(); j++) {

                    Node tankConfigurationNode = tanksConfigurationsNodesList.item(j);
                    if ("tank_configuration".equals(tankConfigurationNode.getNodeName())) {

                        Element tankConfigurationElement = (Element) tankConfigurationNode;

                        int id = DomUtils.resolveIntAttribute(tankConfigurationElement, "id");
                        String animationsName = DomUtils.resolveStringAttribute(tankConfigurationElement, "animations");
                        double movementSpeed = DomUtils.resolveDoubleAttribute(tankConfigurationElement, "movement_speed");
                        double reloadingSpeed = DomUtils.resolveDoubleAttribute(tankConfigurationElement, "reloading_speed");
                        int hitBoxOffsetX = DomUtils.resolveIntAttribute(tankConfigurationElement, "hit_box_offset_x");
                        int hitBoxOffsetY = DomUtils.resolveIntAttribute(tankConfigurationElement, "hit_box_offset_y");
                        int hitBoxWidth = DomUtils.resolveIntAttribute(tankConfigurationElement, "hit_box_width");
                        int hitBoxHeight = DomUtils.resolveIntAttribute(tankConfigurationElement, "hit_box_height");
                        int bulletTypeId = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_type_id");
                        int bulletOffsetNorthX = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_north_x");
                        int bulletOffsetNorthY = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_north_y");
                        int bulletOffsetEastX = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_east_x");
                        int bulletOffsetEastY = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_east_y");
                        int bulletOffsetSouthX = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_south_x");
                        int bulletOffsetSouthY = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_south_y");
                        int bulletOffsetWestX = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_west_x");
                        int bulletOffsetWestY = DomUtils.resolveIntAttribute(tankConfigurationElement, "bullet_offset_west_y");
                        int armorTypeId = DomUtils.resolveIntAttribute(tankConfigurationElement, "armor_type_id");

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

    public List<Tank> getTanksToSpawnList(String pathToFile) {
        System.out.println("Loading tanks to spawn list from " + pathToFile);

        List<Tank> tanksToSpawnList = new ArrayList<>();

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            throw new RuntimeException("Cannot load tanks to spawn list because path to file is not provided");
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            throw new RuntimeException("Cannot load tanks to spawn list because path to file is not exists or directory: " + pathToFile);
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Cannot load tanks to spawn list from file: " + pathToFile, e);
        }

        // Load tanks data
        Document xmlDocument;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            System.out.println("Cannot load tanks to spawn data from file: " + pathToFile);
            throw new RuntimeException("Cannot load tanks to spawn list from file: " + pathToFile, e);
        }

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node tanksToSpawnNode = list.item(i);
            if ("tanks_to_spawn".equals(tanksToSpawnNode.getNodeName())) {

                NodeList tanksToSpawnNodesList = tanksToSpawnNode.getChildNodes();
                for (int j = 0; j < tanksToSpawnNodesList.getLength(); j++) {

                    Node tankToSpawnNode = tanksToSpawnNodesList.item(j);
                    if ("tank_to_spawn".equals(tankToSpawnNode.getNodeName())) {

                        Element tankToSpawnElement = (Element) tankToSpawnNode;

                        int id = DomUtils.resolveIntAttribute(tankToSpawnElement, "id");
                        int teamId = DomUtils.resolveIntAttribute(tankToSpawnElement, "team_id");

                        Tank tank = prepareTank(id, teamId, 0, 0, MoveDirection.NORTH);
                        tanksToSpawnList.add(tank);
                    }
                }
            }
        }

        return tanksToSpawnList;
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
}
