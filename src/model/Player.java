package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<District> ownedDistricts;  // List of districts owned by the player
    private Tile selectedTile;  // The tile selected by the player during their turn

    // Constructor
    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.ownedDistricts = new ArrayList<>();
        this.selectedTile = null;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<District> getOwnedDistricts() {
        return ownedDistricts;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Tile tile) {
        this.selectedTile = tile;
    }
}
