package model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import util.Point3D;

/**
 * Represents the grid of the game board, containing hexagons in different
 * positions.
 */
public class Grid extends Model{
    // Map to store hexagons based on their positions
    private final Map<Point3D, Hexagon> hexagons;
    private final Player player;

    /**
     * Constructor to initialize the grid and add the starting hexagons at the
     * beginning of the game.
     */
    public Grid(Player player) {
        hexagons = new HashMap<>();
        // Creating starting hexagons
        Point3D p1 = new Point3D(0, 0);
        Point3D p2 = new Point3D(-1, 1);
        Point3D p3 = new Point3D(0, -1);
        Point3D p4 = new Point3D(1, 0);
        Hexagon hexagon1 = new Place(p1, 1, DistrictColor.BLUE, this);
        Hexagon hexagon2 = new Quarries(p2, this);
        Hexagon hexagon3 = new Quarries(p3, this);
        Hexagon hexagon4 = new Quarries(p4, this);
        hexagons.put(p1, hexagon1);
        hexagons.put(p2, hexagon2);
        hexagons.put(p3, hexagon3);
        hexagons.put(p4, hexagon4);
        this.player = player;
    }

    /**
     * Just to notify the view that the grid has been initialized
     */
    public void gridInitialized() {
        // Iterate over the hexagons and notify the view
        for (Hexagon hexagon : hexagons.values()) {
            propertyChangeSupport.firePropertyChange("hexagonAdded", null, hexagon);
        }
    }


    public Map<Point3D, Hexagon> getHexagons() {
        return hexagons;
    }

    /**
     * Adds a hexagon to the grid at the specified position.
     *
     * @param hexagon The hexagon to be added to the grid.
     * @return True if the hexagon is successfully added, false otherwise.
     */
    public boolean canAdd(Hexagon hexagon) {
        int x = hexagon.getX();
        int y = hexagon.getY();
        int z = hexagon.getZ();
        // Check if the hexagon has at least one neighbor in the grid
        // Define the directions for the 6 neighbors in a hexagonal grid
        Point[] axialDirections = {
                new Point(1, 0), new Point(1, -1), new Point(0, -1),
                new Point(-1, 0), new Point(-1, 1), new Point(0, 1)
        };
        for (Point direction : axialDirections) {
            Hexagon neighbor = hexagons.get(new Point3D(x + direction.x, y + direction.y, z));
            if (neighbor != null) {
                return true;
            }
        }
        // If the hexagon has no neighbors in the grid, return false
        return false;
    }

    /**
     * Adds a tile to the grid.
     *
     * @param tile The tile to be added to the grid.
     * @return True if the tile is successfully added, false otherwise.
     */
    public boolean addTile(Tile tile) {
        Hexagon[] bellowHexagons = new Hexagon[3];
        // Check if the tile can be placed on the grid
        boolean hasNeighbor = checkNeighborsAndSetBelowTile(tile, bellowHexagons);
        boolean canBePlaced = checkElevation(tile);
        int sameHexagon = countSameHexagons(tile, bellowHexagons);
        // If the tile can be placed, add it to the grid
        if (canBePlaced && hasNeighbor && sameHexagon <= 1) {
            addHexagonsToGrid(tile, bellowHexagons);
        }
        // If the tile has a quarries below, give the player a resource
        if (canBePlaced && hasNeighbor && sameHexagon <= 1){
            for (Hexagon hexagon : tile.hexagons) {
                if(hexagon.getBelow() instanceof Quarries){
                    player.setResources(player.getResources()+1);
                }
            }
        }
        display();
        System.out.println("canBePlaced: " + canBePlaced + ", hasNeighbor: " + hasNeighbor + ", sameHexagon: " + sameHexagon);
        return canBePlaced && hasNeighbor && sameHexagon <= 1;
    }

    /**
     * Checks if the tile can be placed on the grid and sets the below hexagons for
     * the tile.
     *
     * @param tile          The tile to be placed on the grid.
     * @param bellowHexagons The array to store the below hexagons for the tile.
     * @return True if the tile can be placed, false otherwise.
     */
    private boolean checkNeighborsAndSetBelowTile(Tile tile, Hexagon[] bellowHexagons) {
        boolean hasNeighbor = false;
        for (int i = 0; i < 3; i++) {
            Hexagon newHexagon_i = tile.hexagons.get(i);
            if (!hexagons.containsKey(newHexagon_i.getPosition()) && !hasNeighbor) {
                hasNeighbor = canAdd(newHexagon_i);
            } else if (hexagons.containsKey(newHexagon_i.getPosition())) {
                Hexagon topMosthexagon = getHexagon(newHexagon_i.getX(), newHexagon_i.getY());
                if (topMosthexagon != null) {
                    bellowHexagons[i] = topMosthexagon;
                }
                assert topMosthexagon != null;
                newHexagon_i.getPosition().z = topMosthexagon.getZ() + 1;
                System.out.println("New position: " + newHexagon_i.getPosition());
                hasNeighbor = true;
            }
        }
        return hasNeighbor;
    }

