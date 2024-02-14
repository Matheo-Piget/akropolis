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

        for (int i = 0; i <= 2; i++) {
            for (int j = -2; j <= 0; j++) {
                Tile tile = new Place(new Point3D(i, j, 1), 2, DistrictColor.BLUE, this);
                tiles.put(new Point3D(i, j, 1), tile);
                tiles.put(new Point3D(i, j, 2), new Place(new Point3D(i, j, 2), 2, DistrictColor.BLUE, this));
            }
        }

        //tiles.put(new Point3D(0, 0, 2), new Quarrie(new Point3D(0, 0, 2), this));
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
                break;
            }
        }
        if (canBePlaced && hasNeighbor) {
            // We add each tile of the trio to the grid
            for (int i = 0; i < 3; i++) {
                Tile newTile_i = tileTrio.getTile(i);
                newTile_i.setGrid(this);
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
    public Tile neighbor(Tile t , Point p){
        Point p2 = new Point(t.getX(),t.getY());
        return tiles.get(sommePos(p, p2));
    }
    public Point sommePos(Point p1 , Point p2){
        return new Point(p1.x+p2.x, p1.y+p2.y);
    }
    
    public static boolean tuileValide(TileTrio tileTrio){//verifier si les hexagones sont adjacent "coller"
        Tile t1 = tileTrio.getTile(0);
        Tile t2 = tileTrio.getTile(1);
        Tile t3 = tileTrio.getTile(2);
        boolean adjacent1_2 = t1.adjacent(t2);
        boolean adjacent1_3 = t1.adjacent(t3);
        boolean adjacent2_3 = t2.adjacent(t3);
        int z1 =t1.getZ();int z2=t2.getZ();int z3 = t3.getZ();
        System.out.println("z1: " + t1.getZ() + ", z2: " + t2.getZ() + ", z3: " + t3.getZ());
        return (z1 == z2)&&(z2==z3) && adjacent1_2&&adjacent1_3&& adjacent2_3;
    }
    /*  public void addTile(TileTrio tileTrio){
        boolean hasNeighbor;
        boolean isSpported  ; 
        int elevation = tileTrio.getTile(0).getZ() ;// verifier d'abord si au meme niveau avant d'ajouter
    
        for (int i = 1; i < 3; i++) {
            if (tileTrio.getTile(i).getZ()!= elevation) {
                return ;
            }
        }
        
        for (int i = 0; i < 3; i++) {
            Tile tileToadd = tileTrio.getTile(i);
            if (!tiles.containsKey(tileToadd.getPosition())) {
                hasNeighbor = canAdd(tileToadd);
            }
            else{
                isSpported = 
            }

        }
    }
    public boolean isSpported(Tile t ){
        
    }
    
    public boolean canAdd(Tile tile){
        Point [] axialDirection =  {new Point(0, 1), new Point(0, -1),new Point(-1, 1)
            ,new Point(1, 1),new Point(1, -1),new Point(-1, -1),};
        for (Point point : axialDirection) {
            Point3D tileneighber = new Point3D(tile.getX()+point.x, tile.getY()+point.y, 0);
            if (tiles.containsKey(tileneighber)) {
                return true;
            }
        }
        return false ;
    }*/

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
