package model;

// A place is a tile that allow you to earn more score points.
public class Place extends Tile{
    private int stars;
    private DistrictColor color;

    public Place(int x, int y, int z, int stars, Grid grid) {
        super(x, y, grid);
        this.stars = stars;
    }

    public int getStars() {
        return stars;
    }

    @Override
    public String getType() {
        switch (color) {
            case RED:
                return "Barrack Place";
            case BLUE:
                return "Building Place";
            case GREEN:
                return "Garden Place";
            case YELLOW:
                return "Market Place";
            case PURPLE:
                return "Temple Place";
        }
        // This should never happen
        return null;
    }
}
