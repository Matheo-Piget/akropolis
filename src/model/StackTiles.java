package model;

import util.Point3D;

import java.util.Collections;
import java.util.Stack;
/**
 * Représente la pile de tuiles dans le jeu Akropolis. Cette pile contient toutes les tuiles qui peuvent être tirées par les joueurs pendant le jeu.
 */

public class StackTiles extends Stack<Tile>{

     /**
     * Construit une pile de tuiles vide prête à être remplie avec des tuiles.
     */
    public StackTiles(int size){
        super();
        // We lock the size of the stack to avoid any modification
        this.setSize(size);
        generateTiles();
    }

    public void generateTiles() {
        // Generate one place of each type with stars
        for (DistrictColor color : DistrictColor.values()) {
            addPlaceWithStars(color);
        }

        // Generate the rest of the tiles randomly
        generateRandomTiles();
    }

    /**
     * generates place with stars and random tiles
     *
     * @param color The color of the place.
     **/
    private void addPlaceWithStars(DistrictColor color) {
        // Determine the number of stars for each type of place
        int stars = switch (color) {
            case RED -> 2;
            case BLUE, GREEN, YELLOW -> 3;
            case PURPLE -> 4;
        };

        // Create a place with stars of the specified color
        Point3D position = new Point3D(0, 0, 0); // Example position, can be adjusted
        Place place = new Place(position, stars, color);

        // Add the place to a tile and add the tile to the stack
        Tile tile = new Tile(place, null, null);
        add(tile);
    }

    private void generateRandomTiles() {
        // Generate the rest of the tiles randomly
        for (int i = 0; i < size() - DistrictColor.values().length; i++) {
            // Create a tile with three random districts
            Tile tile = new Tile(getRandomDistrict(), getRandomDistrict(), getRandomDistrict());
            add(tile);
        }

    }

    private Hexagon getRandomDistrict() {
        // Create a district with a random color
        Point3D position = new Point3D(0, 0, 0); // Example position, can be adjusted
        return new District(position, getRandomColor());
    }

    private DistrictColor getRandomColor() {
        // Get a random color from the DistrictColor enum
        DistrictColor[] colors = DistrictColor.values();
        int randomIndex = (int) (Math.random() * colors.length);
        return colors[randomIndex];
    }

    /**
     * Mélange les tuiles dans la pile.
     */
    public void shuffle(){
        Collections.shuffle(this);
    }
}
