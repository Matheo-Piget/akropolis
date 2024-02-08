package model;

import java.util.ArrayList;
import java.util.List;
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
        StartingTile startingTile1 = new StartingTile(1, 0, this);
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

     // on regarde si l'elevation est de 1 alors faut que la tuille aie des voisisns pour etre placer 
    public boolean canAdd(Tile tile , Point p){
        int x = tile.getX();
        int y = tile.getY();
        // Check if the tile has at least one neighbor in the grid
        int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 }, { -1, 1 }, { 1, -1 } };
        for (int[] direction : directions) {
            int nx = x + direction[0];
            int ny = y + direction[1];
            if (tiles.containsKey(new Point(nx, ny))) {
                return true;
            }
        }
    
        // If the tile has no neighbors in the grid, return false
        return false;
    }
    
    public boolean addTile(Tile tile){
        // Iterate over each tile in the TileTrio associated with the main tile.
        for (int i = 0; i < 3; i++) {
            Tile newTile_i = tile.getTileTrio().getTile(i);
            boolean canBePlaced;
        // First, check if the tile can be placed (using canAdd for ground-level tiles or isSupported for higher elevation).
            if (newTile_i.getElevation()==1) {
                canBePlaced = canAdd(newTile_i, newTile_i.getPosition());
            }
            else{
                canBePlaced = isSupported(newTile_i);
            }
            if (canBePlaced) {
                Tile existingTile = tiles.get(tile.getPosition());
                if (existingTile!=null) {
                    adjustTileElevation(tile, existingTile);// manage L'elevation 
                }
                
                //Add the tile to the grid
                tiles.put(newTile_i.getPosition(), newTile_i);
                return true;
            }
            
        }
        return false;
    }

    private void adjustTileElevation(Tile newTile , Tile existingTile){
        while (existingTile.getAbove()!=null) {// get the top existingTile ;
            existingTile = existingTile.getAbove(); 
        }
        existingTile.setAbove(newTile);
        newTile.setBelow(existingTile);
    
    }
    private boolean isSupported(Tile tile) {
        // Implement logic to check if the tile is supported by two different tiles at its elevation.
        return true; 
    }

    /**
     * Retrieves the topmost tile at the specified position in the grid.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return The topmost tile at the specified position, or null if no tile exists.
     */
    public Tile getTile(int x, int y) {
        Point point = new Point(x, y);

        // Retrieve all tiles at the specified position
        List<Tile> tilesAtPosition = new ArrayList<>();
        for (Map.Entry<Point, Tile> entry : tiles.entrySet()) {
            if (entry.getKey().equals(point)) {
                tilesAtPosition.add(entry.getValue());
            }
        }

        // Find the topmost tile among the tiles at the specified position
        Tile topmostTile = null;
        for (Tile tile : tilesAtPosition) {
            if (!tile.hasAbove()) {
                // If no tile is currently marked as the topmost, or if this tile is higher, set it as the topmost tile
                if (topmostTile == null || tile.getElevation() > topmostTile.getElevation()) {
                    topmostTile = tile;
                }
            }
        }

        return topmostTile;
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
        return tile.getNeighbors().size() == 6;
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
    /*public int calculateScore() {
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
    }*/

    public int calculateScore() {
        int totalScore = 0;

        for (Tile tile : tiles.values()) {
            switch (tile.getType()) {
                case "Garden":
                    totalScore += calculateGardenScore(tile);
                    break;
                case "Barrack":
                    totalScore += calculateBarrackScore(tile);
                    break;
                case "Building":
                    totalScore += calculateBuildingScore(tile);
                    break;
                case "Temple":
                    totalScore += calculateTempleScore(tile);
                    break;
                case "Market":
                    totalScore += calculateMarketScore(tile);
                    break;
                default:
                    totalScore += calculatePlaceScore((Place) tile);
                    break;
            }
        }

        return totalScore;
    }

    private int calculateGardenScore(Tile tile) {
        return tile.getElevation();
    }

    private int calculateBarrackScore(Tile tile) {
        return (tile.getNeighbors().size() < 6) ? tile.getElevation() : 0;
    }

    private int calculateBuildingScore(Tile tile) {
        int adjacentBuildingScore = 0;
        for (Tile neighbor : tile.getNeighbors()) {
            if (neighbor.getType().equals("Building")) {
                adjacentBuildingScore = Math.max(adjacentBuildingScore, neighbor.getElevation());
            }
        }
        return adjacentBuildingScore;
    }

    private int calculateTempleScore(Tile tile) {
        return tileIsSurrounded(tile) ? 1 : 0;
    }

    private int calculateMarketScore(Tile tile) {
        for (Tile neighbor : tile.getNeighbors()) {
            if (neighbor.getType().equals("Market")) {
                return 0;
            }
        }
        return 1;
    }

    private int calculatePlaceScore(Place place) {
        return place.getStars() * place.getElevation();
    }
}
