package model;

import util.Point3D;

import java.util.Collections;
import java.util.Stack;
/**
 * Représente la pile de tuiles dans le jeu Akropolis. Cette pile contient toutes les tuiles qui peuvent être tirées par les joueurs pendant le jeu.
 */

public class StackTiles extends Stack<Tile>{

    int remainingTiles;

     /**
     * Construit une pile de tuiles vide prête à être remplie avec des tuiles.
     */
    public StackTiles(int size){
        super();
        // We lock the size of the stack to avoid any modification
        remainingTiles = size;
        this.setSize(size);
        generateTiles();

    }

    /**
     * Generates the tiles for the stack randomly.
     */
    public void generateTiles() {
        // Generate one place of each type with stars
        int randomNumberOfPlaces = (int) (Math.random() * 20);
        for (DistrictColor color : DistrictColor.values()) {
            addPlaceWithStars(color);
            remainingTiles--;
        }
        // Generate a random number of places with stars
        for (int i = 0; i < randomNumberOfPlaces; i++) {
            addPlaceWithStars(DistrictColor.values()[(int) (Math.random() * 5)]);
            remainingTiles--;
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

    /**
     * Generates the rest of the tiles randomly.
     */
    private void generateRandomTiles() {
        // Generate the rest of the tiles randomly
        for (int i = 0; i < remainingTiles; i++) {
            // Create a tile with three random districts
            Tile tile = new Tile(getRandomDistrict(), getRandomDistrict(), getRandomDistrict());
            add(tile);
        }

    }

    /**
     * Returns a random district.
     *
     * @return a random district.
     */
    private Hexagon getRandomDistrict() {
        // Create a district with a random color
        Point3D position = new Point3D(0, 0, 0); // Example position, can be adjusted
        return new District(position, getRandomColor());
    }

    /**
     * Returns a random color.
     *
     * @return a random color.
     */
    private DistrictColor getRandomColor() {
        // Get a random color from the DistrictColor enum
        DistrictColor[] colors = DistrictColor.values();
        int randomIndex = (int) (Math.random() * colors.length);
        return colors[randomIndex];
    }

    /**
     * Shuffles the stack of tiles.
     */
    public void shuffle(){
        Collections.shuffle(this);
    }
}