    /**
     * Adds the hexagons of the tile to the grid and sets the below hexagons for the
     * tile.
     *
     * @param tile          The tile to be placed on the grid.
     * @param bellowHexagons The array of below hexagons for the tile.
     */
    private void addHexagonsToGrid(Tile tile, Hexagon[] bellowHexagons) {
        for (int i = 0; i < 3; i++) {
            Hexagon newHexagon_i = tile.hexagons.get(i);
            newHexagon_i.setGrid(this);
            newHexagon_i.setTile(tile);
            if (bellowHexagons[i] != null) {
                newHexagon_i.setBelow(bellowHexagons[i]);
            }
            hexagons.put(newHexagon_i.getPosition(), newHexagon_i);
            // Notify the view that a hexagon has been added
            propertyChangeSupport.firePropertyChange("hexagonAdded", null, newHexagon_i);
        }
    }

    /**
     * Checks if the tile has the same elevation for all its hexagons.
     *
     * @param t The tile to be checked.
     * @return True if the tile has the same elevation for all its hexagons, false
     *         otherwise.
     */
    private boolean checkElevation(Tile t) {
        int elevation = t.hexagons.getFirst().getZ();
        for (int i = 1; i < 3; i++) {
            if (t.hexagons.get(i).getZ() != elevation) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts the number of hexagons in the tile that are the same as the below
     * hexagons.
     *
     * @param tile          The tile to be checked.
     * @param bellowHexagons The array of below hexagons for the tile.
     * @return The number of hexagons in the tile that are the same as the below
     *         hexagons.
     */
    private int countSameHexagons(Tile tile, Hexagon[] bellowHexagons) {
        int sameHexagon = 0;
        for (int i = 0; i < 3; i++) {
            Hexagon newHexagon_i = tile.hexagons.get(i);
            if (bellowHexagons[i] != null && bellowHexagons[i].getType().equals(newHexagon_i.getType())) {
                sameHexagon++;
            }
        }
        return sameHexagon;
    }
    public static boolean isAValidTile(Tile t) {
        Hexagon h1 = t.hexagons.get(0);
        Hexagon h2 = t.hexagons.get(1);
        Hexagon h3 = t.hexagons.get(2);
        boolean adjacent1_2 = h1.isAdjacent(h2);
        boolean adjacent1_3 = h1.isAdjacent(h3);
        boolean adjacent2_3 = h2.isAdjacent(h3);
        int z1 = h1.getZ();
        int z2 = h2.getZ();
        int z3 = h3.getZ();
        System.out.println("z1: " + h1.getZ() + ", z2: " + h2.getZ() + ", z3: " + h3.getZ());
        return (z1 == z2) && (z2 == z3) && adjacent1_2 && adjacent1_3 && adjacent2_3;
    }

    /**
     * Retrieves the topmost hexagon at the specified position in the grid.
     *
     * @param x The x-coordinate of the hexagon.
     * @param y The y-coordinate of the hexagon.
     * @return The topmost hexagon at the specified position, or null if no hexagon
     *         exists.
     */
    public Hexagon getHexagon(int x, int y) {
        // Retrieve all hexagons at the specified position
        Point3D p = new Point3D(x, y);
        Hexagon topMosthexagon = null;
        // While we can retrieve from the hash map, keep going up
        while (hexagons.containsKey(p)){
            topMosthexagon = hexagons.get(p);
            p = new Point3D(topMosthexagon.getX(), topMosthexagon.getY(), p.z + 1);
        } 
        return topMosthexagon;
    }

    /**
     * Displays information about each hexagon in the grid.
     */
    public void display() {
        for (Map.Entry<Point3D, Hexagon> entry : hexagons.entrySet()) {
            Point3D point = entry.getKey();
            Hexagon hexagon = entry.getValue();
            System.out.println("Point: " + point + ", hexagon: " + hexagon);
        }
    }

    /**
     * Return whether the hexagon is surrounded by other hexagons or not
     * 
     * @param hexagon The hexagon to check if it is surrounded
     */
    private boolean hexagonIsSurrounded(Hexagon hexagon) {
        return hexagon.getNeighbors().size() == 6;
    }

    /***
     * Clear the grid/board, for new game or otherwise
     */
    public void clearGrid() {
        hexagons.clear();
    }

    /**
     * Get the top hexagons of the grid
     * @return the top hexagons of the grid
     */
     public ArrayList<Hexagon> getTopHexagons() {
        ArrayList<Hexagon> topHexagons = new ArrayList<>();
        for (Hexagon hexagon : hexagons.values()) {
            Point3D p = new Point3D(hexagon.getX(), hexagon.getY(), hexagon.getZ());
            while (hexagons.containsKey(p)) {
                p = new Point3D(hexagon.getX(), hexagon.getY(), p.z + 1);
            }
            Hexagon he = hexagons.get(new Point3D(hexagon.getX(), hexagon.getY(), p.z - 1));
            if(!topHexagons.contains(he)){
                topHexagons.add(he);
            }
        }
        return topHexagons;
    }

    /**
     * Get the top Place hexagons of the grid
     * @param s the type of the place
     * @return the top Place hexagons of the grid
     */
    public ArrayList<Place> placeDeTypeS(String s){
        ArrayList<Place> topPlaceTypeS = new ArrayList<>();
        for (Hexagon hexagon : getTopHexagons()) {
            if (hexagon instanceof Place) {
                if (hexagon.getType().equals(s)) {
                    topPlaceTypeS.add((Place)hexagon);
                }
            }
        }
        return topPlaceTypeS;
    }

    /**
     * Get the number of stars of the places
     * @param place the places to get the number of stars
     * @return the number of stars of the places
     */
    public int numberOfStars(ArrayList<Place> place){
        int nb =0;
        for (Place p : place) {
            nb+=p.getStars();
        }
        return nb;
    }

    /**
     * Calculate the score of the grid
     * @return the score of the grid
     */
    public int calculateScore( ) {
        int totalScore = 0;
        int buildingScore=0;
        int lastBuildingS = 0;
        for (Hexagon hexagon : getTopHexagons()) {
            switch (hexagon.getType()) {
                case "Garden":
                    totalScore += calculateGardenScore(hexagon);
                    break;
                case "Barrack":
                    totalScore += calculateBarrackScore(hexagon);
                    break;
                case "Building":
                    buildingScore += calculateBuildingScore(hexagon);
                    break;
                case "Temple":
                    totalScore += calculateTempleScore(hexagon);
                    break;
                case "Market":
                    totalScore += calculateMarketScore(hexagon);
                    break;
                default:
                    break;
            }
            if (buildingScore> lastBuildingS) {//
                lastBuildingS = buildingScore;
            }
        }

        return totalScore+lastBuildingS;
    }

    /**
     * Calculate the score of the garden
     * @param hexagon the hexagon to calculate the score
     * @return the score of the garden
     */
    private int calculateGardenScore(Hexagon hexagon) {
        int nb = numberOfStars(placeDeTypeS("Garden Place"));
        return hexagon.getElevation()*nb;
    }

    /**
     * Calculate the score of the barrack
     * @param hexagon the hexagon to calculate the score
     * @return the score of the barrack
     */
    private int calculateBarrackScore(Hexagon hexagon) {
        int nb = numberOfStars(placeDeTypeS("Barrack Place"));
        return (hexagon.getNeighbors().size() < 6) ? hexagon.getElevation()*nb: 0;
    }

    
    /**
     * Get the building neighbors of the hexagon
     * @param hexagon the hexagon to get the building neighbors
     * @return the building neighbors of the hexagon
     */
    private ArrayList<Hexagon> BuildingNeighbors(Hexagon hexagon , ArrayList<Hexagon> visitedHexagons){ 
        ArrayList<Hexagon> buildingNeighbors = new ArrayList<>();
        for (Hexagon neighbor : hexagon.getNeighbors()) {
            if (neighbor.getType().equals("Building")&&!visitedHexagons.contains(neighbor)) {
                buildingNeighbors.add(neighbor);
            }
        }
        return buildingNeighbors;
    }

    /**
     * Calculate the score of the building
     * @param hexagon the hexagon to calculate the score
     * @return the score of the building
     */
    private int calculateBuildingScore(Hexagon hexagon) {
        return visitBuildingHex(hexagon , new ArrayList<>());
    }
    public int visitBuildingHex(Hexagon hexagone , ArrayList<Hexagon> visitedHexagone){
        if (hexagone.getType().equals("Building")&& !visitedHexagone.contains(hexagone)) {
            visitedHexagone.add(hexagone); // we add the hexagone to the visited hexagone
            int score =1; // we add 1 point for each building
            for (Hexagon neighbor : BuildingNeighbors(hexagone ,visitedHexagone)) {
                score += visitBuildingHex(neighbor ,visitedHexagone); // we add the score of the neighbors
            }
            return score; 
        }
        return 0 ;
    } 

    /**
     * Calculate the score of the temple
     * @param hexagon the hexagon to calculate the score
     * @return the score of the temple
     */
    private int calculateTempleScore(Hexagon hexagon) {
        int nb = numberOfStars(placeDeTypeS("Temple Place"));
        return hexagonIsSurrounded(hexagon) ? hexagon.getElevation()*nb : 0;
    }

    /**
     * Calculate the score of the market
     * @param hexagon the hexagon to calculate the score
     * @return the score of the market
     */
    private int calculateMarketScore(Hexagon hexagon) {
        int nb = numberOfStars(placeDeTypeS("Market Place"));
        for (Hexagon neighbor : hexagon.getNeighbors()) {
            if (neighbor.getType().equals("Market")) {
                return 0;
            }
        }
        return nb;
    }
    
}
