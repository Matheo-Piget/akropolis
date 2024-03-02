package model;

import util.Point3D;

// A place is a tile that allow you to earn more score points.
public class Place extends Hexagon{
    private int stars;
    private DistrictColor color;

    public Place(Point3D position, int stars, DistrictColor color, Grid grid) {
        super(position, grid);
        this.stars = stars;
        this.color = color;
    }

    public Place(int x, int y, int stars, DistrictColor color) {
        super(x, y);
        this.stars = stars;
        this.color = color;
    }

    /**
     * Constructor to create a place with specified coordinates (used without grid reference).
     *
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
