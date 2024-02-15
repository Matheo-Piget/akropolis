package model;

import java.awt.Point;
import java.util.*;
import util.Point3D;

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
        // Creating starting tiles
        Point3D p1 = new Point3D(0, 0, 1);
        Point3D p2 = new Point3D(0, 1, 1);
        Point3D p3 = new Point3D(-1, -1, 1);
        Point3D p4 = new Point3D(1, 0, 1);
        Tile tile1 = new Place(p1, 1, DistrictColor.BLUE, this);
        Tile tile2 = new Quarrie(p2, this);
        Tile tile3 = new Quarrie(p3, this);
        Tile tile4 = new Quarrie(p4, this);
        tiles.put(p1, tile1);
        tiles.put(p2, tile2);
        tiles.put(p3, tile3);
        tiles.put(p4, tile4);
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
        Tile[] bellowTiles = new Tile[3];
        boolean hasNeighbor = checkNeighborsAndSetBelowTiles(tileTrio, bellowTiles);
        boolean canBePlaced = checkElevation(tileTrio);
        int sameTile = countSameTiles(tileTrio, bellowTiles);

        if (canBePlaced && hasNeighbor && sameTile <= 1) {
            addTilesToGrid(tileTrio, bellowTiles);
        }

        display();
        System.out.println("canBePlaced: " + canBePlaced + ", hasNeighbor: " + hasNeighbor + ", sameTile: " + sameTile);
        return canBePlaced && hasNeighbor && sameTile <= 1;
    }

    private boolean checkNeighborsAndSetBelowTiles(TileTrio tileTrio, Tile[] bellowTiles) {
        boolean hasNeighbor = false;
        for (int i = 0; i < 3; i++) {
            Tile newTile_i = tileTrio.getTile(i);
            if (!tiles.containsKey(newTile_i.getPosition()) && !hasNeighbor) {
                hasNeighbor = hasNeighbor || canAdd(newTile_i, newTile_i.getPosition());
            } else if (tiles.containsKey(newTile_i.getPosition())) {
                Tile topMostTile = getTile(newTile_i.getX(), newTile_i.getY());
                if (topMostTile != null) {
                    bellowTiles[i] = topMostTile;
                }
                newTile_i.getPosition().z = topMostTile.getZ() + 1;
                hasNeighbor = true;
            }
        }
        return hasNeighbor;
    }

    private void addTilesToGrid(TileTrio tileTrio, Tile[] bellowTiles) {
        for (int i = 0; i < 3; i++) {
            Tile newTile_i = tileTrio.getTile(i);
            newTile_i.setGrid(this);
            newTile_i.setTileTrio(tileTrio);
            if (bellowTiles[i] != null) {
                newTile_i.setBelow(bellowTiles[i]);
                bellowTiles[i].setAbove(newTile_i);
            }
            tiles.put(newTile_i.getPosition(), newTile_i);
        }
    }

    private boolean checkElevation(TileTrio tileTrio) {
        int elevation = tileTrio.getTile(0).getElevation();
        for (int i = 1; i < 3; i++) {
            if (tileTrio.getTile(i).getElevation() != elevation) {
                return false;
            }
        }
        return true;
    }

    private int countSameTiles(TileTrio tileTrio, Tile[] bellowTiles) {
        int sameTile = 0;
        for (int i = 0; i < 3; i++) {
            Tile newTile_i = tileTrio.getTile(i);
            if (bellowTiles[i] != null && bellowTiles[i].getType().equals(newTile_i.getType())) {
                sameTile++;
            }
        }
        return sameTile;
    }

    public Tile neighbor(Tile t, Point p) {
        Point p2 = new Point(t.getX(), t.getY());
        return tiles.get(sommePos(p, p2));
    }

    public Point sommePos(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
    }

    public static boolean tuileValide(TileTrio tileTrio) {// verifier si les hexagones sont adjacent "coller"
        Tile t1 = tileTrio.getTile(0);
        Tile t2 = tileTrio.getTile(1);
        Tile t3 = tileTrio.getTile(2);
        boolean adjacent1_2 = t1.isAdjacent(t2);
        boolean adjacent1_3 = t1.isAdjacent(t3);
        boolean adjacent2_3 = t2.isAdjacent(t3);
        int z1 = t1.getZ();
        int z2 = t2.getZ();
        int z3 = t3.getZ();
        System.out.println("z1: " + t1.getZ() + ", z2: " + t2.getZ() + ", z3: " + t3.getZ());
        return (z1 == z2) && (z2 == z3) && adjacent1_2 && adjacent1_3 && adjacent2_3;
    }
    /*
     * public void addTile(TileTrio tileTrio){
     * boolean hasNeighbor;
     * boolean isSpported ;
     * int elevation = tileTrio.getTile(0).getZ() ;// verifier d'abord si au meme
     * niveau avant d'ajouter
     * 
     * for (int i = 1; i < 3; i++) {
     * if (tileTrio.getTile(i).getZ()!= elevation) {
     * return ;
     * }
     * }
     * 
     * for (int i = 0; i < 3; i++) {
     * Tile tileToadd = tileTrio.getTile(i);
     * if (!tiles.containsKey(tileToadd.getPosition())) {
     * hasNeighbor = canAdd(tileToadd);
     * }
     * else{
     * isSpported =
     * }
     * 
     * }
     * }
     * public boolean isSpported(Tile t ){
     * 
     * }
     * 
     * public boolean canAdd(Tile tile){
     * Point [] axialDirection = {new Point(0, 1), new Point(0, -1),new Point(-1, 1)
     * ,new Point(1, 1),new Point(1, -1),new Point(-1, -1),};
     * for (Point point : axialDirection) {
     * Point3D tileneighber = new Point3D(tile.getX()+point.x, tile.getY()+point.y,
     * 0);
     * if (tiles.containsKey(tileneighber)) {
     * return true;
     * }
     * }
     * return false ;
     * }
     */

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
        for (Map.Entry<Point3D, Tile> entry : tiles.entrySet()) {
            Point3D point = entry.getKey();
            Tile tile = entry.getValue();
            System.out.println("Point: " + point + ", Tile: " + tile);
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

    public ArrayList<Tile> getTopTiles() {
        ArrayList<Tile> topTiles = new ArrayList<>();
        for (Tile tile : tiles.values()) {
            if (!tile.hasAbove() && topTiles.contains(tile) == false) {
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
