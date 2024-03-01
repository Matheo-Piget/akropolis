package model;

import util.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the game board and manages the game.
 */
public class Board {
    private final List<Tuple<Player, Grid>> playerGridList; // List of tuples (player, grid)
    private final StackTiles stackTiles; // The stack of tiles in the game
    private final List<Tile> tableTiles; // The tiles on the table
    private Player currentPlayer; // The current player
    private int manche = 0;

    /**
     * Constructs a new board and initializes the game.
     * @param players The list of players in the game.
     */
    public Board(List<Player> players) {
        playerGridList = new ArrayList<>();
        for (Player player : players) {
            playerGridList.add(new Tuple<>(player, new Grid()));
        }
        stackTiles = new StackTiles(60); // Assuming 60 tiles in the stack
        tableTiles = new ArrayList<>(switchSizePlayers()); // Assuming 3 tiles on the table
        // Initialize the list with the first three tiles from the stack
        for (int i = 0; i < switchSizePlayers(); i++) {
            if (!stackTiles.isEmpty()) {
                tableTiles.add(stackTiles.pop());
            }
        }
        currentPlayer = players.getFirst(); // Set the current player to the first player
        stackTiles.shuffle(); // Shuffle the stack of tiles
    }

    /**
     * Starts the game by starting the first turn.
     */
    public void startGame() {
        startTurn(currentPlayer); // Start the first turn
    }

    /**
     * Starts a turn for the given player.
     * @param player The player for whom the turn is starting.
     */
    public void startTurn(Player player) {
        // Add tiles to the table if the manche is over
        if (manche % getNumberOfPlayers() == 0) {
            for (int i = tableTiles.size(); i < switchSizePlayers(); i++) {
                if (!stackTiles.isEmpty()) {
                    tableTiles.add(stackTiles.pop());
                }
            }
        }

        // TODO: Implement the turn logic when we have the controller
        player.setSelectedTile(tableTiles.getFirst()); // For simplicity, let's say the player chooses the first tile on the table
        tableTiles.removeFirst(); // Remove the chosen tile from the table
        player.getOwnedTiles().add(player.getSelectedTile()); // Add the chosen tile to the player's owned tiles
        getCurrentGrid().addTile(player.getSelectedTile()); // Add the chosen tile to the player's grid
        // Other turn logic can be added here, such as scoring, checking for game end conditions, etc.
        manche++;
        endTurn(player);
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
        int currentIndex = playerGridList.indexOf(playerGridList.stream().filter(t -> t.x.equals(currentPlayer)).findFirst().orElse(null));
        // Increment the index to get the next player (circular list)
        int nextIndex = (currentIndex + 1) % playerGridList.size();
        // Return the next player
        return playerGridList.get(nextIndex).x;
    }

    public List<Tile> getTableTiles() {
        return tableTiles;
    }

    public StackTiles getStackTiles() {
        return stackTiles;
    }

    public int getManche() {
        return manche;
    }

    public Tuple<Player, Grid> getPlayerGrid(Player player) {
        return playerGridList.stream().filter(t -> t.x.equals(player)).findFirst().orElse(null);
    }

    /**
     * Returns the number of players in the game.
     * @return The number of players in the game.
     */
    private int getNumberOfPlayers() {
        return playerGridList.size();
    }

    /**
     * Returns the number of tiles to put on the table according to the number of players.
     * @return The number of tiles to put on the table.
     */
    private int switchSizePlayers() {
        return switch (getNumberOfPlayers()) {
            case 2 -> 4;
            case 4 -> 6;
            default -> 5;
        };
    }

    public boolean addTile(Tile tile) {
        return getCurrentGrid().addTile(tile);
    }

    public boolean addTile(Tile tile, Player player) {
        return getPlayerGrid(player).y.addTile(tile);
    }

    public Hexagon getHexagon(int x, int y) {
        return getCurrentGrid().getHexagon(x, y);
    }

    public int getScore(Player player) {
        return getPlayerGrid(player).y.calculateScore();
    }

    /**
     * Returns the current player.
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Returns the current player's grid.
     * @return The current player's grid.
     */
    public Grid getCurrentGrid() {
        return Objects.requireNonNull(playerGridList.stream().filter(t -> t.x.equals(currentPlayer)).findFirst().orElse(null)).y;
    }

    /**
     * Checks if the game is over.
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return stackTiles.isEmpty() && tableTiles.isEmpty();
    }

    /**
     * Returns the winner of the game.
     * @return The winner of the game.
     */
    public Player getWinner() {
        if (isGameOver()) {
            Player winner = playerGridList.getFirst().x;
            for (Tuple<Player, Grid> tuple : playerGridList) {
                if (tuple.x.getScore() > winner.getScore()) {
                    winner = tuple.x;
                }
            }
            return winner;
        }
        return null;
    }
}
