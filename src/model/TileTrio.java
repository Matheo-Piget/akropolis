package model;

// This class is used to represent the trio of tiles.
public class TileTrio {
    public Tile[] tiles = new Tile[3];

    public TileTrio(Tile tile1, Tile tile2, Tile tile3) {
        tiles[0] = tile1;
        tiles[1] = tile2;
        tiles[2] = tile3;
    }

    public Tile getTile(int i) {
        return tiles[i];
    }

    public void setTile(int i, Tile tile) {
        tiles[i] = tile;
    }

    public boolean contains(Tile tile) {
        return tiles[0] == tile || tiles[1] == tile || tiles[2] == tile;
    }

    public boolean contains(TileTrio trio) {
        return contains(trio.getTile(0)) || contains(trio.getTile(1)) || contains(trio.getTile(2));
    }

    public void rotate() {
        Tile temp = tiles[0];
        tiles[0] = tiles[1];
        tiles[1] = tiles[2];
        tiles[2] = temp;
    }

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
