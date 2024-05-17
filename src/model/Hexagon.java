package model;

import java.awt.Point;
import util.Point3D;

/**
 * Represents a tile on the game grid.
 */
public abstract class Hexagon implements java.io.Serializable{
    private Point3D position; // Coordinates of the tile
    private Hexagon below; // Tile below the current tile

    /**
     * Constructor to create a tile with specified coordinates using a Point3D.
     *
     * @param p The position of the tile.
     */
    public Hexagon(Point3D p) {
        this.position = p;
    }

    /**
     * Constructor to create a tile with specified coordinates using x and y.
     * The z-coordinate is set to 1 by default.
     *
     * @param x The x-coordinate of the tile.
     * @param y The y-coordinate of the tile.
     */
    public Hexagon(int x, int y) {
        this.position = new Point3D(x, y, 1);
    }

    /**
     * Returns the type of the tile in a string format.
     * @return The type of the tile.
     */
    public abstract String getType();

    /**
     * Generates a random hexagon.
     *
     * @return A random hexagon.
     */
    public static Hexagon generateRandomHexagon(){
        int random = (int) (Math.random() * 6);
        return switch (random) {
            case 1 -> new District(0, 0, DistrictColor.RED);
            case 2 -> new District(0, 0, DistrictColor.BLUE);
            case 3 -> new District(0, 0, DistrictColor.GREEN);
            case 4 -> new District(0, 0, DistrictColor.YELLOW);
            case 5 -> new District(0, 0, DistrictColor.PURPLE);
            default -> new Quarries(0, 0);
        };
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
        // Tile containing the hexagons
    }

    public void setPosition(int x, int y){
        this.position = new Point3D(x, y, 1);
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
     * Gets the tile below the current tile.
     *
     * @return The tile below the current tile.
     */
    public Hexagon getBelow() {
        return below;
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
     * Checks if the tile has a tile below it.
     *
     * @return True if the tile has a tile below it, false otherwise.
     */
    public boolean hasBelow() {
        return below != null;
    }

    /**
     * checks if the tile is adjacent to the given tile
     * @param t the tile to check
     * @return true if the tile is adjacent to the given tile, false otherwise
     */
    public boolean isAdjacent(Hexagon t){
        Point[] axialDirections = {
            new Point(1, 0), new Point(1, -1), new Point(0, -1),
            new Point(-1, 0), new Point(-1, 1), new Point(0, 1)
    };
        for (Point point : axialDirections) {
            Point direct = new Point(this.getX()+point.x, this.getY()+point.y);
            if (t.getX() ==direct.x && t.getY()==direct.y) {
                return true;
            }
        }
        return false;
    }

    public String toString(){
        return this.getType() + " at " + this.position.toString();
    }
}
