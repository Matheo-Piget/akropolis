package model;

// The starting tile is three tiles that are placed at the beginning of the game.
public class StartingTile extends Tile{

    public StartingTile(int x, int y, Grid grid) {
        super(x, y, grid);
    }

    @Override
    public String getType() {
        return "StartingTile";
    }
}
