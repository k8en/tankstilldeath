package org.kdepo.games.tankstilldeath.controllers;

import org.kdepo.games.tankstilldeath.model.SpawnSpot;
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

            Node childNode = list.item(i);
            if ("spawn".equals(childNode.getNodeName())) {

                NodeList spawnNodesList = childNode.getChildNodes();
                for (int j = 0; j < spawnNodesList.getLength(); j++) {

                    Node spotNode = spawnNodesList.item(j);
                    if ("spot".equals(spotNode.getNodeName())) {

                        Element spotElement = (Element) spotNode;

                        int x = resolveIntAttribute(spotElement, "x");
                        int y = resolveIntAttribute(spotElement, "y");
                        int team = resolveIntAttribute(spotElement, "team");

                        SpawnSpot spawnSpot = new SpawnSpot(x, y, team);
                        spawnSpotList.add(spawnSpot);
                    }
                }
            }
        }
    }

    private int resolveIntAttribute(Element element, String attributeName) {
        String valueStr = element.getAttribute(attributeName);
        if (valueStr.isEmpty()) {
            System.out.println("Spot '" + attributeName + "' not found for " + element);
        }
        int value = -1;
        try {
            value = Integer.parseInt(valueStr);
        } catch (NumberFormatException e) {
            System.out.println("Spot '" + attributeName + "' not resolved for " + valueStr);
        }
        return value;
    }

    public SpawnSpot getAvailableSpawnSpot(int team) {
        for (SpawnSpot spawnSpot : spawnSpotList) {
            if (spawnSpot.getTeam() == team && !spawnSpot.isActive()) {
                return spawnSpot;
            }
        }
        return null;
    }

    public void update() {
        for (SpawnSpot spawnSpot : spawnSpotList) {
            if (spawnSpot.isActive()) {

            }
        }

    }

    public void render(Graphics2D g) {

    }
}
