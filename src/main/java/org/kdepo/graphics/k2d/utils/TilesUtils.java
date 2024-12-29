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

        // Load resources definitions
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

            Node childNode = list.item(i);
            if ("tiles".equals(childNode.getNodeName())) {

                NodeList resourcesNodesList = childNode.getChildNodes();
                for (int j = 0; j < resourcesNodesList.getLength(); j++) {

                    Node resourceNode = resourcesNodesList.item(j);
                    if ("tile".equals(resourceNode.getNodeName())) {

                        Element resourceElement = (Element) resourceNode;

                        String idStr = resourceElement.getAttribute("id");
                        if (idStr.isEmpty()) {
                            System.out.println("Tile id not found for " + resourceElement);
                            continue;
                        }
                        int id = -1;
                        try {
                            id = Integer.parseInt(idStr);
                        } catch (NumberFormatException e) {
                            System.out.println("Tile id not resolved for " + idStr);
                            continue;
                        }

                        String imageStr = resourceElement.getAttribute("image");
                        if (imageStr.isEmpty()) {
                            System.out.println("Tile image not found for " + resourceElement);
                            continue;
                        }
                        BufferedImage image = resourcesController.getImage(imageStr);

                        TileConfiguration tileConfiguration = new TileConfiguration(
                                id,
                                image
                        );

                        result.put(id, tileConfiguration);
                        System.out.println("Tile configuration loaded: id=" + id + ", image='" + imageStr + "'");
                    }
                }
            }
        }

        return result;
    }

}
