package model;

/**
 * This class represents a trio of tiles.
 */
public class TileTrio {
    // An array to store the three tiles in the trio
    public Tile[] tiles = new Tile[3];

    /**
     * Constructor for creating a TileTrio with three specified tiles.
     *
     * @param tile1 The first tile in the trio.
     * @param tile2 The second tile in the trio.
     * @param tile3 The third tile in the trio.
     */
    public TileTrio(Tile tile1, Tile tile2, Tile tile3) {
        tiles[0] = tile1;
        tiles[1] = tile2;
        tiles[2] = tile3;
    }

    /**
     * Gets the tile at the specified index in the trio.
     *
     * @param i The index of the tile in the trio (0, 1, or 2).
     * @return The tile at the specified index.
     */
    public Tile getTile(int i) {
        return tiles[i];
    }

    /**
     * Sets the tile at the specified index in the trio.
     *
     * @param i    The index of the tile in the trio (0, 1, or 2).
     * @param tile The tile to set at the specified index.
     */
    public void setTile(int i, Tile tile) {
        tiles[i] = tile;
    }

    /**
     * Checks if the trio contains the specified tile.
     *
     * @param tile The tile to check for in the trio.
     * @return True if the trio contains the tile, false otherwise.
     */
    public boolean contains(Tile tile) {
        return tiles[0] == tile || tiles[1] == tile || tiles[2] == tile;
    }

    /**
     * Checks if the trio contains any tile from the specified trio.
     *
     * @param trio The TileTrio to check for tiles in the current trio.
     * @return True if the trio contains any tile from the specified trio, false otherwise.
     */
    public boolean contains(TileTrio trio) {
        return contains(trio.getTile(0)) || contains(trio.getTile(1)) || contains(trio.getTile(2));
    }

    /**
     * Rotates the trio by cyclically changing the position of the tiles.
     */
    public void rotate() {
        Tile temp = tiles[0];
        tiles[0] = tiles[1];
        tiles[1] = tiles[2];
        tiles[2] = temp;
    }

    /**
     * Exchanges two specified tiles within the trio.
     *
     * @param tile1 The first tile to be exchanged.
     * @param tile2 The second tile to be exchanged.
     */
    public void exchange(Tile tile1, Tile tile2) {
        for (int i = 0; i < 3; i++) {
            if (tiles[i] == tile1) {
                tiles[i] = tile2;
            } else if (tiles[i] == tile2) {
                tiles[i] = tile1;
            }
        }
    }
}
