package org.kdepo.graphics.k2d.resources;

import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.fonts.SymbolConfiguration;
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

public class FontUtils {

    public static Font load(String pathToFile) {
        System.out.println("Loading font from " + pathToFile);

        Font result = null;

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            System.out.println("Cannot load font because path to file is not provided");
            return result;
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Cannot load font because path to file is not exists or directory: " + pathToFile);
            return result;
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Cannot load font from file: " + pathToFile);
            e.printStackTrace();
            return result;
        }

        // Load resources definitions
        Document xmlDocument = null;
        try {
            xmlDocument = db.parse(pathToFile);
        } catch (IOException | SAXException e) {
            System.out.println("Cannot load resources definitions from file: " + pathToFile);
            e.printStackTrace();
            return result;
        }

        ResourcesController resourcesController = ResourcesController.getInstance();

        // Prepare empty map for characters configurations and cell data
        HashMap<Integer, SymbolConfiguration> symbolsConfigurationsMap = new HashMap<>();
        int cellWidth = -1;
        int cellHeight = -1;
        String fontName = null;
        BufferedImage fontImage = null;

        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {
            Node childNode = list.item(i);

            if ("symbols".equals(childNode.getNodeName())) {

                Element charactersElement = (Element) childNode;

                // cell_width="_" cell_height="_" font_name="_" image="_"
                String cellWidthAsString = charactersElement.getAttribute("cell_width");
                String cellHeightAsString = charactersElement.getAttribute("cell_height");
                fontName = charactersElement.getAttribute("font_name");
                String imageName = charactersElement.getAttribute("image");
                fontImage = resourcesController.getImage(imageName);

                cellWidth = Integer.parseInt(cellWidthAsString);
                cellHeight = Integer.parseInt(cellHeightAsString);

                NodeList charactersNodeList = childNode.getChildNodes();
                for (int j = 0; j < charactersNodeList.getLength(); j++) {

                    Node characterNode = charactersNodeList.item(j);

                    if ("symbol".equals(characterNode.getNodeName())) {

                        Element characterElement = (Element) characterNode;

                        // code="_" width="_" height="_" x="_" y="_"
                        String code = characterElement.getAttribute("code");
                        String width = characterElement.getAttribute("width");
                        String height = characterElement.getAttribute("height");
                        String x = characterElement.getAttribute("x");
                        String y = characterElement.getAttribute("y");

                        symbolsConfigurationsMap.put(
                                Integer.parseInt(code),
                                new SymbolConfiguration(
                                        Integer.parseInt(code),
                                        Integer.parseInt(width),
                                        Integer.parseInt(height),
                                        Integer.parseInt(x),
                                        Integer.parseInt(y)
                                )
                        );
                    }
                }
            }
        }

        if (!symbolsConfigurationsMap.isEmpty() && cellWidth > 0 && cellHeight > 0) {
            return new Font(fontName, cellWidth, cellHeight, symbolsConfigurationsMap, fontImage);
        } else {
            System.out.println("Font was not added: " + pathToFile);
            return null;
        }
    }

}
