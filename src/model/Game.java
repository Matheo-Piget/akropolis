package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game of Akrapolis.
 */
public class Game {
    private final List<Player> players; // The list of players in the game
    private final StackTiles stackTiles; // The stack of tiles in the game
    private final List<Tile> tableTiles; // The tiles on the table
    private Player currentPlayer; // The current player

    /**
     * Constructs a new game with an empty list of players.
     */
    public Game() {
        players = new ArrayList<>();
        stackTiles = new StackTiles(60); // Assuming 60 tiles in the stack
        tableTiles = new ArrayList<>(3); // Assuming 3 tiles on the table
        // Initialize the list with the first three tiles from the stack
        for (int i = 0; i < 3; i++) {
            if (!stackTiles.isEmpty()) {
                tableTiles.add(stackTiles.pop());
            }
        }
    }

    /**
     * Adds a player to the game.
     * @param player The player to add.
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    /**
     * Starts the game by initializing the stack, shuffling it, and starting the first turn.
     * Throws an exception if there are no players to start the game.
     * @throws IllegalStateException If there are no players to start the game.
     */
    public void startGame() {
        if (!players.isEmpty()) {
            currentPlayer = players.get(0); // Set the current player to the first player
            stackTiles.shuffle(); // Shuffle the stack of tiles
            startTurn(currentPlayer); // Start the first turn
        } else {
            throw new IllegalStateException("Cannot start the game without players. Please add players before starting.");
        }
    }

    /**
     * Starts a turn for the given player.
     * @param player The player for whom the turn is starting.
     */
    public void startTurn(Player player) {
        currentPlayer = player;
    }

    /**
     * Ends the turn for the given player, switches to the next player, and starts their turn.
     * @param player The player for whom the turn is ending.
     */
    public void endTurn(Player player) {
        // Logic to end a turn
        currentPlayer = getNextPlayer(); // Switch to the next player
        startTurn(currentPlayer); // Start the next player's turn
    }

    /**
     * Retrieves the next player in the list.
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
