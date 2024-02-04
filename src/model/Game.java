package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the game with players and turns.
 */
public class Game {
    private final List<Player> players;
    private Player currentPlayer;

    /**
     * Constructor to initialize the game with an empty list of players.
     */
    public Game() {
        players = new ArrayList<>();
    }

    /**
     * Starts the game by setting the current player to the first player and initiating the first turn.
     */
    public void startGame() {
        currentPlayer = players.get(0);
        startTurn(currentPlayer);
    }

    /**
     * Starts a turn for the given player.
     *
     * @param player The player for whom the turn is starting.
     */
    public void startTurn(Player player) {
        // Logic to start a turn
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
