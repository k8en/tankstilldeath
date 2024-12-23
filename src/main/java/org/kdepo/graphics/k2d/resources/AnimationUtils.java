package org.kdepo.graphics.k2d.resources;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.animations.AnimationFrame;
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

public class AnimationUtils {

    public static Map<String, Animation> load(String pathToFile) {
        System.out.println("Loading animation from " + pathToFile);
        Map<String, Animation> result = new HashMap<>();

        // Check that path to file is provided
        if (pathToFile == null || pathToFile.isEmpty()) {
            System.out.println("Cannot load animations because path to file is not provided");
            return result;
        }

        // Check for file existence
        File file = new File(pathToFile);
        if (!file.exists() || file.isDirectory()) {
            System.out.println("Cannot load animations because path to file is not exists or directory: " + pathToFile);
            return result;
        }

        // Prepare parser
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.println("Cannot load animations from file: " + pathToFile);
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

        // Travers through the document
        NodeList rootNodeList = xmlDocument.getChildNodes();
        for (int i = 0; i < rootNodeList.getLength(); i++) {

            Node rootNode = rootNodeList.item(i);
            if ("animations".equals(rootNode.getNodeName())) {

                NodeList animationsNodeList = rootNode.getChildNodes();
                for (int j = 0; j < animationsNodeList.getLength(); j++) {

                    Node animationNode = animationsNodeList.item(j);
                    if ("animation".equals(animationNode.getNodeName())) {

                        Element animationElement = (Element) animationNode;
                        String name = animationElement.getAttribute("name");

                        List<AnimationFrame> animationFrames = new ArrayList<>();

                        NodeList animationNodeChildNodesList = animationNode.getChildNodes();
                        for (int k = 0; k < animationNodeChildNodesList.getLength(); k++) {

                            Node animationChildNode = animationNodeChildNodesList.item(k);
                            if ("frames".equals(animationChildNode.getNodeName())) {

                                NodeList framesNodesList = animationChildNode.getChildNodes();
                                for (int f = 0; f < framesNodesList.getLength(); f++) {

                                    Node framesChildNode = framesNodesList.item(f);
                                    if ("frame".equals(framesChildNode.getNodeName())) {

                                        Element frameElement = (Element) framesChildNode;
                                        String id = frameElement.getAttribute("id");
                                        String duration = frameElement.getAttribute("duration");
                                        String imageResourceId = frameElement.getAttribute("image");

                                        animationFrames.add(new AnimationFrame(
                                                Integer.parseInt(id),
                                                Float.parseFloat(duration),
                                                resourcesController.getImage(imageResourceId)
                                        ));
                                    }
                                }
                            }
                        }

                        // Automatically calculate next/previous frames
                        if (animationFrames.size() > 1) {
                            for (int n = 0; n < animationFrames.size() - 1; n++) {
                                animationFrames.get(n).setNextFrame(animationFrames.get(n + 1));
                            }
                            for (int n = 1; n < animationFrames.size(); n++) {
                                animationFrames.get(n).setPreviousFrame(animationFrames.get(n - 1));
                            }
                        }

                        result.put(name, new Animation(name, animationFrames));
                    }
                }
            }
        }

        return result;
    }

}
