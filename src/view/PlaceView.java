package view;

import java.awt.Color;
import java.awt.Graphics2D;
import model.Place;
import java.awt.Polygon;

/**
 * Represents a place on the game grid.
 */
public class PlaceView extends HexagonView {
    private int stars;

    public PlaceView(int x, int y, int side, Place place) {
        super(x, y, side);
        this.stars = place.getStars();
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

    private Polygon createStar(int x, int y, int radius, int innerRadius, int numPoints) {
        // Create a polygon to represent the star
        Polygon star = new Polygon();
    
        // Calculate the coordinates of the points of the star
        for (int i = 0; i < numPoints * 2; i++) {
            // Determine the radius for this point
            int currentRadius = (i % 2 == 0) ? radius : innerRadius;
    
            // Calculate the coordinates of the point
            double angle = 2 * Math.PI * i / (numPoints * 2);
            int xval = (int) (x + currentRadius * Math.cos(angle));
            int yval = (int) (y + currentRadius * Math.sin(angle));
    
            // Add the point to the star
            star.addPoint(xval, yval);
        }
    
        return star;
    }

    @Override
    public void paint(Graphics2D g) {
        // Set the paint to the texture
        g.setPaint(this.texture);
        // Fill the hexagon with the texture
        g.fillPolygon(this);
        // Draw the border of the hexagon
        g.setColor(Color.BLACK);
        g.setStroke(new java.awt.BasicStroke(3));
        g.draw(this);
    }
}
