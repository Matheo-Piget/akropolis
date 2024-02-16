package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<Hexagon> ownedTiles;  // List of tiles owned by the player
    private Hexagon selectedTile;  // The tile selected by the player during their turn

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

    public List<Hexagon> getOwnedTiles() {
        return ownedTiles;
    }

    public Hexagon getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Hexagon tile) {
        this.selectedTile = tile;
    }
}
