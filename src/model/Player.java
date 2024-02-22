package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the Akrapolis game.
 */
public class Player {
    private String name; // The name of the player
    private int score; // The score of the player
    private List<Tile> ownedTiles; // The tiles owned by the player
    private Tile selectedTile; // The tile selected by the player during their turn
    private int resources; // The resources (rocks) owned by the player

    /**
     * Constructs a new player with the given name.
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.ownedTiles = new ArrayList<>();
        this.selectedTile = null;
        this.resources = 0;
    }

    // Getters and Setters

    /**
     * Gets the name of the player.
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the score of the player.
     * @return The score of the player.
     */
    public int getScore() {
        return score;
    }

    /**
     * Gets the list of tiles owned by the player.
     * @return The list of tiles owned by the player.
     */
    public List<Tile> getOwnedTiles() {
        return ownedTiles;
    }

    /**
     * Gets the selected tile of the player.
     * @return The selected tile of the player.
     */
    public Tile getSelectedTile() {
        return selectedTile;
    }

    /**
     * Sets the selected tile of the player.
     * @param tile The selected tile of the player.
     */
    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
    }

    /**
     * Checks if the player can choose a tile from the provided list based on their resources.
     * @param site The list of tiles to choose from.
     * @param chosen The tile chosen by the player.
     * @return True if the player can choose the tile, false otherwise.
     */
    public boolean canChooseTile(List<Tile> site, Tile chosen) {
        int price = 0;
        for (Tile tile : site) {
            if (tile != chosen) {
                price++;
            } else {
                break;
            }
        }
        return this.resources >= price;
    }

    /**
     * Gets the number of resources (rocks) owned by the player.
     * @return The number of resources (rocks) owned by the player.
     */
    public int getResources() {
        return resources;
    }

    /**
     * Sets the number of resources (rocks) owned by the player.
     * @param resources The number of resources (rocks) owned by the player.
     */
    public void setResources(int resources) {
        this.resources = resources;
    }

    /**
     * Calculates and returns the number of resources (rocks) owned by the player based on the tiles they own.
     * @return The number of resources (rocks) owned by the player.
     */
    public int calculateResources() {
        int resources = 0;
        for (Tile tile : ownedTiles) {
            for (Hexagon hexagon : tile.getHexagons()) {
                if (hexagon instanceof Quarrie) {
                    resources++;
                }
            }
        }
        return resources;
    }
}
