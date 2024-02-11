package model;

import java.awt.Point;
import java.util.*;

import util.Point3D;
import util.Tuple;

/**
 * Represents the grid of the game board, containing tiles in different
 * positions.
 */
public class Grid {
    // Map to store tiles based on their positions
    private Map<Point3D, Tile> tiles;

    /**
     * Constructor to initialize the grid and add the starting tiles at the
     * beginning of the game.
     */
    public Grid() {
        tiles = new HashMap<>();

        Random random = new Random();

        // Génération aléatoire des tuiles
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                Point3D position = new Point3D(x, y, random.nextInt(1,3)); // Toutes les tuiles ont une élévation de 1 par défaut
                Tile tile;
                int randomValue = random.nextInt(100); // Génère un nombre aléatoire entre 0 et 99

                // Utilisation du nombre aléatoire pour déterminer le type de tuile
                if (randomValue < 10) {
                    tile = new District(position, DistrictColor.RED);
                } else if (randomValue < 30) {
                    tile = new Place(position, 1, DistrictColor.BLUE, this);
                } else if (randomValue < 50) {
                    tile = new Quarrie(position, this);
                } else if (randomValue < 70) {
                    tile = new District(position, DistrictColor.GREEN);
                } else if (randomValue < 90) {
                    tile = new Place(position, 1, DistrictColor.YELLOW, this);
                } else {
                    tile = new District(position, DistrictColor.PURPLE);
                }
                tiles.put(position, tile);
            }
        }
    }

    public Map<Point3D, Tile> getTiles() {
        return tiles;
    }

    /**
     * Adds a tile to the grid at the specified position.
     *
     * @param tile The tile to be added to the grid.
     * @return True if the tile is successfully added, false otherwise.
     */

    // on regarde si l'elevation est de 1 alors faut que la tuille aie des voisisns
    // pour etre placer
    public boolean canAdd(Tile tile, Point3D p) {
        int x = tile.getX();
        int y = tile.getY();
        int z = tile.getZ();
        // Check if the tile has at least one neighbor in the grid
        // Define the directions for the 6 neighbors in a hexagonal grid
        Point[] axialDirections = {
            new Point(1, 0), new Point(1, -1), new Point(0, -1),
            new Point(-1, 0), new Point(-1, 1), new Point(0, 1)
        };
        for (Point direction : axialDirections) {
            Tile neighbor = tiles.get(new Point3D(x + direction.x, y + direction.y, z));
            if (neighbor != null) {
                return true;
            }
        }
        // If the tile has no neighbors in the grid, return false
        return false;
    }

    public boolean addTile(TileTrio tileTrio) {
        // Iterate over each tile in the TileTrio associated with the main tile.
        boolean canBePlaced = true;
        boolean hasNeighbor = false;
        Tile[] bellowTiles = new Tile[3];
        int sameTile = 0;
        for (int i = 0; i < 3; i++) {
            Tile newTile_i = tileTrio.getTile(i);
            // First, check if the tile can be placed we only need to verify 
            // one that at least one of the tile is placed next to another tile
            if (!tiles.containsKey(newTile_i.getPosition()) && !hasNeighbor) {
                // That means that this tile will not overlap with another tile
                hasNeighbor = canAdd(newTile_i, newTile_i.getPosition());
                System.out.println("Has neighbor the tile : " + newTile_i.getPosition() + " " + hasNeighbor);
            } else if (tiles.containsKey(newTile_i.getPosition())) {
                // Handle the case where the tile is elevated
                Tuple<Integer, Tile> result = handleElevation(newTile_i, tiles.get(newTile_i.getPosition()));
                sameTile += result.x;
                bellowTiles[i] = result.y;
                System.out.println("Same tile : " + sameTile);
                if(sameTile > 1){
                    canBePlaced = false;
                }
                hasNeighbor = true;
            }
        }
        // We need to verify that they are all at the same level
        int elevation = tileTrio.getTile(0).getElevation();
        for (int i = 1; i < 3; i++) {
            if (tileTrio.getTile(i).getElevation() != elevation) {
                canBePlaced = false;
            }
        }
        if (canBePlaced && hasNeighbor) {
            // We add each tile of the trio to the grid
            for (int i = 0; i < 3; i++) {
                Tile newTile_i = tileTrio.getTile(i);
                if(bellowTiles[i] != null){
                    newTile_i.setBelow(bellowTiles[i]);
                    bellowTiles[i].setAbove(newTile_i);
                }
                tiles.put(newTile_i.getPosition(), newTile_i);
                newTile_i.setGrid(this);
                System.out.println("Tile added at " + newTile_i.getPosition());
            }
        }
        display();
        return canBePlaced && hasNeighbor;
    }

    private Tuple<Integer,Tile> handleElevation(Tile new_tile, Tile existing_tile) {
        // TODO : Handle the case where we overlap a quarrie it should gives us a bonus
        // We need to get the correct z value by getting the tile above the current tile
        Tile topMostTile = existing_tile;
        while (topMostTile.hasAbove()) {
            topMostTile = topMostTile.getAbove();
        }
        new_tile.getPosition().z = topMostTile.getElevation() + 1;
        if (topMostTile.getType().equals(new_tile.getType())) {
            return new Tuple<Integer,Tile>(1, new_tile);
        }
        return new Tuple<Integer,Tile>(0, new_tile);
    }

    /**
     * Retrieves the topmost tile at the specified position in the grid.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     * @return The topmost tile at the specified position, or null if no tile
     *         exists.
     */
    public Tile getTile(int x, int y) {
        Point point = new Point(x, y);
        // Retrieve all tiles at the specified position
        Tile topMostTile = tiles.get(new Point3D(point, 1));
        if (topMostTile == null) {
            return null;
        }
        while (topMostTile.hasAbove()) {
            topMostTile = topMostTile.getAbove();
        }
        return topMostTile;
    }

    /**
     * Displays information about each tile in the grid.
     */
    public void display() {
        for (Tile tile : tiles.values()) {
            System.out.println(tile.getType() + " at " + tile.getPosition());
        }
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
    /*
     * public int calculateScore() {
     * // TODO : This method calculation is not correct, it should be fixed to
     * calculate the score of each district
     * // TODO : peut être séparé cette méthode en plusieurs méthode car elle risque
     * d'être longue
     * // it gives just an example of how to calculate the score
     * int gardenScore = 0;
     * int barrackScore = 0;
     * int buildingScore = 0;
     * ArrayList<Integer> buildingScores = new ArrayList<Integer>();
     * int currentNumberOfBuilding = 0; // We count all the building
     * int maxNumberOfBuilding = 0; // We only count the maximum adjacent building
     * int maxBuildingScore = 0;
     * int templeScore = 0;
     * int marketScore = 0;
     * 
     * int gardenMultiplier = 1;
     * int barrackMultiplier = 1;
     * int buildingMultiplier = 1;
     * int templeMultiplier = 1;
     * int marketMultiplier = 1;
     * 
     * for (Tile tile : tiles.values()) {
     * switch (tile.getType()) {
     * case "Garden Place":
     * gardenMultiplier = ((Place) tile).getStars();
     * break;
     * case "Barrack Place":
     * barrackMultiplier = ((Place) tile).getStars();
     * break;
     * case "Building Place":
     * buildingMultiplier = ((Place) tile).getStars();
     * break;
     * case "Temple Place":
     * templeMultiplier = ((Place) tile).getStars();
     * break;
     * case "Market Place":
     * marketMultiplier = ((Place) tile).getStars();
     * break;
     * 
     * case "Garden":
     * gardenScore += tile.getElevation(); // It should always increase the score
     * break;
     * case "Barrack":
     * // We only increase the score if the barrack is in the border of the grid
     * if (tile.getNeighbors().size() < 6) {
     * barrackScore += tile.getElevation();
     * }
     * break;
     * case "Building":
     * // We only increase the score if the building is next to another building
     * for (Tile neighbor : tile.getNeighbors()) {
     * if (neighbor.getType().equals("Building")) {
     * currentNumberOfBuilding++;
     * buildingScore += tile.getElevation();
     * maxNumberOfBuilding = Math.max(maxNumberOfBuilding, currentNumberOfBuilding);
     * maxBuildingScore = Math.max(maxBuildingScore, buildingScore);
     * break;
     * }
     * }
     * break;
     * case "Temple":
     * // We only increase the score if the temple is surrounded by 6 tiles
     * if (tileIsSurrounded(tile)) {
     * templeScore++;
     * }
     * break;
     * case "Market":
     * // We only increase the score if the market has no adjacent market
     * boolean hasMarket = false;
     * for (Tile neighbor : tile.getNeighbors()) {
     * if (neighbor.getType().equals("Market")) {
     * hasMarket = true;
     * break;
     * }
     * }
     * if (!hasMarket) {
     * marketScore++;
     * }
     * break;
     * }
     * }
     * return gardenScore * gardenMultiplier +
     * barrackScore * barrackMultiplier +
     * maxBuildingScore * buildingMultiplier +
     * templeScore * templeMultiplier +
     * marketScore * marketMultiplier;
     * }
     */

    public List<Tile> getTopTiles() {
        ArrayList<Tile> topTiles = new ArrayList<>();
        for (Tile tile : tiles.values()) {
            if (!tile.hasAbove()) {
                topTiles.add(tile);
            }
        }
        return topTiles;
    }

    public int calculateScore() {
        int totalScore = 0;

        for (Tile tile : getTopTiles()) {
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
