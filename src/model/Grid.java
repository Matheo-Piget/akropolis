package model;

import java.util.ArrayList;

public class Grid {
    private ArrayList<ArrayList<Tile>> tiles;

    public Grid() {
        tiles = new ArrayList<>();
    }

    public ArrayList<ArrayList<Tile>> getTiles() {
        return tiles;
    }

    public void addTile(Tile tile, int x, int y) {
        if (tiles.size() <= x) {
            for (int i = tiles.size(); i <= x; i++) {
                tiles.add(new ArrayList<>());
            }
        }
        if (tiles.get(x).size() <= y) {
            for (int i = tiles.get(x).size(); i <= y; i++) {
                tiles.get(x).add(null);
            }
        }
        tiles.get(x).set(y, tile);
    }

    // I haven't tested this method so I'm not sure if it works
    public void addTile(Tile tile) {
        if (tiles.size() <= tile.getX()) {
            for (int i = tiles.size(); i <= tile.getX(); i++) {
                tiles.add(new ArrayList<>());
            }
        }
        if (tiles.get(tile.getX()).size() <= tile.getY()) {
            for (int i = tiles.get(tile.getX()).size(); i <= tile.getY(); i++) {
                tiles.get(tile.getX()).add(null);
            }
        }
        tiles.get(tile.getX()).set(tile.getY(), tile);
    }

    public Tile getTile(int x, int y) {
        if (x < 0 || x >= tiles.size()) {
            return null;
        }
        if (y < 0 || y >= tiles.get(x).size()) {
            return null;
        }
        return tiles.get(x).get(y);
    }
}
