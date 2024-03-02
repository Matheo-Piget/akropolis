package model;

import java.util.ArrayList;

public class Site extends Model {
    private ArrayList<Tile> tiles;

    public Site(int nbTiles) {
        tiles = new ArrayList<Tile>(nbTiles);
    }

    /**
     * This method is used to update the site with the tiles from the stack
     * @param stackTiles
     * @param numberToDraw
     */
    public void updateSite(StackTiles stackTiles, int numberToDraw) {
        for (int i = tiles.size(); i < numberToDraw; i++) {
            if (!stackTiles.isEmpty()) {
                System.out.println("Drawing a tile from the stack" + stackTiles.peek());
                tiles.add(stackTiles.pop());
            }
        }
        firePropertyChange("tileUpdated", null, tiles);
    }

    public void selectedTile(Tile tile) {
        // Infrom the board that a tile has been selected
        firePropertyChange("tileSelected", null, tile);
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}
