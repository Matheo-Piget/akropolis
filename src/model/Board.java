package model;

// Template for a board which will be used to play the game
public class Board {
    private Grid grid;

    public Board() {
        grid = new Grid();
    }

    public void addTile(Tile tile, int x, int y) {
        grid.addTile(tile);
    }

}
