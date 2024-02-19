package view;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.TexturePaint;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import util.Point3D;

/**
 * Represents an hexagon on the game grid.
 */
public abstract class HexagonView extends Polygon {

    protected TexturePaint texture;
    protected Point3D position;

    public HexagonView(int x, int y, int side) {
        // Calculate the coordinates of the six points of the hexagon
        for (int i = 0; i < 6; i++) {
            int xval = (int) (x + side * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (y + side * Math.sin(i * 2 * Math.PI / 6));
            this.addPoint(xval, yval);
        }
    }

    public HexagonView(int x, int y, int z, int side) {
        // Calculate the coordinates of the six points of the hexagon
        for (int i = 0; i < 6; i++) {
            int xval = (int) (x + side * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (y + side * Math.sin(i * 2 * Math.PI / 6));
            this.addPoint(xval, yval);
        }
    }

    public HexagonView(int x, int y, int side, BufferedImage img) {
        // Calculate the coordinates of the six points of the hexagon
        for (int i = 0; i < 6; i++) {
            int xval = (int) (x + side * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (y + side * Math.sin(i * 2 * Math.PI / 6));
            this.addPoint(xval, yval);
        }
        this.texture = new TexturePaint(img, new java.awt.Rectangle(x, y, side, side));
    }

    public HexagonView(int x, int y, int side, Color color) {
        // Calculate the coordinates of the six points of the hexagon
        for (int i = 0; i < 6; i++) {
            int xval = (int) (x + side * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (y + side * Math.sin(i * 2 * Math.PI / 6));
            this.addPoint(xval, yval);
        }

        // Create a BufferedImage and fill it with the color
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(color);
        g.fillRect(0, 0, 1, 1);
        g.dispose();
        this.texture = new TexturePaint(img, new java.awt.Rectangle(x, y, side, side));
    }

    public HexagonView(int x, int y, int z, int side, Color color) {
        // Calculate the coordinates of the six points of the hexagon
        for (int i = 0; i < 6; i++) {
            int xval = (int) (x + side * Math.cos(i * 2 * Math.PI / 6));
            int yval = (int) (y + side * Math.sin(i * 2 * Math.PI / 6));
            this.addPoint(xval, yval);
        }

        // Create a BufferedImage and fill it with the color
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(darken(color, z));
        g.fillRect(0, 0, 1, 1);
        g.dispose();
        this.texture = new TexturePaint(img, new java.awt.Rectangle(x, y, side, side));
    }

    public abstract void paint(Graphics2D g);

    public void setPosition(Point3D position) {
        this.position = position;
    }

    public Point3D getPosition() {
        return this.position;
    }

    private Color darken(Color c, int z){

        for(int i = 0; i < z; i++){
            c = c.darker();
        }

        return c;

    }

    public TexturePaint darkenTexturePaint(TexturePaint texture, int floor) {
        BufferedImage img = texture.getImage();
        BufferedImage darkImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                Color color = new Color(img.getRGB(i, j), true);
                for (int k = 0; k < floor; k++) {
                    color = color.darker();
                }
                darkImg.setRGB(i, j, color.getRGB());
            }
        }
        return new TexturePaint(darkImg, texture.getAnchorRect());
    }
}
