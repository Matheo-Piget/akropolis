package model;

// They are different districts in the game, each with its own color and its own rules
public class District extends Tile {
    private DistrictColor color;

    public District(int x, int y, DistrictColor color) {
        super(x, y);
        this.color = color;
    }

    @Override
    public String getType() {
        switch (color) {
            case RED:
                return "Barrack";
            case BLUE:
                return "Building";
            case GREEN:
                return "Garden";
            case YELLOW:
                return "Market";
            case PURPLE:
                return "Temple";
        }
        // This should never happen
        return null;
    }
}
