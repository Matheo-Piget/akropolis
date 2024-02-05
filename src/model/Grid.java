package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.Point;
/**
 * Represents the grid of the game board, containing tiles in different
 * positions.
 */
public class Grid {
    // Map to store tiles based on their positions
    private Map<Point, Tile> tiles;

    /**
     * Constructor to initialize the grid and add the starting tiles at the
     * beginning of the game.
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
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, -1 } };
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

    /**
     * Return whether the tile is surrounded by other tiles or not
     * 
     * @param tile The tile to check if it is surrounded
     */
    private boolean tileIsSurrounded(Tile tile) {
        if (tile.getNeighbors().size() == 6) {
            return true;
        }
        return false;
    }

    /***
     * Clear the grid/board, for new game or otherwise
     */
    public void clearGrid() {

        tiles.clear();

    }

    /**
     * Calculate the score of each district
     */
    public int calculateScore() {
        // TODO : This method calculation is not correct, it should be fixed to calculate the score of each district
        // TODO : peut être séparé cette méthode en plusieurs méthode car elle risque d'être longue
        // it gives just an example of how to calculate the score
        int gardenScore = 0;
        int barrackScore = 0;
        int buildingScore = 0;
        ArrayList<Integer> buildingScores = new ArrayList<Integer>();
        int currentNumberOfBuilding = 0; // We count all the building
        int maxNumberOfBuilding = 0; // We only count the maximum adjacent building
        int maxBuildingScore = 0;
        int templeScore = 0;
        int marketScore = 0;

        int gardenMultiplier = 1;
        int barrackMultiplier = 1;
        int buildingMultiplier = 1;
        int templeMultiplier = 1;
        int marketMultiplier = 1;

        for (Tile tile : tiles.values()) {
            switch (tile.getType()) {
                case "Garden Place":
                    gardenMultiplier = ((Place) tile).getStars();
                    break;
                case "Barrack Place":
                    barrackMultiplier = ((Place) tile).getStars();
                    break;
                case "Building Place":
                    buildingMultiplier = ((Place) tile).getStars();
                    break;
                case "Temple Place":
                    templeMultiplier = ((Place) tile).getStars();
                    break;
                case "Market Place":
                    marketMultiplier = ((Place) tile).getStars();
                    break;
                
                case "Garden":
                    gardenScore += tile.getElevation(); // It should always increase the score
                    break;
                case "Barrack":
                    // We only increase the score if the barrack is in the border of the grid
                    if (tile.getNeighbors().size() < 6) {
                        barrackScore += tile.getElevation();
                    }
                    break;
                case "Building":
                    // We only increase the score if the building is next to another building
                    for (Tile neighbor : tile.getNeighbors()) {
                        if (neighbor.getType().equals("Building")) {
                            currentNumberOfBuilding++;
                            buildingScore += tile.getElevation();
                            maxNumberOfBuilding = Math.max(maxNumberOfBuilding, currentNumberOfBuilding);
                            maxBuildingScore = Math.max(maxBuildingScore, buildingScore);
                            break;
                        }
                    }
                    break;
                case "Temple":
                    // We only increase the score if the temple is surrounded by 6 tiles
                    if (tileIsSurrounded(tile)) {
                        templeScore++;
                    }
                    break;
                case "Market":
                    // We only increase the score if the market has no adjacent market
                    boolean hasMarket = false;
                    for (Tile neighbor : tile.getNeighbors()) {
                        if (neighbor.getType().equals("Market")) {
                            hasMarket = true;
                            break;
                        }
                    }
                    if (!hasMarket) {
                        marketScore++;
                    }
                    break;
            }
        }
        return gardenScore * gardenMultiplier +
                barrackScore * barrackMultiplier +
                maxBuildingScore * buildingMultiplier +
                templeScore * templeMultiplier +
                marketScore * marketMultiplier;
    }
}
