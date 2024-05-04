package model;

public class Site extends Model {
    private Tile[] tiles;
    private final int capacity;

    public Site(int capacity) {
        tiles = new Tile[capacity];
        this.capacity = capacity;
    }

    /**
     * This method is used to update the site with the tiles from the stack
     * 
     * @param stackTiles The stack of tiles to update the site with
     */
    public void updateSite(StackTiles stackTiles) {
        // Firstly, we reorder the tiles in the site
        int numberOfTiles = reorderTiles();
        // Then we add the new tiles from the stack
        for (int i = numberOfTiles; i < capacity; i++) {
            if (!stackTiles.isEmpty()) {
                tiles[i] = stackTiles.pop();
            }
        }
        System.out.println(stackTiles.size());
        firePropertyChange("tileUpdated", null, tiles);
    }

    /**
     * This method is used to remove a tile from the site
     *
     * @param tile The tile to remove
     */
    public void removeTile(Tile tile) {
        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] == tile) {
                tiles[i] = null;
                break;
            }
        }
        reorderTiles();
        firePropertyChange("tileUpdated", null, tiles);
    }

    /**
     * This method is used to reorder the tiles in the site
     *
     * @return The number of tiles in the site
     */
    private int reorderTiles() {
        Tile[] newTiles = new Tile[capacity];
        int index = 0;
        for (Tile tile : tiles) {
            if (tile != null) {
                newTiles[index] = tile;
                index++;
            }
        }
        tiles = newTiles;
        return index;
    }

    /**
     * This method is used to check if the site is empty
     *
     * @return True if the site is empty, false otherwise
     */
    public boolean isEmpty() {
        for (Tile tile : tiles) {
            if (tile != null) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is used to get the cost of a tile in the site
     *
     * @param tile The tile to calculate the cost for
     * @return The cost of the tile
     */
    public int calculateCost(Tile tile) {
        int cost = 0;
        for (Tile t : tiles) {
            if (t != tile) {
                cost++;
            } else {
                break;
            }
        }
        return cost;
    }

    /**
     * This method is used to get the tiles in the site
     *
     * @return The tiles in the site
     */
    public Tile[] getTiles() {
        return tiles;
    }
}
