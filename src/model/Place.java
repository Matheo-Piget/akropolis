package model;

import util.Point3D;

/**
 * Represents a place in the game.
 * A place is a hexagon with a number of stars and a color.
 */
public class Place extends Hexagon{
    private final int stars;
    private final DistrictColor color;

    /**
     * Constructor to create a place with specified coordinates.
     * @param x The x-coordinate of the place.
     * @param y The y-coordinate of the place.
     * @param stars The number of stars of the place.
     * @param color The color of the place.
     */
    public Place(int x, int y, int stars, DistrictColor color) {
        super(x, y);
        this.stars = stars;
        this.color = color;
    }

    /**
     * Constructor to create a place with specified coordinates using a Point3D.
     * @param position The position of the place.
     * @param stars The number of stars of the place.
     * @param color The color of the place.
     */
    public Place(Point3D position, int stars, DistrictColor color) {
        super(position);
        this.stars = stars;
        this.color = color;
    }

    /**
     * Returns the number of stars of the place.
     * @return the number of stars of the place.
     */
    public int getStars() {
        return stars;
    }

    /**
     * Returns the types of the place.
     * @return the types of the place.
     */
    @Override
    public String getType() {
        return switch (color) {
            case RED -> "Barrack Place";
            case BLUE -> "Building Place";
            case GREEN -> "Garden Place";
            case YELLOW -> "Market Place";
            case PURPLE -> "Temple Place";
        };
        // This should never happen
    }
}
