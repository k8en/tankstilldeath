package org.kdepo.graphics.k2d.resources;

import org.kdepo.graphics.k2d.animations.Animation;
import org.kdepo.graphics.k2d.fonts.Font;
import org.kdepo.graphics.k2d.utils.ResourcesUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourcesController {

    private static volatile ResourcesController instance;

    private final Map<String, Resource> resourceMap;

    private final Map<String, Map<String, Animation>> animationsMap;

    private final Map<String, BufferedImage> bufferedImagesCache;

    private final Map<String, Font> fontsCache;

    private String path;

    public static ResourcesController getInstance() {
        ResourcesController localInstance = instance;
        if (localInstance == null) {
            synchronized (ResourcesController.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new ResourcesController();
                }
            }
        }
        return localInstance;
    }

    private ResourcesController() {
        System.out.println("ResourcesController initialization..");

        resourceMap = new HashMap<>();
        animationsMap = new HashMap<>();
        bufferedImagesCache = new HashMap<>();
        fontsCache = new HashMap<>();

        System.out.println("ResourcesController initialized!");
    }

    public void setPath(String path) {
        if (!path.endsWith(File.separator)) {
            this.path = path + File.separator;
        } else {
            this.path = path;
        }
    }

    public String getPath() {
        return path;
    }

    public void loadDefinitions(String fileName) {
        resourceMap.clear();
        resourceMap.putAll(ResourcesUtils.loadDefinitions(this.path + fileName));
        System.out.println("Loaded resources: " + resourceMap.size());
    }

    public Resource getResource(String resourceId) {
        return resourceMap.get(resourceId);
    }

    public BufferedImage getImage(String resourceId) {
        BufferedImage result = bufferedImagesCache.get(resourceId);
        if (result == null) {
            Resource resource = resourceMap.get(resourceId);
            if (resource == null) {
                throw new RuntimeException("Resource not found for the next id: " + resourceId);
            }

            String resourcePath = resource.getPath();
            result = ImageUtils.load(path + resourcePath);
            bufferedImagesCache.put(resourceId, result);
        }
        return result;
    }

    public Font getFont(String resourceId) {
        Font result = fontsCache.get(resourceId);
        if (result == null) {
            Resource resource = resourceMap.get(resourceId);
            if (resource == null) {
                throw new RuntimeException("Resource not found for the next id: " + resourceId);
            }

            String resourcePath = resource.getPath();
            result = FontUtils.load(path + resourcePath);
            fontsCache.put(resourceId, result);
        }
        return result;
    }

    public Map<String, Animation> getAnimations(String resourceId) {
        Map<String, Animation> result = animationsMap.get(resourceId);
        if (result == null) {
            Resource resource = resourceMap.get(resourceId);
            if (resource == null) {
                throw new RuntimeException("Resource not found for the next id: " + resourceId);
            }

            String resourcePath = resource.getPath();
            result = AnimationUtils.load(path + resourcePath);
            animationsMap.put(resourceId, result);
        }
        return result;
    }

}
