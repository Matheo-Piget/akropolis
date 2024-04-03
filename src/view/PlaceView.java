package view;

import java.awt.*;

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

        // Dessiner des étoiles en fonction du nombre d'étoiles de la place
        int stars = place.getStars();
        int starSize = 10; // Taille des étoiles

        // Position de départ pour dessiner les étoiles
        int startX = getSize().width / 2 - (starSize * stars) / 2;
        int startY = getSize().height / 2 - starSize / 2;

        // Dessiner chaque étoile
        for (int i = 0; i < stars; i++) {
            // Dessiner une étoile à la position actuelle
            drawStar(g, startX + i * starSize, startY, starSize);
        }
    }

    /**
     * Dessine une étoile à la position spécifiée.
     * @param g L'objet Graphics pour dessiner.
     * @param x La coordonnée x de l'étoile.
     * @param y La coordonnée y de l'étoile.
     * @param size La taille de l'étoile.
     */
    private void drawStar(Graphics g, int x, int y, int size) {
        int xPoints[] = {x + size / 2, x + (int)(0.6 * size), x + size, x + (int)(0.7 * size), x + (int)(0.8 * size),
                x + size / 2, x + (int)(0.2 * size), x + (int)(0.3 * size), x};
        int yPoints[] = {y, y + (int)(0.4 * size), y + (int)(0.4 * size), y + (int)(0.6 * size), y + size,
                y + (int)(0.8 * size), y + size, y + (int)(0.6 * size), y + (int)(0.6 * size)};
        int nPoints = xPoints.length;

        // Dessiner l'étoile
        g.setColor(Color.BLACK); // Couleur de l'étoile
        g.fillPolygon(xPoints, yPoints, nPoints);
    }
}
