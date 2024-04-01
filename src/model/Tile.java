package model;

import java.util.ArrayList;
import java.awt.Point;

/**
 * This class represents a tile that the player can set onto the board.
 */
public class Tile extends Model{
    // An array to store the three hexagons that make up the tile
    public ArrayList<Hexagon> hexagons = new ArrayList<>();

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

    public void setCoordinates(ArrayList<Point> coordinates) {
        for (int i = 0; i < hexagons.size(); i++) {
            hexagons.get(i).setPosition(coordinates.get(i).x, coordinates.get(i).y);
        }
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

    public ArrayList<Hexagon> getHexagons() {
        return hexagons;
    }
}
