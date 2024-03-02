package model;

import util.Point3D;

// They are different districts in the game, each with its own color and its own rules
public class District extends Hexagon {
    private DistrictColor color;

    public District(Point3D p, DistrictColor color) {
        super(p);
        this.color = color;
    }

    public District(int x, int y, DistrictColor color) {
        super(x, y);
        this.color = color;
    }

    /**
     * Returns the type of district.
     * @return the type of district.
     */
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
