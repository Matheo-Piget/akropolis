package view;

import java.awt.Color;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.awt.Graphics2D;
import java.util.Objects;

/** This class is a factory for loading images. */
public class TextureFactory {
    private static HashMap<String, BufferedImage> textures = new HashMap<>();

    public static BufferedImage getTexture(String imageName) {
        BufferedImage texture = textures.get(imageName);
        System.out.println(textures);
        if (texture != null) {
            return texture;
        }
        // If the image is not in the hashmap, create it to load it
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        switch (imageName) {
            case "quarries":
                try {
                    img = ImageIO.read(Objects.requireNonNull(TextureFactory.class.getResourceAsStream("/quarries.png")));
                } catch (IOException e) {
                    Color gray = new Color(128, 128, 128);
                    // Then fill a TexturePaint with the color
                    g.setColor(gray);
                    g.fillRect(0, 0, 1, 1);
                }
                break;
            case "barrack":
                try {
                    img = ImageIO.read(Objects.requireNonNull(TextureFactory.class.getResourceAsStream("/barrack.png")));
                } catch (IOException e) {
                    Color red = new Color(255, 0, 0);
                    // Then fill a TexturePaint with the color
                    g.setColor(red);
                    g.fillRect(0, 0, 1, 1);
                    g.dispose();
                }
                break;
            case "building":
                try {
                    img = ImageIO.read(Objects.requireNonNull(TextureFactory.class.getResourceAsStream("/building.png")));
                } catch (IOException e) {
                    Color blue = new Color(0, 0, 255);
                    // Then fill a TexturePaint with the color
                    g.setColor(blue);
                    g.fillRect(0, 0, 1, 1);
                }
                break;
            case "garden":
                try {
                    img = ImageIO.read(Objects.requireNonNull(TextureFactory.class.getResourceAsStream("/garden.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                    Color green = new Color(0, 255, 0);
                    // Then fill a TexturePaint with the color
                    g.setColor(green);
                    g.fillRect(0, 0, 1, 1);
                }
                break;
            case "market":
                // Load the image
                try {
                    img = ImageIO.read(Objects.requireNonNull(TextureFactory.class.getResourceAsStream("/market.png")));
                } catch (IOException e) {
                    e.printStackTrace();
                    Color yellow = new Color(255, 255, 0);
                    // Then fill a TexturePaint with the color
                    g.setColor(yellow);
                    g.fillRect(0, 0, 1, 1);
                }
                break;
            case "temple":
                try {
                    img = ImageIO.read(Objects.requireNonNull(TextureFactory.class.getResourceAsStream("/temple.png")));
                } catch (IOException e) {
                    Color purple = new Color(255, 0, 255);
                    // Then fill a TexturePaint with the color
                    g.setColor(purple);
                    g.fillRect(0, 0, 1, 1);

                }
                break;
        }
        g.dispose();
        textures.put(imageName, img);
        return img;
    }
}
