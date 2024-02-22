package model;

// Template for a board which will be used to play the game
public class Board {
    private Grid grid;
    private Player player;

    public Board() {
        grid = new Grid();
    }

    public Board(String playerName){
        grid = new Grid();
        player = new Player(playerName);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    // Only for the terminal version of the game to debug purposes
    public void display() {
        grid.display();
    }
    public Grid getGrid(){
        return this.grid;
    }

    // We can get a hexagon from the grid
    public Hexagon getHexagon(int x, int y) {
        return grid.getHexagon(x, y);
    }

    // We can add a tile only next to another tile if not we can't add it and we return false
    public boolean addTile(Tile tiles) {
        return grid.addTile(tiles, player);
    }

    public int getScore() {
        return grid.calculateScore();
    }

}
