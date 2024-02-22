package view;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.TexturePaint;
import java.awt.Graphics2D;

/** This class is a factory for loading images. */
public class TextureFactory {

    public static TexturePaint getTexture(String imageName) {
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        switch (imageName) {
            case "barrack":
                // Return a red img
                Color red = new Color(255, 0, 0);
                // Then fill a TexturePaint with the color
                g.setColor(red);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
            case "building":
                // Return a blue img
                Color blue = new Color(0, 0, 255);
                // Then fill a TexturePaint with the color
                g.setColor(blue);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
            case "garden":
                // Return a green img
                Color green = new Color(0, 255, 0);
                // Then fill a TexturePaint with the color
                g.setColor(green);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
            case "market":
                // Return a yellow img
                Color yellow = new Color(255, 255, 0);
                // Then fill a TexturePaint with the color
                g.setColor(yellow);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
            case "temple":
                // Return a purple img
                Color purple = new Color(255, 0, 255);
                // Then fill a TexturePaint with the color
                g.setColor(purple);
                g.fillRect(0, 0, 1, 1);
                g.dispose();
        }
        return new TexturePaint(img, new java.awt.Rectangle(0, 0, 1, 1));
    }
}
