package view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.TexturePaint;
import java.util.HashMap;
import java.awt.Graphics2D;

/** This class is a factory for loading images. */
public class TextureFactory {
    private static HashMap<String, TexturePaint> textures = new HashMap<>();

    public static TexturePaint getTexture(String imageName) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        TexturePaint texture = textures.get(imageName);
        if (texture != null) {
            return texture;
        }
        // If the image is not in the hashmap, create it to load it
        switch (imageName) {
            case "quarries":
                Color gray = new Color(128, 128, 128);
                // Then fill a TexturePaint with the color
                g.setColor(gray);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
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
                g.dispose();
            case "garden":
                Color green = new Color(0, 255, 0);
                // Then fill a TexturePaint with the color
                g.setColor(green);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
            case "market":
                Color yellow = new Color(255, 255, 0);
                // Then fill a TexturePaint with the color
                g.setColor(yellow);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
            case "temple":
                Color purple = new Color(255, 0, 255);
                // Then fill a TexturePaint with the color
                g.setColor(purple);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
        }
        TexturePaint newTexture = new TexturePaint(img, new java.awt.Rectangle(0, 0, 1, 1));
        textures.put(imageName, newTexture);
        return newTexture;
    }
}
