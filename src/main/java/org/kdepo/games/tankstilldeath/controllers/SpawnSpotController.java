package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.model.MoveDirection;
import org.kdepo.games.tankstilldeath.model.SpawnSpot;
import org.kdepo.graphics.k2d.utils.DomUtils;
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
import java.util.List;

public class SpawnSpotController {

    private static SpawnSpotController instance;

    private final List<SpawnSpot> spawnSpotList;

    public static SpawnSpotController getInstance() {
        if (instance == null) {
            instance = new SpawnSpotController();
        }
        return instance;
    }

    private SpawnSpotController() {
        spawnSpotList = new ArrayList<>();
    }

    public void loadSpawnSpotData(String pathToFile) {
        System.out.println("Loading spawn spots data from " + pathToFile);

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            System.out.println("Cannot load spawn spots data because path to file is not provided");
            return;
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Cannot load spawn spots data because path to file is not exists or directory: " + pathToFile);
            return;
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Cannot load spawn spots data from file: " + pathToFile);
            e.printStackTrace();
            return;
        }

        // Load spawn spots data
        Document xmlDocument = null;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            System.out.println("Cannot load spawn spots data from file: " + pathToFile);
            e.printStackTrace();
            return;
        }

        spawnSpotList.clear();

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node spawnSpotsNode = list.item(i);
            if ("spawn_spots".equals(spawnSpotsNode.getNodeName())) {

                NodeList spawnSpotsNodesList = spawnSpotsNode.getChildNodes();
                for (int j = 0; j < spawnSpotsNodesList.getLength(); j++) {

                    Node spawnSpotNode = spawnSpotsNodesList.item(j);
                    if ("spawn_spot".equals(spawnSpotNode.getNodeName())) {

                        Element spawnSpotElement = (Element) spawnSpotNode;

                        int x = DomUtils.resolveIntAttribute(spawnSpotElement, "x");
                        int y = DomUtils.resolveIntAttribute(spawnSpotElement, "y");
                        int teamId = DomUtils.resolveIntAttribute(spawnSpotElement, "team_id");
                        MoveDirection moveDirection = MoveDirection.valueOf(DomUtils.resolveStringAttribute(spawnSpotElement, "move_direction").toUpperCase());

                        SpawnSpot spawnSpot = new SpawnSpot(x, y, teamId, moveDirection);
                        spawnSpotList.add(spawnSpot);
                    }
                }
            }
        }
    }

    public SpawnSpot getAvailableSpawnSpot(int teamId) {
        for (SpawnSpot spawnSpot : spawnSpotList) {
            if (spawnSpot.getTeamId() == teamId && !spawnSpot.isActive()) {
                return spawnSpot;
            }
        }
        return null;
    }

    public void update() {
        for (SpawnSpot spawnSpot : spawnSpotList) {
            if (spawnSpot.isActive()) {
                spawnSpot.update();
            }
        }
    }

    public void render(Graphics2D g) {

    }
}
