package org.kdepo.graphics.k2d.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ResourcesController {

    private static volatile ResourcesController instance;

    private final Map<String, Resource> resourceMap;

    private final Map<String, BufferedImage> bufferedImagesCache;

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
        bufferedImagesCache = new HashMap<>();

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
        resourceMap.putAll(ResourceUtils.loadDefinitions(this.path + fileName));
        System.out.println("Loaded resources: " + resourceMap.size());
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

}
