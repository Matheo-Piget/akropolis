package model;

import java.util.ArrayList;
import util.Point3D;
import java.awt.Point;

/**
 * Represents a tile on the game grid.
 */
public abstract class Hexagon {
    private Point3D position; // Coordinates of the tile
    private Grid grid; // Reference to the grid containing the tile
    private Hexagon above; // Tile above the current tile
    private Hexagon below; // Tile below the current tile
    private Tile tile; // Tile containing the hexagons

    /**
     * Constructor to create a tile with specified coordinates and grid.
     *
     * @param p    The position of the tile.
     * @param grid The grid containing the tile.
     */
    public Hexagon(Point3D p, Grid grid) {
        this.position = p;
        this.grid = grid;
    }

    /**
     * Constructor to create a tile with specified coordinates (used without grid reference).
     *
     * @param p The position of the tile.
     */
    public Hexagon(Point3D p) {
        this.position = p;
    }

    public abstract String getType();

    /**
     * Sets the grid reference for the tile.
     *
     * @param grid The grid containing the tile.
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
    }

    /**
     * Gets the elevation of the tile.
     *
     * @return The current elevation of the tile.
     */
    public int getElevation() {
        return position.z;
    }

    public void setTile(Tile tile){
        this.tile = tile;
    }

    /**
     * Gets the x-coordinate of the tile.
     *
     * @return The x-coordinate of the tile.
     */
    public int getX() {
        return position.x;
    }

    /**
     * Gets the y-coordinate of the tile.
     *
     * @return The y-coordinate of the tile.
     */
    public int getY() {
        return position.y;
    }

    /**
     * Gets the z-coordinate of the tile.
     *
     * @return The z-coordinate of the tile.
     */
    public int getZ() {
        return position.z;
    }
    
    public Point3D getPosition(){
        return this.position;
    }

    /**
     * Gets the tile above the current tile.
     *
     * @return The tile above the current tile.
     */
    public Hexagon getAbove() {
        return above;
    }

    /**
     * Gets the tile below the current tile.
     *
     * @return The tile below the current tile.
     */
    public Hexagon getBelow() {
        return below;
    }

    /**
     * Sets the tile above the current tile.
     *
     * @param aboveTile The tile above the current tile.
     */
    public void setAbove(Hexagon aboveTile) {
        this.above = aboveTile;
    }

    /**
     * Sets the tile below the current tile.
     *
     * @param belowTile The tile below the current tile.
     */
    public void setBelow(Hexagon belowTile) {
        this.below = belowTile;
    }

    /**
     * Gets a list of neighboring tiles based on the hexagonal grid layout.
     *
     * @return List of neighboring tiles.
     */
    public ArrayList<Hexagon> getNeighbors() {
        ArrayList<Hexagon> neighbors = new ArrayList<>();

        // Define the directions for the 6 neighbors in a hexagonal grid
        Point axialDirections[] = {
            new Point(1, 0), new Point(1, -1), new Point(0, -1),
            new Point(-1, 0), new Point(-1, 1), new Point(0, 1)
        };
        
        // Check each direction and add the neighboring tile if it exists
        for (Point direction : axialDirections) {
            Hexagon neighbor = grid.getHexagon(position.x + direction.x, position.y + direction.y);
            if (neighbor != null) {
                neighbors.add(neighbor);
            }
        }

        return neighbors;
    }

    /**
     * Checks if the tile has a tile above it.
     *
     * @return True if the tile has a tile above it, false otherwise.
     */
    public boolean hasAbove() {
        return above != null;
    }

    /**
     * Checks if the tile has a tile below it.
     *
     * @return True if the tile has a tile below it, false otherwise.
     */
    public boolean hasBelow() {
        return below != null;
    }
    
    public boolean isAdjacent(Hexagon t){
        Point [] axialDirection =  {new Point(1, 0), new Point(1, -1),new Point(0, -1)
                                    ,new Point(-1, 0),new Point(-1, 1),new Point(0, 1),};           
        for (Point point : axialDirection) {
            Point direct = new Point(this.getX()+point.x, this.getY()+point.y);
            if (t.getX() ==direct.x && t.getY()==direct.y) {
                System.out.println("T");
                return true;
            }
        }
        return false;
    }

    public String toString(){
        return this.getType() + " at " + this.position.toString();
    }
}
