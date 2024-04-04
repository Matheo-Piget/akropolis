package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import model.Place;

/**
 * Represents a place on the game grid.
 */
public class PlaceView extends HexagonView {
    private final Place place;
    private static final Color strokeColor = Color.BLACK;

    public PlaceView(int x, int y, int z, Place place, int size) {
        super(x, y, z, size);
        this.place = place;
        switch (place.getType()) {
            case "Barrack Place":
                this.texture = TextureFactory.getTexture("barrack");
                break;
            case "Building Place":
                this.texture = TextureFactory.getTexture("building");
                break;
            case "Garden Place":
                this.texture = TextureFactory.getTexture("garden");
                break;
            case "Market Place":
                this.texture = TextureFactory.getTexture("market");
                break;
            case "Temple Place":
                this.texture = TextureFactory.getTexture("temple");
                break;
            default:
                break;
        }
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public void paintComponent(java.awt.Graphics g) {
        super.paintComponent(g);
        if (render == null) {
            renderHexagon(strokeColor, texture);
        }
        g.drawImage(render, 0, 0, null);

        int stars = place.getStars();
        int starSize = 15; // Height of the star

        // Position of the first star
        int startX = getSize().width / 2 - (starSize * stars) / 2;
        int startY = getSize().height / 2 - starSize / 2;

        // Draw the stars
        for (int i = 0; i < stars; i++) {
            // Dessiner une étoile à la position actuelle
            drawStar(g, startX + i * starSize, startY, starSize);
        }
    }

    /**
     * Draws a star at the given position with the given size.
     * @param g the graphics context
     * @param x the x coordinate of the star
     * @param y the y coordinate of the star
     * @param size the size of the star
     */
    private void drawStar(Graphics g, int x, int y, int size) {
        int[] xPoints = {x + size / 2, x + (int)(0.6 * size), x + size, x + (int)(0.7 * size), x + (int)(0.8 * size),
                x + size / 2, x + (int)(0.2 * size), x + (int)(0.3 * size), x, x + (int)(0.4 * size)};
        int[] yPoints = {y, y + (int)(0.4 * size), y + (int)(0.4 * size), y + (int)(0.6 * size), y + size,
                y + (int)(0.8 * size), y + size, y + (int)(0.6 * size), y + (int)(0.4 * size), y + (int)(0.4 * size)};
        int nPoints = xPoints.length;


        // Convert to graphics2D
        Graphics2D g2d = (Graphics2D) g;
        // Antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(Color.BLACK); // Couleur de l'étoile
        g2d.fillPolygon(xPoints, yPoints, nPoints);
        // Free the resources
        g2d.dispose();
    }
}
