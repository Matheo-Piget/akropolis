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

    public boolean isEmpty() {
        for (Tile tile : tiles) {
            if (tile != null) {
                return false;
            }
        }
        return true;
    }

    public int calculateCost(Tile tile) {
        int cost = 0;
        for (Tile t : tiles) {
            if (t != tile) {
                cost++;
            }
            else {
                break;
            }
        }
        return cost;
    }

    public Tile[] getTiles() {
        return tiles;
    }
}
