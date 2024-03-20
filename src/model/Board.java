package model;

import util.Tuple;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Represents the game board and manages the game.
 */
public class Board extends Model {
    private final List<Tuple<Player, Grid>> playerGridList; // List of tuples (player, grid)
    private final StackTiles stackTiles; // The stack of tiles in the game
    private final Site site; // The tiles on the table
    private Player currentPlayer; // The current player
    private int manche = 0;

    /**
     * Constructs a new board and initializes the game.
     * @param players The list of players in the game.
     */
    public Board(List<Player> players) {
        playerGridList = new ArrayList<>();
        int nb_rocks = 1;
        for (Player player : players) {
            player.setResources(nb_rocks);
            playerGridList.add(new Tuple<>(player, new Grid(player)));
            nb_rocks++;
        }
        stackTiles = new StackTiles(60); // Assuming 60 tiles in the stack
        site = new Site();
        currentPlayer = players.get(0); // Set the current player to the first player
        stackTiles.shuffle(); // Shuffle the stack of tiles
    }

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        propertyChangeSupport.removePropertyChangeListener(pcl);
    }

    public boolean setSelectedTile(Tile tile) {
        if(canChooseTile(tile)) {
            currentPlayer.setSelectedTile(tile);
            return true;
        }
        return false;
    }

    public void updatePlayerInfo() {
        propertyChangeSupport.firePropertyChange("playerUpdated", null, currentPlayer);
    }

    public void updateRemainingTilesInfo() {
        propertyChangeSupport.firePropertyChange("tilesRemainingUpdated", null, stackTiles.size());
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
        getCurrentGrid().display();
        System.out.println("Player " + player.getName() + "'s turn");
        // Add tiles to the table if the manche is over
        if (manche % getNumberOfPlayers() == 0) {
            site.updateSite(stackTiles, switchSizePlayers());
        }
        updateRemainingTilesInfo();
        updatePlayerInfo();
    }

    /**
     * Adds a tile to the grid of the current player.
     */
    public void addTileToGrid() {
        Tile tile = currentPlayer.getSelectedTile();
        if(tile == null) return; // No tile selected
        if (addTile(tile)) {
            currentPlayer.setResources(currentPlayer.getResources() - site.calculateCost(tile));
            endTurn();
        }
    }

    /**
     * Ends the turn for the given player, switches to the next player, and starts their turn.
     */
    public void endTurn() {
        System.out.println("End of turn : "+ currentPlayer.getName());
        System.out.println("Next turn : "+ getNextPlayer().getName());

        // Logic to end a turn
        currentPlayer = getNextPlayer(); // Switch to the next player

        startTurn(currentPlayer); // Start the next player's turn
    }

    /**
     * Retrieves the next player in the list.
     * @return The next player in the list.
     */
    private Player getNextPlayer() {
        // Trouver l'index du joueur actuel dans la liste des joueurs
        int currentIndex = -1;
        for (int i = 0; i < playerGridList.size(); i++) {
            if (playerGridList.get(i).x.equals(currentPlayer)) {
                currentIndex = i;
                break;
            }
        }

        if (currentIndex != -1) {
            // Incrémenter l'index pour obtenir le prochain joueur (liste circulaire)
            int nextIndex = (currentIndex + 1) % playerGridList.size();
            // Retourner le joueur correspondant à l'index suivant
            return playerGridList.get(nextIndex).x;
        } else {
            // Si le joueur actuel n'est pas trouvé dans la liste, retourner null
            return null;
        }
    }

    public Site getSite() {
        return site;
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
    public int switchSizePlayers() {
        System.out.println(getNumberOfPlayers());
        return switch (getNumberOfPlayers()) {
            case 1, 2 -> 3;
            case 3 -> 4;
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
        return stackTiles.isEmpty() && site.getTiles().isEmpty();
    }

    /**
     * Returns the winner of the game.
     * @return The winner of the game.
     */
    public Player getWinner() {
        if (isGameOver()) {
            Player winner = playerGridList.get(0).x;
            for (Tuple<Player, Grid> tuple : playerGridList) {
                if (getScore(tuple.x) > getScore(winner)) {
                    winner = tuple.x;
                }
            }
            return winner;
        }
        return null;
    }

    /**
     * Checks if the player can choose a tile from the provided list based on their resources.
     * @param chosen The tile chosen by the player.
     * @return True if the player can choose the tile, false otherwise.
     */
    public boolean canChooseTile(Tile chosen) {
        int price = 0;
        for (Tile tile : site.getTiles()) {
            if (tile != chosen) {
                price++;
            } else {
                break;
            }
        }
        return currentPlayer.getResources() >= price;
    }
}
