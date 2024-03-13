package model;

import java.util.ArrayList;
import java.awt.geom.Point2D;

/**
 * This class represents a tile that the player can set onto the board.
 */
public class Tile extends Model{
    // An array to store the three hexagons that make up the tile
    public ArrayList<Hexagon> hexagons = new ArrayList<>();
    private int rotation = 90;

    /**
     * Constructor for creating a Tile with three specified hexagons.
     *
     * @param tile1 The first tile in the trio.
     * @param tile2 The second tile in the trio.
     * @param tile3 The third tile in the trio.
     */
    public Tile(Hexagon tile1, Hexagon tile2, Hexagon tile3) {
        hexagons.add(tile1);
        hexagons.add(tile2);
        hexagons.add(tile3);
    }

    /**
     * Exchanges two specified hexagons in the tile.
     *
     * @param hexagon1 The first hexagon to be exchanged.
     * @param hexagon2 The second hexagon to be exchanged.
     */
    public void exchange(Hexagon hexagon1, Hexagon hexagon2) {
        int index1 = hexagons.indexOf(hexagon1);
        int index2 = hexagons.indexOf(hexagon2);
        hexagons.set(index1, hexagon2);
        hexagons.set(index2, hexagon1);
    }

    public void rotate() {
        rotation = (rotation + 90) % 360;
    }

    public void placeNeighbors() {
        Hexagon frontHex = hexagons.get(0);
        int x = frontHex.getX();
        int y = frontHex.getY();
    
        switch (rotation) {
            case 0:
                hexagons.get(1).setPosition(x + 1, y);
                hexagons.get(2).setPosition(x + 1, y - 1);
                break;
            case 90:
                hexagons.get(1).setPosition(x, y - 1);
                hexagons.get(2).setPosition(x - 1, y - 1);
                break;
            case 180:
                hexagons.get(1).setPosition(x - 1, y);
                hexagons.get(2).setPosition(x - 1, y + 1);
                break;
            case 270:
                hexagons.get(1).setPosition(x, y + 1);
                hexagons.get(2).setPosition(x + 1, y + 1);
                break;
        }
    }

    public ArrayList<Hexagon> getHexagons() {
        return hexagons;
    }
}
