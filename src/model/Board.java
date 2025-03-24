package model;

import java.util.ArrayList;
import util.Pair;
import java.util.List;

/**
 * Represents the game board and manages the game.
 */
public class Board extends Model {
    private final ArrayList<Player> playerList; // List of players in the game
    private final StackTiles stackTiles; // The stack of tiles in the game
    private final Site site; // The tiles on the table
    private Player currentPlayer; // The current player
    private int manche = 0;

    /**
     * Constructs a new board and initializes the game.
     * 
     * @param players The list of players in the game.
     */
    public Board(List<Player> players) {
        playerList = new ArrayList<>();
        int nb_rocks = 1;
        for (Player player : players) {
            player.setResources(nb_rocks);
            player.setGrid(new Grid(player));
            playerList.add(player);
            nb_rocks++;
        }
        stackTiles = new StackTiles(10); // Initialize the stack of tiles
        site = new Site(switchSizePlayers());
        currentPlayer = playerList.get(0); // The first player starts
        stackTiles.shuffle(); // Shuffle the stack of tiles
    }

    public boolean setSelectedTile(Tile tile) {
        if (canChooseTile(tile)) {
            playerList.get(manche % playerList.size()).setSelectedTile(tile);
            return true;
        }
        return false;
    }

    /**
     * Starts the game by starting the first turn.
     */
    public void startGame() {
        startTurn(currentPlayer);
    }

    /**
     * Starts a turn for the given player.
     * 
     * @param player The player for whom the turn is starting.
     */
    public void startTurn(Player player) {
        if (isGameOver()) {
            firePropertyChange("gameOver", null, getPlayersOrdered());
            return;
        }
        currentPlayer.getGrid().display();
        // Add tiles to the table if the manche is over
        if (manche % getNumberOfPlayers() == 0) {
            site.updateSite(stackTiles);
        }
        firePropertyChange("nextTurn", null, player);
    }

    /**
     * Adds a tile to the grid of the current player.
     */
    public void addTileToGrid() {
        Tile tile = currentPlayer.getSelectedTile();
        if (tile == null)
            return; // No tile selected
        if (addTile(tile)) {
            currentPlayer.setResources(currentPlayer.getResources() - site.calculateCost(tile));
            // Remove the tile from the site
            site.removeTile(tile);
            firePropertyChange("addTile", null, null);
            endTurn();
        }
    }

    /**
     * Ends the turn for the given player, switches to the next player, and starts
     * their turn.
     */
    public void endTurn() {
        manche++;
        currentPlayer = getNextPlayer(); // Switch to the next player
        currentPlayer.setSelectedTile(null); // Reset the selected tile
        startTurn(currentPlayer); // Start the next player's turn
    }

    /**
     * Retrieves the next player in the list.
     * 
     * @return The next player in the list.
     */
    private Player getNextPlayer() {
        return playerList.get((manche) % playerList.size());
    }

    public ArrayList<Grid> getGrids() {
        ArrayList<Grid> grids = new ArrayList<>();
        for (Player player : playerList) {
            grids.add(player.getGrid());
        }
        return grids;
    }

    /**
     * Returns the site where the tiles are buyable.
     * 
     * @return The site where the tiles are buyable.
     */
    public Site getSite() {
        return site;
    }

    /**
     * Returns the stack of tiles.
     * 
     * @return The stack of tiles.
     */
    public StackTiles getStackTiles() {
        return stackTiles;
    }

    /**
     * Returns the number of players in the game.
     * 
     * @return The number of players in the game.
     */
    private int getNumberOfPlayers() {
        return playerList.size();
    }

    /**
     * Returns the number of tiles to put on the stack according to the number of
     * players.
     * 
     * @return The number of tiles to put on the stack.
     */
    public int switchSizeStackPlayer() {
        return switch (getNumberOfPlayers()) {
            case 1, 2 -> 30;
            case 3 -> 40;
            default -> 60;
        };
    }

    /**
     * Returns the number of tiles to put on the table according to the number of
     * players.
     * 
     * @return The number of tiles to put on the table.
     */
    public int switchSizePlayers() {
        return switch (getNumberOfPlayers()) {
            case 1, 2 -> 3;
            case 3 -> 4;
            default -> 5;
        };
    }

    /**
     * Adds a tile to the grid of the current player.
     * 
     * @param tile The tile to add to the grid.
     * @return True if the tile was added, false otherwise.
     */
    public boolean addTile(Tile tile) {
        return currentPlayer.getGrid().addTile(tile);
    }

    public Hexagon getHexagon(int x, int y) {
        return currentPlayer.getGrid().getHexagon(x, y);
    }

    public int getScore(Player player) {
        return player.getGrid().calculateScore();
    }

    /**
     * Returns the current player.
     * 
     * @return The current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Checks if the game is over.
     * 
     * @return true if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return stackTiles.isEmpty() && site.size() <= 1;
    }

    /**
     * Returns the players of the game ordered from best to worst.
     * 
     * @return The players of the game ordered from best to worst.
     */
    public List<Pair<String, Integer>> getPlayersOrdered() {
        List<Player> sortedPlayers = new ArrayList<>(playerList);
        sortedPlayers.sort((p1, p2) -> Integer.compare(getScore(p2), getScore(p1)));
        List<Pair<String, Integer>> players = new ArrayList<>();
        for (Player player : sortedPlayers) {
            players.add(new Pair<>(player.getName(), getScore(player)));
        }
        return players;
    }

    /**
     * Checks if the player can choose a tile from the provided list based on their
     * resources.
     * 
     * @param chosen The tile chosen by the player.
     * @return True if the player can choose the tile, false otherwise.
     */
    public boolean canChooseTile(Tile chosen) {
        int cost = site.calculateCost(chosen);
        return currentPlayer.getResources() >= cost;
    }
}
