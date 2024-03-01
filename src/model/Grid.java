package model;

import java.awt.Point;
import java.util.*;
import util.Point3D;

/**
 * Represents the grid of the game board, containing hexagons in different
 * positions.
 */
public class Grid {
    // Map to store hexagons based on their positions
    private Map<Point3D, Hexagon> hexagons;
    private Player player;

    /**
     * Constructor to initialize the grid and add the starting hexagons at the
     * beginning of the game.
     */
    public Grid(Player player) {
        hexagons = new HashMap<>();
        // Creating starting hexagons
        Point3D p1 = new Point3D(0, 0);
        Point3D p2 = new Point3D(0, 1);
        Point3D p3 = new Point3D(-1, -1);
        Point3D p4 = new Point3D(1, 0);
        Hexagon hexagon1 = new Place(p1, 1, DistrictColor.BLUE, this);
        Hexagon hexagon2 = new Quarrie(p2, this);
        Hexagon hexagon3 = new Quarrie(p3, this);
        Hexagon hexagon4 = new Quarrie(p4, this);
        hexagons.put(p1, hexagon1);
        hexagons.put(p2, hexagon2);
        hexagons.put(p3, hexagon3);
        hexagons.put(p4, hexagon4);
        this.player = player;
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

    // on regarde si l'elevation est de 1 alors faut que la tuille aie des voisisns
    // pour etre placer
    public boolean canAdd(Hexagon hexagon, Point3D p) {
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
        boolean hasNeighbor = checkNeighborsAndSetBelowTile(tile, bellowHexagons);
        boolean canBePlaced = checkElevation(tile);
        int samehexagon = countSameHexagons(tile, bellowHexagons);

        if (canBePlaced && hasNeighbor && samehexagon <= 1) {
            addHexagonsToGrid(tile, bellowHexagons);
        }

        if (canBePlaced && hasNeighbor && samehexagon <= 1){
            for (Hexagon hexagon : tile.hexagons) {
                if(hexagon.getBelow() instanceof Quarrie){
                    player.setResources(player.getResources()+1);
                }
            }
        }


        display();
        System.out.println("canBePlaced: " + canBePlaced + ", hasNeighbor: " + hasNeighbor + ", samehexagon: " + samehexagon);
        return canBePlaced && hasNeighbor && samehexagon <= 1;
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
                hasNeighbor = hasNeighbor || canAdd(newHexagon_i, newHexagon_i.getPosition());
            } else if (hexagons.containsKey(newHexagon_i.getPosition())) {
                Hexagon topMosthexagon = getHexagon(newHexagon_i.getX(), newHexagon_i.getY());
                if (topMosthexagon != null) {
                    bellowHexagons[i] = topMosthexagon;
                }
                newHexagon_i.getPosition().z = topMosthexagon.getZ() + 1;
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
                bellowHexagons[i].setAbove(newHexagon_i);
            }
            hexagons.put(newHexagon_i.getPosition(), newHexagon_i);
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
        int elevation = t.hexagons.get(0).getZ();
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
        int samehexagon = 0;
        for (int i = 0; i < 3; i++) {
            Hexagon newHexagon_i = tile.hexagons.get(i);
            if (bellowHexagons[i] != null && bellowHexagons[i].getType().equals(newHexagon_i.getType())) {
                samehexagon++;
            }
        }
        return samehexagon;
    }

    public Hexagon neighbor(Hexagon t, Point p) {
        Point p2 = new Point(t.getX(), t.getY());
        return hexagons.get(sommePos(p, p2));
    }

