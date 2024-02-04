package model;

import java.util.HashMap;
import java.util.Map;
import java.awt.Point;

/**
 * Represents the grid of the game board, containing tiles in different positions.
 */
public class Grid {
    // Map to store tiles based on their positions
    private Map<Point, Tile> tiles;

    /**
     * Constructor to initialize the grid and add the starting tiles at the beginning of the game.
     */
    public Grid() {
        tiles = new HashMap<>();

        // Creating starting tiles
        StartingTile startingTile1 = new StartingTile(0, 0, this);
        StartingTile startingTile2 = new StartingTile(-1, 1, this);
        StartingTile startingTile3 = new StartingTile(1, 1, this);

        // Setting up the tile trio relationship
        startingTile1.setTileTrio(new TileTrio(startingTile1, startingTile2, startingTile3));
        startingTile2.setTileTrio(new TileTrio(startingTile1, startingTile2, startingTile3));
        startingTile3.setTileTrio(new TileTrio(startingTile1, startingTile2, startingTile3));

        // Adding starting tiles to the grid
        tiles.put(new Point(0, 0), startingTile1);
        tiles.put(new Point(-1, 1), startingTile2);
        tiles.put(new Point(1, 1), startingTile3);
    }

    /**
     * Adds a tile to the grid at the specified position.
     *
     * @param tile The tile to be added to the grid.
     * @return True if the tile is successfully added, false otherwise.
     */
    public boolean addTile(Tile tile) {
        int x = tile.getX();
        int y = tile.getY();
        Point newPoint = new Point(x, y);

        // Check if a tile with the same coordinates already exists
        if (tiles.containsKey(newPoint)) {
            Tile existingTile = tiles.get(newPoint);

            // Check if neither tile has a level above/below
            if (!existingTile.hasAbove() && !tile.hasBelow()) {
                // Set them as each other's levels
                existingTile.setAbove(tile);
                tile.setBelow(existingTile);
                tiles.put(newPoint, tile);
                return true;
            }
        }

        // Check if the tile has at least one neighbor in the grid
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}, {-1, 1}, {1, -1}};
        for (int[] direction : directions) {
            int nx = x + direction[0];
            int ny = y + direction[1];
            if (!tiles.containsKey(new Point(nx, ny))) {
                // Add the tile to the grid
                tiles.put(newPoint, tile);
                return true;
            }
        }

        // If the tile has no neighbors in the grid, return false
        return false;
    }

    /**
     * Retrieves the tile at the specified position in the grid.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return The tile at the specified position, or null if no tile exists.
     */
    public Tile getTile(int x, int y) {
        return tiles.get(new Point(x, y));
    }

    /**
     * Displays information about each tile in the grid.
     */
    public void display() {
        tiles.forEach((point, tile) -> {
            System.out.println("Tile: " + tile.getClass() + " " + tile.getX() + " " + tile.getY() + " ");
        });
    }
}
