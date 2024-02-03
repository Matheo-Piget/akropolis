package model;

// A place is a tile that allow you to earn more score points.
public class Place extends Tile{
    private int stars;

    public Place(int x, int y, int z, int stars, Grid grid) {
        super(x, y, grid);
        this.stars = stars;
    }
}
