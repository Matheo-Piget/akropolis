package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Represents the game with players and turns.
 */
public class Game {
    private final List<Player> players;
    private final StackTiles stacktiles;
    private List<Tile> tableTiles;
    private Player currentPlayer;

    /**
     * Constructor to initialize the game with an empty list of players.
     */
    public Game() {
        players = new ArrayList<>();
        stacktiles = new StackTiles(60); 
        tableTiles = new ArrayList<>(3);
        // Initialiser la liste avec les trois premières tuiles du paquet
        for (int i = 0; i < 3; i++) {
            if (!stacktiles.isEmpty()) {
                tableTiles.add(stacktiles.pop());
            }
        }
        
    }

    /***
     * Add a player to our list of players
     * @param player a player
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Démarre le jeu en définissant le joueur actuel sur le premier joueur et en lançant le premier tour.
     * Lève une exception si aucun joueur n'est présent pour démarrer le jeu.
     *
     * @throws IllegalStateException si aucun joueur n'est présent pour démarrer le jeu.
     */
    public void startGame() {
        if (!players.isEmpty()) {
            this.addPlayer(new Player("TunisianMonster")); // la creation et l'ajoute dun joueur
            currentPlayer = players.get(0); // recuperer le joueur qui vient d'etre ajouté
            stacktiles.shuffle(); // melanger les tuiles
            startTurn(currentPlayer);
        } else {
            throw new IllegalStateException("Impossible de démarrer le jeu sans joueur. Veuillez ajouter des joueurs avant de commencer.");
        }
    }

    /**
     * Starts a turn for the given player.
     *
     * @param player The player for whom the turn is starting.
     */
    public void startTurn(Player player) {
        // Logic to start a turn

    }
    public void chooseTilefromeTable(Player player ,int tileIndex){
        //logic pour choisir les tuiles
    }
    public void displayTableTiles() {
        for (int i = 0; i < tableTiles.size(); i++) {
            System.out.println(i + ": " + tableTiles.get(i).toString());
        }
    }
    

    /**
     * Ends the turn for the given player, switches to the next player, and starts their turn.
     *
     * @param player The player for whom the turn is ending.
     */
    public void endTurn(Player player) {
        // Logic to end a turn
        currentPlayer = getNextPlayer();
        startTurn(currentPlayer);
    }

    /**
     * Retrieves the next player in the list.
     *
     * @return The next player in the list.
     */
    private Player getNextPlayer() {
        // Get the index of the current player
        int currentIndex = players.indexOf(currentPlayer);

        // Increment the index to get the next player (circular list)
        int nextIndex = (currentIndex + 1) % players.size();

        // Return the next player
        return players.get(nextIndex);
    }
}
