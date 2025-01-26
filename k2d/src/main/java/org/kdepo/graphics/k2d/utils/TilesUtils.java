package org.kdepo.graphics.k2d.utils;

import org.kdepo.graphics.k2d.resources.ResourcesController;
import org.kdepo.graphics.k2d.tiles.TileConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TilesUtils {

    public static Map<Integer, TileConfiguration> loadConfigurations(String pathToFile) {
        System.out.println("Loading tiles configurations from " + pathToFile);
        Map<Integer, TileConfiguration> result = new HashMap<>();

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            System.out.println("Cannot load tiles configurations because path to file is not provided");
            return result;
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Cannot load tiles configurations because path to file is not exists or directory: " + pathToFile);
            return result;
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Cannot load tiles configurations from file: " + pathToFile);
            e.printStackTrace();
            return result;
        }

        // Load tile configurations
        Document xmlDocument = null;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            System.out.println("Cannot load tiles configurations from file: " + pathToFile);
            e.printStackTrace();
            return result;
        }

        ResourcesController resourcesController = ResourcesController.getInstance();

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node tilesConfigurationsNode = list.item(i);
            if ("tiles_configurations".equals(tilesConfigurationsNode.getNodeName())) {

                NodeList tilesConfigurationsNodesList = tilesConfigurationsNode.getChildNodes();
                for (int j = 0; j < tilesConfigurationsNodesList.getLength(); j++) {

                    Node tileConfigurationNode = tilesConfigurationsNodesList.item(j);
                    if ("tile_configuration".equals(tileConfigurationNode.getNodeName())) {

                        Element tileConfigurationElement = (Element) tileConfigurationNode;

                        int id = DomUtils.resolveIntAttribute(tileConfigurationElement, "id");
                        String imageName = DomUtils.resolveStringAttribute(tileConfigurationElement, "image");
                        int hitBoxX = DomUtils.resolveIntAttribute(tileConfigurationElement, "hit_box_x");
                        int hitBoxY = DomUtils.resolveIntAttribute(tileConfigurationElement, "hit_box_y");
                        int hitBoxWidth = DomUtils.resolveIntAttribute(tileConfigurationElement, "hit_box_width");
                        int hitBoxHeight = DomUtils.resolveIntAttribute(tileConfigurationElement, "hit_box_height");

                        BufferedImage image = resourcesController.getImage(imageName);

                        TileConfiguration tileConfiguration = new TileConfiguration(
                                id,
                                image,
                                hitBoxX,
                                hitBoxY,
                                hitBoxWidth,
                                hitBoxHeight
                        );

                        result.put(id, tileConfiguration);
                        System.out.println("Tile configuration loaded " + tileConfiguration);
                    }
                }
            }
        }

        return result;
    }
}