    public Point sommePos(Point p1, Point p2) {
        return new Point(p1.x + p2.x, p1.y + p2.y);
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
    /*
     * public void addhexagon(hexagon hexagon){
     * boolean hasNeighbor;
     * boolean isSpported ;
     * int elevation = hexagon.gethexagon(0).getZ() ;// verifier d'abord si au meme
     * niveau avant d'ajouter
     * 
     * for (int i = 1; i < 3; i++) {
     * if (hexagon.gethexagon(i).getZ()!= elevation) {
     * return ;
     * }
     * }
     * 
     * for (int i = 0; i < 3; i++) {
     * hexagon hexagonToadd = hexagon.gethexagon(i);
     * if (!hexagons.containsKey(hexagonToadd.getPosition())) {
     * hasNeighbor = canAdd(hexagonToadd);
     * }
     * else{
     * isSpported =
     * }
     * 
     * }
     * }
     * public boolean isSpported(hexagon t ){
     * 
     * }
     * 
     * public boolean canAdd(hexagon hexagon){
     * Point [] axialDirection = {new Point(0, 1), new Point(0, -1),new Point(-1, 1)
     * ,new Point(1, 1),new Point(1, -1),new Point(-1, -1),};
     * for (Point point : axialDirection) {
     * Point3D hexagonneighber = new Point3D(hexagon.getX()+point.x, hexagon.getY()+point.y,
     * 0);
     * if (hexagons.containsKey(hexagonneighber)) {
     * return true;
     * }
     * }
     * return false ;
     * }
     */

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
        Hexagon topMosthexagon = hexagons.get(new Point3D(x,y));
        if (topMosthexagon == null) {
            return null;
        }
        while (topMosthexagon.hasAbove()) {
            topMosthexagon = topMosthexagon.getAbove();
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
     * for (hexagon hexagon : hexagons.values()) {
     * switch (hexagon.getType()) {
     * case "Garden Place":
     * gardenMultiplier = ((Place) hexagon).getStars();
     * break;
     * case "Barrack Place":
     * barrackMultiplier = ((Place) hexagon).getStars();
     * break;
     * case "Building Place":
     * buildingMultiplier = ((Place) hexagon).getStars();
     * break;
     * case "Temple Place":
     * templeMultiplier = ((Place) hexagon).getStars();
     * break;
     * case "Market Place":
     * marketMultiplier = ((Place) hexagon).getStars();
     * break;
     * 
     * case "Garden":
     * gardenScore += hexagon.getElevation(); // It should always increase the score
     * break;
     * case "Barrack":
     * // We only increase the score if the barrack is in the border of the grid
     * if (hexagon.getNeighbors().size() < 6) {
     * barrackScore += hexagon.getElevation();
     * }
     * break;
     * case "Building":
     * // We only increase the score if the building is next to another building
     * for (hexagon neighbor : hexagon.getNeighbors()) {
     * if (neighbor.getType().equals("Building")) {
     * currentNumberOfBuilding++;
     * buildingScore += hexagon.getElevation();
     * maxNumberOfBuilding = Math.max(maxNumberOfBuilding, currentNumberOfBuilding);
     * maxBuildingScore = Math.max(maxBuildingScore, buildingScore);
     * break;
     * }
     * }
     * break;
     * case "Temple":
     * // We only increase the score if the temple is surrounded by 6 hexagons
     * if (hexagonIsSurrounded(hexagon)) {
     * templeScore++;
     * }
     * break;
     * case "Market":
     * // We only increase the score if the market has no adjacent market
     * boolean hasMarket = false;
     * for (hexagon neighbor : hexagon.getNeighbors()) {
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

    /**
     * Get the top hexagons of the grid
     * @return the top hexagons of the grid
     */
     public ArrayList<Hexagon> getTopHexagons() {
        ArrayList<Hexagon> tophexagons = new ArrayList<>();
        for (Hexagon hexagon : hexagons.values()) {
            if (!hexagon.hasAbove() && tophexagons.contains(hexagon) == false) {
                tophexagons.add(hexagon);
            }
        }
        return tophexagons;
    }

    /**
     * Get the top Place hexagons of the grid
     * @param s
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
    public int nbetoile(ArrayList<Place> place){
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
        int buildingscore=0;
        int lastbuildingS = 0;
        for (Hexagon hexagon : getTopHexagons()) {
            switch (hexagon.getType()) {
                case "Garden":
                    totalScore += calculateGardenScore(hexagon);
                    break;
                case "Barrack":
                    totalScore += calculateBarrackScore(hexagon);
                    break;
                case "Building":
                    buildingscore += calculateBuildingScore(hexagon);
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
            if (buildingscore> lastbuildingS) {// si le score est pas plus grand on garde le dernier plus grand pour le nombre de batiment qui sont collé
                lastbuildingS = buildingscore;
            }
        }

        return totalScore+lastbuildingS;
    }

    /**
     * Calculate the score of the garden
     * @param hexagon the hexagon to calculate the score
     * @return the score of the garden
     */
    private int calculateGardenScore(Hexagon hexagon) {
        int nb = nbetoile(placeDeTypeS("Garden Place"));
        return hexagon.getElevation()*nb;
    }

    /**
     * Calculate the score of the barrack
     * @param hexagon the hexagon to calculate the score
     * @return the score of the barrack
     */
    private int calculateBarrackScore(Hexagon hexagon) {
        int nb = nbetoile(placeDeTypeS("Barrack Place"));
        return (hexagon.getNeighbors().size() < 6) ? hexagon.getElevation()*nb: 0;
    }

    
    /**
     * Get the building neighbors of the hexagon
     * @param hexagon the hexagon to get the building neighbors
     * @return the building neighbors of the hexagon
     */
    private ArrayList<Hexagon> BuildingNeighbors(Hexagon hexagon , ArrayList<Hexagon> visitedHexagons){ //pour avoir les voisins de type building quin'ont pas été visité
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
        return visitBuildingHex(hexagon , new ArrayList<Hexagon>());
    }
    public int visitBuildingHex(Hexagon hexagone , ArrayList<Hexagon> visitedHexagone){
        if (hexagone.getType().equals("Building")&& !visitedHexagone.contains(hexagone)) {
            visitedHexagone.add(hexagone); // on ajoute cette hexagone aux hexagones parcourus
            int score =1; // on compte 1 point de cette hexagone et pour chaque hexagone 
            for (Hexagon neighbor : BuildingNeighbors(hexagone ,visitedHexagone)) { // on parcours ses voisins qui n'ont pas été visité
                score += visitBuildingHex(neighbor ,visitedHexagone); // on appel recursivement la fonction pour qu'elle parcours les voisins des voisins de cette hexagone en ajoutant le nombre des points qui ont été ajouté 
            }
            return score;// retourn le socre 
        }
        return 0 ; // on retourn 0 si ca été deja visité ou c'est pas du type buildings
    } 

    /**
     * Calculate the score of the temple
     * @param hexagon the hexagon to calculate the score
     * @return the score of the temple
     */
    private int calculateTempleScore(Hexagon hexagon) {
        int nb = nbetoile(placeDeTypeS("Temple Place"));
        return hexagonIsSurrounded(hexagon) ? hexagon.getElevation()*nb : 0;
    }

    /**
     * Calculate the score of the market
     * @param hexagon the hexagon to calculate the score
     * @return the score of the market
     */
    private int calculateMarketScore(Hexagon hexagon) {
        int nb = nbetoile(placeDeTypeS("Market Place"));
        for (Hexagon neighbor : hexagon.getNeighbors()) {
            if (neighbor.getType().equals("Market")) {
                return 0;
            }
        }
        return 1*nb;
    }

    /**
     * Calculate the score of the place
     * @param place the place to calculate the score
     * @return the score of the place
     */
    
}
