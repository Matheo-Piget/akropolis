package model;

import util.Point3D;

// A place is a tile that allow you to earn more score points.
public class Place extends Tile{
    private int stars;
    private DistrictColor color;

    public Place(Point3D position, int stars, DistrictColor color, Grid grid) {
        super(position, grid);
        this.stars = stars;
        this.color = color;
    }

    public int getStars() {
        return stars;
    }

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
