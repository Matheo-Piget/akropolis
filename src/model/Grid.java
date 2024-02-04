package model;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

public class Grid {
    private Map<Point, Tile> tiles;

    public Grid() {
        tiles = new HashMap<>();
        StartingTile startingTile1 = new StartingTile(0, 0, this);
        StartingTile startingTile2 = new StartingTile(-1, 1, this);
        StartingTile startingTile3 = new StartingTile(1, 1, this);
        startingTile1.setTileTrio(new TileTrio(startingTile1, startingTile2, startingTile3));
        startingTile2.setTileTrio(new TileTrio(startingTile1, startingTile2, startingTile3));
        startingTile3.setTileTrio(new TileTrio(startingTile1, startingTile2, startingTile3));
        tiles.put(new Point(0, 0), startingTile1);
        tiles.put(new Point(-1, 1), startingTile2);
        tiles.put(new Point(1, 1), startingTile3);
    }

    public boolean addTile(Tile tile) {
        int x = tile.getX();
        int y = tile.getY();
        Point newPoint = new Point(x, y);
    
        // Check if a tile with the same coordinates already exists
        if (tiles.containsKey(newPoint)) {
            return false;
        }
    
        // Check if the tile has at least one neighbor in the grid
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        for (int[] direction : directions) {
            int nx = x + direction[0];
            int ny = y + direction[1];
            if (tiles.containsKey(new Point(nx, ny))) {
                // Add the tile to the grid
                tiles.put(newPoint, tile);
                return true;
            }
        }
    
        // If the tile has no neighbors in the grid, return false
        return false;
    }

    public Tile getTile(int x, int y) {
        return tiles.get(new Point(x, y));
    }

    public void display() {
        tiles.forEach((point, tile) -> {
            System.out.println("Tile: " + tile.getClass() + " " + tile.getX() + " " + tile.getY() + " ");
        });
    }
}
