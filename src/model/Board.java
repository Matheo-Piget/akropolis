package model;

// Template for a board which will be used to play the game
public class Board {
    private Grid grid;

    public Board() {
        grid = new Grid();
    }

    // Only for the terminal version of the game to debug purposes
    public void display() {
        grid.display();
    }

    // We can get a tile from the grid
    public Tile getTile(int x, int y) {
        return grid.getTile(x, y);
    }

    // We can add a tile only next to another tile if not we can't add it and we return false
    public boolean addTile(Tile tile) {
        return grid.addTile(tile);
    }

}
