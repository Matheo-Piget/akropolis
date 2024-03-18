package model;

import java.util.ArrayList;

public class Site extends Model {
    private ArrayList<Tile> tiles;

    public Site() {
        tiles = new ArrayList<Tile>();
    }

    /**
     * This method is used to update the site with the tiles from the stack
     * @param stackTiles
     * @param numberToDraw
     */
    public void updateSite(StackTiles stackTiles, int numberToDraw) {
        System.out.println("updateSite");
        for (int i = tiles.size(); i < numberToDraw; i++) {
            if (!stackTiles.isEmpty()) {
                tiles.add(stackTiles.pop());
            }
        }
        firePropertyChange("tileUpdated", null, tiles);
    }

    public int calculateCost(Tile tile) {
        int cost = 0;
        for (Tile t : tiles) {
            if (t != tile) {
                cost++;
            }
        }
        return cost;
    }

    public ArrayList<Tile> getTiles() {
        return tiles;
    }
}
