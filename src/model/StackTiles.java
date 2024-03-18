package model;

import util.Point3D;

import java.util.Collections;
import java.util.Stack;
import java.util.Random;

/**
 * Représente la pile de tuiles dans le jeu Akropolis. Cette pile contient toutes les tuiles qui peuvent être tirées par les joueurs pendant le jeu.
 */

public class StackTiles extends Stack<Tile>{

    int remainingTiles;
    Random random = new Random();

     /**
     * Construit une pile de tuiles vide prête à être remplie avec des tuiles.
     */
    public StackTiles(int size){
        super();
        // We lock the size of the stack to avoid any modification
        remainingTiles = size;
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
        // Determine a random number of stars between 1 and 5
        int stars = random.nextInt(5) + 1;

        // Create a place with stars of the specified color
        Point3D position = new Point3D(0, 0, 1); 
        Place place = new Place(position, stars, color);

        // Then the other two hexagons are random
        Hexagon t2 = Hexagon.generateRandomHexagon();
        Hexagon t3 = Hexagon.generateRandomHexagon();

        // Add the place to a tile and add the tile to the stack
        Tile tile = new Tile(place, t2, t3);
        push(tile);
    }

    /**
     * Generates the rest of the tiles randomly.
     */
    private void generateRandomTiles() {
        // Generate the rest of the tiles randomly
        for (int i = 0; i < remainingTiles; i++) {
            // Create a tile with three random districts
            Hexagon h1 = Hexagon.generateRandomHexagon();
            Hexagon h2 = Hexagon.generateRandomHexagon();
            Hexagon h3 = Hexagon.generateRandomHexagon();
            Tile tile = new Tile(h1, h2, h3);
            push(tile);
        }

    }

    /**
     * Shuffles the stack of tiles.
     */
    public void shuffle(){
        Collections.shuffle(this);
    }
}
