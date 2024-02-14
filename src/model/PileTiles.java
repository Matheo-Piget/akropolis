package model;

import java.util.Collections;
import java.util.Stack;
/**
 * Représente la pile de tuiles dans le jeu Akropolis. Cette pile contient toutes les tuiles qui peuvent être tirées par les joueurs pendant le jeu.
 */

public class PileTiles {
    private Stack<Tile> piletiles;
    
     /**
     * Construit une pile de tuiles vide prête à être remplie avec des tuiles.
     */
    public PileTiles(int capacity) {
        piletiles = new Stack<>();
    }


    public Tile pop(){// Retire et retourne la tuile supérieure de la pile.
        return piletiles.isEmpty()? null: piletiles.pop() ;
    }
    public boolean isEmpty(){
        return piletiles.isEmpty();
    }
    /**
     * Ajoute une tuile à la pile.
     * @param tile La tuile à ajouter à la pile.
     */
    public void addTile(Tile tile) {
        piletiles.push(tile);
    }
    public void shuffle(){ // pour melanger le packet
        Collections.shuffle(piletiles);
    }
}
