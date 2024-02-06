package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<Tile> ownedTiles;  // List of tiles owned by the player
    private Tile selectedTile;  // The tile selected by the player during their turn

    // Constructor
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.ownedTiles = new ArrayList<>();
        this.selectedTile = null;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<Tile> getOwnedTiles() {
        return ownedTiles;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
    }
}
