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
