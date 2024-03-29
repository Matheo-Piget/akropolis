package model;

/**
 * Represents a player in the Akrapolis game.
 */
public class Player extends Model {
    private String name; // The name of the player
    private Grid grid; // The grid of the player
    private Tile selectedTile; // The tile selected by the player during their turn
    private int resources; // The resources (rocks) owned by the player

    /**
     * Constructs a new player with the given name.
     * @param name The name of the player.
     */
    public Player(String name) {
        this.name = name;
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
     * Get the grid of the player.
     * @return The grid of the player.
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Set the grid of the player.
     * @param grid
     */
    public void setGrid(Grid grid) {
        this.grid = grid;
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
}
