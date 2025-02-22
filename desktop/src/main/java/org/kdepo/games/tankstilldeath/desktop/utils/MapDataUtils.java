package org.kdepo.games.tankstilldeath.desktop.utils;

import org.kdepo.games.tankstilldeath.desktop.model.MapData;
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

public class MapDataUtils {

    public static MapData loadMapData(String pathToFile) {
        System.out.println("Loading map data from " + pathToFile);
        MapData mapData = new MapData();

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            throw new RuntimeException("Cannot load map data because path to file is not provided");
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            throw new RuntimeException("Cannot load map data because path to file is not exists or directory: " + pathToFile);
        }

        // Set path to map folder
        String pathToFolder = file.getParent();
        mapData.setPathToFolder(pathToFolder);

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException("Cannot load map data from file: " + pathToFile, e);
        }

        // Load map data
        Document xmlDocument = null;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            throw new RuntimeException("Cannot load map data from file: " + pathToFile, e);
        }

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node childNode = list.item(i);
            if ("map".equals(childNode.getNodeName())) {

                NodeList mapNodesList = childNode.getChildNodes();
                for (int j = 0; j < mapNodesList.getLength(); j++) {

                    Node propertyNode = mapNodesList.item(j);
                    if ("property".equals(propertyNode.getNodeName())) {

                        Element propertyElement = (Element) propertyNode;

                        String name = propertyElement.getAttribute("name");
                        if (name.isEmpty()) {
                            System.out.println("Map data property name not found for " + propertyElement);
                            continue;
                        }

                        switch (name) {
                            case "map_name": {
                                String mapName = DomUtils.resolveStringAttribute(propertyElement, "value");
                                if (mapName.isEmpty()) {
                                    mapName = null;
                                }
                                mapData.setMapName(mapName);

                                break;
                            }
                            case "next_map": {
                                String nextMap = DomUtils.resolveStringAttribute(propertyElement, "value");
                                if (nextMap.isEmpty()) {
                                    nextMap = null;
                                }
                                mapData.setNextMap(nextMap);

                                break;
                            }
                            case "layer_0": {
                                String fileName = DomUtils.resolveStringAttribute(propertyElement, "value");
                                if (fileName.isEmpty()) {
                                    throw new RuntimeException("Layer 0 file name not found for " + pathToFile);
                                }
                                mapData.setFileNameLayer0(fileName);

                                break;
                            }
                            case "layer_1": {
                                String fileName = DomUtils.resolveStringAttribute(propertyElement, "value");
                                if (fileName.isEmpty()) {
                                    throw new RuntimeException("Layer 1 file name not found for " + pathToFile);
                                }
                                mapData.setFileNameLayer1(fileName);

                                break;
                            }
                            case "layer_2": {
                                String fileName = DomUtils.resolveStringAttribute(propertyElement, "value");
                                if (fileName.isEmpty()) {
                                    throw new RuntimeException("Layer 2 file name not found for " + pathToFile);
                                }
                                mapData.setFileNameLayer2(fileName);

                                break;
                            }
                            case "spawn_spots": {
                                String fileName = DomUtils.resolveStringAttribute(propertyElement, "value");
                                if (fileName.isEmpty()) {
                                    System.out.println("Spawn spots file name not found for " + propertyElement);
                                    continue;
                                }
                                mapData.setFileNameSpawnSpots(fileName);

                                break;
                            }
                            case "tanks_to_spawn": {
                                String fileName = DomUtils.resolveStringAttribute(propertyElement, "value");
                                if (fileName.isEmpty()) {
                                    System.out.println("Tanks file name not found for " + propertyElement);
                                    continue;
                                }
                                mapData.setFileNameTanksToSpawn(fileName);

                                break;
                            }
                            case "active_tanks_limit":
                                int activeTanksLimit = DomUtils.resolveIntAttribute(propertyElement, "value");
                                mapData.setActiveTanksLimit(activeTanksLimit);
                                break;
                        }
                    }
                }
            }
        }

        System.out.println("Loaded " + mapData);

        return mapData;
    }
}
