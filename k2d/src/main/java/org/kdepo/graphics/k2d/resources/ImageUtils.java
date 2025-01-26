package org.kdepo.graphics.k2d.resources;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static BufferedImage load(String pathToFile) {
        File imageFile = new File(pathToFile);
        BufferedImage image;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.out.println("Image not loaded [" + pathToFile + "]: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return image;
    }

}
