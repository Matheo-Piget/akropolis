package model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int score;
    private List<Tile> ownedTiles;  // List of tiles owned by the player
    private Hexagon selectedTile;  // The tile selected by the player during their turn
    private int rocks = 0;


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

    public Hexagon getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(Hexagon tile) {
        this.selectedTile = tile;
    }
    public boolean Can_Choose_Tile(List<Tile> site, Tile choosed) {
        int price = 0;
        for (Tile tile : site) {
            if (tile != choosed) {
                price++;
            } else {
                break;
            }
        }
        if (this.rocks >= price) {
            return true;
        } else {
            return false;
        }
    }

    public int getRocks(){
        int rocks_get=0;
         for(Tile tile:ownedTiles){
            for(Hexagon hexagon:tile.hexagons){
                if(hexagon instanceof Quarrie){
                    rocks_get++;
                }
            }
         }
         return rocks_get;
    }
}