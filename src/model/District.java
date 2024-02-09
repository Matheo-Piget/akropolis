package model;

import util.Point3D;

// They are different districts in the game, each with its own color and its own rules
public class District extends Tile {
    private DistrictColor color;

    public District(Point3D p, DistrictColor color, Grid grid) {
        super(p, grid);
        this.color = color;
    }

    @Override
    public String getType() {
        return switch (color) {
            case RED -> "Barrack";
            case BLUE -> "Building";
            case GREEN -> "Garden";
            case YELLOW -> "Market";
            case PURPLE -> "Temple";
        };
        // This should never happen
    }
}
