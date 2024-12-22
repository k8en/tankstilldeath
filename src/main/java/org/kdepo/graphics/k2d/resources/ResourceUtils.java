package org.kdepo.graphics.k2d.resources;

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
import java.util.HashMap;
import java.util.Map;

public class ResourceUtils {

    public static Map<String, Resource> loadDefinitions(String pathToFile) {
        System.out.println("Loading resource definitions from " + pathToFile);
        Map<String, Resource> result = new HashMap<>();

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            System.out.println("Cannot load resources definitions because path to file is not provided");
            return result;
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Cannot load resource definitions because path to file is not exists or directory: " + pathToFile);
            return result;
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Cannot load resources definitions from file: " + pathToFile);
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

        // Travers through the document
        NodeList list = xmlDocument.getChildNodes();
        for (int i = 0; i < list.getLength(); i++) {

            Node childNode = list.item(i);
            if ("resources".equals(childNode.getNodeName())) {

                NodeList resourcesNodesList = childNode.getChildNodes();
                for (int j = 0; j < resourcesNodesList.getLength(); j++) {

                    Node resourceNode = resourcesNodesList.item(j);
                    if ("resource".equals(resourceNode.getNodeName())) {

                        Element resourceElement = (Element) resourceNode;

                        String typeStr = resourceElement.getAttribute("type");
                        if (typeStr.isEmpty()) {
                            System.out.println("Resource type not found for " + resourceElement);
                            continue;
                        }
                        ResourceType type = null;
                        try {
                            type = ResourceType.valueOf(typeStr.toUpperCase());
                        } catch (IllegalArgumentException e) {
                            System.out.println("Resource type not resolved for " + typeStr.toUpperCase());
                            continue;
                        }

                        String id = resourceElement.getAttribute("id");
                        if (id.isEmpty()) {
                            System.out.println("Resource id not found for " + resourceElement);
                            continue;
                        }

                        String path = resourceElement.getAttribute("path");
                        if (path.isEmpty()) {
                            System.out.println("Resource path not found for " + resourceElement);
                            continue;
                        }

                        Resource resource = new Resource();
                        resource.setType(type);
                        resource.setId(id);
                        resource.setPath(path);

                        result.put(id, resource);
                        System.out.println("Loaded " + type + " resource as '" + id + "' with path " + path);
                    }
                }
            }
        }

        return result;
    }

}
