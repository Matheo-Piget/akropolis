package model;


// They are different districts in the game, each with its own color and its own rules
public class District extends Tile {
    private DistrictColor color;

    public District(int x, int y, DistrictColor color, Grid grid) {
        super(x, y, grid);
        this.color = color;
    }
}
