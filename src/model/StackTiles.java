package model;

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
    }

    public void generateTiles(){
        // TODO: Implement this method
    }

    
}
