package model;

// Quarries are a type of tile that can be placed on the board that allow you to earn stones useful for buying tiles.
public class Quarrie extends Tile{

    public Quarrie(int x, int y) {
        super(x, y);
    }

    @Override
    public String getType() {
        return "Quarrie";
    }
}