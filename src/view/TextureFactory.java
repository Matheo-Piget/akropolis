package view;

import java.awt.Color;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;

/** This class is a factory for loading images. */
public class TextureFactory {
    private static HashMap<String, BufferedImage> textures = new HashMap<>();

    public static BufferedImage getTexture(String imageName) {
        BufferedImage texture = textures.get(imageName);
        if (texture != null) {
            return texture;
        }
        // If the image is not in the hashmap, create it to load it
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        switch (imageName) {
            case "quarries":
                Color gray = new Color(128, 128, 128);
                // Then fill a TexturePaint with the color
                g.setColor(gray);
                g.fillRect(0, 0, 1, 1);
                break;
            case "barrack":
                Color red = new Color(255, 0, 0);
                // Then fill a TexturePaint with the color
                g.setColor(red);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
            case "building":
                Color blue = new Color(0, 0, 255);
                // Then fill a TexturePaint with the color
                g.setColor(blue);
                g.fillRect(0, 0, 1, 1);
                break;
            case "garden":
                Color green = new Color(0, 255, 0);
                // Then fill a TexturePaint with the color
                g.setColor(green);
                g.fillRect(0, 0, 1, 1);
                break;
            case "market":
                // Load the image
                try {
                    img = ImageIO.read(TextureFactory.class.getResourceAsStream("/market.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                    Color yellow = new Color(255, 255, 0);
                    // Then fill a TexturePaint with the color
                    g.setColor(yellow);
                    g.fillRect(0, 0, 1, 1);
                    break;
                }
                break;
            case "temple":
                Color purple = new Color(255, 0, 255);
                // Then fill a TexturePaint with the color
                g.setColor(purple);
                g.fillRect(0, 0, 1, 1);
                break;
        }
        g.dispose();
        textures.put(imageName, img);
        return img;
    }
}
